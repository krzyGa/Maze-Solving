package maze.ai.search;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maze.ai.maze.Spot;

import java.util.List;
import java.util.Vector;

import static java.lang.Math.abs;
import java.time.LocalTime;

public class AStar {

    private GraphicsContext gc;

    private Spot grid[][];
    private Spot start;
    private Spot end;

    private int rows;
    private int cols;

    private List<Spot> openSet;
    private List<Spot> closedSet;
    private List<Spot> path;
    
    private long sum;
    private int count;

    private float weightAndHeight;
    private SearchStatus searchStatus;

    public AStar(Spot[][] grid, GraphicsContext gc, float wAh) {
        this.grid = grid;

        // remove random walls


        this.weightAndHeight = wAh;
        this.cols = this.grid.length;
        this.rows = this.grid.length;

        this.gc = gc;
        this.searchStatus = SearchStatus.WAITING;

        start = this.grid[0][0];
        end = this.grid[cols - 1][rows - 1];
        
        this.sum=0;
        this.count=0;

        this.openSet = new Vector<Spot>();
        this.closedSet = new Vector<Spot>();

        this.openSet.add(this.start);
    }


    public boolean startSolving(boolean oneStep, boolean allSteps) {
        boolean isDone = false;
        LocalTime timeStart;
        LocalTime timeEnd;
  
        if(oneStep || allSteps) {
            timeStart=LocalTime.now();
            this.searchStatus = SearchStatus.SEARCHING;
            Spot test;
            if (openSet.size() > 0) {
                int lowestIndex = 0;
                for (int i = 0; i < openSet.size(); i++) {
                    if (openSet.get(i).f < openSet.get(lowestIndex).f) {
                        lowestIndex = i;
                    }
                }
                openSet.get(lowestIndex).addSpotNeighbours(openSet.get(lowestIndex).x, openSet.get(lowestIndex).y, grid, cols, rows);
                Spot currentTemp = openSet.get(lowestIndex);
                test = currentTemp;
                if (currentTemp == end) {
                    isDone = true;
                    this.searchStatus = SearchStatus.DONE;
                }

                openSet.remove(currentTemp);
                closedSet.add(currentTemp);

                List<Spot> neighbors = currentTemp.neighbour;
                for (int i = 0; i < neighbors.size(); i++) {
                    Spot neighbor = neighbors.get(i);

                    if (!closedSet.contains(neighbor) && !neighbor.walls[(neighbor.direction.ordinal()+2)%4]) {
                        int tempG = currentTemp.g + 1;
                        boolean newPath = false;
                        if (openSet.contains(neighbor)) {        // is out there a better g?
                            if (tempG < neighbor.g) {
                                neighbor.g = tempG;
                                newPath = true;
                            }
                        } else {
                            neighbor.g = tempG;
                            newPath = true;
                            openSet.add(neighbor);
                        }

                        // HEURISTIC PART
                        if (newPath) {
                            neighbor.h = (int) heuristic(neighbor, end);
                            neighbor.f = neighbor.g + neighbor.h;
                            neighbor.comeFrome = currentTemp;
                        }
                    }
                }
            } else {
                // no solution
                this.searchStatus = SearchStatus.NO_SOLUTION;
                System.out.println("No solution");
                return true;
            }



            path = new Vector<Spot>();
            Spot temp = test;
            path.add(temp);
            while (temp.comeFrome != null) {
                path.add(temp.comeFrome);
                temp = temp.comeFrome;
            }
            
          timeEnd=LocalTime.now();
          sum +=(timeEnd.getNano()-timeStart.getNano()+ timeEnd.getSecond()-timeStart.getSecond());
          this.count++;
          
            this.drawMaze(gc, this.weightAndHeight, Color.web("0x313335"), Color.web("0xb0b0b0"));
            this.drawPath(gc, this.weightAndHeight, this.closedSet, Color.GREEN, 6);
            this.drawPath(gc, this.weightAndHeight, this.openSet, Color.RED, 6);
            this.drawPath(gc, this.weightAndHeight, this.path, Color.web("0x5a86ff"), 6);

            if(isDone){ 
                return true;
            }
        }
        return false;
    }


    public void drawMaze(GraphicsContext gc,  float wAh, Color rectColor, Color strokeColor) {
        for (int i = 0; i < cols; i++) {
            for (int j = 0; j < rows; j++) {
                gc.setFill(rectColor);
                gc.fillRect(wAh * i + 1, wAh * j + 1, wAh, wAh);
                grid[i][j].drawSpot(strokeColor, gc);
                //gc.fillText(toString().valueOf(this.grid[i][j].countWalls), wAh * i + 10, wAh * j + (wAh - 10));
            }
        }
    }

    public void drawPath(GraphicsContext gc, float wAh,  List<Spot> path, Color color, int wAhOfTile) {
        for(int i = 0; i < path.size(); i++) {
            gc.setFill(color);
            gc.fillOval(wAh * path.get(i).x + (wAh / 2) - (wAhOfTile / 2), wAh * path.get(i).y + (wAh / 2) - (wAhOfTile / 2), wAhOfTile, wAhOfTile);
        }

        if(path == this.path) {
            for(int i = 0; i < path.size() - 1; i++) {
                gc.setStroke(color);

                gc.strokeLine(  (wAh * path.get(i).x + (wAh / 2) - (wAhOfTile / 2)) + 3 , (wAh * path.get(i).y + (wAh / 2) - (wAhOfTile / 2)) + 3,
                        (wAh * path.get(i+1).x + (wAh / 2) - (wAhOfTile / 2)) + 3, (wAh * path.get(i+1).y + (wAh / 2) - (wAhOfTile / 2)) + 3);
            }
        }
    }

    public double heuristic(Spot a, Spot b) {
        //return  Point2D.distance(a.x, a.y, b.x, b.y);
        //manhattan distance heuristic
        return abs(a.x - b.x) + abs(a.y - b.y);
    }

    public void resetMaze(Spot grid[][]){
        this.searchStatus = SearchStatus.WAITING;
        this.grid = grid;

        this.path.clear();
        this.closedSet.clear();
        this.openSet.clear();

        start = this.grid[0][0];
        end = this.grid[cols - 1][rows - 1];
        
        this.count=0;
        this.sum=0;

        this.openSet.add(this.start);
    }

    public void setSearchStatus(SearchStatus searchStatus) {
        this.searchStatus = searchStatus;
    }

    public String getSearchStatus() {
        return searchStatus.name();
    }
       public void setCount(int count) {
        this.count = count;
    }

    public String getCount() {
        return ""+this.count;
    }
     public void setTime(int time) {
        this.sum = time;
    }

    public String getTime() {
        return (this.sum)/1000+" ms.";
    }
}
