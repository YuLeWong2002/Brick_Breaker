package brickGame;


import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

/**
 * Represents a block in the game, which is serializable for saving/loading game state.
 */
public class Block implements Serializable {

    /**
     * A default block instance with special values indicating it's not part of the regular game board.
     * This is useful for creating a placeholder block with transparent color.
     */
    private static final Block block = new Block(-1, -1, Color.TRANSPARENT, 99);

    /** The row position of the block in the game grid. */
    public int row;

    /** The column position of the block in the game grid. */
    public int column;


    /** Indicates whether the block has been destroyed. */
    public boolean isDestroyed = false;

    /** The color of the block. */
    private final Color color;

    /** The type of the block. */
    public int type;

    /** The x-coordinate of the block in the game grid. */
    public int x;

    /** The y-coordinate of the block in the game grid. */
    public int y;

    /** The width of the block. */
    private final int width = 100;

    /** The height of the block. */
    private final int height = 30;

    /** The padding at the top of the block. */
    private final int paddingTop = height * 2;

    /** The horizontal padding of the block. */
    private final int paddingH = 50;

    /** The graphical representation of the block. */
    public Rectangle rect;

    /** No hit or an undefined state. */
    public static final int NO_HIT = -1;

    /** Hit the right side. */
    public static final int HIT_RIGHT = 0;

    /** Hit the bottom side. */
    public static final int HIT_BOTTOM = 1;

    /** Hit the left side. */
    public static final int HIT_LEFT = 2;

    /** Hit the top side. */
    public static final int HIT_TOP = 3;

    /** Hit the top-left corner. */
    public static final int HIT_TOP_LEFT = 4;

    /** Hit the top-right corner. */
    public static final int HIT_TOP_RIGHT = 5;

    /** Hit the bottom-left corner. */
    public static final int HIT_BOTTOM_LEFT = 6;

    /** Hit the bottom-right corner. */
    public static final int HIT_BOTTOM_RIGHT = 7;

    /** Represents a normal game block. */
    public static final int BLOCK_NORMAL = 99;

    /** Represents a chocolate game block. */
    public static final int BLOCK_CHOCO = 100;

    /** Represents a star game block. */
    public static final int BLOCK_STAR = 101;

    /** Represents a heart game block. */
    public static final int BLOCK_HEART = 102;

    /** Represents a penalty game block. */
    public static final int BLOCK_PENALTY = 103;

    /** Represents a special game block. */
    public static final int BLOCK_SPECIAL = 104;

    /**
     * Constructs a new Block with the specified parameters.
     *
     * @param row    The row index of the block.
     * @param column The column index of the block.
     * @param color  The color of the block.
     * @param type   The type of the block.
     */

    public Block(int row, int column, Color color, int type) {
        this.row = row;
        this.column = column;
        this.color = color;
        this.type = type;

        draw();
    }

