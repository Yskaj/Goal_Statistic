package org.statistic_goal;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
    public static void main(String[] args) {
        Path path = Paths.get("src", "test", "java", "goalscorers.csv");
        Converter converter = new ConvertCVStoJSON(path);
        converter.convert();
    }
}