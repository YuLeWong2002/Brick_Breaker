package brickGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class UIController {
    private Main main;
    private GameController gameController;
    private GameInitializer gameInitializer;
    public UIController(Main main, Stage primaryStage) {
        this.main = main;
        this.primaryStage = primaryStage;
        if(main == null) {
            System.out.println("UImain is null");
        } else {System.out.println("UINot null");}
    }
    public GameInitializer getGameInitializer() { return this.gameInitializer; }
    public GameController getGameController() { return this.gameController; }
    private Stage primaryStage;
    private Button load;
    private Button newGame;
    public  Pane             root;
    public Pane getRoot() { return root; }
    private Label            scoreLabel;
    private Label            heartLabel;
    private Label            levelLabel;
    private int sceneWidth = 500;
    public int getSceneWidth() { return sceneWidth; }
    private int sceneHeight = 700;
    public int getSceneHeight() { return sceneHeight; }
    public void initGame() {
        if (gameInitializer == null) {
            gameInitializer = new GameInitializer(main);
            gameController = new GameController(main, this, primaryStage, gameInitializer);
        }
    }
    public void initializeUI() {
        if (gameInitializer.getLoadFromSave() == false) {
            showNewLevel();
            System.out.println("Level: " + gameInitializer.getLevel());
            gameInitializer.initBall();
            gameInitializer.initBreak();
            gameInitializer.initBoard();
            if(main == null) {
                System.out.println("main is null");
            } else {System.out.println("Not null");}

            load = createButton("Load Game", 220, 300);
            newGame = createButton("Start New Game", 220, 340);

        }

        root = new Pane();
        scoreLabel = new Label("Score: " + gameController.getScore());
        levelLabel = new Label("Level: " + gameInitializer.getLevel());
        levelLabel.setTranslateY(20);
        heartLabel = new Label("Heart : " + gameController.getHeart());
        heartLabel.setTranslateX(sceneWidth - 70);
        if (gameInitializer.getLoadFromSave() == false) {
            root.getChildren().addAll(gameInitializer.getRect(), gameInitializer.getBall(), scoreLabel, heartLabel, levelLabel, newGame, load);
        } else {
            root.getChildren().addAll(gameInitializer.getRect(), gameInitializer.getBall(), scoreLabel, heartLabel, levelLabel);
        }
        System.out.println("WTH"+gameInitializer.getBlocks());
        for (Block block : gameInitializer.getBlocks()) {
            root.getChildren().add(block.rect);
        }
        Scene scene = new Scene(root, sceneWidth, sceneHeight);
        scene.getStylesheets().add("style.css");
        scene.setOnKeyPressed(gameController);

        // Set up the primary stage
        primaryStage.setTitle("Game");
        primaryStage.setScene(scene);
        // Show the primary stage
        primaryStage.show();
        if (gameInitializer.getLoadFromSave() == false) {
            if (gameInitializer.getLevel() > 1 && gameInitializer.getLevel() < 18) {
                load.setVisible(false);
                newGame.setVisible(false);
                gameInitializer.startLevel();
            }
            // Add event handlers for buttons
            load.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    gameController.loadGame();

                    load.setVisible(false);
                    newGame.setVisible(false);
                }
            });

            newGame.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    gameInitializer.initializeEngine(main);
                    load.setVisible(false);
                    newGame.setVisible(false);
                    System.out.println("BLOCKSNUMINBUTTON: " + gameInitializer.getBlocks());
                }
            });
        } else {
            gameInitializer.initializeEngine(main);
            gameInitializer.setLoadFromSave(false);
        }

    }
    public void startGame() { try {
        main.start(primaryStage);
    } catch (Exception e) {
        e.printStackTrace(); // Handle the exception as needed
    } }

    public void showNewLevel() {
        System.out.println("Current Level: " + gameInitializer.getLevel());
        gameInitializer.setLevel(gameInitializer.getLevel() + 1);
        System.out.println("New Level: " + gameInitializer.getLevel());
        if (gameInitializer.getLevel() > 1) {
            new Score().showMessage("Level Up :)", main);
        }
        if (gameInitializer.getLevel() == 18) {
            new Score().showWin(main);
        }
    }

    private Button createButton(String text, double translateX, double translateY) {
        Button button = new Button(text);
        button.setTranslateX(translateX);
        button.setTranslateY(translateY);
        return button;
    }
    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getHeartLabel() {
        return heartLabel;
    }
}

