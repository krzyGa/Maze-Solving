package maze.ai.maze;

import java.util.Stack;
import java.util.concurrent.ThreadLocalRandom;

public class Generator {

    private Spot grid[][];
    private Spot current;
    private Stack<Spot> stackSpot;

    private int rows;
    private int cols;

    private int wallsPercentToErease;
    public Generator(int rowsAndCols, int wallsPercentToErease) {
        this.wallsPercentToErease = wallsPercentToErease;
        this.rows = rowsAndCols;
        this.cols = rowsAndCols;
        this.grid = new Spot[rowsAndCols][rowsAndCols];
        this.stackSpot = new Stack<Spot>();
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                grid[i][j] = new Spot(i, j, 700/rowsAndCols);
            }
        }
        current = this.grid[0][0];
    }

    public void generateMaze() {
        while(!isGenerateMazeFinished()) {
            // step 1
            current.visited = true;
            Spot next = current.getRandomNeighbour(current.x, current.y,  grid,  cols,  rows);
            if(next!=null) {
                next.visited = true;

                //step 2
                stackSpot.push(current);

                // step 3
                next.removeWalls(current, next);

                // step 4
                current = next;
            }
            else {
                if (!stackSpot.empty()) {
                    current = stackSpot.pop();
                }
            }
        }

        this.cleanMazeAsUnvisited();
        this.generateNonPerfectMaze(this.wallsPercentToErease);
    }

    public void generateNonPerfectMaze(int percentToRemove) {
        int countRemoved = 0;
        int totalWalls = 0;
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                totalWalls += this.grid[i][j].countWalls;
            }
        }
        totalWalls = (totalWalls - 2*(cols + rows)) / 2;
        int wallsToRemove = totalWalls * percentToRemove / 100;
        while(countRemoved != wallsToRemove) {
            int randX = ThreadLocalRandom.current().nextInt(0, cols);
            int randY = ThreadLocalRandom.current().nextInt(0, rows);

            int randNeighbour = ThreadLocalRandom.current().nextInt(0, 4);

            //TOP, RIGHT, BOTTOM, LEFT

            switch (randNeighbour) {
                case 0: {
                    if(randY > 0) {
                        //usuwam z rand TOP i z neighbour BOTTOM
                        if(this.grid[randX][randY].walls[0]) {
                            this.grid[randX][randY].walls[0]        = false;
                            this.grid[randX][randY - 1].walls[2]    = false;
                            this.grid[randX][randY].countWalls--;
                            this.grid[randX][randY - 1].countWalls--;
                            countRemoved++;
                        }
                    }
                    break;
                }
                case 1: {
                    if(randX < cols-1) {
                        //usuwam z rand RIGTH i z neighbour LEFT
                        if(this.grid[randX][randY].walls[1]) {
                            this.grid[randX][randY].walls[1]        = false;
                            this.grid[randX + 1][randY].walls[3]    = false;
                            this.grid[randX][randY].countWalls--;
                            this.grid[randX + 1][randY].countWalls--;
                            countRemoved++;
                        }
                    }
                    break;
                }
                case 2: {
                    if(randY  < rows-1) {
                        //usuwam z rand BOTTOM i z neighbour TOP
                        if(this.grid[randX][randY].walls[2]) {
                            this.grid[randX][randY].walls[2]        = false;
                            this.grid[randX][randY + 1].walls[0]    = false;
                            this.grid[randX][randY].countWalls--;
                            this.grid[randX][randY + 1].countWalls--;
                            countRemoved++;
                        }
                    }
                    break;
                }
                case 3: {
                    if(randX > 0) {
                        //usuwam z rand LEFT i z neighbour RIGHT
                        if(this.grid[randX][randY].walls[3]) {
                            this.grid[randX][randY].walls[3]        = false;
                            this.grid[randX - 1][randY].walls[1]    = false;
                            this.grid[randX][randY].countWalls--;
                            this.grid[randX - 1][randY].countWalls--;
                            countRemoved++;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void cleanMazeAsUnvisited(){
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                this.grid[i][j].visited = false;
            }
        }
    }

    public void cleanMaze() {}

    private boolean isGenerateMazeFinished() {
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                if(!grid[i][j].visited)
                    return false;
            }
        }
        return true;
    }

    public int getGenerateStatus(){
        int visited = 0;
        for(int i = 0; i < cols; i++) {
            for(int j = 0; j < rows; j++) {
                if(grid[i][j].visited)
                    visited++;
            }
        }
        return (int)(100 * visited / this.cols * this.rows);
    }

    public Spot[][] getGrid() {
        return grid;
    }
}
