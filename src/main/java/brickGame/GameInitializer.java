package brickGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Random;

public class GameInitializer {
    private int level = 0;
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

    public GameInitializer(Main main) {
        this.main = main;
        if(main == null) {
            System.out.println("Constructor Init main is null");
        } else {System.out.println("GameInit Not null");}
    }
    public Circle getBall() {
        return ball;
    }
    public double getxBall() { return xBall; }
    public void setxBall(double x) {xBall = x; }
    public void setyBall(double y) {yBall = y; }
    public double getyBall() { return yBall; }
    public double getxBreak() { return xBreak; }
    public void setxBreak(double newXBreak) {
        // Ensure newXBreak stays within the range [0, 370]
        newXBreak = Math.max(0.0, Math.min(370.0, newXBreak));
        this.xBreak = newXBreak;
    }
    public boolean getLoadFromSave() { return loadFromSave; }
    public void setLoadFromSave(boolean load) { loadFromSave = load; }
    private UIController uiController = Main.getUiController();

    public void initBall(Pane pane) {
        Random random = new Random();
        xBall = random.nextInt(uiController.getSceneWidth()) + 1;
        yBall = random.nextInt(uiController.getSceneHeight() - ((level + 1) * Block.getHeight()) - 100) + ((level + 1) * Block.getHeight()) + 15;
//        yBall = random.nextInt(uiController.getSceneHeight() - 200) + ((level + 1) * Block.getHeight()) + 15;
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));

        // Add the ball to the Pane
        pane.getChildren().add(ball);
    }

    public void initBreak(Pane pane) {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);

        pane.getChildren().add(rect);
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
                } else if (r % 10 == 4) {
                    type = Block.BLOCK_PENALTY;
                }
                else {
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
    }

    public void stopEngine() {
        if (engine != null) {
            engine.stop();
        }
    }

    public int getLevel() { return level; }
    public void setLevel(int count) { level = count; }

    public void startLevel() {
        if (level > 1 && level < 4) {
            if(main == null) {
                System.out.println("main is null");
            } else {System.out.println("Not null");}
            initializeEngine(Main.getUiController().getLoader().getController());

        } else{
            loadFromSave = false;
        }
    }
    public void initializeElements(Pane pane) {
        initBall(pane);
        initBreak(pane);
        initBoard();
        if(!loadFromSave) {
            for (Block block : blocks) {
                pane.getChildren().add(block.rect);
            }
        }
    }

    public void initializeLoadElements(Pane pane) {
//        initBall(pane);
//        initBreak(pane);
        //initBoard();
        if(loadFromSave) {
            for (Block block : blocks) {
                pane.getChildren().add(block.rect);
            }
        }
        System.out.println("BlocksNum generated: " + blocks.size());
    }
}

