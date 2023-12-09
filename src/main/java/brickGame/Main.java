package brickGame;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private static GameInitializer gameInitializer;
    private static UIController uiController;
    private static MainMenuController mainMenuController;
    private static BackgroundMusic backgroundMusic;
    public static String savePath    = "D:/save/save.mdds";
    String mediaPath = "src/main/resources/GameMusic.mp3";
    Stage primaryStage;
    public static Scene scene;
    public static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        if(uiController == null) {
            uiController = new UIController(this, primaryStage);
        }
        if(gameInitializer == null) {
            gameInitializer = new GameInitializer(this);
        }
        if(mainMenuController == null) {
            mainMenuController = new MainMenuController(this);
        }
        if(backgroundMusic == null) {
            backgroundMusic = new BackgroundMusic(mediaPath);
        }
        if(!backgroundMusic.isPlaying()) {
            backgroundMusic.play();
        }
        uiController.initializeUI();
    }

    public static void main(String[] args) { launch(args); }
    public static UIController getUiController() { return uiController; }
    public static GameInitializer getGameInitializer() { return gameInitializer; }
    public static BackgroundMusic getBackgroundMusic() { return backgroundMusic; }
}