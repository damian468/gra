package com.example;

import java.io.Serializable;

public class Treasure extends Item {
    private boolean isCollected;

    public Treasure(double x, double y) {
        super(x, y);
        this.isCollected = false;
    }

    public void collect(Player player) {
        isCollected = true;
        player.getScore().addPoints(100);
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
