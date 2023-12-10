package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;
import java.util.Random;

/**
 * A serializable representation of a Bonus.
 */
public class Bonus implements Serializable {
    /** The Rectangle representing the bonus. */
    public Rectangle choco;

    /** The x-coordinate of the bonus. */
    public double x;

    /** The y-coordinate of the bonus. */
    public double y;

    /** The time when the bonus was created. */
    public long timeCreated;

    /** Indicates whether the bonus has been taken. */
    public boolean taken = false;

    /**
     * Constructs a Bonus with the specified row and column.
     *
     * @param row    The row of the Bonus.
     * @param column The column of the Bonus.
     */
    public Bonus(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + ((double) Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + ((double) Block.getHeight() / 2) - 15;

        draw();
    }

    /**
     * Draws the bonus by creating a Rectangle and setting its properties.
     */
    private void draw() {
        choco = new Rectangle();
        choco.setWidth(30);
        choco.setHeight(30);
        choco.setX(x);
        choco.setY(y);

        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }

        choco.setFill(new ImagePattern(new Image(url)));
    }
}
