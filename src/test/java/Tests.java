import org.statistic_goal.GoalStatistics;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Tests {
    @DisplayName("Test Goals")
    @Test
    public void testGoals() throws IOException {
        Path csv = Paths.get("src", "test", "java", "tests", "test.csv");
        GoalStatistics.getGoalStatistics(csv.toString());
        Path json = Path.of(csv.toString().substring(0, csv.toString().length() - 4) + "JSON.json");
        Path jsonExpected = Path.of(csv.toString().substring(0, csv.toString().length() - 4) + "JSONExpect.json");
        String actual = Files.readAllLines(json).toString();
        String expected = Files.readAllLines(jsonExpected).toString();
        System.out.println(actual);
        System.out.println(expected);
        assertEquals(actual, expected);
    }
}
