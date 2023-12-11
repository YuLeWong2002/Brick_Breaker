package brickGame;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.Random;

/**
 * The GameInitializer class is responsible for managing the initialization and state of the game.
 * It holds information about the game level, ball, paddle, blocks, and related properties.
 */
public class GameInitializer {

    /** The current level of the game. */
    private int level = 0;

    /** The ball used in the game. */
    private Circle ball;

    /** The radius of the ball. */
    private final int ballRadius = 10;

    /** The x-coordinate of the ball. */
    private double xBall;

    /** The y-coordinate of the ball. */
    private double yBall;

    /** The paddle (rectangle) used in the game. */
    private Rectangle rect;

    /** The x-coordinate of the paddle. */
    private double xBreak = 0.0f;

    /** The y-coordinate of the paddle. */
    private final double yBreak = 640.0f;

    /** The width of the paddle. */
    private final int breakWidth = 130;

    /** Half of the width of the paddle. */
    private final int halfBreakWidth = breakWidth / 2;

    /** The height of the paddle. */
    private final int breakHeight = 30;

    /** Indicates whether a special "heart" block exists in the game. */
    private boolean isExistHeartBlock = false;

    /** Indicates whether the game is being loaded from a saved state. */
    private boolean loadFromSave = false;

    /** The list of blocks present in the game. */
    private final ArrayList<Block> blocks = new ArrayList<>();

