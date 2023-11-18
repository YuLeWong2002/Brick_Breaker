package brickGame;

import javafx.application.Platform;

public class GameEngine {

    private OnAction onAction;
    private int fps = 15;
    private Thread gameThread;
    private boolean isStopped = true;
    private long time = 0;

    public void setOnAction(OnAction onAction) {
        this.onAction = onAction;
    }

    /**
     * @param fps set fps and convert it to millisecond
     */
    public void setFps(int fps) {
        this.fps = 1000 / fps;
    }

    private void gameLoop() {
        while (!isStopped) {
            onAction.onUpdate();
            onAction.onPhysicsUpdate();
            onAction.onTime(time);
            time++;

            try {
                Thread.sleep(fps);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore the interrupted status
                break;
            }
        }
    }

    public void start() {
        if (isStopped) {
            isStopped = false;
            initialize();
            gameThread = new Thread(this::gameLoop);
            gameThread.start();
        }
    }

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

    private void initialize() {
        Platform.runLater(() -> onAction.onInit());
    }

    public interface OnAction {
        void onUpdate();

        void onInit();

        void onPhysicsUpdate();

        void onTime(long time);
    }
}