package brickGame;

public class BallMovement {
    GameInitializer gameInitializer = Main.getGameInitializer();
    UIController uiController = Main.getUiController();
    private GameController gameController;
    public BallMovement(Main main, GameController gameController) {
        this.main = main;
        this.gameController = gameController;
        if(main == null) {
            System.out.println("Ball main is null");
        } else {System.out.println("Ball Not null");}
    }
    private Main main;
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

    public double getvX() {
        return vX;
    }

    public void setvX(double vX) { this.vX = vX; }

    public boolean isGoDownBall() {
        return goDownBall;
    }
    public void setCollideToBreak(boolean collideToBreak) { this.collideToBreak = collideToBreak; }
    public void setCollideToBreakAndMoveToRight(boolean collideToBreakAndMoveToRight) { this.collideToBreakAndMoveToRight= collideToBreakAndMoveToRight; }
    public void setCollideToRightWall(boolean collideToRightWall) { this.collideToRightWall = collideToRightWall; }
    public void setCollideToLeftWall(boolean collideToLeftWall) {this.collideToLeftWall = collideToLeftWall; }
    public boolean isGoRightBall() { return goRightBall; }
    public void setGoDownBall(boolean goDownBall) { this.goDownBall = goDownBall; }
    public void setGoRightBall(boolean goRightBall) {this.goRightBall = goRightBall; }

    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    public boolean isCollideToBreak() {
        return collideToBreak;
    }

    public boolean isCollideToBreakAndMoveToRight() {
        return collideToBreakAndMoveToRight;
    }

    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }
    public void setCollideToRightBlock(boolean collideToRightBlock) { this.collideToRightBlock = collideToRightBlock; }

    public void setCollideToLeftBlock(boolean collideToLeftBlock) { this.collideToLeftBlock = collideToLeftBlock; }

    public void setCollideToTopBlock(boolean collideToTopBlock) { this.collideToTopBlock = collideToTopBlock; }

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
            if (!gameController.getIsGoldStatus()) {
                //TODO game over
                gameController.setHeart(gameController.getHeart()-1);
                new Score().show((double) uiController.getSceneWidth() / 2, (double) uiController.getSceneHeight() / 2, -1, main);

                if (gameController.getHeart() == 0) {
                    new Score().showGameOver(main);
                    gameInitializer.stopEngine();
                }

            }
            //return;
        }

        if (gameInitializer.getyBall() >= gameController.getyBreak() - gameInitializer.getBallRadius()) {
            //System.out.println("Collide1");
            if (gameInitializer.getxBall() >= gameInitializer.getxBreak() && gameInitializer.getxBall() <= gameInitializer.getxBreak() + gameInitializer.getBreakWidth()) {
                gameController.setHItTime(gameController.getTime());
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (gameInitializer.getxBall() - gameController.getCenterBreakX()) / ((double) gameInitializer.getBreakWidth() / 2);

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

                if (gameInitializer.getxBall() - gameController.getCenterBreakX() > 0) {
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
}
