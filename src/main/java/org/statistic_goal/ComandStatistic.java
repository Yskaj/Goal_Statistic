package org.statistic_goal;

import java.util.HashMap;

public class ComandStatistic {
    String name;
    int goals_scored;
    int goals_missed;
    HashMap<String, Goal> scorers; //Список забивших игроков

    public ComandStatistic(String name) {
        this.name = name;
        this.goals_scored = 0;
        this.goals_missed = 0;
        this.scorers = new HashMap<>();
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
    }

    void addMissed(String nameScorer) { // Команде забили Автогол
        goals_missed++;
        if (scorers.containsKey(nameScorer)) { //Есть ли Игрок в списке бомбардиров?
            scorers.get(nameScorer).goals++;   // Добавим игроку очков за автогол
        } else {
            scorers.put(nameScorer, new Goal(1, 0));
        }
    }
    void addMissed() { // Команде забили гол
        goals_missed++;
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