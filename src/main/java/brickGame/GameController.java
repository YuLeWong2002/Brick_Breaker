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
import java.util.Random;

/**
 * Controller class for managing the game-related logic, handling user input events, and
 * updating the game UI. Implements {@link EventHandler} for key events and
 * {@link GameEngine.OnAction} for game actions.
 */
public class GameController implements EventHandler<KeyEvent>, GameEngine.OnAction {
    /**
     * The root Pane of the game UI, containing all game elements.
     */
    @FXML
    private Pane root;

    /**
     * The label displaying the current score in the game UI.
     */
    @FXML
    public Label scoreLabel;

    /**
     * The label displaying the remaining heart/life count in the game UI.
     */
    @FXML
    public Label heartLabel;

    /**
     * The label displaying the current level in the game UI.
     */
    @FXML
    public Label levelLabel;
    /**
     * The UIController instance associated with the game.
     */
    private final UIController uiController = Main.getUiController();

    /**
     * The GameInitializer instance associated with the game.
     */
    private final GameInitializer gameInitializer = Main.getGameInitializer();

    /**
     * The BallMovement instance responsible for controlling the movement of the game ball.
     */
    private final BallMovement ballMovement;

    /**
     * The GameIOController instance responsible for managing game input/output.
     */
    private final GameIOController gameIOController;

    /**
     * Constructor for the GameController class. Initializes the BallMovement and GameIOController instances.
     * If additional initialization is needed, it can be added here.
     */
    public GameController() {
        ballMovement = new BallMovement(this);
        gameIOController = new GameIOController(this, ballMovement);
    }
    /**
     * Represents the x-coordinate of the paddle in the game.
     */
    private double xBreak = 0.0;

    /**
     * Represents the x-coordinate of the center of the paddle.
     */
    private double centerBreakX;

    /**
     * Represents the y-coordinate of the paddle in the game.
     */
    private double yBreak = 640.0;

    /**
     * Indicates whether the paddle has gold status.
     */
    private boolean isGoldStatus = false;

    /**
     * Represents the count of destroyed blocks in the game.
     */
    private int destroyedBlockCount = 0;

    /**
     * Represents the heart count in the game.
     */
    private static int heart = 500;

    /**
     * Represents the score in the game.
     */
    private static int score = 0;

    /**
     * Represents the time variable in the game.
     */
    private long time = 0;

    /**
     * Represents the time when the gold status hit the star block.
     */
    private long goldTime = 0;

    /**
     * Represents the time when the paddle was last hit.
     */
    private long hitTime = 0;

    /**
     * Represents the direction constant for left movement of the paddle.
     */
    private static final int LEFT = 1;

    /**
     * Represents the direction constant for right movement of the paddle.
     */
    private static final int RIGHT = 2;

    /**
     * Represents a collection of Bonus items dropped after some certain blocks destroyed.
     */
    private final ArrayList<Bonus> choco = new ArrayList<>();

    /**
     * Represents a collection of Penalty items dropped after some certain blocks destroyed.
     */
    private final ArrayList<Penalty> penalty = new ArrayList<>();


    /**
     * Initiates the transition to the next level of the game. This method is intended to be run on the JavaFX Application
     * thread using {@link Platform#runLater(Runnable)} to ensure UI updates occur on the JavaFX Application thread.
     */
    public void nextLevel() {
        Platform.runLater(() -> {
            try {
                // Set the horizontal velocity of the ball for the next level
                ballMovement.setvX(1.000);
                // Stop the game engine
                gameInitializer.stopEngine();
                // Reset collision flags for the ball
                ballMovement.resetCollideFlags();
                // Set the ball to move downward
                ballMovement.setGoDownBall(true);
                // Reset gold status and heart block existence
                isGoldStatus = false;
                gameInitializer.setExistHeartBlock(false);
                // Reset timers
                hitTime = 0;
                time = 0;
                goldTime = 0;
                // Clear blocks, bonuses, and penalties
                gameInitializer.stopEngine();
                gameInitializer.getBlocks().clear();
                choco.clear();
                // Reset destroyed block count
                destroyedBlockCount = 0;
                // Notify the UI controller of the level up
                uiController.levelUp();
                // Initialize the UI for the new level
                uiController.initializeUI();

            } catch (Exception e) {
                // Print the stack trace in case of an exception
                e.printStackTrace();
            }
        });
    }

