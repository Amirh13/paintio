package com.example.paintio;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class paintio2 extends Application {

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

        // Handle mouse click event
        scene.setOnMouseClicked(this::handleMouseClick);

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

    private void handleMouseClick(MouseEvent event) {
        // Calculate the direction of movement based on the mouse click position relative to the player
        double mouseX = event.getX();
        double mouseY = event.getY();
        double playerCenterX = (playerX * TILE_SIZE) + TILE_SIZE / 2;
        double playerCenterY = (playerY * TILE_SIZE) + TILE_SIZE / 2;
        double deltaX = mouseX - playerCenterX;
        double deltaY = mouseY - playerCenterY;

        // Update the direction of movement
        if (Math.abs(deltaX) > Math.abs(deltaY)) {
            dy = 0;
            dx = (int) Math.signum(deltaX) * TILE_SIZE;
        } else {
            dx = 0;
            dy = (int) Math.signum(deltaY) * TILE_SIZE;
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






