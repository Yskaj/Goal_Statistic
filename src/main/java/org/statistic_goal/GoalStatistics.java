package org.statistic_goal;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.stream.Stream;

public class GoalStatistics {
    static HashMap<String, ComandStatistic> statistic;
    static String sep;

    static public Path getGoalStatistics(String csv_file) {
        readCSV(csv_file);
        return sortStatistic(csv_file);
    }

    private static Path sortStatistic(String csv) {
        List<JSONObject> sortingList = new ArrayList<>();
        for (ComandStatistic cs : statistic.values()) {
            String[] top_scorer = bestScorer(cs);//Найдем лучшего бомбардира
            sortingList.add(new JSONObject()
                    .put("team", cs.name)
                    .put("goals_scored", cs.goals_scored)
                    .put("goals_missed", cs.goals_missed)
                    .put("top_scorer", new JSONObject()
                            .put("name", top_scorer[0])
                            .put("goals_scored", top_scorer[1])));
        }
        sortingList.sort((o1, o2) -> {
            int firstGoals = o1.getInt("goals_scored");
            int secondGoals = o2.getInt("goals_scored");
            if (firstGoals == secondGoals) {
                return (o1.get("team").toString().compareTo(o2.get("team").toString()));//сортируем команды по имени
            }
            return (firstGoals < secondGoals) ? 1 : -1;//сортируем по кол-ву забитых мячей
        });
        return WriteJSON(sortingList,csv);
    }

    private static Path WriteJSON(List<JSONObject> jsonStatistic,String csv) {
        Path resourceDirectory = Paths.get(csv.substring(0,csv.length()-4) + "JSON.json");
        try {
            Files.deleteIfExists(resourceDirectory);
            Files.createFile(resourceDirectory);
            Files.writeString(resourceDirectory, "[", StandardOpenOption.APPEND);
            sep = System.lineSeparator();
            int jsonSize = jsonStatistic.size();
            for (int i = 0; i < jsonSize - 1; i++) {
                Files.writeString(resourceDirectory, prettyJSONMaker(jsonStatistic.get(i)) + ",", StandardOpenOption.APPEND);
            }
            Files.writeString(resourceDirectory, prettyJSONMaker(jsonStatistic.get(jsonSize-1)) + sep + "]", StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resourceDirectory;
    }

    private static String prettyJSONMaker(JSONObject command) {

        JSONObject topScorer = command.getJSONObject("top_scorer");
        return sep + "    {" + sep + "        \"team\": \"" + command.get("team") + "\"," + sep
                + "        \"goals_scored\": \"" + command.get("goals_scored") + "\"," + sep
                + "        \"goals_missed\": \"" + command.get("goals_missed") + "\"," + sep
                + "        \"top_scorer\": {" + sep
                + "            \"name\": \"" + topScorer.get("name") + "\"," + sep
                + "            \"goals_scored\": \"" + topScorer.get("goals_scored") + "\"" + sep
                + "        }" + sep + "    }";
    }

    private static String[] bestScorer(ComandStatistic cs) {  //выбираем лучшего бомбардира
        if (cs.scorers.isEmpty()) return new String[]{"", ""};//если никто из команды не забил
        String scorer = Collections.max(cs.scorers.entrySet(), Map.Entry.comparingByValue()).getKey();
        return new String[]{scorer, String.valueOf(cs.scorers.get(scorer).goals)};
    }

    private static void readCSV(String str_file) {
        statistic = new HashMap<>();
        try (Stream<String> lines = Files.lines(Path.of(str_file))) {
            lines.skip(1).forEach(line ->
                    fillStatistic(line.split(","))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void fillStatistic(String[] date) {
        String comandFirst = date[1];
        String comandSecond = date[2];
        String nameComand = date[3];
        String nameScorer = date[4];
        boolean penalty = Boolean.parseBoolean(date[7]);
        boolean own_goal = Boolean.parseBoolean(date[6]);
        addComand(comandFirst);
        addComand(comandSecond);
        String loseComand = (nameComand.equals(comandFirst)) ? comandSecond : comandFirst; //кому забили гол
        if (!own_goal) { // был ли автогол?
            statistic.get(nameComand).addGoal(nameScorer, penalty);
            statistic.get(loseComand).addMissed();
        } else {
            statistic.get(nameComand).addMissed(nameScorer);
        }
    }

    static void addComand(String comand) { // Добавляет команду в statistic(HashMap<String, ComandStatistic>)
        if (!statistic.containsKey(comand)) {
            statistic.put(comand, new ComandStatistic(comand));
        }
    }
}
