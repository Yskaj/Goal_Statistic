package org.statistic_goal;

import java.nio.file.Path;

public class ConvertCVStoJSON extends Converter {
    ConvertCVStoJSON(Path path) {
        super(path);
    }

    @Override
    void convert() {
        ReadFile readFile = new ReadFileCSV();
        Sorter sorter = new SorterCSV();
        WriteFile writeFile = new WriteFileJSON();
        super.converting(readFile, sorter, writeFile);
    }
}
