package com.example;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MazeMap implements Serializable {
    private List<List<Integer>> layout;
    private final int TILE_SIZE = 20; //kafelki

    public void loadMap(String lvlMapPath) {
        layout = new ArrayList<>();
       ;

        try (Scanner scanner = new Scanner(getClass().getResourceAsStream(lvlMapPath))) {
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
