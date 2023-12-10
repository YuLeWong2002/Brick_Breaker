package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Random;

/**
 * The {@code Penalty} class represents a penalty object in the game. Penalties are typically
 * spawned on the game grid and can be collected by the player for various effects.
 */
public class Penalty {
    /**
     * The graphical representation of the penalty, displayed as a rectangle on the game grid.
     */
    public Rectangle crackedBrick;

    /**
     * The x-coordinate of the penalty's position.
     */
    public double x;

    /**
     * The y-coordinate of the penalty's position.
     */
    public double y;

    /**
     * The time at which the penalty was created.
     */
    public long timeCreated;

    /**
     * Indicates whether the penalty has been taken or collected by the player.
     */
    public boolean taken = false;

    /**
     * Constructs a penalty object at the specified row and column on the game grid.
     *
     * @param row    The row on the game grid where the penalty is located.
     * @param column The column on the game grid where the penalty is located.
     */
    public Penalty(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    /**
     * Initializes the graphical representation of the penalty, setting its dimensions,
     * position, and filling it with an image pattern.
     */
    private void draw() {
        crackedBrick = new Rectangle();
        crackedBrick.setWidth(30);
        crackedBrick.setHeight(30);
        crackedBrick.setX(x);
        crackedBrick.setY(y);

        // Randomly select an image for the penalty
        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "FireBall1.png";
        } else {
            url = "FireBall2.png";
        }

        crackedBrick.setFill(new ImagePattern(new Image(url)));
    }
}