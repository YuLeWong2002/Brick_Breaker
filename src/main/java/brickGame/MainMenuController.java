package brickGame;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;

/**
 * Controller class for the main menu screen. Handles user interactions and initiates game-related actions.
 */
public class MainMenuController {

    /** Button for loading a saved game. */
    @FXML
    private Button load;

    /** Button for starting a new game. */
    @FXML
    private Button newGame;

    /**
     * Default constructor for the MainMenuController class.
     * This constructor is automatically called when the FXML file is loaded.
     */
    public MainMenuController() {
    }

    /**
     * Event handler method for the "Load" button click.
     * Switches to the game scene, initializes game elements, and loads a saved game.
     *
     * @throws IOException If an error occurs during the loading of the game.
     */
    @FXML
    private void onClickLoad() throws IOException {
        Main.getUiController().switchToGameScene();
        GameController gameController = Main.getUiController().getGameController();
        Main.getGameInitializer().initBall(gameController.getRoot());
        Main.getGameInitializer().initBreak(gameController.getRoot());
        gameController.getGameIOController().loadGame();
        load.setVisible(false);
        newGame.setVisible(false);
    }

    /**
     * Event handler method for the "New Game" button click.
     * Switches to the game scene and starts a new game with initialized elements.
     *
     * @throws IOException If an error occurs during the initialization of the new game.
     */
    @FXML
    private void onClickNewGame() throws IOException{
        load.setVisible(false);
        newGame.setVisible(false);
        Main.getUiController().switchToGameScene();
        Main.getUiController().startNewGameElements();
        Main.getGameInitializer().initializeEngine(Main.getUiController().loader.getController());
    }
}
