package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class Penalty {
    public Rectangle crackedBrick;

    public double x;
    public double y;
    public long timeCreated;
    public boolean taken = false;

    public Penalty(int row, int column) {
        x = (column * (Block.getWidth())) + Block.getPaddingH() + (Block.getWidth() / 2) - 15;
        y = (row * (Block.getHeight())) + Block.getPaddingTop() + (Block.getHeight() / 2) - 15;

        draw();
    }

    private void draw() {
        crackedBrick = new Rectangle();
        crackedBrick.setWidth(30);
        crackedBrick.setHeight(30);
        crackedBrick.setX(x);
        crackedBrick.setY(y);

        String url;
        if (new Random().nextInt(20) % 2 == 0) {
            url = "bonus1.png";
        } else {
            url = "bonus2.png";
        }

        crackedBrick.setFill(new ImagePattern(new Image(url)));
    }
}
