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

public class Paint extends Application {

    private static final int TILE_SIZE = 20;
    private static final int NUM_TILES = 31; // Number of tiles in each row/column

    private int playerX = 15; // Player's X position (tile index)
    private int playerY = 15; // Player's Y position (tile index)

    private int viewX = 0; // View's X position (in pixels)
    private int viewY = 0; // View's Y position (in pixels)

    private int dx = 0; // View's X direction of movement
    private int dy = 0; // View's Y direction of movement

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
        for (int i = 0; i < NUM_TILES; i++) {
            for (int j = 0; j < NUM_TILES; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.WHITE);
                } else {
                    gc.setFill(Color.LIGHTGREY);
                }
                gc.fillRect(i * TILE_SIZE - viewX, j * TILE_SIZE - viewY, TILE_SIZE, TILE_SIZE);
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
                dy = TILE_SIZE;
                break;
            case DOWN:
                dy = -TILE_SIZE;
                break;
            case LEFT:
                dx = TILE_SIZE;
                break;
            case RIGHT:
                dx = -TILE_SIZE;
                break;
        }
    }

    private void startAutomaticMovement(GraphicsContext gc) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            // Update the view's position based on the direction of movement
            viewX += dx;
            viewY += dy;

            // Redraw the scene
            drawTerrain(gc);
            drawPlayer(gc);

            // Reset the movement direction after updating the view
            dx = 0;
            dy = 0;
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
