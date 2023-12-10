package brickGame;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

/**
 * The {@code Score} class provides methods for displaying score-related messages,
 * such as showing scores, game over messages, and victory messages in a JavaFX game.
 */
public class Score {

    /**
     * The game controller associated with the UI, used to access the root node and restart the game.
     */
    GameController gameController = Main.getUiController().getLoader().getController();

    /**
     * Displays a score label animation at the specified position on the game screen.
     *
     * @param x     The x-coordinate of the position.
     * @param y     The y-coordinate of the position.
     * @param score The score to be displayed.
     */
    public void show(final double x, final double y, int score) {
        String sign;
        if (score >= 0) {
            sign = "+";
        } else {
            sign = "";
        }
        final Label label = new Label(sign + score);
        label.setTranslateX(x);
        label.setTranslateY(y);

        Platform.runLater(() -> gameController.getRoot().getChildren().add(label));

        new Thread(() -> {
            try {
                for (int i = 0; i < 21; i++) {
                    final int finalI = i;
                    Platform.runLater(() -> {
                        label.setScaleX(finalI);
                        label.setScaleY(finalI);
                        label.setOpacity((20 - finalI) / 20.0);
                    });
                    Thread.sleep(15);
                }
                // Remove the label after the animation
                Platform.runLater(() -> gameController.getRoot().getChildren().remove(label));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }


    /**
     * Displays a message label animation at a fixed position on the game screen.
     *
     * @param message The message to be displayed.
     */
    public void showMessage(String message) {
        final Label label = new Label(message);
        label.setTranslateX(220);
        label.setTranslateY(340);

        Platform.runLater(() -> gameController.getRoot().getChildren().add(label));

        new Thread(() -> {
            for (int i = 0; i < 21; i++) {
                try {
                    label.setScaleX(Math.abs(i-10));
                    label.setScaleY(Math.abs(i-10));
                    label.setOpacity((20 - i) / 20.0);
                    Thread.sleep(15);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Displays a game over message along with a restart button on the game screen.
     */
    public void showGameOver() {
        Platform.runLater(() -> {
            Label label = new Label("Game Over :(");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            Button restart = new Button("Restart");
            restart.setTranslateX(220);
            restart.setTranslateY(300);
            restart.setOnAction(event -> gameController.restartGame());

            gameController.getRoot().getChildren().addAll(label, restart);

        });
    }

    /**
     * Displays a victory message on the game screen.
     */
    public void showWin() {
        Platform.runLater(() -> {
            Label label = new Label("You Win :)");
            label.setTranslateX(200);
            label.setTranslateY(250);
            label.setScaleX(2);
            label.setScaleY(2);

            gameController.getRoot().getChildren().addAll(label);

        });
    }
}