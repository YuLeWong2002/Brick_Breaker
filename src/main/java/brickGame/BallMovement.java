package brickGame;

import javafx.application.Platform;

/**
 * The {@code BallMovement} class represents the movement and behavior of the ball in a game.
 * It interacts with the {@link GameInitializer}, {@link UIController}, {@link GameController}, and {@link Main} classes
 * to manage the ball's movement and collisions within the game.
 */
public class BallMovement {

    /**
     * The GameInitializer instance associated with the game.
     */
    GameInitializer gameInitializer = Main.getGameInitializer();
    /**
     * The UIController instance associated with the game.
     */
    UIController uiController = Main.getUiController();
    /**
     * The controller responsible for managing game-related logic.
     */
    private final GameController gameController;

    /**
     * Constructs a new {@code BallMovement} instance.
     *
     * @param gameController  The game controller responsible for handling user input and game events.
     */
    public BallMovement(GameController gameController) {
        this.gameController = gameController;
    }

    /**
     * Flag indicating whether the ball is moving downward.
     */
    private boolean goDownBall = true;

    /**
     * Flag indicating whether the ball is moving to the right.
     */
    private boolean goRightBall = true;

    /**
     * Flag indicating whether the ball is colliding with the paddle.
     */
    private boolean collideToBreak = false;

    /**
     * Flag indicating whether the ball is colliding with the paddle and moving to the right.
     */
    private boolean collideToBreakAndMoveToRight = true;

    /**
     * Flag indicating whether the ball is colliding with the right wall.
     */
    private boolean collideToRightWall = false;

    /**
     * Flag indicating whether the ball is colliding with the left wall.
     */
    private boolean collideToLeftWall = false;

    /**
     * Flag indicating whether the ball is colliding with a block on the right.
     */
    private boolean collideToRightBlock = false;

    /**
     * Flag indicating whether the ball is colliding with the bottom of a block.
     */
    private boolean collideToBottomBlock = false;

    /**
     * Flag indicating whether the ball is colliding with a block on the left.
     */
    private boolean collideToLeftBlock = false;

    /**
     * Flag indicating whether the ball is colliding with the top of a block.
     */
    private boolean collideToTopBlock = false;

    /**
     * The horizontal velocity of the ball.
     */
    private double vX = 1.000;

    /**
     * The vertical velocity of the ball.
     */
    private final double vY = 1.000;

    /**
     * Resets all collision flags related to the ball's interaction with the game elements.
     * This method sets flags to false, indicating that the ball is not currently colliding
     * with specific game elements such as the paddle, walls, or blocks.
     */
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

    /**
     * Updates the position of the ball based on its current physics and collisions with game elements.
     * The method uses the ball's velocity, direction, and collision flags to calculate its new position.
     * Additionally, it handles collisions with the paddle, walls, and blocks, adjusting the ball's movement accordingly.
     * If the ball collides with the paddle, the method calculates the new velocity based on the collision position,
     * allowing for different bounce behaviors. If the ball reaches the top or bottom of the scene, it adjusts
     * its direction accordingly. If the ball hits the right or left walls, it changes its horizontal direction.
     * If the ball collides with blocks, the collision flags are set to handle block-specific behavior.
     */
    public void setPhysicsToBall() {

        Platform.runLater(() -> {
            if (goDownBall) {
                gameInitializer.setyBall(gameInitializer.getyBall() + vY);
            } else {
                gameInitializer.setyBall(gameInitializer.getyBall() - vY);
            }

            if (goRightBall) {
                gameInitializer.setxBall(gameInitializer.getxBall() + vX);
            } else {
                gameInitializer.setxBall(gameInitializer.getxBall() - vX);
            }
        });


        if (gameInitializer.getyBall() - gameInitializer.getBallRadius() <= 0) {
            resetCollideFlags();
            goDownBall = true;
            return;
        }
        if (gameInitializer.getyBall() + gameInitializer.getBallRadius() >= uiController.getSceneHeight()) {
            goDownBall = false;
            if (!gameController.getIsGoldStatus()) {
                gameController.setHeart(gameController.getHeart()-1);
                new Score().show((double) uiController.getSceneWidth() / 2, (double) uiController.getSceneHeight() / 2, -1);

                if (gameController.getHeart() == 0) {
                    new Score().showGameOver();
                    gameInitializer.stopEngine();
                }

            }
        }

        if (gameInitializer.getyBall() >= gameController.getyBreak() - gameInitializer.getBallRadius()) {
            if (gameInitializer.getxBall() >= gameInitializer.getxBreak() && gameInitializer.getxBall() <= gameInitializer.getxBreak() + gameInitializer.getBreakWidth()) {
                gameController.setHItTime(gameController.getTime());
                resetCollideFlags();
                collideToBreak = true;
                goDownBall = false;

                double relation = (gameInitializer.getxBall() - gameController.getCenterBreakX()) / ((double) gameInitializer.getBreakWidth() / 2);

                if (Math.abs(relation) <= 0.3) {
                    vX = Math.abs(relation);
                } else if (Math.abs(relation) > 0.3 && Math.abs(relation) <= 0.7) {
                    vX = (Math.abs(relation) * 1.5) + (gameInitializer.getLevel() / 3.500);
                } else {
                    vX = (Math.abs(relation) * 2) + (gameInitializer.getLevel() / 3.500);
                }

                if (gameInitializer.getxBall() - gameController.getCenterBreakX() > 0) {
                    collideToBreakAndMoveToRight = true;
                } else {
                    collideToBreakAndMoveToRight = false;
                }
            }
        }

        if (gameInitializer.getxBall() + gameInitializer.getBallRadius() >= uiController.getSceneWidth()) {
            resetCollideFlags();
            collideToRightWall = true;
        }

        if (gameInitializer.getxBall() - gameInitializer.getBallRadius() <= 0) {
            resetCollideFlags();
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
            goRightBall = false;
        }

        if (collideToTopBlock) {
            goDownBall = false;
        }

        if (collideToBottomBlock) {
            goDownBall = true;
        }
    }

