package maze.ai.search;

import static java.lang.Thread.sleep;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;
import java.util.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maze.ai.maze.Direction;

import maze.ai.maze.Spot;



public class Bfs {

    private GraphicsContext gc;

 
    private Spot grid[][];
    private Spot start;
    private Spot end;

    private int maze[][];
    private Direction moves[][];
    private int step;
    private int rows;
    private int cols;
    private Spot lastNode;

    private List<Spot> openSet;
    private List<Spot> path;
    private List<Spot> path2;
    private List<Spot> queue;
    
    private long sum;
    private int count;

    private float weightAndHeight;
    private SearchStatus searchStatus;
    private Spot[][] prev;

    public Bfs(Spot[][] grid, GraphicsContext gc, float wAh) {

        this.grid = grid;
        this.weightAndHeight = wAh;
        this.cols = this.grid.length;
        this.rows = this.grid.length;
        prev = new Spot[rows][cols];

        start = this.grid[0][0];
        end = this.grid[cols - 1][rows - 1];
        
        this.sum=0;
        this.count=0;
        
        this.moves= new Direction[rows][cols];
       
        start = this.grid[0][0];
        end = this.grid[cols - 1][rows - 1];
        this.queue=new Vector<Spot>();
        this.openSet = new Vector<Spot>();
        this.path = new Vector<Spot>();
        this.path2 = new Vector<Spot>();
        this.maze=new int[rows][cols];
        this.openSet.add(this.start);
        //this.path.add(this.start);
        ustawOdwiedzenia();
        this.gc = gc;
        this.searchStatus = SearchStatus.WAITING;
        this.start.visited2=true;
        this.start.direction2=Direction.TOP;
           
            this.queue.add(start);
            path2.add(start);
            ustawOdwiedzenia();

    }
    private void ustawOdwiedzenia(){
        for(int i=0;i<cols;i++){
            for(int j=0;j<rows;j++){
               this.grid[i][j].direction2=Direction.EMPTY;
               this.grid[i][j].visited2=false;
        }
        }
    }

