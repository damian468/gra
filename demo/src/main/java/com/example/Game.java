package com.example;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.io.IOException;


public class Game extends Application implements Serializable{
    private List<Level> levels;
    private int currentLevelIndex;
    private Score score;
    private boolean isGameOver;
    private boolean isPaused;
    private Label scoreLabel;
    private Label healthLabel;
    @Override
    public void start(Stage stage) {
        score = new Score();
        levels = new ArrayList<>();
        loadLevels();
        currentLevelIndex = 0;
        isGameOver = false;
        isPaused = false;

        Canvas canvas = new Canvas(800, 580);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        BorderPane root = new BorderPane();
        root.setCenter(canvas);


        scoreLabel = new Label("Score: 0");
        healthLabel = new Label("Health: 100");
        Button startButton = new Button("Start");
        Button pauseButton = new Button("Pause");
        Button resumeButton = new Button("Resume");
        Button saveButton = new Button("Save");
        Button loadButton = new Button("Load");
        Button restartButton = new Button("Restart");



        VBox panel = new VBox();

        panel.setStyle("-fx-background-color: lightgray; -fx-padding: 10px; -fx-border-color: black; -fx-border-width: 2px;");
        panel.getChildren().addAll(scoreLabel, healthLabel, restartButton, pauseButton, resumeButton, saveButton, loadButton);
        root.setRight(panel);





        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("2D Labiryntowa Gra");
        stage.setResizable(false);
        stage.show();

        scene.setOnKeyPressed(this::handleKeyInput);

        startButton.setOnAction(e -> startGame());
        pauseButton.setOnAction(e -> pauseGame());
        resumeButton.setOnAction(e -> resumeGame());
        saveButton.setOnAction(e -> saveGame("game_save.ser"));
        loadButton.setOnAction(e -> loadGame("game_save.ser"));
        restartButton.setOnAction(e -> restartGame());

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && !isGameOver) {
                    update();
                    render(gc);
                }
            }
        }.start();

        canvas.addEventFilter(MouseEvent.MOUSE_PRESSED, e -> {
            canvas.requestFocus();
        });

        panel.setFocusTraversable(false);
        startButton.setFocusTraversable(false);
        pauseButton.setFocusTraversable(false);
        resumeButton.setFocusTraversable(false);
        saveButton.setFocusTraversable(false);
        loadButton.setFocusTraversable(false);
        restartButton.setFocusTraversable(false);
    }




    private void startGame() {
        isPaused = false;
        isGameOver = false;
        currentLevelIndex = 0;
        score = new Score();
        loadLevels();
        updateScore();
        updateHealth();
    }

    private void pauseGame() {
        isPaused = true;
    }

    private void resumeGame() {
        isPaused = false;
    }

    private void restartGame() {
        isGameOver = false;
        currentLevelIndex = 0;
        loadLevels();
        updateScore();
        updateHealth();
    }

    private void loadLevels() {
        levels.clear();
        for(int i=1;i<4;i++){
            levels.add(new Level(i));
        }

    }

    private void update() {
        if (!isGameOver) {
            levels.get(currentLevelIndex).update();
            updateScore();
            updateHealth();
            checkGameOver();
            checkLevelCompletion();

        }
    }

    private void render(GraphicsContext gc) {
        gc.clearRect(0, 0, 800, 600); // Dostosowanie do zmniejszonego okna
        levels.get(currentLevelIndex).render(gc);
    }

    private void checkLevelCompletion() {
        if (levels.get(currentLevelIndex).isLevelCompleted()) {
            score.addPoints(levels.get(currentLevelIndex).getPlayer().getScore().getPoints());
            currentLevelIndex++;
            if (currentLevelIndex >= levels.size()) {
                isGameOver = true;
                showEndGameAlert("You win!", "Congratulations! You completed all levels!");

            }

        }
    }

    private void checkGameOver() {
        if (levels.get(currentLevelIndex).getPlayer().getHealth() <= 0) {
            isGameOver = true;
            showEndGameAlert("Game Over!", "You lost your health!");
            System.out.println("Game Over!");
        }
    }

    private void handleKeyInput(KeyEvent event) {
        if (!isGameOver && !isPaused) {
            if (event.getCode() == KeyCode.UP) {
                levels.get(currentLevelIndex).getPlayer().move("UP");
            } else if (event.getCode() == KeyCode.DOWN) {
                levels.get(currentLevelIndex).getPlayer().move("DOWN");
            } else if (event.getCode() == KeyCode.LEFT) {
                levels.get(currentLevelIndex).getPlayer().move("LEFT");
            } else if (event.getCode() == KeyCode.RIGHT) {
                levels.get(currentLevelIndex).getPlayer().move("RIGHT");
            }
            update();
        }
    }

    private void updateScore() {

        scoreLabel.setText("Score: " + levels.get(currentLevelIndex).getPlayer().getScore().getPoints()+" ");
    }

    private void updateHealth() {
        healthLabel.setText("Health: " + levels.get(currentLevelIndex).getPlayer().getHealth());
    }

    public void saveGame(String filePath) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(levels);
            oos.writeObject(currentLevelIndex);
            oos.writeObject(score);

            showAlert("", "Game saved successfully.");
        } catch (IOException e) {
            showAlert("Save Error", "An error occurred while saving the game.");

        }
    }

    public void loadGame(String filePath) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            levels = (List<Level>) ois.readObject();
            currentLevelIndex = (Integer) ois.readObject();
            score = (Score) ois.readObject();
            isGameOver = false;
            isPaused = false;
            updateScore();

        } catch (IOException | ClassNotFoundException e) {
            showAlert("Load Error", "cant find the file to load.");

        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showEndGameAlert(String title, String message) {
        Platform.runLater(() -> {
            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initStyle(StageStyle.UTILITY);
            dialog.setTitle(title);

            Label label = new Label(message +"\nYour score: " + score.getPoints() + "\nDo you want to play again?");
            Button restartButton = new Button("Restart");
            Button exitButton = new Button("Exit");

            restartButton.setOnAction(e -> {
                restartGame();
                dialog.close();
            });

            exitButton.setOnAction(e -> {
                exitGame();
                dialog.close();
            });

            VBox vbox = new VBox(10, label, restartButton, exitButton);
            vbox.setStyle("-fx-padding: 10; -fx-alignment: center;");

            Scene dialogScene = new Scene(vbox, 300, 150);
            dialog.setScene(dialogScene);
            dialog.showAndWait();

        });

    }

    private void exitGame() {
        System.exit(0);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
