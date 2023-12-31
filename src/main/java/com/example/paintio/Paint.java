package com.example.paintio;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Paint extends Application {

    private static final int TILE_SIZE = 20;
    private static final int NUM_TILES = 31; // Number of tiles in each row/column

    private int playerX = 15; // Player's X position (tile index)
    private int playerY = 15; // Player's Y position (tile index)

    private double viewX = 0; // View's X position (in pixels)
    private double viewY = 0; // View's Y position (in pixels)

    private int dx = 0; // View's X direction of movement
    private int dy = 0; // View's Y direction of movement

    private List<Pixel> passedPixels = new ArrayList<>(); // Pixels that passed over the player

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene scene = new Scene(root, NUM_TILES * TILE_SIZE, NUM_TILES * TILE_SIZE);

        Canvas canvas = new Canvas(NUM_TILES * TILE_SIZE, NUM_TILES * TILE_SIZE);
        GraphicsContext gc = canvas.getGraphicsContext2D();

        drawTerrain(gc);
        drawPlayer(gc);

        root.getChildren().add(canvas);

        scene.setOnKeyPressed(event -> handleKeyPress(event.getCode()));

        primaryStage.setTitle("Checkered Terrain");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Start the automatic movement
        startAutomaticMovement(gc);
    }

    private void drawTerrain(GraphicsContext gc) {
        int startTileX = (int) Math.floor(viewX / TILE_SIZE);
        int startTileY = (int) Math.floor(viewY / TILE_SIZE);
        int endTileX = (int) Math.ceil((viewX + NUM_TILES * TILE_SIZE) / TILE_SIZE);
        int endTileY = (int) Math.ceil((viewY + NUM_TILES * TILE_SIZE) / TILE_SIZE);

        for (int i = startTileX; i < endTileX; i++) {
            for (int j = startTileY; j < endTileY; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.LIGHTGREY);
                }
                gc.fillRect(i * TILE_SIZE - viewX, j * TILE_SIZE - viewY, TILE_SIZE, TILE_SIZE);

                if (passedPixels.contains(new Pixel(i, j))) {
                    gc.setFill(Color.rgb(255,0,0,.5));
                    gc.fillRect(i * TILE_SIZE - viewX, j * TILE_SIZE - viewY, TILE_SIZE, TILE_SIZE);
                }
            }
        }
    }

    private void drawPlayer(GraphicsContext gc) {
        gc.setFill(Color.RED);
        gc.fillRect(playerX * TILE_SIZE, playerY * TILE_SIZE, TILE_SIZE, TILE_SIZE);
    }

    private void handleKeyPress(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                dx = 0;
                dy = -TILE_SIZE;
                break;
            case DOWN:
                dx = 0;
                dy = TILE_SIZE;
                break;
            case LEFT:
                dx = -TILE_SIZE;
                dy = 0;
                break;
            case RIGHT:
                dx = TILE_SIZE;
                dy = 0;
                break;
        }
    }

    private void startAutomaticMovement(GraphicsContext gc) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            // Update the view's position based on the direction of movement
            viewX += dx;
            viewY += dy;

            // Check if any pixels pass over the player and add them to the list
            int playerTileX = (int) Math.floor(playerX + viewX / TILE_SIZE);
            int playerTileY = (int) Math.floor(playerY + viewY / TILE_SIZE);
            passedPixels.add(new Pixel(playerTileX, playerTileY));

            // Redraw the scene
            drawTerrain(gc);
            drawPlayer(gc);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private static class Pixel {
        private int x;
        private int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            Pixel pixel = (Pixel) obj;
            return x == pixel.x && y == pixel.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}

