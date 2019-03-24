package maze.ai.controller;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import maze.ai.maze.Generator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Setup implements Initializable {
    public Button button;
    public Slider slider;
    public Pane rootPane;
    public Slider sldWallsPercent;
    public Button btnAlgorithms;

    private Generator mazeGenerator;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void handleGenerateMaze(MouseEvent mouseEvent){
        this.mazeGenerator = new Generator((int) this.slider.getValue(), (int) this.sldWallsPercent.getValue());
        this.mazeGenerator.generateMaze();
        this.button.setDisable(true);
    }

    public void handleAlgorithms(MouseEvent mouseEvent)  throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxml/main.fxml"));
        Stage stage = new Stage(StageStyle.DECORATED);
        Pane pane = fxmlLoader.load();
        Scene scene = new Scene(pane);
        stage.setScene(scene);

        Main main = fxmlLoader.<Main>getController();
        // setting the same grid for both methods
        main.setGrid(this.mazeGenerator.getGrid());
        main.setWeightAndHeight( 700 / (int) this.slider.getValue());

        // A* part
        main.initializeAstarSearch();
        main.drawAstarMaze();

        // Bfs
        main.initializeBfsSearch();
        main.drawBfsMaze();

        rootPane.getChildren().setAll(pane);

        //root.getStylesheets().add("maze/ai/controller/fxml/css/style.css");

        FXMLLoader fxmlLoaderGenetic = new FXMLLoader(getClass().getResource("fxml/experiment.fxml"));
        Stage stageGenetic = new Stage(StageStyle.DECORATED);
        Pane paneGenetic = fxmlLoaderGenetic.load();
        paneGenetic.getStylesheets().add("maze/ai/controller/fxml/css/style.css");
        Scene sceneGenetic = new Scene(paneGenetic);
        stageGenetic.setScene(sceneGenetic);

        Experiment experiment = fxmlLoaderGenetic.<Experiment>getController();

        experiment.setGrid(this.mazeGenerator.getGrid());
        experiment.setWeightAndHeight( 700 / (int) this.slider.getValue());

        //experiment.initializeGenetic();
        //experiment.drawGeneticMaze();

        stageGenetic.show();

    }
}
