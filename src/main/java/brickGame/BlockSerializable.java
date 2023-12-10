package brickGame;

import java.io.Serializable;

/**
 * A serializable representation of a Block.
 */
public class BlockSerializable implements Serializable {
    /** The row of the Block. */
    public final int row;

    /** The column of the Block. */
    public final int j;

    /** The type of the Block. */
    public final int type;

    /**
     * Constructs a BlockSerializable with the specified row, column, and type.
     *
     * @param row  The row of the Block.
     * @param j    The column of the Block.
     * @param type The type of the Block.
     */
    public BlockSerializable(int row, int j, int type) {
        this.row = row;
        this.j = j;
        this.type = type;
    }
}
