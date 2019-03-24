package maze.ai.controller;

import javafx.animation.AnimationTimer;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import maze.ai.maze.Spot;
import maze.ai.search.AStar;
import maze.ai.search.Bfs;
import maze.ai.search.SearchStatus;

import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Math.abs;

public class Main implements Initializable{

    public Button btnStartSolvingA;
    public Button btnOneStepA;
    public Button btnAllStepsA;
    public Button btnResetA;

    public Button btnOneStepBfs;
    public Button btnAllStepsBfs;
    public Button btnResetBfs;
    public Button btnStartSolvingBfs;

    public Label lblSearchStatusA;
    public Label lblSearchStatusBfs;
    
    public Label lblCountA;
    public Label lblCountBfs;
    
    public Label lblTimeA;
    public Label lblTimeBfs;

    private boolean oneStepA;
    private boolean allStepsA;

    private boolean oneStepBfs;
    private boolean allStepsBfs;

    public Canvas canvasAstar;
    public Canvas canvasBfs;

    private GraphicsContext gcAstar;
    private GraphicsContext gcBfs;

    private AStar aStarSearch;
    private Bfs bfsSearch;  // for now only

    private Spot grid[][];

    private float weightAndHeight;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.gcAstar = canvasAstar.getGraphicsContext2D();
        this.gcBfs = canvasBfs.getGraphicsContext2D();

        this.oneStepA = false;
        this.allStepsA = false;
        this.oneStepBfs= false;
        this.allStepsBfs = false;

        this.btnAllStepsA.setDisable(true);
        this.btnOneStepA.setDisable(true);
        this.btnAllStepsBfs.setDisable(true);
        this.btnOneStepBfs.setDisable(true);

    }


    private void drawMazes() {

        new AnimationTimer()
        {
            public void handle(long currentNanoTime) {
                if(startAStarSearch(oneStepA, allStepsA)) {

                    lblSearchStatusA.textProperty().setValue(aStarSearch.getSearchStatus());
                    lblCountA.textProperty().setValue(aStarSearch.getCount());
                    lblTimeA.textProperty().setValue(aStarSearch.getTime());
                    
                    oneStepA = false;
                    allStepsA = false;

                    this.stop();
                }

                if(startBfsSearch(oneStepBfs, allStepsBfs)) {

                   lblSearchStatusBfs.textProperty().setValue(bfsSearch.getSearchStatus());
                   lblCountBfs.textProperty().setValue(bfsSearch.getCount());
                   lblTimeBfs.textProperty().setValue(bfsSearch.getTime());
                   oneStepBfs = false;
                   allStepsBfs= false;

                    this.stop();
                }

                if(oneStepA)
                oneStepA = false;

                if(oneStepBfs)
                    oneStepBfs = false;


            }
        }.start();
    }

    public void setGrid(Spot grid[][]){
        this.grid = grid;
    }

    public void setWeightAndHeight(float weightAndHeight) {
        this.weightAndHeight = weightAndHeight;
    }

    public void initializeAstarSearch() {
        this.aStarSearch = new AStar(this.grid, this.gcAstar, this.weightAndHeight);
        this.lblSearchStatusA.textProperty().setValue(this.aStarSearch.getSearchStatus());
    }

    public void initializeBfsSearch() {
        this.bfsSearch = new Bfs(this.grid, this.gcBfs, this.weightAndHeight);
        this.lblSearchStatusBfs.textProperty().setValue(this.bfsSearch.getSearchStatus());
    }

    private boolean startAStarSearch(boolean oneStep, boolean allSteps) {
        this.lblSearchStatusA.textProperty().setValue(this.aStarSearch.getSearchStatus());
        this.lblCountA.textProperty().setValue(aStarSearch.getCount());
        this.lblTimeA.textProperty().setValue(aStarSearch.getTime());                  
        return this.aStarSearch.startSolving(oneStep, allSteps);
    }

    private boolean startBfsSearch(boolean oneStep, boolean allSteps) {
        this.lblSearchStatusBfs.textProperty().setValue(this.bfsSearch.getSearchStatus());
        this.lblCountBfs.textProperty().setValue(this.bfsSearch.getCount());
        this.lblTimeBfs.textProperty().setValue(this.bfsSearch.getTime());
        return this.bfsSearch.startSolving(oneStep, allSteps);
    }

    public void drawAstarMaze() {
        this.aStarSearch.drawMaze(this.gcAstar, this.weightAndHeight, Color.web("0x313335"), Color.web("0xb0b0b0"));
    }

    public void drawBfsMaze() {
        this.bfsSearch.drawMaze(this.gcBfs, this.weightAndHeight, Color.web("0x313335"), Color.web("0xb0b0b0"));
    }

    public void handleOneStepA(MouseEvent mouseEvent) {
        this.oneStepA = true;
    }
    public void handleOneStepBfs(MouseEvent mouseEvent) {
        this.oneStepBfs = true;
    }

    public void handleAllStepsA(MouseEvent mouseEvent) {
        this.allStepsA = true;
    }
    public void handleAllSteps(MouseEvent mouseEvent) {
        this.allStepsBfs = true;
    }

    public void handleStartA(MouseEvent mouseEvent) {
        this.aStarSearch.setSearchStatus(SearchStatus.READY);

        this.btnStartSolvingA.setDisable(true);
        this.btnAllStepsA.setDisable(false);
        this.btnOneStepA.setDisable(false);

        this.drawMazes();
    }

    public void handleStartBfs(MouseEvent mouseEvent) {
        this.bfsSearch.setSearchStatus(SearchStatus.READY);

        this.btnStartSolvingBfs.setDisable(true);
        this.btnAllStepsBfs.setDisable(false);
        this.btnOneStepBfs.setDisable(false);

        this.drawMazes();
    }

    public void handleResetA(MouseEvent mouseEvent) {
        this.aStarSearch.resetMaze(this.grid);
        this.lblSearchStatusA.textProperty().setValue(this.aStarSearch.getSearchStatus());
        this.lblCountA.textProperty().setValue(aStarSearch.getCount());
        this.lblTimeA.textProperty().setValue(aStarSearch.getTime()); 
        this.oneStepA = false;
        this.allStepsA = false;
        

        this.drawAstarMaze();

        this.btnStartSolvingA.setDisable(false);
        this.btnAllStepsA.setDisable(true);
        this.btnOneStepA.setDisable(true);
    }

    public void handleResetGenetic(MouseEvent mouseEvent) {
        this.bfsSearch.resetMaze(this.grid);
        this.lblSearchStatusBfs.textProperty().setValue(this.bfsSearch.getSearchStatus());
        this.lblCountBfs.textProperty().setValue(this.bfsSearch.getCount());
        this.lblTimeBfs.textProperty().setValue(this.bfsSearch.getTime());
        this.oneStepBfs = false;
        this.allStepsBfs = false;

        this.drawBfsMaze();

        this.btnStartSolvingBfs.setDisable(false);
        this.btnAllStepsBfs.setDisable(true);
        this.btnOneStepBfs.setDisable(true);
    }
}
