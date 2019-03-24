package maze.ai.maze;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;
import java.util.Vector;

public class Spot {

    public int f;
    public int g;
    public int h;
    public int x;
    public int y;

    private float widthAndHeight;
    public int countWalls;

    public boolean walls[];
    public boolean visited;
    public boolean visited2;
    public List<Spot> neighbour;
    public Direction direction;
    public Direction direction2;
    
    public Spot comeFrome;
    private ListIterator<Spot> lit;

    public Spot(int x, int y, float widthAndHeight) {
        this.countWalls = 4;
        this.widthAndHeight = widthAndHeight;
        this.visited = false;
        this.visited2 = false;
        this.neighbour = new Vector<Spot>();
        this.direction=Direction.EMPTY;

        this.walls = new boolean[4];
        this.walls[0] = true;
        this.walls[1] = true;
        this.walls[2] = true;
        this.walls[3] = true;

        this.x = x;
        this.y = y;

        this.lit = this.neighbour.listIterator(0);
        this.comeFrome = (this.lit.hasPrevious() ? this.lit.previous() : null);
    }

    public void drawSpot(Color color, GraphicsContext gc) {
        gc.setFill(color);
        gc.setStroke(color);

        float wAh = this.widthAndHeight;

        if(this.walls[0]) {
            gc.strokeLine(x * wAh,y * wAh,x*wAh+wAh,y*wAh);
        }
        if(this.walls[1]) {
            gc.strokeLine(x * wAh+wAh,y * wAh,x*wAh+wAh,y*wAh+wAh);
        }
        if(this.walls[2]) {
            gc.strokeLine(x * wAh+wAh,y * wAh+wAh,x*wAh,y*wAh+wAh);
        }
        if(this.walls[3]) {
            gc.strokeLine(x * wAh,y * wAh+wAh,x*wAh,y*wAh);
        }

        if(visited) {
            gc.setFill(Color.web("0x2b2b2b"));
            gc.fillRect(wAh * x + 1,wAh * y + 1,wAh,wAh);
        }
    }


    public void addSpotNeighbours(int i, int j, Spot grid[][], int cols, int rows) {
        this.neighbour.clear();

        Spot right = null;
        Spot left = null;
        Spot top = null;
        Spot bottom = null;

        if(i < cols - 1) {
            right = grid[i+1][j];
        }

        if(i > 0) {
            left = grid[i-1][j];
        }

        if(j < rows - 1) {
            bottom = grid[i][j+1];
        }

        if(j > 0) {
            top = grid[i][j - 1];
        }

        if(top!=null && !top.visited) {
            top.direction = Direction.TOP;
            this.neighbour.add(top);
        }

        if(right!=null && !right.visited) {
            right.direction = Direction.RIGHT;
            this.neighbour.add(right);
        }

        if(bottom!=null && !bottom.visited) {
            bottom.direction = Direction.BOTTOM;
            this.neighbour.add(bottom);
        }

        if(left!=null && !left.visited) {
            left.direction = Direction.LEFT;
            this.neighbour.add(left);
        }
    }

    public Spot getRandomNeighbour(int i, int j, Spot grid[][], int cols, int rows){
        this.addSpotNeighbours( i,  j,  grid,  cols,  rows);
        if(this.neighbour.size() > 0) {
            Random generator = new Random();
            Spot returnNeighbour = this.neighbour.get(generator.nextInt(this.neighbour.size()));
            int tempX = this.x - returnNeighbour.x;
            int tempY = this.y - returnNeighbour.y;
            String direction;
            if(tempX == 0) {
                if(tempY == 1) {
                    direction = "Top";
                    this.direction = Direction.TOP;
                }
                else {
                    direction = "Bottom";
                    this.direction = Direction.BOTTOM;
                }
            }
            else {
                if(tempX == 1) {
                    direction = "Left";
                    this.direction = Direction.LEFT;
                }
                else {
                    direction = "Right";
                    this.direction = Direction.RIGHT;
                }
            }
            return returnNeighbour;
        }
        else return null;
    }

    public void removeWalls(Spot current, Spot next) {
        int x = current.x - next.x;
        if (x == 1) {
            current.walls[3] = false;
            next.walls[1]    = false;
            current.countWalls--;
            next.countWalls--;
        }
        else if (x == -1) {
            current.walls[1] = false;
            next.walls[3]    = false;
            current.countWalls--;
            next.countWalls--;
        }

        int y = current.y - next.y;
        if (y == 1) {
            current.walls[0] = false;
            next.walls[2]    = false;
            current.countWalls--;
            next.countWalls--;
        }
        else if (y == -1) {
            current.walls[2] = false;
            next.walls[0]    = false;
            current.countWalls--;
            next.countWalls--;
        }
    }
}
