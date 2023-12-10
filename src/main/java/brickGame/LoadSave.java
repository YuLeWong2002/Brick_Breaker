package brickGame;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Represents the state of the game, including various flags, scores, and positions.
 */
public class LoadSave {
    /**
     * Indicates whether a heart block exists in the game.
     */
    public boolean isExistHeartBlock;

    /**
     * Indicates whether the gold status is active.
     */
    public boolean isGoldStatus;

    /**
     * Indicates whether the ball should move down.
     */
    public boolean goDownBall;

    /**
     * Indicates whether the ball should move to the right.
     */
    public boolean goRightBall;

    /**
     * Indicates whether the ball should collide to paddle.
     */
    public boolean collideToBreak;

    /**
     * Indicates whether the ball should collide to paddle and move to the right.
     */
    public boolean collideToBreakAndMoveToRight;

    /**
     * Indicates whether the ball has collided with the right wall.
     */
    public boolean collideToRightWall;

    /**
     * Indicates whether the ball has collided with the left wall.
     */
    public boolean collideToLeftWall;

    /**
     * Indicates whether the ball has collided with a block on the right.
     */
    public boolean collideToRightBlock;

    /**
     * Indicates whether the ball has collided with a block on the bottom.
     */
    public boolean collideToBottomBlock;

    /**
     * Indicates whether the ball has collided with a block on the left.
     */
    public boolean collideToLeftBlock;

    /**
     * Indicates whether the ball has collided with a block on the top.
     */
    public boolean collideToTopBlock;

    /**
     * Represents the current level of the game.
     */
    public int level;

    /**
     * Represents the current score in the game.
     */
    public int score;

    /**
     * Represents the number of hearts in the game.
     */
    public int heart;

    /**
     * Represents the count of destroyed blocks in the game.
     */
    public int destroyedBlockCount;

    /**
     * The x-coordinate of the ball's position.
     */
    public double xBall;

    /**
     * The y-coordinate of the ball's position.
     */
    public double yBall;

    /**
     * The x-coordinate of the paddle position.
     */
    public double xBreak;

    /**
     * The y-coordinate of the paddle position.
     */
    public double yBreak;

    /**
     * The center x-coordinate of the break position.
     */
    public double centerBreakX;

    /**
     * The current time in the game.
     */
    public long time;

    /**
     * The time when the gold status is activated.
     */
    public long goldTime;

    /**
     * The velocity of the ball.
     */
    public double vX;

    /**
     * List of blocks in the game.
     */
    public ArrayList<BlockSerializable> blocks = new ArrayList<BlockSerializable>();

    /**
     * Reads the game state from a saved file using ObjectInputStream.
     * The method populates the instance fields of the LoadSave object with the saved values.
     */
    public void read() {


        try {
            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(new File(Main.savePath)));


            level = inputStream.readInt();
            score = inputStream.readInt();
            heart = inputStream.readInt();
            //destroyedBlockCount = inputStream.readInt();


            xBall = inputStream.readDouble();
            yBall = inputStream.readDouble();
            xBreak = inputStream.readDouble();
            yBreak = inputStream.readDouble();
            centerBreakX = inputStream.readDouble();
            time = inputStream.readLong();
            goldTime = inputStream.readLong();
            vX = inputStream.readDouble();


            isExistHeartBlock = inputStream.readBoolean();
            isGoldStatus = inputStream.readBoolean();
            goDownBall = inputStream.readBoolean();
            goRightBall = inputStream.readBoolean();
            collideToBreak = inputStream.readBoolean();
            collideToBreakAndMoveToRight = inputStream.readBoolean();
            collideToRightWall = inputStream.readBoolean();
            collideToLeftWall = inputStream.readBoolean();
            collideToRightBlock = inputStream.readBoolean();
            collideToBottomBlock = inputStream.readBoolean();
            collideToLeftBlock = inputStream.readBoolean();
            collideToTopBlock = inputStream.readBoolean();


            try {
                blocks = (ArrayList<BlockSerializable>) inputStream.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
