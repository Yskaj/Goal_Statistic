package org.statistic_goal;

import java.nio.file.Path;

public abstract class Converter {
    static Path path;

    Converter(Path path) {
        this.path = path;
    }

    void converting(ReadFile readFile, Sorter sorter, WriteFile writeFile) {
        readFile.read(path.toString());
        sorter.sort(readFile);
        writeFile.write(sorter);
    }

    abstract void convert();
}