    /**
     * Draws and initializes the block based on its type and position.
     */
    private void draw() {
        x = (column * width) + paddingH;
        y = (row * height) + paddingTop;

        rect = new Rectangle();
        rect.setWidth(width);
        rect.setHeight(height);
        rect.setX(x);
        rect.setY(y);

        if (type == BLOCK_CHOCO) {
            Image image = new Image("choco.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_HEART) {
            Image image = new Image("heart.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if (type == BLOCK_STAR) {
            Image image = new Image("star.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if(type == BLOCK_PENALTY) {
            Image image = new Image("BrokenBrick.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        } else if(type == BLOCK_SPECIAL) {
            Image image = new Image("SpecialBrick.jpg");
            ImagePattern pattern = new ImagePattern(image);
            rect.setFill(pattern);
        }
        else {
            rect.setFill(color);
        }

    }

    /**
     * Gets the corners of the block given its position and dimensions.
     *
     * @param x      The x-coordinate of the block.
     * @param y      The y-coordinate of the block.
     * @param width  The width of the block.
     * @param height The height of the block.
     * @return A 2D array representing the corners of the block.
     */
    public static double[][] getBlockCorners(double x, double y, double width, double height) {
        double[][] corners = new double[4][2];

        // Top-left corner
        corners[0][0] = x;
        corners[0][1] = y;

        // Top-right corner
        corners[1][0] = x + width;
        corners[1][1] = y;

        // Bottom-left corner
        corners[2][0] = x;
        corners[2][1] = y + height;

        // Bottom-right corner
        corners[3][0] = x + width;
        corners[3][1] = y + height;

        return corners;
    }

    /**
     * Checks if the ball hits the block and returns the hit code.
     *
     * @param xBall      The x-coordinate of the ball.
     * @param yBall      The y-coordinate of the ball.
     * @param ballRadius The radius of the ball.
     * @return The hit code indicating the side of the block that was hit.
     */
    public int checkHitToBlock(double xBall, double yBall, double ballRadius) {
        if (isDestroyed) {
            return NO_HIT;
        }

        double blockRight = x + width;
        double blockBottom = y + height;

        // Calculate the closest point on the block to the ball
        double closestX = Math.max(x, Math.min(xBall, blockRight));
        double closestY = Math.max(y, Math.min(yBall, blockBottom));

        // Calculate the distance between the closest point and the ball's center
        double distanceX = xBall - closestX;
        double distanceY = yBall - closestY;
        double distanceSquared = distanceX * distanceX + distanceY * distanceY;

        List<Integer> hitCodes = new ArrayList<>();

        // Check if the distance is less than the ball's radius squared
        if (distanceSquared < (ballRadius * ballRadius)) {
            double deltaX = Math.min(Math.abs(xBall - x), Math.abs(xBall - blockRight));
            double deltaY = Math.min(Math.abs(yBall - y), Math.abs(yBall - blockBottom));

            if (deltaX < deltaY) {
                if (xBall > x + (double) width / 2) {
                    hitCodes.add(HIT_RIGHT);
                } else {
                    hitCodes.add(HIT_LEFT);
                }
            } else {
                if (yBall > y + (double) height / 2) {
                    hitCodes.add(HIT_BOTTOM);
                } else {
                    hitCodes.add(HIT_TOP);
                }
            }
        } else {
            // Calculate the corners of the block
            double[][] corners = getBlockCorners(x, y, width, height);

            // Calculate distances from the ball's center to each corner
            double[] distances = new double[4];
            for (int i = 0; i < 4; i++) {
                double distanceXCorner = xBall - corners[i][0];
                double distanceYCorner = yBall - corners[i][1];
                distances[i] = distanceXCorner * distanceXCorner + distanceYCorner * distanceYCorner;
            }

            // Check if the distance to each corner is less than the ball's radius squared
            for (int i = 0; i < 4; i++) {
                if (distances[i] < (ballRadius * ballRadius)) {
                    // Determine the corner based on the index
                    switch (i) {
                        case 0:
                            hitCodes.add(HIT_TOP_LEFT);
                            break;
                        case 1:
                            hitCodes.add(HIT_TOP_RIGHT);
                            break;
                        case 2:
                            hitCodes.add(HIT_BOTTOM_LEFT);
                            break;
                        case 3:
                            hitCodes.add(HIT_BOTTOM_RIGHT);
                            break;
                    }
                }
            }
        }

        // Handle multiple hits
        if (!hitCodes.isEmpty()) {
            // Prioritize hits in a specific order
            if (hitCodes.contains(HIT_TOP_RIGHT) && hitCodes.contains(HIT_TOP_LEFT)) {
                return HIT_TOP;
            } else if (hitCodes.contains(HIT_BOTTOM_RIGHT) && hitCodes.contains(HIT_BOTTOM_LEFT)){
                return HIT_BOTTOM;
            } else if (hitCodes.contains(HIT_TOP_RIGHT) && hitCodes.contains(HIT_BOTTOM_RIGHT)) {
                return HIT_RIGHT;
            } else if (hitCodes.contains(HIT_BOTTOM_LEFT) && hitCodes.contains(HIT_TOP_LEFT)) {
                return HIT_LEFT;
//            }  else if (hitCodes.contains(HIT_LEFT) && hitCodes.contains(HIT_TOP_LEFT)){
//                return HIT_LEFT;
//            } else if (hitCodes.contains(HIT_RIGHT) && hitCodes.contains(HIT_TOP_RIGHT)) {
//                return HIT_RIGHT;
//            } else if (hitCodes.contains(HIT_LEFT) && hitCodes.contains(HIT_BOTTOM_LEFT)) {
//                return HIT_LEFT;
//            } else if (hitCodes.contains(HIT_RIGHT) && hitCodes.contains(HIT_BOTTOM_RIGHT)){
//                return HIT_RIGHT;
//            } else if (hitCodes.contains(HIT_TOP) && hitCodes.contains(HIT_TOP_RIGHT)) {
//                return HIT_TOP;
//            } else if (hitCodes.contains(HIT_BOTTOM) && hitCodes.contains(HIT_BOTTOM_LEFT)) {
//                return HIT_BOTTOM;
//            } else if (hitCodes.contains(HIT_BOTTOM) && hitCodes.contains(HIT_BOTTOM_RIGHT)) {
//                return HIT_TOP;
//            } else if (hitCodes.contains(HIT_TOP) && hitCodes.contains(HIT_TOP_LEFT)) {
//                return HIT_TOP;
            } else {
                // Return the first hit code if none of the priority conditions are met
                return hitCodes.get(0);
            }
        }

        return NO_HIT;
    }

    /**
     * Gets the top padding of the block.
     *
     * @return The top padding value.
     */
    public static int getPaddingTop() {
        return block.paddingTop;
    }

    /**
     * Gets the horizontal padding of the block.
     *
     * @return The horizontal padding value.
     */
    public static int getPaddingH() {
        return block.paddingH;
    }

    /**
     * Gets the height of the block.
     *
     * @return The height value.
     */
    public static int getHeight() {
        return block.height;
    }

    /**
     * Gets the width of the block.
     *
     * @return The width value.
     */
    public static int getWidth() {
        return block.width;
    }
}
