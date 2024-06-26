package com.example;


public class Potion extends Position {
    private int healthRestore;

    public Potion(double x, double y, int healthRestore) {
        super(x, y);
        this.healthRestore = healthRestore;
    }

    public void consume(Player player) {
        player.addHealth(healthRestore);
    }

    @Override
    public void interact(Player player) {
        consume(player);
    }
}
