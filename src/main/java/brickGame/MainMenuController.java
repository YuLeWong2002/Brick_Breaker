package brickGame;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.Objects;

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
    private void onClickLoad() {
        Main.getGameController().loadGame();
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
