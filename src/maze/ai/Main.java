package maze.ai;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("controller/fxml/setup.fxml"));
        root.getStylesheets().add("maze/ai/controller/fxml/css/style.css");
        primaryStage.setTitle("Maze Solver");
        primaryStage.setScene(new Scene(root, 1550, 900));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
