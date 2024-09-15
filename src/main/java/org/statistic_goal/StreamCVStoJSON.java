package org.statistic_goal;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static java.lang.System.lineSeparator;

public class StreamCVStoJSON {
    Map<String, ComandStatistic> statistic;
    Path resourceDirectory;

    public StreamCVStoJSON(Path path) {
        statistic = new HashMap<>();
        resourceDirectory = Paths.get(path.toString().substring(0, path.toString().length() - 4) + "JSON.json");
        createJSONFile(resourceDirectory);
        try (Stream<String> lines = Files.lines(path)) {
            lines.skip(1)
                    .forEach(s -> fillStatistic(s.split(",")));

            statistic.values().stream()
                    .map(ComandStatistic::mainComandData)
                    .sorted(listComparator)
                    .toList()
                    .forEach(this::writeStringToFile);

            Files.writeString(resourceDirectory, lineSeparator() + "]", StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeStringToFile(List<String> s) {
        try {
            Files.writeString(this.resourceDirectory, prettyJSONMaker(toJSONObject(s)) + ",", StandardOpenOption.APPEND);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createJSONFile(Path resourceDirectory) {
        try {
            Files.deleteIfExists(resourceDirectory);
            Files.createFile(resourceDirectory);
            Files.writeString(resourceDirectory, "[", StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Comparator<List<String>> listComparator = (o1, o2) -> {
        int firstGoals = Integer.parseInt(o1.get(1));
        int secondGoals = Integer.parseInt(o2.get(1));
        if (firstGoals == secondGoals) {
            return (o1.get(0).compareTo(o2.get(0)));
        }
        return (firstGoals < secondGoals) ? 1 : -1;
    };

    private void fillStatistic(String[] date) {
        String comandFirst = date[1];
        String comandSecond = date[2];
        String nameComand = date[3];
        String nameScorer = date[4];
        boolean penalty = Boolean.parseBoolean(date[7]);
        boolean own_goal = Boolean.parseBoolean(date[6]);
        statistic.computeIfAbsent(comandFirst, k -> new ComandStatistic(comandFirst));
        statistic.computeIfAbsent(comandSecond, k -> new ComandStatistic(comandSecond));
        String loseComand = (nameComand.equals(comandFirst)) ? comandSecond : comandFirst; //кому забили гол
        if (!own_goal) { // был ли автогол?
            statistic.get(nameComand).addGoal(nameScorer, penalty);
            statistic.get(loseComand).addMissed();
        } else {
            statistic.get(nameComand).addMissed(nameScorer);
        }
    }

    private JSONObject toJSONObject(List<String> comand) {
        return new JSONObject()
                .put("team", comand.get(0))
                .put("goals_scored", comand.get(1))
                .put("goals_missed", comand.get(2))
                .put("top_scorer", new JSONObject()
                        .put("name", comand.get(3))
                        .put("goals_scored", comand.get(4)));
    }

    private String prettyJSONMaker(JSONObject comand) {
        String sep = lineSeparator();
        JSONObject topScorer = comand.getJSONObject("top_scorer");
        return sep + "    {" + sep + "        \"team\": \"" + comand.get("team") + "\"," + sep
                + "        \"goals_scored\": \"" + comand.get("goals_scored") + "\"," + sep
                + "        \"goals_missed\": \"" + comand.get("goals_missed") + "\"," + sep
                + "        \"top_scorer\": {" + sep
                + "            \"name\": \"" + topScorer.get("name") + "\"," + sep
                + "            \"goals_scored\": \"" + topScorer.get("goals_scored") + "\"" + sep
                + "        }" + sep + "    }";
    }
}