    /**
     * Indicates whether the bgm is currently in a playing state.
     * If true, the bgm is actively being played; if false, the game is paused.
     */
    private boolean isPlaying = true;
    /**
     * Handles key events for user input, responding to specific key presses such as left and right arrow keys,
     * saving the game, and toggling between pause and resume states using the 'S' and 'SPACE' keys, respectively.
     *
     * @param event The KeyEvent representing the key press event.
     */
    @Override
    public void handle(KeyEvent event) {
        switch (event.getCode()) {
            case LEFT:
                // Move the game paddle to the left
                move(LEFT);
                break;
            case RIGHT:
                // Move the game paddle to the right
                move(RIGHT);
                break;
            case S:
                // Save the current game state
                gameIOController.saveGame();
                break;
            case SPACE:
                // Toggle between pause and resume states
                if (isPlaying) {
                    // Pause the game and show the pause message
                    Main.getBackgroundMusic().pause();
                    uiController.showPause();
                } else {
                    // Resume the game and show the resume message
                    Main.getBackgroundMusic().resume();
                    uiController.showResume();
                }
                isPlaying = !isPlaying; // Toggle the playing state
                break;
            case P:
                gameInitializer.stopEngine();
                break;
            case R:
                gameInitializer.startEngine();
        }
    }


