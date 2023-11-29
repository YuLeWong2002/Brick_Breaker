package brickGame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;



public class GameController implements EventHandler<KeyEvent> {
    private Main main;
    private UIController uiController;
    private GameInitializer gameInitializer;  // Remove the local instance
    public GameController(Main main, UIController uiController, Stage primaryStage, GameInitializer gameInitializer) {
        this.main = main;
        this.uiController = uiController;
        this.primaryStage = primaryStage;
        this.gameInitializer = gameInitializer;
        if(main == null) {
            System.out.println("GameCmain is null");
        } else {System.out.println("GameCNot null");}
    }
    private Stage primaryStage;
    private double xBreak = 0.0f;
    private double centerBreakX;
    public double getCenterBreakX() { return centerBreakX; }
    private double yBreak = 640.0f;
    private boolean goDownBall                  = true;
    private boolean goRightBall                 = true;
    private boolean isGoldStatus      = false;
    private boolean isExistHeartBlock = false;
    private int destroyedBlockCount = 0;
    private int  heart    = 3;
    private int  score    = 0;
    private long time     = 0;
    private long goldTime = 0;
    private long hitTime  = 0;
    private static int LEFT  = 1;
    private static int RIGHT = 2;
    public static String savePath    = "D:/save/save.mdds";
    public static String savePathDir = "D:/save/";
    private boolean collideToBreak               = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall           = false;
    private boolean collideToLeftWall            = false;
    private boolean collideToRightBlock          = false;
    private boolean collideToBottomBlock         = false;
    private boolean collideToLeftBlock           = false;
    private boolean collideToTopBlock            = false;
    private double vX = 1.000;
//    GameInitializer gameInitializer = new GameInitializer(main);
//    GameInitializer gameInitializer = uiController.getGameInitializer();
    private ArrayList<Bonus> choco = new ArrayList<Bonus>();
    private Color[]          colors = new Color[]{
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
    public int getScore() { return score; }
    public void setScore(int count) { score = count; }
    public int getHeart() { return heart; }
    public void setHeart(int count) { heart = count; }
    public void loadGame() {

        LoadSave loadSave = new LoadSave();
        loadSave.read();


        isExistHeartBlock = loadSave.isExistHeartBlock;
        isGoldStatus = loadSave.isGoldStatus;
        goDownBall = loadSave.goDownBall;
        goRightBall = loadSave.goRightBall;
        collideToBreak = loadSave.collideToBreak;
        collideToBreakAndMoveToRight = loadSave.collideToBreakAndMoveToRight;
        collideToRightWall = loadSave.collideToRightWall;
        collideToLeftWall = loadSave.collideToLeftWall;
        collideToRightBlock = loadSave.collideToRightBlock;
        collideToBottomBlock = loadSave.collideToBottomBlock;
        collideToLeftBlock = loadSave.collideToLeftBlock;
        collideToTopBlock = loadSave.collideToTopBlock;
        gameInitializer.setLevel(loadSave.level);
        score = loadSave.score;
        heart = loadSave.heart;
        destroyedBlockCount = loadSave.destroyedBlockCount;
        gameInitializer.setxBall(loadSave.xBall);
        gameInitializer.setyBall(loadSave.yBall);
        xBreak = loadSave.xBreak;
        yBreak = loadSave.yBreak;
        centerBreakX = loadSave.centerBreakX;
        time = loadSave.time;
        goldTime = loadSave.goldTime;
        vX = loadSave.vX;

        gameInitializer.getBlocks().clear();
        choco.clear();

        for (BlockSerializable ser : loadSave.blocks) {
            int r = new Random().nextInt(200);
            gameInitializer.getBlocks().add(new Block(ser.row, ser.j, colors[r % colors.length], ser.type));
        }


        try {
            gameInitializer.setLoadFromSave(true);
            uiController.startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void nextLevel() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    vX = 1.000;

                    gameInitializer.stopEngine();
                    resetCollideFlags();
                    goDownBall = true;

                    isGoldStatus = false;
                    isExistHeartBlock = false;


                    hitTime = 0;
                    time = 0;
                    goldTime = 0;

                    gameInitializer.stopEngine();
                    gameInitializer.getBlocks().clear();
                    choco.clear();
                    destroyedBlockCount = 0;
                    uiController.startGame();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                move(LEFT);
                break;
            case RIGHT:

                move(RIGHT);
                break;
            case DOWN:
                //setPhysicsToBall();
                break;
            case S:
                saveGame();
                break;
        }
    }

    private void move(final int direction) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int sleepTime = 4;
                for (int i = 0; i < 30; i++) {
                    if (gameInitializer.getxBreak() == (uiController.getSceneWidth() - gameInitializer.getBreakWidth()) && direction == RIGHT) {
                        return;
                    }
                    if (gameInitializer.getxBreak() == 0 && direction == LEFT) {
                        return;
                    }
                    if (direction == RIGHT) {
                        gameInitializer.setxBreak(gameInitializer.getxBreak()+1.0);
                    } else {
                        gameInitializer.setxBreak(gameInitializer.getxBreak()-1.0);
                    }
                    centerBreakX = gameInitializer.getxBreak() + gameInitializer.getHalfBreakWidth();
                    System.out.println("BreakX: " + gameInitializer.getxBreak());
                    try {
                        Thread.sleep(sleepTime);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (i >= 20) {
                        sleepTime = i;
                    }
                }
            }
        }).start();


    }

    private void saveGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

                    outputStream.writeInt(gameInitializer.getLevel());
                    outputStream.writeInt(score);
                    outputStream.writeInt(heart);
                    outputStream.writeInt(destroyedBlockCount);


                    outputStream.writeDouble(gameInitializer.getxBall());
                    outputStream.writeDouble(gameInitializer.getyBall());
                    outputStream.writeDouble(gameInitializer.getxBreak());
                    outputStream.writeDouble(yBreak);
                    outputStream.writeDouble(centerBreakX);
                    outputStream.writeLong(time);
                    outputStream.writeLong(goldTime);
                    outputStream.writeDouble(vX);


                    outputStream.writeBoolean(isExistHeartBlock);
                    outputStream.writeBoolean(isGoldStatus);
                    outputStream.writeBoolean(goDownBall);
                    outputStream.writeBoolean(goRightBall);
                    outputStream.writeBoolean(collideToBreak);
                    outputStream.writeBoolean(collideToBreakAndMoveToRight);
                    outputStream.writeBoolean(collideToRightWall);
                    outputStream.writeBoolean(collideToLeftWall);
                    outputStream.writeBoolean(collideToRightBlock);
                    outputStream.writeBoolean(collideToBottomBlock);
                    outputStream.writeBoolean(collideToLeftBlock);
                    outputStream.writeBoolean(collideToTopBlock);

                    ArrayList<BlockSerializable> blockSerializable = new ArrayList<BlockSerializable>();
                    for (Block block : gameInitializer.getBlocks()) {
                        if (block.isDestroyed) {
                            continue;
                        }
                        blockSerializable.add(new BlockSerializable(block.row, block.column, block.type));
                    }

                    outputStream.writeObject(blockSerializable);

                    new Score().showMessage("Game Saved", main);


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        outputStream.flush();
                        outputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }


    public int getDestroyedBlockCount() { return destroyedBlockCount; }

    public void setDestroyedBlockCount(int count) { destroyedBlockCount = count; }

    private void resetCollideFlags() {

        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;
        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

}
