package brickGame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;



public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    private final Main main;
    private final UIController uiController;
    private final GameInitializer gameInitializer;  // Remove the local instance
    public GameController(Main main, UIController uiController, Stage primaryStage, GameInitializer gameInitializer) {
        this.main = main;
        this.uiController = uiController;
        this.primaryStage = primaryStage;
        this.gameInitializer = gameInitializer;
        if(main == null) {
            System.out.println("GameCmain is null");
        } else {System.out.println("GameCNot null");}
    }
    private final Stage primaryStage;
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
    private double vY = 1.000;

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

    public void resetCollideFlags() {

        collideToBreak = false;
        collideToBreakAndMoveToRight = false;
        collideToRightWall = false;
        collideToLeftWall = false;
        collideToRightBlock = false;
        collideToBottomBlock = false;
        collideToLeftBlock = false;
        collideToTopBlock = false;
    }

    public void setPhysicsToBall() {
        //v = ((time - hitTime) / 1000.000) + 1.000;

        if (goDownBall) {
            gameInitializer.setyBall(gameInitializer.getyBall()+vY);
        } else {
            gameInitializer.setyBall(gameInitializer.getyBall()-vY);
        }

        if (goRightBall) {
            gameInitializer.setxBall(gameInitializer.getxBall()+vX);
        } else {
            gameInitializer.setxBall(gameInitializer.getxBall()-vX);
        }

        if (gameInitializer.getyBall() <= 0) {
            //vX = 1.000;
            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (gameInitializer.getyBall() >= uiController.getSceneHeight()) {
            goDownBall = false;
            if (!isGoldStatus) {
                //TODO game over
                heart--;
                new Score().show((double) uiController.getSceneWidth() / 2, (double) uiController.getSceneHeight() / 2, -1, main);

                if (heart == 0) {
                    new Score().showGameOver(main);
                    gameInitializer.stopEngine();
                }

            }
            //return;
        }

        if (gameInitializer.getyBall() >= yBreak - gameInitializer.getBallRadius()) {
            //System.out.println("Collide1");
            if (gameInitializer.getxBall() >= gameInitializer.getxBreak() && gameInitializer.getxBall() <= gameInitializer.getxBreak() + gameInitializer.getBreakWidth()) {
                hitTime = time;
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (gameInitializer.getxBall() - centerBreakX) / ((double) gameInitializer.getBreakWidth() / 2);

                if (Math.abs(relation) <= 0.3) {
                    //vX = 0;
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (gameInitializer.getLevel() / 3.500);
                    //System.out.println("vX " + vX);
                } else {
                    vX = (Math.abs(relation) * 2) + (gameInitializer.getLevel() / 3.500);
                    //System.out.println("vX " + vX);
                }

                if (gameInitializer.getxBall() - centerBreakX > 0) {
                    collideToBreakAndMoveToRight = true;
                } else {
                    collideToBreakAndMoveToRight = false;
                }
                //System.out.println("Collide2");
            }
        }

        if (gameInitializer.getxBall() >= uiController.getSceneWidth()) {
            resetCollideFlags();
            //vX = 1.000;
            collideToRightWall = true;
        }

        if (gameInitializer.getxBall() <= 0) {
            resetCollideFlags();
            //vX = 1.000;
            collideToLeftWall = true;
        }

        if (collideToBreak) {
            if (collideToBreakAndMoveToRight) {
                goRightBall = true;
            } else {
                goRightBall = false;
            }
        }

        //Wall Collide

        if (collideToRightWall) {
            goRightBall = false;
        }

        if (collideToLeftWall) {
            goRightBall = true;
        }

        //Block Collide

        if (collideToRightBlock) {
            goRightBall = true;
        }

        if (collideToLeftBlock) {
            goRightBall = true;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }


    }

    public void checkDestroyedCount() {
        System.out.println("DestroyedCount: " + destroyedBlockCount);
        System.out.println("BlocksNum: " + gameInitializer.getBlocks().size());
        if (destroyedBlockCount == gameInitializer.getBlocks().size()) {
            //TODO win level todo...
            //System.out.println("You Win");
            nextLevel();
        }
    }

    public void restartGame() {

        try {
            gameInitializer.setLevel(0);
            heart = 3;
            score = 0;
            vX = 1.000;
            destroyedBlockCount = 0;
            resetCollideFlags();
            goDownBall = true;

            isGoldStatus = false;
            isExistHeartBlock = false;
            hitTime = 0;
            time = 0;
            goldTime = 0;

            gameInitializer.getBlocks().clear();
            choco.clear();

            uiController.startGame();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                uiController.getScoreLabel().setText("Score: " + score);
                uiController.getHeartLabel().setText("Heart : " + heart);

                gameInitializer.getRect().setX(gameInitializer.getxBreak());
                gameInitializer.getRect().setY(yBreak);
                gameInitializer.getBall().setCenterX(gameInitializer.getxBall());
                gameInitializer.getBall().setCenterY(gameInitializer.getyBall());

                for (Bonus choco : choco) {
                    choco.choco.setY(choco.y);
                }
            }
        });

        if (gameInitializer.getyBall() >= Block.getPaddingTop() && gameInitializer.getyBall() <= (Block.getHeight() * (gameInitializer.getLevel() + 1)) + Block.getPaddingTop()) {
            for (final Block block : gameInitializer.getBlocks()) {
                int hitCode = block.checkHitToBlock(gameInitializer.getxBall(), gameInitializer.getyBall(), gameInitializer.getBallRadius());
                if (hitCode != Block.NO_HIT) {
                    score += 1;
                    new Score().show(block.x, block.y, 1, main);

                    block.rect.setVisible(false);
                    block.isDestroyed = true;
                    destroyedBlockCount++;
                    //System.out.println("size is " + blocks.size());
                    resetCollideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                uiController.getRoot().getChildren().add(choco.choco);
                            }
                        });
                        this.choco.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        gameInitializer.getBall().setFill(new ImagePattern(new Image("goldball.png")));
                        System.out.println("gold ball");
                        uiController.getRoot().getStyleClass().add("goldRoot");
                        isGoldStatus = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        collideToRightBlock = true;
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        collideToBottomBlock = true;
                    } else if (hitCode == Block.HIT_LEFT) {
                        collideToLeftBlock = true;
                    } else if (hitCode == Block.HIT_TOP) {
                        collideToTopBlock = true;
                    }

                }

                //TODO hit to break and some work here....
                //System.out.println("Break in row:" + block.row + " and column:" + block.column + " hit");
            }
        }
    }

    @Override
    public void onInit() {

    }

    @Override
    public void onPhysicsUpdate() {
        checkDestroyedCount();
        setPhysicsToBall();

        if (time - goldTime > 5000) {
            gameInitializer.getBall().setFill(new ImagePattern(new Image("ball.png")));
            uiController.getRoot().getStyleClass().remove("goldRoot");
            isGoldStatus = false;
        }

        for (Bonus choco : choco) {
            if (choco.y > uiController.getSceneHeight() || choco.taken) {
                continue;
            }
            if (choco.y >= yBreak && choco.y <= yBreak + gameInitializer.getBreakHeight() && choco.x >= gameInitializer.getxBreak() && choco.x <= gameInitializer.getxBreak() + gameInitializer.getBreakWidth()) {
                System.out.println("You Got it and +3 score for you");
                choco.taken = true;
                choco.choco.setVisible(false);
                score += 3;
                new Score().show(choco.x, choco.y, 3, main);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

        //System.out.println("time is:" + time + " goldTime is " + goldTime);

    }

    @Override
    public void onTime(long time) {
        this.time = time;
    }

}
