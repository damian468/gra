package com.example;

import java.io.Serializable;

public abstract class Enemy implements Serializable {
    private double x;
    private double y;
    protected int damage;
    private int stepCounter;
    private int stepLimit;

    public Enemy(double x, double y, int damage, int stepLimit) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.stepCounter = 0;
        this.stepLimit = stepLimit;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getDamage() {
        return damage;
    }

    public void move() {
        stepCounter++;
        if (stepCounter >= stepLimit) {
            stepCounter = 0;
            performMove();
        }
    }

    protected abstract void performMove();

    public abstract void interact(Player player);
}
