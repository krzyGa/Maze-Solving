package maze.ai.search;
import java.util.concurrent.TimeUnit;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import maze.ai.maze.Spot;

import maze.ai.search.genetic.Population;

// only for now
public class Genetic {

    private GraphicsContext gc;

    private Spot grid[][];
    private Spot start;
    private Spot end;

    private Population population;
    private int countPopulation;

    private int rows;
    private int cols;

    private int sizeDNA;

    private float weightAndHeight;
    private SearchStatus searchStatus;

    private int generationNumber;
    private int DNAStep;

    public Genetic(Spot[][] grid, GraphicsContext gc, float wAh, int populationSize, int sizeDNA, int mutationRate) {

        this.grid = grid;
        this.weightAndHeight = wAh;
        this.cols = this.grid.length;
        this.rows = this.grid.length;
        this.sizeDNA = sizeDNA;

        this.DNAStep = 0;
        this.generationNumber = 0;

        start = this.grid[0][0];
        end = this.grid[cols - 1][rows - 1];

        this.population = new Population(populationSize, this.weightAndHeight, cols, rows, this.end, sizeDNA, mutationRate);
        this.countPopulation = 0;

        this.gc = gc;
        this.searchStatus = SearchStatus.WAITING;

    }

    public boolean startSolving(boolean oneStep, boolean allSteps) throws InterruptedException {
        boolean isDone = false;
        if (oneStep || allSteps) {

            this.drawMaze(gc, this.weightAndHeight, Color.web("0x313335"), Color.web("0xb0b0b0"));
            this.population.updatePopulation(this.countPopulation, this.grid);
            this.countPopulation++;
            this.DNAStep = countPopulation;
            if (this.countPopulation == this.sizeDNA) {
                this.countPopulation = 0;
                this.DNAStep = countPopulation;
                this.generationNumber++;
                this.population.evaluate();
                if (this.population.checkFitness().getKey()) {

                    this.population.drawPath(gc, weightAndHeight, Color.web("0x5a86ff"), 7, this.population.checkFitness().getValue(), true);
                    if (this.population.checkIsCompleted()) return true;
                }
                this.population.selection();

            }

            if (isDone) {
                return true;
            }
        }
        return false;
    }

    public void drawPath(GraphicsContext gc, float wAh, Color color, int wAhOfTile) throws InterruptedException {
        population.drawPath(gc, wAh, color, wAhOfTile, 0, false);
    }

    public void drawMaze(GraphicsContext gc, float wAh, Color rectColor, Color strokeColor) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                gc.setFill(rectColor);
                gc.fillRect(wAh * i + 1, wAh * j + 1, wAh, wAh);
                //gc.strokeRect(wAh * i + 1, wAh * j + 1, wAh, wAh);
                grid[i][j].drawSpot(strokeColor, gc);
                // gc.fillText(toString().valueOf(this.grid[i][j].countWalls), wAh * i + 10, wAh * j + );
            }
        }
        this.population.drawPopulation(gc, 1, wAh, Color.YELLOW, 7);
       //this.drawPath(gc, wAh , Color.WHITE, 7);
    }

    public void resetMaze(Spot grid[][]) {
        this.searchStatus = SearchStatus.WAITING;
        this.grid = grid;

        start = this.grid[0][0];
        end = this.grid[cols - 1][rows - 1];
    }

    public void setSearchStatus(SearchStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getSearchStatus() {
        return searchStatus.name();
    }

    public String getGenerationNumber() {
        return  Integer.toString(this.generationNumber);
    }

    public String getDNAStep() {
        return Integer.toString(this.DNAStep);
    }
}
