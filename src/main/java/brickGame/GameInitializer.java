package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Random;

public class GameInitializer {
    private int level = 0;
    private int sceneWidth = 500;
    private int sceneHeight = 700;
    private Circle ball;
    private int       ballRadius = 10;
    private double xBall;
    private double yBall;
    private Rectangle rect;
    private double xBreak = 0.0f;
    private double yBreak = 640.0f;
    private int breakWidth     = 130;
    private int breakHeight    = 30;
    public Circle getBall() {
        return ball;
    }
    public void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeight - 200) + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    public Rectangle getRect() { return rect; }

    public void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);
    }
}
