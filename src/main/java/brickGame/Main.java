    package brickGame;

    import javafx.application.Application;
    import javafx.application.Platform;
    import javafx.event.EventHandler;
    import javafx.scene.image.Image;
    import javafx.scene.input.KeyEvent;
    import javafx.scene.layout.Pane;
    import javafx.scene.paint.ImagePattern;
    import javafx.stage.Stage;

    import java.io.*;
    import java.util.ArrayList;

    public class Main extends Application implements EventHandler<KeyEvent>, GameEngine.OnAction {

        private GameInitializer gameInitializer;
        private UIController uiController;
        public UIController getUiController() { return uiController; }
        private GameController gameController;
        private double centerBreakX;
        private double yBreak = 640.0f;
        private int breakWidth     = 130;
        private int breakHeight    = 30;
        private int halfBreakWidth = breakWidth / 2;
        private int sceneWidth = 500;
        private int sceneHeight = 700;
        private static int LEFT  = 1;
        private static int RIGHT = 2;
        private boolean isGoldStatus      = false;
        private boolean isExistHeartBlock = false;
        private int       ballRadius = 10;
        private double v = 1.000;
        private long time     = 0;
        private long hitTime  = 0;
        private long goldTime = 0;
        public static String savePath    = "D:/save/save.mdds";
        public static String savePathDir = "D:/save/";
        private ArrayList<Bonus> choco = new ArrayList<Bonus>();
        Stage primaryStage;
        @Override
        public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;
            if(uiController == null) {
                uiController = new UIController(Main.this, primaryStage);
            }
            uiController.initGame();
            gameInitializer = uiController.getGameInitializer();
            gameController = uiController.getGameController();
            uiController.initializeUI();
            //System.out.println("Start: " + gameInitializer.getBlocks());
        }

        public static void main(String[] args) { launch(args); }

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

        float oldXBreak;

        private void move(final int direction) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    int sleepTime = 4;
                    for (int i = 0; i < 30; i++) {
                        if (gameInitializer.getxBreak() == (sceneWidth - breakWidth) && direction == RIGHT) {
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
                        centerBreakX = gameInitializer.getxBreak() + halfBreakWidth;
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

        private boolean goDownBall                  = true;
        private boolean goRightBall                 = true;
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

        private void setPhysicsToBall() {
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
            if (gameInitializer.getyBall() >= sceneHeight) {
                goDownBall = false;
                if (!isGoldStatus) {
                    //TODO game over
                    gameController.setHeart(gameController.getHeart()-1);
                    new Score().show(sceneWidth / 2, sceneHeight / 2, -1, this);

                    if (gameController.getHeart() == 0) {
                        new Score().showGameOver(this);
                        gameInitializer.stopEngine();
                    }

                }
                //return;
            }

            if (gameInitializer.getyBall() >= yBreak - ballRadius) {
                //System.out.println("Collide1");
                if (gameInitializer.getxBall() >= gameInitializer.getxBreak() && gameInitializer.getxBall() <= gameInitializer.getxBreak() + breakWidth) {
                    hitTime = time;
                    resetCollideFlags();
                    collideToBreak = true;
                    goDownBall = false;

                    double relation = (gameInitializer.getxBall() - centerBreakX) / (breakWidth / 2);

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

            if (gameInitializer.getxBall() >= sceneWidth) {
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

        private void checkDestroyedCount() {
            System.out.println("DestroyedCount: " + gameController.getDestroyedBlockCount());
            System.out.println("BlocksNum: " + gameInitializer.getBlocks().size());
            if (gameController.getDestroyedBlockCount() == gameInitializer.getBlocks().size()) {
                //TODO win level todo...
                //System.out.println("You Win");
                gameController.nextLevel();
            }
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
                        outputStream.writeInt(gameController.getScore());
                        outputStream.writeInt(gameController.getHeart());
                        outputStream.writeInt(gameController.getDestroyedBlockCount());


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

                        new Score().showMessage("Game Saved", Main.this);


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



        public void restartGame() {

            try {
                gameInitializer.setLevel(0);
                gameController.setHeart(3);
                gameController.setScore(0);
                vX = 1.000;
                gameController.setDestroyedBlockCount(0);
                resetCollideFlags();
                goDownBall = true;

                isGoldStatus = false;
                isExistHeartBlock = false;
                hitTime = 0;
                time = 0;
                goldTime = 0;

                gameInitializer.getBlocks().clear();
                choco.clear();

                start(primaryStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpdate() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {

                    uiController.getScoreLabel().setText("Score: " + gameController.getScore());
                    uiController.getHeartLabel().setText("Heart : " + gameController.getHeart());

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
                    int hitCode = block.checkHitToBlock(gameInitializer.getxBall(), gameInitializer.getyBall(), ballRadius);
                    if (hitCode != Block.NO_HIT) {
                        gameController.setScore(gameController.getScore()+1);
                        new Score().show(block.x, block.y, 1, this);

                        block.rect.setVisible(false);
                        block.isDestroyed = true;
                        gameController.setDestroyedBlockCount(gameController.getDestroyedBlockCount() + 1);
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
                            gameController.setHeart(gameController.getHeart()+1);
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
                if (choco.y > sceneHeight || choco.taken) {
                    continue;
                }
                if (choco.y >= yBreak && choco.y <= yBreak + breakHeight && choco.x >= gameInitializer.getxBreak() && choco.x <= gameInitializer.getxBreak() + breakWidth) {
                    System.out.println("You Got it and +3 score for you");
                    choco.taken = true;
                    choco.choco.setVisible(false);
                    gameController.setScore(gameController.getScore()+3);
                    new Score().show(choco.x, choco.y, 3, this);
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
