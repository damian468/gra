package com.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Map implements Serializable {
    private List<List<Integer>> layout;
    private final int TILE_SIZE = 20; //kafelki

    public void loadMap(int level) {
        layout = new ArrayList<>();
        String lvlpath;
        switch (level){
            case 1: lvlpath = "/levels/level1.txt";
            break;
            case 2: lvlpath = "/levels/level2.txt";
            break;
            case 3: lvlpath = "/levels/level3.txt";
            break;
            default: lvlpath = "/levels/level1.txt";
            break;
        }
        try (Scanner scanner = new Scanner(getClass().getResourceAsStream(lvlpath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<Integer> row = new ArrayList<>();
                Scanner lineScanner = new Scanner(line);
                while (lineScanner.hasNextInt()) {
                    row.add(lineScanner.nextInt());
                }
                lineScanner.close();
                layout.add(row);
            }
        }


    }

    public boolean canMoveTo(double x, double y, boolean isGhost) {
        int col = (int) x;
        int row = (int) y;
        if(isGhost){
            return row >= 0 && row < layout.size() && col >= 0 && col < layout.get(row).size();
        }
        return row >= 0 && row < layout.size() && col >= 0 && col < layout.get(row).size() && layout.get(row).get(col) == 0;
    }

    public List<int[]> getAvailablePositions() {
        List<int[]> positions = new ArrayList<>();
        for (int i = 0; i < layout.size(); i++) {
            for (int j = 0; j < layout.get(i).size(); j++) {
                if (layout.get(i).get(j) == 0) {
                    positions.add(new int[]{i, j});
                }
            }
        }
        return positions;
    }

    public void render(GraphicsContext gc) {

        for (int i = 0; i < layout.size(); i++) {
            for (int j = 0; j < layout.get(i).size(); j++) {
                if (layout.get(i).get(j) == 1) {
                    gc.setFill(Color.BLACK);
                    gc.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }
}
