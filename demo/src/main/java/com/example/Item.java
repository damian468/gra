package com.example;

import java.io.Serializable;

public abstract class Item implements Serializable {
    protected double x;
    protected double y;
    protected boolean isInteracted;
    public Item(double x, double y) {
        this.x = x;
        this.y = y;
        this.isInteracted = false;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public boolean isInteracted() {
        return isInteracted;
    }
    public abstract void interact(Player player);
}
