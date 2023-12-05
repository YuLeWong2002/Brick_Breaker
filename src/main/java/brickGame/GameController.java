package brickGame;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;


import java.util.ArrayList;




public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    @FXML
    private Pane root;
    @FXML
    public Label scoreLabel;
    @FXML
    public Label heartLabel;
    @FXML
    public Label levelLabel;

    public Pane getRoot() {
        return root;
    }
    private UIController uiController = Main.getUiController();
    private GameInitializer gameInitializer = Main.getGameInitializer();
    private BallMovement ballMovement;
    private GameIOController gameIOController;
    private Main main = uiController.getMain();
    public GameController() {
        // Initialization code if needed
        ballMovement = new BallMovement(main, this);
        gameIOController = new GameIOController(main, this, ballMovement);
    }


    public GameIOController getGameIOController() {
        return gameIOController;
    }

    private double xBreak = 0.0f;
    private double centerBreakX;
    public double getCenterBreakX() { return centerBreakX; }
    private double yBreak = 640.0f;

    public double getyBreak() {
        return yBreak;
    }

    private boolean isGoldStatus      = false;
    public boolean getIsGoldStatus() { return isGoldStatus; }
    public void setGoldStatus(boolean isGoldStatus) { this.isGoldStatus = isGoldStatus; }

    public long getGoldTime() {
        return goldTime;
    }

    private boolean isExistHeartBlock = false;

    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    public void setExistHeartBlock(boolean isExistHeartBlock) { this.isExistHeartBlock = isExistHeartBlock; }
    private int destroyedBlockCount = 0;
    private static int  heart    = 3;
    private static int  score    = 0;
    private long time     = 0;

    public long getTime() {
        return time;
    }

    public void setHItTime(long hitTime) { this.hitTime = hitTime; }

    private long goldTime = 0;
    private long hitTime  = 0;
    public long getHitTime() { return time; }
    private static int LEFT  = 1;
    private static int RIGHT = 2;

    private ArrayList<Bonus> choco = new ArrayList<Bonus>();

    public ArrayList<Bonus> getChoco() {
        return choco;
    }

    public int getScore() { return score; }
    public void setScore(int count) { score = count; }
    public int getHeart() { return heart; }
    public void setHeart(int count) { heart = count; }
    public void setxBreak(double count) {this.xBreak = count; }
    public void setyBreak(double yBreak) { this.yBreak = yBreak; }
    public void setCenterBreakX(double centerBreakX) { this.centerBreakX = centerBreakX; }
    public void setTime(long time) { this.time = time; }
    public void setGoldTime(long goldTime) { this.goldTime = goldTime; }

    public void nextLevel() {
        Platform.runLater(() -> {
            try {
                ballMovement.setvX(1.000);

                gameInitializer.stopEngine();
                ballMovement.resetCollideFlags();
                ballMovement.setGoDownBall(true);

                isGoldStatus = false;
                isExistHeartBlock = false;

                hitTime = 0;
                time = 0;
                goldTime = 0;

                gameInitializer.stopEngine();
                gameInitializer.getBlocks().clear();
                choco.clear();
                destroyedBlockCount = 0;
                uiController.initializeUI();

            } catch (Exception e) {
                e.printStackTrace();
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
                gameIOController.saveGame();
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
                        System.out.println("Scene width: "+ uiController.getSceneWidth());
                        System.out.println("Break width: "+ gameInitializer.getBreakWidth());
                        System.out.println("xBreak: " + gameInitializer.getxBreak());
                        return;
                    }
                    if (gameInitializer.getxBreak() == 0 && direction == LEFT) {
                        return;
                    }

                    // Update UI on JavaFX Application Thread
                    Platform.runLater(() -> {
                        if (direction == RIGHT) {
                            gameInitializer.setxBreak(gameInitializer.getxBreak() + 1.0);
                        } else {
                            gameInitializer.setxBreak(gameInitializer.getxBreak() - 1.0);
                        }
                        centerBreakX = gameInitializer.getxBreak() + gameInitializer.getHalfBreakWidth();
                        System.out.println("BreakX: " + gameInitializer.getxBreak());
                    });

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



    public int getDestroyedBlockCount() { return destroyedBlockCount; }

    public void setDestroyedBlockCount(int count) { destroyedBlockCount = count; }


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
            ballMovement.setvX(1.000);
            destroyedBlockCount = 0;
            ballMovement.resetCollideFlags();
            ballMovement.setGoDownBall(true);

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
        Platform.runLater(() -> {

            scoreLabel.setText("Score: " + score);
            heartLabel.setText("Heart: " + heart);

            gameInitializer.getRect().setX(gameInitializer.getxBreak());
            gameInitializer.getRect().setY(yBreak);
            gameInitializer.getBall().setCenterX(gameInitializer.getxBall());
            gameInitializer.getBall().setCenterY(gameInitializer.getyBall());

            for (Bonus choco : choco) {
                choco.choco.setY(choco.y);
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
                    ballMovement.resetCollideFlags();

                    if (block.type == Block.BLOCK_CHOCO) {
                        final Bonus choco = new Bonus(block.row, block.column);
                        choco.timeCreated = time;
                        Platform.runLater(() -> root.getChildren().add(choco.choco));
                        this.choco.add(choco);
                    }

                    if (block.type == Block.BLOCK_STAR) {
                        goldTime = time;
                        gameInitializer.getBall().setFill(new ImagePattern(new Image("goldball.png")));
                        System.out.println("gold ball");
                        root.getStyleClass().add("goldRoot");
                        isGoldStatus = true;
                    }

                    if (block.type == Block.BLOCK_HEART) {
                        heart++;
                    }

                    if (hitCode == Block.HIT_RIGHT) {
                        ballMovement.setCollideToRightBlock(true);
                    } else if (hitCode == Block.HIT_BOTTOM) {
                        ballMovement.setCollideToBottomBlock(true);
                    } else if (hitCode == Block.HIT_LEFT) {
                        ballMovement.setCollideToLeftBlock(true);
                    } else if (hitCode == Block.HIT_TOP) {
                        ballMovement.setCollideToTopBlock(true);
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
        ballMovement.setPhysicsToBall();

        if (time - goldTime > 5000) {
            gameInitializer.getBall().setFill(new ImagePattern(new Image("ball.png")));
            root.getStyleClass().remove("goldRoot");
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
