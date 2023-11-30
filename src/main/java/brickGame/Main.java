    package brickGame;

    import javafx.application.Application;
    import javafx.stage.Stage;

    public class Main extends Application {

        private GameInitializer gameInitializer;
        private UIController uiController;
        public UIController getUiController() { return uiController; }
        private GameController gameController;
        private double v = 1.000;
        public static String savePath    = "D:/save/save.mdds";
        Stage primaryStage;
        @Override
        public void start(Stage primaryStage) throws Exception {
            this.primaryStage = primaryStage;
            if(uiController == null) {
                uiController = new UIController(Main.this, primaryStage);
            }
            uiController.initGame();
            gameInitializer = uiController.getGameInitializer();
            gameController = uiController.getGameController();
            uiController.initializeUI();
        }

        public static void main(String[] args) { launch(args); }

        float oldXBreak;
    }
