package brickGame;

import javafx.scene.paint.Color;

import java.io.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * Handles saving and loading game state.
 */
public class GameIOController {

    /** Reference to the main application class. */
    private final Main main;

    /** Reference to the game controller responsible for game logic. */
    private final GameController gameController;

    /** Reference to the user interface controller. */
    private final UIController uiController = Main.getUiController();

    /** Reference to the ball movement controller. */
    private final BallMovement ballMovement;

    /** Reference to the game initializer responsible for setting up the game. */
    private final GameInitializer gameInitializer = Main.getGameInitializer();

    /** The directory path for saving game data. */
    public static String savePathDir = "D:/save/";

    private final Color[]          colors = new Color[]{
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

    /**
     * Constructs a new GameIOController with references to the main application, game controller,
     * ball movement controller, and game initializer.
     *
     * @param main            Reference to the main application class.
     * @param gameController  Reference to the game controller.
     * @param ballMovement    Reference to the ball movement controller.
     */
    public GameIOController(Main main, GameController gameController, BallMovement ballMovement) {
        this.main = main;
        this.gameController = gameController;
        this.ballMovement = ballMovement;
        if(main == null) {
            System.out.println("GameIO main is null");
        } else {System.out.println("GameIO Not null");}
    }

    /**
     * Loads the game state from a saved file using the LoadSave instance. It updates various game components and initializes the game with the loaded data.
     */
    public void loadGame() {

        /**
         * The LoadSave instance used for reading and storing game state data during loading and saving operations.
         */
        LoadSave loadSave = new LoadSave();
        loadSave.read();

        gameInitializer.setExistHeartBlock(loadSave.isExistHeartBlock);
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

    /**
     * Saves the current state of the game to a file in a separate thread. It creates a new thread to perform the file writing operation.
     */
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


                    outputStream.writeDouble(gameInitializer.getxBall());
                    outputStream.writeDouble(gameInitializer.getyBall());
                    outputStream.writeDouble(gameInitializer.getxBreak());
                    outputStream.writeDouble(gameController.getyBreak());
                    outputStream.writeDouble(gameController.getCenterBreakX());
                    outputStream.writeLong(gameController.getTime());
                    outputStream.writeLong(gameController.getGoldTime());
                    outputStream.writeDouble(ballMovement.getvX());


                    outputStream.writeBoolean(gameInitializer.isExistHeartBlock());
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
