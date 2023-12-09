package brickGame;


import javafx.fxml.FXML;
import javafx.scene.control.Button;
import java.io.IOException;


public class MainMenuController {

    @FXML
    private Button load;
    @FXML
    private Button newGame;
    private Main main;

    public MainMenuController() {
    }

    public MainMenuController(Main main) {
        this.main = main;
    }

    @FXML
    private void onClickLoad() throws IOException {
        Main.getUiController().switchToGameScene();
        GameController gameController = Main.getUiController().getGameController();
        Main.getGameInitializer().initBall(gameController.getRoot());
        Main.getGameInitializer().initBreak(gameController.getRoot());
//        GameController gameController = Main.getUiController().getGameController();
        gameController.getGameIOController().loadGame();
//        Main.getUiController().switchToGameScene();
//        Main.getUiController().startLoadGameElements();
//        GameController gameController = Main.getUiController().getGameController();
//        gameController.getGameIOController().loadGame();
        load.setVisible(false);
        newGame.setVisible(false);
    }

    @FXML
    private void onClickNewGame() throws IOException{
        load.setVisible(false);
        newGame.setVisible(false);
        Main.getUiController().switchToGameScene();
        Main.getUiController().startNewGameElements();
        Main.getGameInitializer().initializeEngine(Main.getUiController().loader.getController());
    }
}
