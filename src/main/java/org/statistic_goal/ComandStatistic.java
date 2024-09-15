package org.statistic_goal;

import java.util.*;

public class ComandStatistic {
    String name;
    int goals_scored;
    int goals_missed;
    Map<String, Goal> scorers; //Список забивших игроков
    String bestScorer;
    int bestGoal;

    public ComandStatistic(String name) {
        this.name = name;
        this.goals_scored = 0;
        this.goals_missed = 0;
        this.scorers = new TreeMap<>();
    }

    public void addGoal(String scorerName, boolean penalty) {//Команда забила гол
        goals_scored++;
        if (!scorers.containsKey(scorerName)) {
            if (!penalty) {//пенальти
                scorers.put(scorerName, new Goal(1, 0));
            } else {
                scorers.put(scorerName, new Goal(1, 1));
            }
        } else {
            Goal goals = scorers.get(scorerName);
            goals.goals++;
            if (penalty) goals.penalty++;
        }
        computeBestScorer();
    }

    void addMissed(String nameScorer) { // Команде забили Автогол
        goals_missed++;
        if (scorers.containsKey(nameScorer)) { //Есть ли Игрок в списке бомбардиров?
            scorers.get(nameScorer).goals++;   // Добавим игроку очков за автогол
        } else {
            scorers.put(nameScorer, new Goal(1, 0));
        }
        computeBestScorer();
    }

    void addMissed() { // Команде забили гол
        goals_missed++;
    }

    public List<String> getComandData() {
        return new ArrayList<>(Arrays.asList(name, String.valueOf(goals_scored),
                String.valueOf(goals_missed),bestScorer,String.valueOf(bestGoal)));
    }
    private void computeBestScorer(){
        bestScorer = Collections.max(scorers.entrySet(), Map.Entry.comparingByValue()).getKey();
        bestGoal = scorers.get(bestScorer).goals;
    }
}

class Goal implements Comparable<Goal> {
    int goals;
    int penalty;

    public Goal(int goals, int penalty) {
        this.goals = goals;
        this.penalty = penalty;
    }

    @Override
    public int compareTo(Goal o) {
        if (this.goals == o.goals) {//если кол-во голов равно, сравним пенальти
            return (this.penalty < o.penalty) ? 1 : -1;
        }
        return (this.goals > o.goals) ? 1 : -1;
    }
}