    /**
     * Gets the horizontal velocity of the ball.
     *
     * @return The current horizontal velocity of the ball.
     */
    public double getvX() {
        return vX;
    }

    /**
     * Checks if the ball is currently moving downward.
     *
     * @return True if the ball is moving downward, false otherwise.
     */
    public boolean isGoDownBall() {
        return goDownBall;
    }

    /**
     * Checks if the ball is currently moving to the right.
     *
     * @return True if the ball is moving to the right, false otherwise.
     */
    public boolean isGoRightBall() {
        return goRightBall;
    }

    /**
     * Checks if the ball is currently colliding with the top of a block.
     *
     * @return True if the ball is colliding with the top of a block, false otherwise.
     */
    public boolean isCollideToTopBlock() {
        return collideToTopBlock;
    }

    /**
     * Checks if the ball is currently colliding with the left side of a block.
     *
     * @return True if the ball is colliding with the left side of a block, false otherwise.
     */
    public boolean isCollideToLeftBlock() {
        return collideToLeftBlock;
    }

    /**
     * Checks if the ball is currently colliding with the bottom of a block.
     *
     * @return True if the ball is colliding with the bottom of a block, false otherwise.
     */
    public boolean isCollideToBottomBlock() {
        return collideToBottomBlock;
    }

    /**
     * Checks if the ball is currently colliding with the right side of a block.
     *
     * @return True if the ball is colliding with the right side of a block, false otherwise.
     */
    public boolean isCollideToRightBlock() {
        return collideToRightBlock;
    }

    /**
     * Checks if the ball is currently colliding with the break/paddle element.
     *
     * @return True if the ball is colliding with the break/paddle, false otherwise.
     */
    public boolean isCollideToBreak() {
        return collideToBreak;
    }

    /**
     * Checks if the ball is currently colliding with the break/paddle and moving to the right.
     *
     * @return True if the ball is colliding with the break/paddle and moving to the right, false otherwise.
     */
    public boolean isCollideToBreakAndMoveToRight() {
        return collideToBreakAndMoveToRight;
    }

    /**
     * Checks if the ball is currently colliding with the left wall.
     *
     * @return True if the ball is colliding with the left wall, false otherwise.
     */
    public boolean isCollideToLeftWall() {
        return collideToLeftWall;
    }

    /**
     * Checks if the ball is currently colliding with the right wall.
     *
     * @return True if the ball is colliding with the right wall, false otherwise.
     */
    public boolean isCollideToRightWall() {
        return collideToRightWall;
    }

    /**
     * Sets the horizontal velocity of the ball.
     *
     * @param vX The new horizontal velocity of the ball.
     */
    public void setvX(double vX) {
        this.vX = vX;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the break/paddle.
     *
     * @param collideToBreak True if the ball is colliding with the break/paddle, false otherwise.
     */
    public void setCollideToBreak(boolean collideToBreak) {
        this.collideToBreak = collideToBreak;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the break/paddle and moving to the right.
     *
     * @param collideToBreakAndMoveToRight True if the ball is colliding with the break/paddle and moving to the right, false otherwise.
     */
    public void setCollideToBreakAndMoveToRight(boolean collideToBreakAndMoveToRight) {
        this.collideToBreakAndMoveToRight = collideToBreakAndMoveToRight;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the right wall.
     *
     * @param collideToRightWall True if the ball is colliding with the right wall, false otherwise.
     */
    public void setCollideToRightWall(boolean collideToRightWall) {
        this.collideToRightWall = collideToRightWall;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the left wall.
     *
     * @param collideToLeftWall True if the ball is colliding with the left wall, false otherwise.
     */
    public void setCollideToLeftWall(boolean collideToLeftWall) {
        this.collideToLeftWall = collideToLeftWall;
    }

    /**
     * Sets the flag indicating the ball's downward movement.
     *
     * @param goDownBall True if the ball is moving downward, false otherwise.
     */
    public void setGoDownBall(boolean goDownBall) {
        this.goDownBall = goDownBall;
    }

    /**
     * Sets the flag indicating the ball's rightward movement.
     *
     * @param goRightBall True if the ball is moving to the right, false otherwise.
     */
    public void setGoRightBall(boolean goRightBall) {
        this.goRightBall = goRightBall;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the bottom of a block.
     *
     * @param collideToBottomBlock True if the ball is colliding with the bottom of a block, false otherwise.
     */
    public void setCollideToBottomBlock(boolean collideToBottomBlock) {
        this.collideToBottomBlock = collideToBottomBlock;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the right side of a block.
     *
     * @param collideToRightBlock True if the ball is colliding with the right side of a block, false otherwise.
     */
    public void setCollideToRightBlock(boolean collideToRightBlock) {
        this.collideToRightBlock = collideToRightBlock;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the left side of a block.
     *
     * @param collideToLeftBlock True if the ball is colliding with the left side of a block, false otherwise.
     */
    public void setCollideToLeftBlock(boolean collideToLeftBlock) {
        this.collideToLeftBlock = collideToLeftBlock;
    }

    /**
     * Sets the flag indicating whether the ball is colliding with the top of a block.
     *
     * @param collideToTopBlock True if the ball is colliding with the top of a block, false otherwise.
     */
    public void setCollideToTopBlock(boolean collideToTopBlock) {
        this.collideToTopBlock = collideToTopBlock;
    }

}
