package org.statistic_goal;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class WriteFileJSON implements WriteFile {
    @Override
    public void write(Sorter sortedFile) {
        List<List<String>> jsonStatistic = (List<List<String>>) sortedFile.getData();
        Path resourceDirectory = Paths.get(Converter.path.toString().substring(0, Converter.path.toString().length() - 4) + "JSON.json");
        try {
            Files.deleteIfExists(resourceDirectory);
            Files.createFile(resourceDirectory);
            Files.writeString(resourceDirectory, "[", StandardOpenOption.APPEND);
            int jsonSize = jsonStatistic.size();
            for (int i = 0; i < jsonSize - 1; i++) {
                Files.writeString(resourceDirectory, prettyJSONMaker(toJSONObject(jsonStatistic.get(i))) + ",", StandardOpenOption.APPEND);
            }
            Files.writeString(resourceDirectory, prettyJSONMaker(toJSONObject(jsonStatistic.get(jsonSize - 1))) + System.lineSeparator() + "]", StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
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
        String sep = System.lineSeparator();
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
