package org.statistic_goal;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SorterCSV implements Sorter {
    List<List<String>> sortingList;

    SorterCSV() {
        sortingList = new ArrayList<>();
    }

    @Override
    public List<List<String>> getData() {
        return sortingList;
    }

    @Override
    public void sort(ReadFile readFile) {
        Map<String, ComandStatistic> data = (Map<String, ComandStatistic>) readFile.getData();
        addMatches(data);
        sortingList.sort((o1, o2) -> {
            int firstGoals = Integer.parseInt(o1.get(1));
            int secondGoals = Integer.parseInt(o2.get(1));
            if (firstGoals == secondGoals) {
                return (o1.get(0).compareTo(o2.get(0)));//сортируем команды по имени
            }
            return (firstGoals < secondGoals) ? 1 : -1;//сортируем по кол-ву забитых мячей
        });
    }

    private void addMatches(Map<String, ComandStatistic> data) {
        for (ComandStatistic cs : data.values()) {
            List<String> comand = cs.getComandData();
            sortingList.add(comand);
        }
    }
}
