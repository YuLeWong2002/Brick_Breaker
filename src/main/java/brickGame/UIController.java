package brickGame;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import static brickGame.Main.getGameInitializer;
import static brickGame.Main.scene;

public class UIController {
    private final Main main;
    private final Stage primaryStage;
    private GameController gameController;
    FXMLLoader loader;
    private final int sceneWidth = 500;
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
            new Score().showMessage("Level Up :)", main);
        }
    }

    /**
     * Displays a win message using the Score class.
     */
    public void showWin() {
        new Score().showWin(main);
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


    private void initializeMainMenu() throws IOException {
        scene = new Scene(Main.loadFXML("MainMenu"), sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Brick Breaker");
        primaryStage.show();
        levelUp();
    }

    @FXML
    public void switchToGameScene() throws IOException {
        loader = new FXMLLoader(getClass().getResource("/Game.fxml"));
        Parent gameRoot = loader.load();
        gameController = loader.getController();
        scene.setOnKeyPressed(gameController);
        Main.scene.setRoot(gameRoot);
    }

    public void startNewGameElements() {
        showNewLevel();
        setLabelText();
        if(!getGameInitializer().getLoadFromSave()) {
            Main.getGameInitializer().initializeElements(gameController.getRoot());
        }
    }

    public void startLoadGameElements() {
        setLabelText();
        Main.getGameInitializer().initializeLoadElements(gameController.getRoot());
    }

    public void startSpecialLevelElements() {
            showNewLevel();
        setLabelText();
        if(!getGameInitializer().getLoadFromSave()) {
            Main.getGameInitializer().initializeSpecialElements(gameController.getRoot());
        }
    }

    public void setLabelText() {
        gameController.levelLabel.setText("Level : " + gameController.getGameInitializer().getLevel());
        gameController.heartLabel.setText("Heart : " + gameController.getHeart());
        gameController.scoreLabel.setText("Score : " + gameController.getScore());
    }

    public void showPause() {
        new Score().showMessage("Pause", main);
    }

    public void showResume() {
        new Score().showMessage("Resume", main);
    }
    public Main getMain() { return main; }
    public int getSceneWidth() { return sceneWidth; }
    public int getSceneHeight() { return sceneHeight; }
    public GameController getGameController() { return gameController; }
    public FXMLLoader getLoader() { return loader; }
}

