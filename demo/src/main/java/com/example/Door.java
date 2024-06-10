package com.example;

import java.io.Serializable;

public class Door extends Item {
    private boolean isOpen;
    private Key key;

    public Door(double x, double y, Key key) {
        super(x, y);
        this.isOpen = false;
        this.key = key;
    }

    public Key getKey() {return key;}
    public void open() {
        isOpen = true;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public boolean canOpen(Player player) {
        return player.getInventory().contains(key);
    }

    @Override
    public void interact(Player player) {
        if (canOpen(player)) {
            open();
        }
    }
}
