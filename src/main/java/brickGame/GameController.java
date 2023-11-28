package brickGame;

import javafx.application.Platform;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Random;



public class GameController {
    private Main main;
    private UIController uiController;
    public GameController(Main main, UIController uiController, Stage primaryStage) {
        this.main = main;
        this.uiController = uiController;
        this.primaryStage = primaryStage;
        if(main == null) {
            System.out.println("GameCmain is null");
        } else {System.out.println("GameCNot null");}
    }
    private Stage primaryStage;
    private double xBreak = 0.0f;
    private double centerBreakX;
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
    private boolean collideToBreak               = false;
    private boolean collideToBreakAndMoveToRight = true;
    private boolean collideToRightWall           = false;
    private boolean collideToLeftWall            = false;
    private boolean collideToRightBlock          = false;
    private boolean collideToBottomBlock         = false;
    private boolean collideToLeftBlock           = false;
    private boolean collideToTopBlock            = false;
    private double vX = 1.000;
    GameInitializer gameInitializer = new GameInitializer(main);
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
