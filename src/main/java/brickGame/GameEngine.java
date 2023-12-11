package brickGame;

import javafx.application.Platform;

/**
 * The GameEngine class is responsible for managing the game loop and coordinating game-related actions.
 */
public class GameEngine {

    /** Interface for handling game-related actions such as updates, initialization, physics updates, and time updates. */
    private OnAction onAction;

    /** Frames per second setting for the game loop. */
    private int fps = 15;

    /** Thread for running the game loop. */
    private Thread gameThread;

    /** Flag indicating whether the game loop is stopped. */
    private boolean isStopped = true;

    /** Current time value within the game engine. */
    private long time = 0;

    /**
     * Sets the {@link OnAction} interface to handle game-related actions.
     *
     * @param onAction The instance implementing the {@link OnAction} interface.
     *                 This interface defines methods for handling game updates, initialization, physics updates,
     *                 and updating time within the game engine.
     */
    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
        System.out.println("Game engine");
    }

    /**
     * @param fps set fps and convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 800 / fps;
    }

    /**
     * The main game loop responsible for continuously updating the game state.
     * The loop calls the relevant methods from the {@link OnAction} interface to handle game updates,
     * physics updates, and time updates.
     */
    private void gameLoop() {
        while (!isStopped) {
            // Update the game state
            onAction.onUpdate();
            // Update physics-related aspects of the game
            onAction.onPhysicsUpdate();
            // Update the game time and notify the game engine
            onAction.onTime(time);
            time++;
            try {
                Thread.sleep(fps);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    /**
     * Starts the game engine and initiates the game loop if it is currently stopped.
     * If the game loop is already running, this method has no effect.
     */
    public void start() {
        if (isStopped) {
            // Set the game loop to running state
            isStopped = false;
            initialize();
            gameThread = new Thread(this::gameLoop);
            gameThread.start();
        }
    }

    /**
     * Stops the game engine and halts the game loop if it is currently running.
     * If the game loop is already stopped, this method has no effect.
     */
    public void stop() {
        if (!isStopped) {
            isStopped = true;
            if (gameThread != null) {
                try {
                    gameThread.join(); // Wait for the thread to finish
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Restore the interrupted status
                }
            }
        }
    }

    /**
     * Initializes the game by invoking the {@link OnAction#onInit()} method on the JavaFX Application Thread.
     * This method is intended for performing setup tasks before the game loop starts.
     */
    private void initialize() {
        Platform.runLater(() -> onAction.onInit());
    }

    /**
     * Interface for handling game-related actions such as updates, initialization, physics updates, and time updates.
     */
    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}