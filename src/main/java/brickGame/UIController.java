package brickGame;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Random;

import static brickGame.Main.getGameInitializer;
import static brickGame.Main.scene;

public class UIController {
    private Main main;
    private Stage primaryStage;
    private GameController gameController;
    FXMLLoader loader;
    private int sceneWidth = 500;
    private int sceneHeight = 700;

    public UIController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
        if(main == null) {
            System.out.println("UI main is null");
        } else {System.out.println("UI Not null");}
    }

    public void startGame() { try {
        main.start(primaryStage);
    } catch (Exception e) {
        e.printStackTrace(); // Handle the exception as needed
    } }

    public void showNewLevel() {
        System.out.println("Current Level: " + Main.getGameInitializer().getLevel());
        Main.getGameInitializer().setLevel(Main.getGameInitializer().getLevel() + 1);
        System.out.println("New Level: " + Main.getGameInitializer().getLevel());
        if (Main.getGameInitializer().getLevel() > 1 && Main.getGameInitializer().getLevel() < 4) {
            new Score().showMessage("Level Up :)", main);
        }
        if (Main.getGameInitializer().getLevel() == 4) {
            new Score().showWin(main);
        }
    }
    private Color[]          colors = new Color[]{
            Color.MAGENTA,
            Color.RED,
            Color.GOLD,
            Color.CORAL,
            Color.AQUA,
            Color.VIOLET,
            Color.GREENYELLOW,
            Color.ORANGE,
            Color.PINK,
            Color.SLATEGREY,
            Color.YELLOW,
            Color.TOMATO,
            Color.TAN,
    };

    public void initializeUI() throws IOException {
        if (!getGameInitializer().getLoadFromSave()) {
            if (Main.getGameInitializer().getLevel() < 1) {
                scene = new Scene(Main.loadFXML("MainMenu"), sceneWidth, sceneHeight);
                scene.getStylesheets().add("style.css");
                primaryStage.setScene(scene);
                primaryStage.setTitle("Brick Breaker");
                primaryStage.show();
            } else {
                switchToGameScene();
                startNewGameElements();
                Main.getGameInitializer().startLevel();
            }
        } else {
//            for (BlockSerializable ser : getGameController().getGameIOController().getLoadSave().blocks) {
//                int r = new Random().nextInt(200);
//                Block block = new Block(ser.row, ser.j, colors[r % colors.length], ser.type);
//                getGameInitializer().getBlocks().add(block);
//
//                // Add the Rectangle to the Pane
//                gameController.getRoot().getChildren().add(block.rect);
//            }
            startLoadGameElements();
            getGameInitializer().initializeEngine(gameController);
            getGameInitializer().setLoadFromSave(false);
        }
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
        if(!getGameInitializer().getLoadFromSave()) {
            showNewLevel();
        }
        gameController.levelLabel.setText("Level: " + getGameInitializer().getLevel());
        gameController.heartLabel.setText("Heart: " + gameController.getHeart());
        gameController.scoreLabel.setText("Score: " + gameController.getScore());
        if(!getGameInitializer().getLoadFromSave()) {
            Main.getGameInitializer().initializeElements(gameController.getRoot());
        }
    }

    public void startLoadGameElements() {
        gameController.levelLabel.setText("Level: " + gameController.getGameInitializer().getLevel());
        gameController.heartLabel.setText("Heart: " + gameController.getHeart());
        gameController.scoreLabel.setText("Score: " + gameController.getScore());
        Main.getGameInitializer().initializeLoadElements(gameController.getRoot());
    }

    public int getSceneWidth() { return sceneWidth; }
    public int getSceneHeight() { return sceneHeight; }

    public GameController getGameController() {
        return gameController;
    }

    public Main getMain() { return main; }

    public FXMLLoader getLoader() { return loader; }
}