    public boolean startSolving(boolean oneStep, boolean allSteps) {
       
    boolean isDone = false;
    LocalTime timeStart;
    LocalTime timeEnd;
  

        if(oneStep || allSteps) {
            timeStart=LocalTime.now();
            this.searchStatus = SearchStatus.SEARCHING;
             Spot test;
               
                test=queue.remove(0);
               
                if(test==this.end){
                    this.path.add(this.end);
                     this.path.add(this.end);
                    while(this.grid[test.x][test.y]!=this.start){
  
                      
                        if(this.grid[test.x][test.y].direction2==Direction.TOP){
                           this.grid[test.x][test.y+1].visited2=true;
                            this.path.add(this.grid[test.x][test.y+1]);
                            test=this.grid[test.x][test.y+1];
                            
                        }
                        else if(this.grid[test.x][test.y].direction2==Direction.RIGHT){
                             this.grid[test.x-1][test.y].visited2=true;
                            this.path.add(this.grid[test.x-1][test.y]);
                            test= this.grid[test.x-1][test.y];
                            
                        }
                         else if(this.grid[test.x][test.y].direction2==Direction.BOTTOM){
                            this.grid[test.x][test.y-1].visited2=true;
                            this.path.add(this.grid[test.x][test.y-1]);
                            test=this.grid[test.x][test.y-1];
                        }
                         else if(this.grid[test.x][test.y].direction2==Direction.LEFT){
                            this.grid[test.x+1][test.y].visited2=true;
                            this.path.add(this.grid[test.x+1][test.y]);
   
                            test=this.grid[test.x+1][test.y];
                        }
                                             
               }
                    
                    isDone=true;
                    this.path.remove(0);
                    this.searchStatus = SearchStatus.DONE;
                
                }
            else{
                   path2.remove(0);  
                   
                 if(this.grid[test.x][test.y].walls[0]==false &&this.grid[test.x][test.y-1].direction2==Direction.EMPTY ){
                      this.grid[test.x][test.y-1].direction2= Direction.TOP;
                      this.queue.add(this.grid[test.x][test.y-1]);
                      this.path2.add(this.grid[test.x][test.y-1]);
                      this.openSet.add(this.grid[test.x][test.y-1]);
                 }
                  if(this.grid[test.x][test.y].walls[1]==false && this.grid[test.x+1][test.y].direction2==Direction.EMPTY ){
                      this.grid[test.x+1][test.y].direction2= Direction.RIGHT;
                      this.queue.add(this.grid[test.x+1][test.y]);
                      this.path2.add(this.grid[test.x+1][test.y]);
                      this.openSet.add(this.grid[test.x+1][test.y]);
                  }
                  if(this.grid[test.x][test.y].walls[2]==false && this.grid[test.x][test.y+1].direction2==Direction.EMPTY){
                      this.grid[test.x][test.y+1].direction2= Direction.BOTTOM;
                      this.queue.add(this.grid[test.x][test.y+1]);
                      this.path2.add(this.grid[test.x][test.y+1]);
                      this.openSet.add(this.grid[test.x][test.y+1]);
                  }
                  if(this.grid[test.x][test.y].walls[3]==false && this.grid[test.x-1][test.y].direction2==Direction.EMPTY ){
                      this.grid[test.x-1][test.y].direction2= Direction.LEFT;
                      this.queue.add(this.grid[test.x-1][test.y]);
                      this.path2.add(this.grid[test.x-1][test.y]);
                      this.openSet.add(this.grid[test.x-1][test.y]);
                  }
               }
           
          this.count++;
          timeEnd=LocalTime.now();    
          sum +=(timeEnd.getNano()-timeStart.getNano()+ timeEnd.getSecond()-timeStart.getSecond());
       
          this.drawMaze(gc, this.weightAndHeight, Color.web("0x313335"), Color.web("0xb0b0b0"));
          this.drawPath(gc, this.weightAndHeight, this.openSet, Color.GREEN, 6);
          this.drawPath(gc, this.weightAndHeight, this.path2, Color.RED, 6);
          this.drawPath(gc, this.weightAndHeight, this.path,Color.web("0x5a86ff"), 6);

            if(isDone) {
              return true;
            }
        }
        return false;
    }

   public void drawPath(GraphicsContext gc, float wAh,  List<Spot> path, Color color, int wAhOfTile) {

       for(int i = 0; i < path.size(); i++) {
            gc.setFill(color);
            gc.fillOval(wAh * path.get(i).x + (wAh / 2) - (wAhOfTile / 2), wAh * path.get(i).y + (wAh / 2) - (wAhOfTile / 2), wAhOfTile, wAhOfTile);
       }
       
        if(path == this.path) {
            for(int i = 0; i < path.size()-1 ; i++) {
                gc.setStroke(color);
                gc.setFill(color);
                gc.strokeLine(  (wAh * path.get(i).x + (wAh / 2) - (wAhOfTile / 2)) + 3 , (wAh * path.get(i).y + (wAh / 2) - (wAhOfTile / 2)) + 3,
                        (wAh * path.get(i+1).x + (wAh / 2) - (wAhOfTile / 2)) + 3, (wAh * path.get(i+1).y + (wAh / 2) - (wAhOfTile / 2)) + 3);
                gc.fillOval(wAh * path.get(i).x + (wAh / 2) - (wAhOfTile / 2), wAh * path.get(i).y + (wAh / 2) - (wAhOfTile / 2), wAhOfTile, wAhOfTile);
            }
        }
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
       this.drawPath(gc, wAh ,path, Color.web("0x5a86ff"), 7);
    }

    public void resetMaze(Spot grid[][]) {
            this.searchStatus = SearchStatus.WAITING;
            this.grid = grid;
            this.path.clear();
            this.path2.clear();
            this.queue.clear();
            this.openSet.clear();
            this.count=0;
            this.sum=0;
            this.start.visited2=true;
            this.start.direction2=Direction.TOP;
            start = this.grid[0][0];
            end = this.grid[cols - 1][rows - 1];
            this.queue.add(start);
            this.path2.add(start);
            ustawOdwiedzenia();
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
    private void Sleep(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}

