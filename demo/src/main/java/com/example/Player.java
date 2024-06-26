package com.example;

import java.util.ArrayList;
import java.util.List;

public class Player extends Position {
    private int health;
    private List<Key> inventory;
    private Score score;
    private MazeMap mazeMap;

    public Player(double x, double y) {
        super(x, y);
        this.health = 100;
        this.inventory = new ArrayList<>();
        this.score = new Score();
    }

    public void setMap(MazeMap mazeMap) {
        this.mazeMap = mazeMap;
    }

    public void move(String direction) {
        double newX = x;
        double newY = y;

        switch (direction) {
            case "UP":
                newY -= 1;
                break;
            case "DOWN":
                newY += 1;
                break;
            case "LEFT":
                newX -= 1;
                break;
            case "RIGHT":
                newX += 1;
                break;
        }

        if (mazeMap.canMoveTo(newX, newY,false)) {
            x = newX;
            y = newY;
        }
    }



    public int getHealth() {
        return health;
    }

    public void reduceHealth(int amount) {
        health -= amount;
        if (health < 0) {
            health = 0;
        }
    }

    public void decreaseScore(int amount) {
        score.addPoints(-1*amount);
        if (score.getPoints() < 0) {
            score.setPoints(0);
        }
    }

    public void addHealth(int amount) {
        health += amount;
    }



    public List<Key> getInventory() {
        return inventory;
    }

    public void setInventory(Key key) {
        inventory.add(key);
    }
    public Score getScore() {
        return score;
    }

    @Override
    public void interact(Player player) {
        // ze soba to on nie zinteraktuje zabardzo XD
    }


}
