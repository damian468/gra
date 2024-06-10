package com.example;

import java.io.Serializable;

public class Score implements Serializable {
    private int points;

    public Score() {
        this.points = 0;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {this.points = points;}
}
