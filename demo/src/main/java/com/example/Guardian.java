package com.example;

import java.util.Random;

public class Guardian extends Enemy {
    private Map map;
    private Random random = new Random();

    public Guardian(double x, double y, int damage, int stepLimit) {
        super(x, y, damage, stepLimit);
    }

    @Override
    protected void performMove() {
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};
        int direction = random.nextInt(4);
        double newX = getX() + dx[direction];
        double newY = getY() + dy[direction];
        if (map.canMoveTo(newX, newY,false)) {
            setX(newX);
            setY(newY);
        }
    }

    @Override
    public void interact(Player player) {
        player.reduceHealth(damage);
    }

    public void setMap(Map map) {
        this.map = map;
    }
}
