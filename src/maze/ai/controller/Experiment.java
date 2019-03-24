package maze.ai.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import maze.ai.maze.Generator;
import maze.ai.maze.Spot;
import maze.ai.search.AStar;
import maze.ai.search.Bfs;
import maze.ai.search.Genetic;
import maze.ai.search.SearchStatus;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class Experiment implements Initializable {

    public Canvas canvasGenetic;
    public Button btnResetGenetic;
    public Button btnStartSolvingGenetic;
    public Label lblTimeGenetic;
    public Label lblCountGenetic;

    public Slider sldPopulation;
    public Slider sldDNA;
    public Slider sldMutation;

    public Label lblGeneration;
    public Label lblStep;

    boolean oneStepGenetic;
    boolean allStepsGenetic;
    private GraphicsContext gcGenetic;
    private Genetic geneticSearch;


    private Spot grid[][];

    private float weightAndHeight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gcGenetic = canvasGenetic.getGraphicsContext2D();
        this.oneStepGenetic = true;
        this.allStepsGenetic = false;
    }

    private void drawMazes() {

        new AnimationTimer()
        {
            public void handle(long currentNanoTime) {

                try {
                    if(startGeneticSearch(oneStepGenetic, allStepsGenetic)) {

                        oneStepGenetic = false;
                        allStepsGenetic = false;

                        this.stop();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // if(oneStepGenetic)
                //    oneStepGenetic = false;


            }
        }.start();
    }

    public void setGrid(Spot grid[][]){
        this.grid = grid;
    }

    public void setWeightAndHeight(float weightAndHeight) {
        this.weightAndHeight = weightAndHeight;
    }

    public void initializeGenetic() {
        this.geneticSearch = new Genetic(this.grid, this.gcGenetic, this.weightAndHeight, (int) this.sldPopulation.getValue(), (int) this.sldDNA.getValue(), (int) this.sldMutation.getValue());
    }

    private boolean startGeneticSearch(boolean oneStep, boolean allSteps) throws InterruptedException {
        this.lblGeneration.textProperty().setValue(this.geneticSearch.getGenerationNumber());
        this.lblStep.textProperty().setValue(this.geneticSearch.getDNAStep());
        return this.geneticSearch.startSolving(oneStep, allSteps);
    }


    public void drawGeneticMaze() {
        this.geneticSearch.drawMaze(this.gcGenetic, this.weightAndHeight, Color.web("0x313335"), Color.web("0xb0b0b0"));
    }



    public void handleResetGenetic(MouseEvent mouseEvent) {
        this.geneticSearch.resetMaze(this.grid);


        //this.oneStepGenetic = false;
       // this.allStepsGenetic = false;

        this.drawGeneticMaze();

        this.btnStartSolvingGenetic.setDisable(false);
        //this.btnAllStepsGenetic.setDisable(true);
       // this.btnOneStepGenetic.setDisable(true);
    }

    public void handleStartGenetic(MouseEvent mouseEvent) {
        this.initializeGenetic();
        this.drawGeneticMaze();
        this.geneticSearch.setSearchStatus(SearchStatus.READY);

        this.btnStartSolvingGenetic.setDisable(true);
       // this.btnAllStepsGenetic.setDisable(false);
        //this.btnOneStepGenetic.setDisable(false);

        this.drawMazes();
    }
}
