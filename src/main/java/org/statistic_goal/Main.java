package org.statistic_goal;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args){
        //Path csv =  Paths.get("src", "test", "java","tests","test.csv");
        Path csv =  Paths.get("src", "test", "java","goalscorers.csv");
        GoalStatistics.getGoalStatistics(csv.toString());
    }
}