package brickGame;

import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GameInitializer {
    private int level = 0;
    private int sceneWidth = 500;
    private int sceneHeight = 700;
    private Circle ball;
    private int ballRadius = 10;
    public int getBallRadius() { return ballRadius; }
    private double xBall;
    private double yBall;
    private Rectangle rect;
    private double xBreak = 0.0f;
    private double yBreak = 640.0f;
    private int breakWidth = 130;
    public int getBreakWidth() { return breakWidth; }
    private int halfBreakWidth = breakWidth / 2;
    public int getHalfBreakWidth() { return halfBreakWidth; }
    private int breakHeight = 30;
    public int getBreakHeight() { return breakHeight; }
    private boolean isExistHeartBlock = false;
    private boolean loadFromSave = false;
    private ArrayList<Block> blocks = new ArrayList<Block>();
    private Color[] colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };
    private Main main;
    private GameEngine engine;
//    public void setMain(Main main) { this.main = main; }
//
//    public Main getMain() {
//        return main;
//    }

    public GameInitializer(Main main) {
        this.main = main;
//        setMain(getMain());
//        this.main = getMain();
        if(main == null) {
            System.out.println("Constructor Init main is null");
        } else {System.out.println("GameInitNot null");}
    }
    public Circle getBall() {
        return ball;
    }
    public double getxBall() { return xBall; }
    public void setxBall(double x) {xBall = x; }
    public void setyBall(double y) {yBall = y; }
    public double getyBall() { return yBall; }
    public double getxBreak() { return xBreak; }
    public void setxBreak(double count) { xBreak = count; }
    public boolean getLoadFromSave() { return loadFromSave; }
    public void setLoadFromSave(boolean load) { loadFromSave = load; }

    public void initBall() {
        Random random = new Random();
        xBall = random.nextInt(sceneWidth) + 1;
        yBall = random.nextInt(sceneHeight - 200) + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));
    }

    public void initBreak() {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);
    }

    public Rectangle getRect() {
        return rect;
    }
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
                //System.out.println("colors " + r % (colors.length));
                System.out.println(blocks.size()+"Yoo");
            }
        }
    }

    public void initializeEngine(GameEngine.OnAction onAction) {
        if (engine != null) {
            // Stop the existing engine if it's running
            engine.stop();
        }
        engine = new GameEngine();
        engine.setOnAction(onAction);
        System.out.println("test");
        engine.setFps(120);
        engine.start();
        System.out.println("Blocks in initializeEngine"+getBlocks());
    }

    public void stopEngine() {
        if (engine != null) {
            engine.stop();
        }
    }

    public int getLevel() { return level; }
    public void setLevel(int count) { level = count; }

    public void startLevel() {
        if (level > 1 && level < 18) {
            if(main == null) {
                System.out.println("main is null");
            } else {System.out.println("Not null");}
            initializeEngine(main.getUiController().getGameController());

        } else{
            engine = new GameEngine();
            engine.setOnAction(main.getUiController().getGameController());
            engine.setFps(120);
            engine.start();
            loadFromSave = false;
        }
    }
}
