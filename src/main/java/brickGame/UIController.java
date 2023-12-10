package brickGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static brickGame.Main.getGameInitializer;
import static brickGame.Main.scene;

/**
 * Represents the UI controller responsible for managing UI-related functionality.
 * This class interacts with the Main class and other components to control the UI flow.
 */
public class UIController {
    /**
     * The main application instance associated with the UI controller.
     */
    private final Main main;

    /**
     * The primary stage for the JavaFX application.
     */
    private final Stage primaryStage;

    /**
     * The controller responsible for managing game-related logic.
     */
    private GameController gameController;

    /**
     * The FXMLLoader for loading FXML files in the UI.
     */
    public FXMLLoader loader;

    /**
     * The width of the scene in pixels.
     */
    private final int sceneWidth = 500;

    /**
     * The height of the scene in pixels.
     */
    private final int sceneHeight = 700;

    /**
     * Constructs a new UIController with the specified Main instance and primary stage.
     *
     * @param main         The Main instance associated with the application.
     * @param primaryStage The primary stage for the JavaFX application.
     */
    public UIController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
    }

    /**
     * Initiates the start of the game through the associated Main instance.
     * Catches any exceptions that may occur during the game start and prints the stack trace.
     */
    public void startGame() {
        try {
            main.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }

    /**
     * Increases the game level by one using the associated GameInitializer instance.
     */
    public void levelUp() {
        Main.getGameInitializer().setLevel(Main.getGameInitializer().getLevel() + 1);
    }

    /**
     * Displays a new level message if the current level is between 2 and 19 (inclusive).
     * Uses the Score class to show the message.
     */
    public void showNewLevel() {
        if (Main.getGameInitializer().getLevel() > 1 && Main.getGameInitializer().getLevel() < 20) {
            new Score().showMessage("Level Up :)");
        }
    }

    /**
     * Displays a win message using the Score class.
     */
    public void showWin() {
        new Score().showWin();
    }

    /**
     * Initializes the User Interface (UI) based on the current state of the game, handling various scenarios such as starting a new game,
     * loading a saved game, or displaying a win screen. This method is responsible for orchestrating the setup of UI elements, scene transitions,
     * and game initialization processes.
     *
     * @throws IOException If an input/output error occurs during the initialization process, particularly when loading UI-related resources.
     */
    public void initializeUI() throws IOException {
        if (!getGameInitializer().getLoadFromSave()) {
            // Start a new game
            if (Main.getGameInitializer().getLevel() < 1) {
                initializeMainMenu();
            }
            // Continue an existing game with regular levels
            else if (Main.getGameInitializer().getLevel() >= 1 && Main.getGameInitializer().getLevel() < 18) {
                switchToGameScene();
                startNewGameElements();
                Main.getGameInitializer().startLevel();
            }
            // Continue an existing game with a special level
            else if (Main.getGameInitializer().getLevel() >= 18 && Main.getGameInitializer().getLevel() < 20) {
                switchToGameScene();
                startSpecialLevelElements();
                Main.getGameInitializer().startLevel();
            }
            // The game has been completed
            else {
                showWin();
                Main.getBackgroundMusic().stop();
            }
        }
        // Load a saved game
        else {
            startLoadGameElements();
            getGameInitializer().initializeEngine(gameController);
            getGameInitializer().setLoadFromSave(false);
        }
    }


    /**
     * Initializes the main menu of the Brick Breaker game, setting up the UI components,
     * scene properties, and preparing the stage for display.
     *
     * @throws IOException If an input/output error occurs during the initialization process,
     *                     particularly when loading the FXML file for the main menu.
     */
    private void initializeMainMenu() throws IOException {
        // Create a new scene using the MainMenu FXML file
        scene = new Scene(Main.loadFXML("MainMenu"), sceneWidth, sceneHeight);
        // Apply a stylesheet to the main menu scene for styling
        scene.getStylesheets().add("style.css");
        // Set the main menu scene to be displayed on the primary stage
        primaryStage.setScene(scene);
        // Set the title of the primary stage
        primaryStage.setTitle("Brick Breaker");
        // Show the primary stage
        primaryStage.show();
        // Perform level up logic, as the main menu initialization implies progressing to the next level
        levelUp();
    }


    /**
     * Switches the application scene to the game scene, loading the "Game.fxml" file
     * and setting up the necessary controllers and event handlers for gameplay.
     *
     * @throws IOException If an input/output error occurs during the loading of the "Game.fxml" file.
     */
    @FXML
    public void switchToGameScene() throws IOException {
        // Create a new FXMLLoader to load the Game.fxml file
        loader = new FXMLLoader(getClass().getResource("/Game.fxml"));
        // Load the root node of the game scene from the FXMLLoader
        Parent gameRoot = loader.load();
        // Get the controller associated with the loaded FXML file (GameController)
        gameController = loader.getController();
        // Set the event handler for key presses on the current scene to the GameController
        scene.setOnKeyPressed(gameController);
        // Set the root of the main scene to the loaded game scene
        Main.scene.setRoot(gameRoot);
    }


    /**
     * Initiates the setup of elements for a new game and new level, including displaying a new level message,
     * setting label text, and initializing game elements if not loading from a saved state.
     */
    public void startNewGameElements() {
        // Display a new level message if applicable
        showNewLevel();
        // Set label text based on game state
        setLabelText();
        // Initialize game elements if not loading from a saved state
        if (!getGameInitializer().getLoadFromSave()) {
            Main.getGameInitializer().initializeElements(gameController.getRoot());
        }
    }

    /**
     * Initiates the setup of elements for a loaded game, setting label text and initializing
     * elements based on the loaded state.
     */
    public void startLoadGameElements() {
        // Set label text based on game state
        setLabelText();
        // Initialize elements for a loaded game
        Main.getGameInitializer().initializeLoadElements(gameController.getRoot());
    }


    /**
     * Initiates the setup of elements for a special game level(level 18 and 19), including displaying a new level message,
     * setting label text, and initializing special elements if not loading from a saved state.
     */
    public void startSpecialLevelElements() {
        // Display a new level message for special levels
        showNewLevel();
        // Set label text based on game state
        setLabelText();
        // Initialize special elements if not loading from a saved state
        if (!getGameInitializer().getLoadFromSave()) {
            Main.getGameInitializer().initializeSpecialElements(gameController.getRoot());
        }
    }

    /**
     * Sets the text for various labels in the game UI, including the level label, heart label, and score label.
     * The text is based on the current state of the game controller.
     */
    public void setLabelText() {
        // Set the level label text
        gameController.levelLabel.setText("Level : " + gameController.getGameInitializer().getLevel());
        // Set the heart label text
        gameController.heartLabel.setText("Heart : " + gameController.getHeart());
        // Set the score label text
        gameController.scoreLabel.setText("Score : " + gameController.getScore());
    }


    /**
     * Displays a pause message using the Score class, indicating that the game is currently paused.
     */
    public void showPause() {
        // Show a pause message using the Score class
        new Score().showMessage("Pause");
    }

    /**
     * Displays a resume message using the Score class, indicating that the game is being resumed.
     */
    public void showResume() {
        // Show a resume message using the Score class
        new Score().showMessage("Resume");
    }

    /**
     * Retrieves the Main instance associated with the UI controller.
     *
     * @return The Main instance.
     */
    public Main getMain() {
        return main;
    }

    /**
     * Retrieves the width of the scene in pixels.
     *
     * @return The width of the scene.
     */
    public int getSceneWidth() {
        return sceneWidth;
    }

    /**
     * Retrieves the height of the scene in pixels.
     *
     * @return The height of the scene.
     */
    public int getSceneHeight() {
        return sceneHeight;
    }

    /**
     * Retrieves the GameController instance associated with the UI controller.
     *
     * @return The GameController instance.
     */
    public GameController getGameController() {
        return gameController;
    }

    /**
     * Retrieves the FXMLLoader instance used for loading FXML files in the UI.
     *
     * @return The FXMLLoader instance.
     */
    public FXMLLoader getLoader() {
        return loader;
    }
}