    /**
     * Moves the paddle (breakable object) in the specified direction on a separate thread.
     * The movement is animated with a gradual change in the x-coordinate of the paddle.
     *
     * @param direction The direction of movement: {@link #LEFT} for left, {@link #RIGHT} for right.
     */
    private void move(final int direction) {
        new Thread(() -> {
            // Initial sleep time for smooth animation
            int sleepTime = 4;

            // Loop for gradual movement
            for (int i = 0; i < 30; i++) {
                // Check if the paddle has reached the right boundary
                if (gameInitializer.getxBreak() == (uiController.getSceneWidth() - gameInitializer.getBreakWidth()) && direction == RIGHT) {
                    return;
                }

                // Check if the paddle has reached the left boundary
                if (gameInitializer.getxBreak() == 0 && direction == LEFT) {
                    return;
                }

                // Update UI on JavaFX Application Thread
                Platform.runLater(() -> {
                    if (direction == RIGHT) {
                        // Move the paddle to the right
                        gameInitializer.setxBreak(gameInitializer.getxBreak() + 1.0);
                    } else {
                        // Move the paddle to the left
                        gameInitializer.setxBreak(gameInitializer.getxBreak() - 1.0);
                    }
                    centerBreakX = gameInitializer.getxBreak() + gameInitializer.getHalfBreakWidth();
                });

                try {
                    // Pause to create a smooth animation effect
                    Thread.sleep(sleepTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // Adjust sleep time for smoother animation after a certain duration
                if (i >= 20) {
                    sleepTime = i;
                }
            }
        }).start();
    }

    /**
     * Checks whether the count of destroyed blocks matches the total number of blocks.
     * If true, it indicates that all blocks have been destroyed, and the game proceeds to the next level.
     * Prints the current count of destroyed blocks and the total number of blocks to the console.
     */
    public void checkDestroyedCount() {
        // Print the current count of destroyed blocks to the console
        System.out.println("DestroyedCount: " + destroyedBlockCount);

        // Print the total number of blocks to the console
        System.out.println("BlocksNum: " + gameInitializer.getBlocks().size());

        // Check if all blocks have been destroyed
        if (destroyedBlockCount == gameInitializer.getBlocks().size()) {
            // Proceed to the next level of the game
            nextLevel();
        }
    }

    /**
     * Restarts the game, resetting various game-related parameters and initiating the game start sequence.
     * This method is intended to be used when restarting the game after a game over or at the player's discretion.
     * It sets the level to zero, resets the heart count, score, ball velocity, and other game-related variables.
     * Clears the blocks and bonuses from the previous game, and initiates the start of the game through the UI controller.
     */
    public void restartGame() {
        try {
            // Reset game parameters for a new game
            gameInitializer.setLevel(0);
            heart = 3;
            score = 0;
            ballMovement.setvX(1.000);
            destroyedBlockCount = 0;
            ballMovement.resetCollideFlags();
            ballMovement.setGoDownBall(true);

            // Reset various game-related flags and timers
            isGoldStatus = false;
            gameInitializer.setExistHeartBlock(false);
            hitTime = 0;
            time = 0;
            goldTime = 0;

            // Clear blocks and bonuses from the previous game
            gameInitializer.getBlocks().clear();
            choco.clear();

            // Initiate the start of the game through the UI controller
            uiController.startGame();
        } catch (Exception e) {
            // Print the stack trace in case of an exception
            e.printStackTrace();
        }
    }

    /**
     * Overrides the {@link GameEngine.OnAction#onUpdate()} interface method to handle the update event.
     * Updates the game logic and triggers the update of the UI on the JavaFX Application Thread.
     */
    @Override
    public void onUpdate() {
        // Update game logic
        updateGameLogic();

        // Update UI on the JavaFX Application Thread
        Platform.runLater(this::updateUI);
    }

    /**
     * Updates the game logic by iterating through blocks, checking for collisions with the ball,
     * and handling the collisions accordingly.
     * This method is called during the game update to process interactions between the ball and blocks.
     */
    private void updateGameLogic() {
        // Update blocks and collisions
        for (final Block block : gameInitializer.getBlocks()) {
            // Check for collisions between the ball and the current block
            int hitCode = block.checkHitToBlock(gameInitializer.getxBall(), gameInitializer.getyBall(), gameInitializer.getBallRadius());

            // Handle the collision based on the hit code
            if (hitCode != Block.NO_HIT) {
                handleBlockCollision(block, hitCode);
            }
        }
    }

    /**
     * Updates the UI components by setting the text for score and heart labels,
     * and adjusting the positions of paddle, ball, bonuses, and penalties on the JavaFX Application Thread.
     * This method is called during the game update to reflect changes in the game state on the user interface.
     */
    private void updateUI() {
        // Update UI components
        scoreLabel.setText("Score: " + score);
        heartLabel.setText("Heart: " + heart);

        // Set the position of the paddle (breakable object)
        gameInitializer.getRect().setX(gameInitializer.getxBreak());
        gameInitializer.getRect().setY(yBreak);

        // Set the position of the ball
        gameInitializer.getBall().setCenterX(gameInitializer.getxBall());
        gameInitializer.getBall().setCenterY(gameInitializer.getyBall());

        // Update positions of bonuses on the JavaFX Application Thread
        Platform.runLater(() -> {
            for (Bonus choco : choco) {
                choco.choco.setY(choco.y);
            }
            for (Penalty penalty : penalty) {
                penalty.crackedBrick.setY(penalty.y);
            }
        });
    }


    /**
     * Determines whether to generate a Bonus based on a predefined logic.
     * The decision is made randomly, for example, with a 50% probability.
     *
     * @return true if a Bonus should be generated, false otherwise.
     */
    private boolean shouldGenerateBonus() {
        return new Random().nextBoolean();
    }


    /**
     * Handles collisions between the ball and a block, updating game state and triggering
     * specific actions based on the type of the block and the hit code.
     *
     * @param block   The block with which the ball has collided.
     * @param hitCode The code indicating the type and direction of the collision.
     */
    private void handleBlockCollision(Block block, int hitCode) {
        // Increment score and display a score message
        score += 1;
        new Score().show(block.x, block.y, 1);

        // Hide the block and mark it as destroyed
        block.rect.setVisible(false);
        block.isDestroyed = true;
        destroyedBlockCount++;

        // Reset collision flags for the ball
        ballMovement.resetCollideFlags();

        // Handle specific actions based on the type of block and hit code
        if (block.type == Block.BLOCK_CHOCO) {
            // Create and display a Bonus (choco) associated with the block
            final Bonus choco = new Bonus(block.row, block.column);
            choco.timeCreated = time;
            Platform.runLater(() -> root.getChildren().add(choco.choco));
            this.choco.add(choco);
        } else if (block.type == Block.BLOCK_STAR) {
            // Activate gold status and change ball appearance
            goldTime = time;
            gameInitializer.getBall().setFill(new ImagePattern(new Image("goldball.png")));
            root.getStyleClass().add("goldRoot");
            isGoldStatus = true;
        } else if (block.type == Block.BLOCK_PENALTY) {
            // Create and display a Penalty associated with the block
            final Penalty penalty = new Penalty(block.row, block.column);
            penalty.timeCreated = time;
            Platform.runLater(() -> root.getChildren().add(penalty.crackedBrick));
            this.penalty.add(penalty);
        } else if (block.type == Block.BLOCK_SPECIAL) {
            // Generate either a Bonus or a Penalty based on game conditions
            if (shouldGenerateBonus()) {
                final Bonus specialChoco = new Bonus(block.row, block.column);
                specialChoco.timeCreated = time;
                Platform.runLater(() -> root.getChildren().add(specialChoco.choco));
                this.choco.add(specialChoco);
            } else {
                final Penalty specialPenalty = new Penalty(block.row, block.column);
                specialPenalty.timeCreated = time;
                Platform.runLater(() -> root.getChildren().add(specialPenalty.crackedBrick));
                this.penalty.add(specialPenalty);
            }
        } else if (block.type == Block.BLOCK_HEART) {
            // Increment the heart count
            heart++;
        }

        // Handle collisions based on the hitCode indicating the direction of collision
        if (hitCode == Block.HIT_RIGHT) {
            ballMovement.setCollideToRightBlock(true);
        } else if (hitCode == Block.HIT_BOTTOM) {
            ballMovement.setCollideToBottomBlock(true);
        } else if (hitCode == Block.HIT_LEFT) {
            ballMovement.setCollideToLeftBlock(true);
        } else if (hitCode == Block.HIT_TOP) {
            ballMovement.setCollideToTopBlock(true);
        } else if (hitCode == Block.HIT_TOP_LEFT) {
            ballMovement.setCollideToTopBlock(true);
            ballMovement.setCollideToLeftBlock(true);
        } else if (hitCode == Block.HIT_TOP_RIGHT) {
            ballMovement.setCollideToTopBlock(true);
            ballMovement.setCollideToRightBlock(true);
        } else if (hitCode == Block.HIT_BOTTOM_LEFT) {
            ballMovement.setCollideToBottomBlock(true);
            ballMovement.setCollideToLeftBlock(true);
        } else if (hitCode == Block.HIT_BOTTOM_RIGHT) {
            ballMovement.setCollideToBottomBlock(true);
            ballMovement.setCollideToRightBlock(true);
        }
    }

    /**
     * Overrides the {@link GameEngine.OnAction#onInit()} interface method to handle the initialization event.
     * This method is called during the game initialization phase, allowing for custom initialization logic.
     */
    @Override
    public void onInit() {

    }

    /**
     * Overrides the {@link GameEngine.OnAction#onPhysicsUpdate()} interface method to handle physics-related updates.
     * This method is called during the game's physics update phase, allowing for custom physics and game logic.
     */
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
                new Score().show(choco.x, choco.y, 3);
            }
            choco.y += ((time - choco.timeCreated) / 1000.000) + 1.000;
        }

