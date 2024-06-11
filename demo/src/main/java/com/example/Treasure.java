package com.example;


import java.util.Random;

public class Treasure extends Position {
    private boolean isCollected;
    private Score score;
    public Treasure(double x, double y) {
        super(x, y);
        this.isCollected = false;
        this.score = new Score();
        Random random = new Random();
        score.addPoints(random.nextInt(10)*10);
    }

    public void collect(Player player) {
        isCollected = true;
        player.getScore().addPoints(score.getPoints());
    }

    public boolean isCollected() {
        return isCollected;
    }

    @Override
    public void interact(Player player) {
        if (!isCollected) {
            collect(player);
        }
    }
}
