package ClassesTests;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.statistic_goal.ReadFile;
import org.statistic_goal.ReadFileCSV;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ReaderTests {
    String path = "src\\test\\java\\ClassesTests\\test_csv\\reader\\";
    @DisplayName("Test Goals")
    @Test
    public void r() {
        ReadFile readFile = new ReadFileCSV();
        readFile.read(path+"withoutNames.csv");
//        assertEquals();
    }
}
