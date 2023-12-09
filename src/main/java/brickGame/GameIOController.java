package brickGame;

import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GameIOController {
    private Main main;
    private GameController gameController;
    private UIController uiController = Main.getUiController();
    private BallMovement ballMovement;
    private GameInitializer gameInitializer = Main.getGameInitializer();
    public static String savePathDir = "D:/save/";
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
    public GameIOController(Main main, GameController gameController, BallMovement ballMovement) {
        this.main = main;
        this.gameController = gameController;
        this.ballMovement = ballMovement;
        if(main == null) {
            System.out.println("GameIO main is null");
        } else {System.out.println("GameIO Not null");}
    }
    private LoadSave loadSave;
    public LoadSave getLoadSave() { return loadSave; }

    public void loadGame() {

        loadSave = new LoadSave();
        loadSave.read();

        gameController.setExistHeartBlock(loadSave.isExistHeartBlock);
        gameController.setGoldStatus(loadSave.isGoldStatus);
        gameController.setGoldStatus(loadSave.isGoldStatus);
        ballMovement.setGoDownBall(loadSave.goDownBall);
        ballMovement.setGoRightBall(loadSave.goRightBall);
        ballMovement.setCollideToBreak(loadSave.collideToBreak);
        ballMovement.setCollideToBreakAndMoveToRight(loadSave.collideToBreakAndMoveToRight);
        ballMovement.setCollideToRightWall(loadSave.collideToRightWall);
        ballMovement.setCollideToLeftWall(loadSave.collideToLeftWall);
        ballMovement.setCollideToRightBlock(loadSave.collideToRightBlock);
        ballMovement.setCollideToBottomBlock(loadSave.collideToBottomBlock);
        ballMovement.setCollideToLeftBlock(loadSave.collideToLeftBlock);
        ballMovement.setCollideToTopBlock(loadSave.collideToTopBlock);
        gameInitializer.setLevel(loadSave.level);
        gameController.setScore(loadSave.score);
        gameController.setHeart(loadSave.heart);
        //gameController.setDestroyedBlockCount(loadSave.destroyedBlockCount);
        gameInitializer.setxBall(loadSave.xBall);
        gameInitializer.setyBall(loadSave.yBall);
        gameController.setxBreak(loadSave.xBreak);
        gameController.setyBreak(loadSave.yBreak);
        gameController.setCenterBreakX(loadSave.centerBreakX);
        gameController.setTime(loadSave.time);
        gameController.setGoldTime(loadSave.goldTime);
        ballMovement.setvX(loadSave.vX);

        gameInitializer.getBlocks().clear();
        gameController.getChoco().clear();

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

    public void saveGame() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                new File(savePathDir).mkdirs();
                File file = new File(Main.savePath);
                ObjectOutputStream outputStream = null;
                try {
                    outputStream = new ObjectOutputStream(new FileOutputStream(file));

                    outputStream.writeInt(gameInitializer.getLevel());
                    outputStream.writeInt(gameController.getScore());
                    outputStream.writeInt(gameController.getHeart());
                    //outputStream.writeInt(gameController.getDestroyedBlockCount());


                    outputStream.writeDouble(gameInitializer.getxBall());
                    outputStream.writeDouble(gameInitializer.getyBall());
                    outputStream.writeDouble(gameInitializer.getxBreak());
                    outputStream.writeDouble(gameController.getyBreak());
                    outputStream.writeDouble(gameController.getCenterBreakX());
                    outputStream.writeLong(gameController.getTime());
                    outputStream.writeLong(gameController.getGoldTime());
                    outputStream.writeDouble(ballMovement.getvX());


                    outputStream.writeBoolean(gameController.isExistHeartBlock());
                    outputStream.writeBoolean(gameController.getIsGoldStatus());
                    outputStream.writeBoolean(ballMovement.isGoDownBall());
                    outputStream.writeBoolean(ballMovement.isGoRightBall());
                    outputStream.writeBoolean(ballMovement.isCollideToBreak());
                    outputStream.writeBoolean(ballMovement.isCollideToBreakAndMoveToRight());
                    outputStream.writeBoolean(ballMovement.isCollideToRightWall());
                    outputStream.writeBoolean(ballMovement.isCollideToLeftWall());
                    outputStream.writeBoolean(ballMovement.isCollideToRightBlock());
                    outputStream.writeBoolean(ballMovement.isCollideToBottomBlock());
                    outputStream.writeBoolean(ballMovement.isCollideToLeftBlock());
                    outputStream.writeBoolean(ballMovement.isCollideToTopBlock());

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
}
