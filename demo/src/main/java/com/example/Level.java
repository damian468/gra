package com.example;


import java.io.Serializable;
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class Level implements Serializable {
    private MazeMap map;
    private Player player;
    private List<Enemy> enemies;
    private List<Trap> traps;
    private List<Door> doors;
    private List<Key> keys;
    private List<Lever> levers;
    private List<Potion> potions;
    private List<Treasure> treasures;
    private final int TILE_SIZE = 20;
    private final int ITEM_SIZE = 15;



    public Level(int level) {
        this.enemies = new ArrayList<>();
        this.traps = new ArrayList<>();
        this.doors = new ArrayList<>();
        this.keys = new ArrayList<>();
        this.levers = new ArrayList<>();
        this.potions = new ArrayList<>();
        this.treasures = new ArrayList<>();
        loadLevelData(level);

    }

    public void loadLevelData(int level) {
        map = new MazeMap();
        String lvlMapPath, lvlconfigPath;
        switch (level){
            case 1: lvlMapPath = "/levels/level1.txt";
                lvlconfigPath = "/levels/config_lvl1.txt";
                break;
            case 2: lvlMapPath = "/levels/level2.txt";
                lvlconfigPath = "/levels/config_lvl2.txt";
                break;
            case 3: lvlMapPath = "/levels/level3.txt";
                lvlconfigPath = "/levels/config_lvl3.txt";
                break;
            default: lvlMapPath = "/levels/level1.txt";
                lvlconfigPath = "/levels/config_lvl1.txt";
                break;
        }
        map.loadMap(lvlMapPath);

        player = new Player(1, 1);
        player.setMap(map);
        List<int[]> availablePositions = map.getAvailablePositions();

        int[] pos;


        Map<String, Integer> levelData = new HashMap<>();

        try (Scanner scanner =  new Scanner(getClass().getResourceAsStream(lvlconfigPath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(" : ");
                if (parts.length == 2) {
                    String item = parts[0].trim();
                    int number = Integer.parseInt(parts[1].trim());
                    levelData.put(item, number);
                }
            }
        }

        //doorX, doorY, ghosts, guardians, traps,potions, treasures, key


        if(levelData.get("Keys") > 0){

            for(int i = 0; i < levelData.get("Keys"); i++){
                pos = getRandomPosition(availablePositions);
                keys.add(new Key(pos[1], pos[0], 1));

            }
            doors.add(new Door(levelData.get("DoorX"),levelData.get("DoorY"), keys.get(0)));
        }
        else{
            pos = getRandomPosition(availablePositions);
            doors.add(new Door(levelData.get("DoorX"), levelData.get("DoorY"), null));
            levers.add(new Lever(pos[1], pos[0], doors.get(0)));

        }


        for(int i=0; i < levelData.get("Ghosts"); i++){
            pos = getRandomPosition(availablePositions);
            Ghost ghost = new Ghost(pos[1], pos[0], 10, 5);
            ghost.setMap(map);
            enemies.add(ghost);

        }

        Guardian guardian = new Guardian(levelData.get("DoorX"), levelData.get("DoorY"), 20, 3);
        guardian.setMap(map);
        enemies.add(guardian);
        for(int i=0; i < levelData.get("Guardians")-1; i++){
            pos = getRandomPosition(availablePositions);
            guardian = new Guardian(pos[1], pos[0], 20, 3);
            guardian.setMap(map);
            enemies.add(guardian);

        }


        for(int i=0; i < levelData.get("Traps"); i++){
            pos = getRandomPosition(availablePositions);
            traps.add(new Trap(pos[1], pos[0], 10));

        }



        for(int i=0; i < levelData.get("Potions"); i++) {
            pos = getRandomPosition(availablePositions);
            potions.add(new Potion(pos[1], pos[0], 20));
        }
        for(int i=0; i < levelData.get("Treasures"); i++) {
            pos = getRandomPosition(availablePositions);
            treasures.add(new Treasure(pos[1], pos[0]));
        }


    }

    private int[] getRandomPosition(List<int[]> availablePositions) {
        Random rand = new Random();
        int index = rand.nextInt(availablePositions.size());
        int[] pos = availablePositions.get(index);
        availablePositions.remove(index);
        return pos;
    }

    public Player getPlayer() {
        return player;
    }

    public void update() {
        for (Enemy enemy : enemies) {
            enemy.move();
            if (enemy.getX() == player.getX() && enemy.getY() == player.getY()) {
                enemy.interact(player);
            }
        }

        for (Door door : doors) {
            if (door.getX() == player.getX() && door.getY() == player.getY()) {
                if(door.canOpen(player)){
                    door.open();
                }
            }
        }



        for (Trap trap : traps) {
            if (trap.getX() == player.getX() && trap.getY() == player.getY()) {
                trap.trigger(player);
            }
        }

        for (Lever lever : levers) {
            if (lever.getX() == player.getX() && lever.getY() == player.getY()) {
                lever.activate();
            }
        }

        List<Key> keysToRemove = new ArrayList<>();
        for (Key key : keys) {
            if (key.getX() == player.getX() && key.getY() == player.getY()) {
               key.interact(player);
                keysToRemove.add(key);
            }
        }
        keys.removeAll(keysToRemove);

        List<Potion> potionsToRemove = new ArrayList<>();
        for (Potion potion : potions) {
            if (potion.getX() == player.getX() && potion.getY() == player.getY()) {
                potion.consume(player);
                potionsToRemove.add(potion);
            }
        }
        potions.removeAll(potionsToRemove);

        List<Treasure> treasuresToRemove = new ArrayList<>();
        for (Treasure treasure : treasures) {
            if (!treasure.isCollected() && treasure.getX() == player.getX() && treasure.getY() == player.getY()) {
                treasure.collect(player);
                treasuresToRemove.add(treasure);
            }
        }
        treasures.removeAll(treasuresToRemove);
    }

    public void render(GraphicsContext gc) {
        map.render(gc);
        Image ghostImage = new Image(getClass().getResourceAsStream("/graph/ghost.png"));
        Image guardianImage = new Image(getClass().getResourceAsStream("/graph/guardian.png"));
        Image playerImage = new Image(getClass().getResourceAsStream("/graph/player.png"));
        Image keyImage = new Image(getClass().getResourceAsStream("/graph/key.png"));
        Image gemImage = new Image(getClass().getResourceAsStream("/graph/gem.png"));
        Image potionImage = new Image(getClass().getResourceAsStream("/graph/potion.png"));
        Image trapImage = new Image(getClass().getResourceAsStream("/graph/trap.png"));
        gc.drawImage(playerImage, player.getX() * TILE_SIZE, player.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);

        for (Enemy enemy : enemies) {
            if(enemy instanceof Ghost){
                gc.drawImage(ghostImage, enemy.getX()* TILE_SIZE, enemy.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
            else{
                gc.drawImage(guardianImage, enemy.getX()* TILE_SIZE, enemy.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }


        }

        // Rysowanie pułapek

        for (Trap trap : traps) {
            if(trap.isInteracted()){
                gc.drawImage(trapImage, trap.getX()* TILE_SIZE, trap.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }
            else{
                gc.setFill(Color.WHITE);
                gc.fillRect(trap.getX() * TILE_SIZE, trap.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
            }

        }

        // Rysowanie drzwi
        Image doorImage;
        for (Door door : doors) {
            if (door.isOpen()) {
                doorImage = new Image(getClass().getResourceAsStream("/graph/openeddoor.png"));

            } else {
                doorImage = new Image(getClass().getResourceAsStream("/graph/door.png"));

            }
            gc.drawImage(doorImage, door.getX() * TILE_SIZE, door.getY() * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // Rysowanie kluczy
        for (Key key : keys) {
            gc.drawImage(keyImage, key.getX() * TILE_SIZE, key.getY()  * TILE_SIZE, ITEM_SIZE, ITEM_SIZE);

        }

        // Rysowanie dźwigni
        Image leverImage;
        for (Lever lever : levers) {
            if(lever.getLinkedDoor().isOpen()){
                leverImage = new Image(getClass().getResourceAsStream("/graph/lever.png"));
            }
            else{
                leverImage = new Image(getClass().getResourceAsStream("/graph/lever2.png"));
            }
            gc.drawImage(leverImage, lever.getX() * TILE_SIZE, lever.getY()  * TILE_SIZE, ITEM_SIZE, ITEM_SIZE);
        }

        // Rysowanie eliksirów

        for (Potion potion : potions) {
            gc.drawImage(potionImage, potion.getX() * TILE_SIZE, potion.getY()  * TILE_SIZE, ITEM_SIZE, ITEM_SIZE);
        }


        // Rysowanie skarbów

        for (Treasure treasure : treasures) {
            gc.drawImage(gemImage, treasure.getX() * TILE_SIZE, treasure.getY()  * TILE_SIZE, ITEM_SIZE, ITEM_SIZE);

        }
    }

    public boolean isLevelCompleted() {
        for (Door door : doors) {
            if (door.isOpen() && player.getX() == door.getX() && player.getY() == door.getY()) {
                return true;
            }
        }
        return false;
    }
}
