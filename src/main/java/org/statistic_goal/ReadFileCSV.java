package org.statistic_goal;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ReadFileCSV implements ReadFile {
    Map<String, ComandStatistic> statistic;

    public ReadFileCSV() {
        this.statistic = new HashMap<>();
    }

    @Override
    public Map<String, ComandStatistic> getData() {
        return statistic;
    }

    @Override
    public void read(String path) {
        try (Stream<String> lines = Files.lines(Path.of(path))) {
            lines.skip(1).forEach(line ->
                    fillStatistic(line.split(","))
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void fillStatistic(String[] date) {
        String comandFirst = date[1];
        String comandSecond = date[2];
        String nameComand = date[3];
        String nameScorer = date[4];
        boolean penalty = Boolean.parseBoolean(date[7]);
        boolean own_goal = Boolean.parseBoolean(date[6]);
        statistic.computeIfAbsent(comandFirst,k-> new ComandStatistic(comandFirst));
        statistic.computeIfAbsent(comandSecond,k-> new ComandStatistic(comandSecond));
        String loseComand = (nameComand.equals(comandFirst)) ? comandSecond : comandFirst; //кому забили гол
        if (!own_goal) { // был ли автогол?
            statistic.get(nameComand).addGoal(nameScorer, penalty);
            statistic.get(loseComand).addMissed();
        } else {
            statistic.get(nameComand).addMissed(nameScorer);
        }
    }
}