        for (Penalty penalty : penalty) {
            if (penalty.y > uiController.getSceneHeight() || penalty.taken) {
                continue;
            }
            if (penalty.y >= yBreak && penalty.y <= yBreak + gameInitializer.getBreakHeight() && penalty.x >= gameInitializer.getxBreak() && penalty.x <= gameInitializer.getxBreak() + gameInitializer.getBreakWidth()) {
                System.out.println("You Got flamed and -3 score");
                penalty.taken = true;
                penalty.crackedBrick.setVisible(false);
                score -= 3;
                new Score().show(penalty.x, penalty.y, -3);
            }
            penalty.y += ((time - penalty.timeCreated) / 1000.000) + 1.000;
        }
    }

    /**
     * Overrides the {@link GameEngine.OnAction#onTime(long)} interface method to update the game time.
     * This method is called to synchronize the game time with the provided time parameter.
     *
     * @param time The new time value to set for the game.
     */
    @Override
    public void onTime(long time) {
        this.time = time;
    }

    /**
     * Gets the root pane of the game.
     *
     * @return The root pane of the game.
     */
    public Pane getRoot() {
        return root;
    }

    /**
     * Gets the game initializer associated with the game.
     *
     * @return The game initializer instance.
     */
    public GameInitializer getGameInitializer() {
        return gameInitializer;
    }

    /**
     * Gets the game I/O controller associated with the game.
     *
     * @return The game I/O controller instance.
     */
    public GameIOController getGameIOController() {
        return gameIOController;
    }

    /**
     * Gets the center X-coordinate of the paddle.
     *
     * @return The center X-coordinate of the paddle.
     */
    public double getCenterBreakX() {
        return centerBreakX;
    }

    /**
     * Gets the Y-coordinate of the paddle.
     *
     * @return The Y-coordinate of the paddle.
     */
    public double getyBreak() {
        return yBreak;
    }

    /**
     * Checks if the game is in gold status.
     *
     * @return true if the game is in gold status, false otherwise.
     */
    public boolean getIsGoldStatus() {
        return isGoldStatus;
    }

    /**
     * Gets the time when gold status was activated.
     *
     * @return The time when gold status was activated.
     */
    public long getGoldTime() {
        return goldTime;
    }

    /**
     * Gets the current game time.
     *
     * @return The current game time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Gets the list of Bonus objects (choco) in the game.
     *
     * @return The list of Bonus objects.
     */
    public ArrayList<Bonus> getChoco() {
        return choco;
    }

    /**
     * Gets the current score in the game.
     *
     * @return The current score.
     */
    public int getScore() {
        return score;
    }

    /**
     * Sets the gold status of the game.
     *
     * @param isGoldStatus true to activate gold status, false otherwise.
     */
    public void setGoldStatus(boolean isGoldStatus) {
        this.isGoldStatus = isGoldStatus;
    }

    /**
     * Sets the hit time for the game.
     *
     * @param hitTime The hit time to set.
     */
    public void setHItTime(long hitTime) {
        this.hitTime = hitTime;
    }

    /**
     * Sets the current score in the game.
     *
     * @param count The score to set.
     */
    public void setScore(int count) {
        score = count;
    }

    /**
     * Gets the current heart count in the game.
     *
     * @return The current heart count.
     */
    public int getHeart() {
        return heart;
    }

    /**
     * Sets the current heart count in the game.
     *
     * @param count The heart count to set.
     */
    public void setHeart(int count) {
        heart = count;
    }

    /**
     * Sets the X-coordinate of the paddle.
     *
     * @param count The X-coordinate to set.
     */
    public void setxBreak(double count) {
        this.xBreak = count;
    }

    /**
     * Sets the Y-coordinate of the paddle.
     *
     * @param yBreak The Y-coordinate to set.
     */
    public void setyBreak(double yBreak) {
        this.yBreak = yBreak;
    }

    /**
     * Sets the center X-coordinate of the paddle.
     *
     * @param centerBreakX The center X-coordinate to set.
     */
    public void setCenterBreakX(double centerBreakX) {
        this.centerBreakX = centerBreakX;
    }

    /**
     * Sets the current game time.
     *
     * @param time The game time to set.
     */
    public void setTime(long time) {
        this.time = time;
    }

    /**
     * Sets the time when gold status was activated.
     *
     * @param goldTime The gold status activation time to set.
     */
    public void setGoldTime(long goldTime) {
        this.goldTime = goldTime;
    }
}