    /** An array of colors used for representing different types of blocks in the game. */
    private final Color[] colors = new Color[]{
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

    /** The game engine responsible for managing game-related actions and updates. */
    private GameEngine engine;

    /** The UI controller for handling user interface interactions and updates. */
    private final UIController uiController = Main.getUiController();

    /**
     * Default constructor for the GameInitializer class.
     * This constructor is used to create instances of the GameInitializer class.
     */
    public GameInitializer() {
    }

    /**
     * Initializes the game ball, setting its initial position and appearance, and adds it to the specified Pane.
     *
     * @param pane The Pane to which the ball will be added.
     */
    public void initBall(Pane pane) {
        Random random = new Random();
        xBall = random.nextInt(uiController.getSceneWidth()) + 1;
        if(level < 18) {
            int minAllowedY = ((level + 1) * Block.getHeight()) + 60;
            int maxAllowedY = uiController.getSceneHeight() - 90;
            minAllowedY = Math.max(minAllowedY, Block.getHeight() + 60);
            yBall = random.nextInt(maxAllowedY - minAllowedY) + minAllowedY;
        } else if (level == 18) {
            int minAllowedY = (6 * Block.getHeight()) + 60;
            int maxAllowedY = uiController.getSceneHeight() - 90;
            minAllowedY = Math.max(minAllowedY, Block.getHeight() + 60);
            yBall = random.nextInt(maxAllowedY - minAllowedY) + minAllowedY;
        } else if (level == 19) {
            int minAllowedY = (11 * Block.getHeight()) + 60;
            int maxAllowedY = uiController.getSceneHeight() - 90;
            minAllowedY = Math.max(minAllowedY, Block.getHeight() + 60);
            yBall = random.nextInt(maxAllowedY - minAllowedY) + minAllowedY;
        }
        ball = new Circle();
        ball.setRadius(ballRadius);
        ball.setFill(new ImagePattern(new Image("ball.png")));

        // Add the ball to the Pane
        pane.getChildren().add(ball);
    }

    /**
     * Initializes the game paddle, setting its initial position and appearance,
     * and adds it to the specified Pane.
     *
     * @param pane The Pane to which the paddle will be added.
     */

    public void initBreak(Pane pane) {
        rect = new Rectangle();
        rect.setWidth(breakWidth);
        rect.setHeight(breakHeight);
        rect.setX(xBreak);
        rect.setY(yBreak);

        ImagePattern pattern = new ImagePattern(new Image("block.jpg"));

        rect.setFill(pattern);

        pane.getChildren().add(rect);
    }

    /**
     * Initializes the game board by generating random blocks based on the current game level.
     * Blocks are added to the blocks list with random types and colors.
     * The presence of certain special blocks is determined by random conditions.
     */
    public void initBoard() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < level + 1; j++) {
                int r = new Random().nextInt(500);
                if (r % 5 == 0) {
                    continue;
                }
                int type;
                if (r % 10 == 1) {
                    type = Block.BLOCK_CHOCO;
                } else if (r % 10 == 2) {
                    if (!isExistHeartBlock) {
                        type = Block.BLOCK_HEART;
                        isExistHeartBlock = true;
                    } else {
                        type = Block.BLOCK_NORMAL;
                    }
                } else if (r % 10 == 3) {
                    type = Block.BLOCK_STAR;
                } else if (r % 10 == 4) {
                    type = Block.BLOCK_PENALTY;
                }
                else {
                    type = Block.BLOCK_NORMAL;
                }
                blocks.add(new Block(j, i, colors[r % (colors.length)], type));
            }
        }
    }

    /**
     * Initializes a special game board with a limited number of special blocks.
     * Special blocks are added to the blocks list with a transparent color.
     * The decision to generate a special block is based on random conditions.
     */
    public void initSpecialBoard(int level) {
        int type = Block.BLOCK_SPECIAL;
        boolean shouldGenerateBlock;

        int columnsToGenerate;
        if (level == 18) {
            columnsToGenerate = 5;
        } else if (level == 19) {
            columnsToGenerate = 10;
        } else {
            // Default to 5 columns for other levels
            columnsToGenerate = 5;
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < columnsToGenerate; j++) {
                int r = new Random().nextInt(500);
                shouldGenerateBlock = r % 5 != 0;
                if (shouldGenerateBlock) {
                    blocks.add(new Block(j, i, Color.TRANSPARENT, type));
                }
            }
        }
    }


    /**
     * Initializes the game engine with the provided OnAction listener and starts the game engine.
     * If an existing engine is running, it is stopped before creating and starting a new one.
     *
     * @param onAction The OnAction listener to handle game events and updates.
     */
    public void initializeEngine(GameEngine.OnAction onAction) {
        if (engine != null) {
            // Stop the existing engine if it's running
            engine.stop();
        }
        engine = new GameEngine();
        engine.setOnAction(onAction);
        engine.setFps(120);
        engine.start();
    }

    /**
     * Stops the game engine if it is currently running.
     */
    public void stopEngine() {
        if (engine != null) {
            engine.stop();
        }
    }

    public void startEngine() {
        if(engine != null) {
            engine.start();
        }
    }

    /**
     * Initiates the game level based on the current level value.
     * If the level is within a certain range (exclusive), it initializes the game engine with the UI controller's loader controller.
     * If the level is outside the specified range, it sets the loadFromSave flag to false.
     */
    public void startLevel() {
        if (level > 1 && level < 20) {
            initializeEngine(Main.getUiController().getLoader().getController());
        } else{
            loadFromSave = false;
        }
    }

    /**
     * Initializes game elements such as the ball, paddle, and blocks in the specified Pane.
     * If not loading from a saved game, it adds the blocks to the Pane for display.
     *
     * @param pane The Pane where the game elements will be initialized.
     */
    public void initializeElements(Pane pane) {
        initBall(pane);
        initBreak(pane);
        initBoard();
        if(!loadFromSave) {
            for (Block block : blocks) {
                pane.getChildren().add(block.rect);
            }
        }
    }

    /**
     * Initializes game elements for loading a saved game in the specified Pane.
     * Adds the blocks to the Pane for display.
     *
     * @param pane The Pane where the game elements will be initialized for loading a saved game.
     */
    public void initializeLoadElements(Pane pane) {
        if(loadFromSave) {
            for (Block block : blocks) {
                pane.getChildren().add(block.rect);
            }
        }
    }

    /**
     * Initializes special game elements such as the ball, paddle, and special blocks in the specified Pane.
     * If not loading from a saved game, it adds the special blocks to the Pane for display.
     *
     * @param pane The Pane where the special game elements will be initialized.
     */
    public void initializeSpecialElements(Pane pane) {
        initBall(pane);
        initBreak(pane);
        initSpecialBoard(level);
        if(!loadFromSave) {
            for (Block block : blocks) {
                pane.getChildren().add(block.rect);
            }
        }
    }

    /**
     * Gets the radius of the ball.
     *
     * @return The radius of the ball.
     */
    public int getBallRadius() {
        return ballRadius;
    }

    /**
     * Gets the width of the paddle.
     *
     * @return The width of the paddle.
     */
    public int getBreakWidth() {
        return breakWidth;
    }

    /**
     * Gets half of the width of the paddle.
     *
     * @return Half of the width of the paddle.
     */
    public int getHalfBreakWidth() {
        return halfBreakWidth;
    }

    /**
     * Gets the height of the paddle.
     *
     * @return The height of the paddle.
     */
    public int getBreakHeight() {
        return breakHeight;
    }

    /**
     * Gets the Circle representing the ball.
     *
     * @return The Circle representing the ball.
     */
    public Circle getBall() {
        return ball;
    }

    /**
     * Gets the X-coordinate of the ball.
     *
     * @return The X-coordinate of the ball.
     */
    public double getxBall() {
        return xBall;
    }

    /**
     * Gets the Y-coordinate of the ball.
     *
     * @return The Y-coordinate of the ball.
     */
    public double getyBall() {
        return yBall;
    }

    /**
     * Gets the X-coordinate of the paddle.
     *
     * @return The X-coordinate of the paddle.
     */
    public double getxBreak() {
        return xBreak;
    }

    /**
     * Checks if the game is loaded from a saved state.
     *
     * @return True if the game is loaded from a saved state, false otherwise.
     */
    public boolean getLoadFromSave() {
        return loadFromSave;
    }

    /**
     * Gets the Rectangle representing the paddle.
     *
     * @return The Rectangle representing the paddle.
     */
    public Rectangle getRect() {
        return rect;
    }

    /**
     * Gets the list of blocks in the game.
     *
     * @return The list of blocks in the game.
     */
    public ArrayList<Block> getBlocks() {
        return blocks;
    }

    /**
     * Gets the current level of the game.
     *
     * @return The current level of the game.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Sets the level of the game.
     *
     * @param count The level to set.
     */
    public void setLevel(int count) {
        level = count;
    }

    /**
     * Checks if a heart block exists in the game.
     *
     * @return True if a heart block exists, false otherwise.
     */
    public boolean isExistHeartBlock() {
        return isExistHeartBlock;
    }

    /**
     * Sets the X-coordinate of the ball.
     *
     * @param x The new X-coordinate of the ball.
     */
    public void setxBall(double x) {
        xBall = x;
    }

    /**
     * Sets the Y-coordinate of the ball.
     *
     * @param y The new Y-coordinate of the ball.
     */
    public void setyBall(double y) {
        yBall = y;
    }

    /**
     * Sets the X-coordinate of the paddle, ensuring it stays within the range [0, 370].
     *
     * @param newXBreak The new X-coordinate of the paddle.
     */
    public void setxBreak(double newXBreak) {
        newXBreak = Math.max(0.0, Math.min(370.0, newXBreak));
        this.xBreak = newXBreak;
    }

    /**
     * Sets the flag indicating whether the game is loaded from a saved state.
     *
     * @param load True if the game is loaded from a saved state, false otherwise.
     */
    public void setLoadFromSave(boolean load) {
        loadFromSave = load;
    }

    /**
     * Sets the flag indicating whether a heart block exists in the game.
     *
     * @param isExistHeartBlock True if a heart block exists, false otherwise.
     */
    public void setExistHeartBlock(boolean isExistHeartBlock) {
        this.isExistHeartBlock = isExistHeartBlock;
    }

}

