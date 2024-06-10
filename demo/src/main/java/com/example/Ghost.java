package com.example;

import java.util.Random;

public class Ghost extends Enemy {
    private MazeMap mazeMap;
    private Random random = new Random();

    public Ghost(double x, double y, int damage, int stepLimit) {
        super(x, y, damage, stepLimit);
    }

    @Override
    protected void performMove() {
        int[] dx = {-1, 0, 1, 0};
        int[] dy = {0, -1, 0, 1};
        int direction = random.nextInt(4);
        double newX = getX() + dx[direction];
        double newY = getY() + dy[direction];
        if (mazeMap.canMoveTo(newX, newY, true)) {
            setX(newX);
            setY(newY);
        }
    }

    @Override
    public void interact(Player player) {
       player.decreaseScore(10);
    }

    public void setMap(MazeMap mazeMap) {
        this.mazeMap = mazeMap;
    }
}
