/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze.ai.search.genetic;

import static java.lang.Math.abs;
import java.util.List;
import java.util.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import maze.ai.maze.Spot;

/**
 *
 * @author Krzys
 */
public class Agent {

    int rows;
    int cols;
    float wAh;
    public Spot spot;
    public DNA dna;
    private List<Spot> path;

    private boolean done;

    public float fitness;
    public boolean isCompleted;

    public Agent(int sizeDNA, int mutationRate, float wAh, int rows, int cols, DNA dna) {
        this.done = false;
        this.isCompleted = false;
        this.fitness = 0;
        this.rows = rows;
        this.cols = cols;
        this.wAh = wAh;
        this.spot = new Spot(0, 0, wAh);
        this.path = new Vector<Spot>();
        Spot temp = new Spot(this.spot.x, this.spot.y, this.wAh);
        this.path.add(temp);

        if (dna != null) {
            this.dna = dna;
        } else {
            this.dna = new DNA(sizeDNA, mutationRate,null);
        }
    }

    public void draw(GraphicsContext gc, int spotNumber, float wAh, Color elipseColor, int wAhOfTile) {
        gc.setFill(elipseColor);
        gc.fillOval(wAh * this.spot.x + (wAh / 2) - (wAhOfTile / 2), wAh * this.spot.y + (wAh / 2) - (wAhOfTile / 2), wAhOfTile, wAhOfTile);
        gc.fillText(toString().valueOf(spotNumber), wAh * this.spot.x + 5, wAh * this.spot.y + (wAh - 10));

    }

    public void drawPath(GraphicsContext gc, float wAh, Color color, int wAhOfTile) {
        for (int i = 0; i < this.path.size(); i++) {
            gc.setFill(color);
            gc.fillOval(wAh * this.path.get(i).x + (wAh / 2) - (wAhOfTile / 2), wAh * this.path.get(i).y + (wAh / 2) - (wAhOfTile / 2), wAhOfTile, wAhOfTile);
        }
        for (int i = 0; i < this.path.size() - 1; i++) {
            gc.setFill(color);
            gc.setStroke(color);

            gc.strokeLine((wAh * this.path.get(i).x + (wAh / 2) - (wAhOfTile / 2)) + 3, (wAh * this.path.get(i).y + (wAh / 2) - (wAhOfTile / 2)) + 3,
                    (wAh * this.path.get(i + 1).x + (wAh / 2) - (wAhOfTile / 2)) + 3, (wAh * this.path.get(i + 1).y + (wAh / 2) - (wAhOfTile / 2)) + 3);
        }
    }

    public void update(int countPopulation, Spot target, Spot grid[][]) {

        float distance = abs(this.spot.x - target.x) + abs(this.spot.y - target.y);

        if (distance < 4) {
        //if(this.spot.x == target.x && this.spot.y == target.y) {
            this.isCompleted = true;
        }

        if (!this.isCompleted) {
            //TOP, RIGHT, BOTTOM, LEFT
            switch (this.dna.genes[countPopulation]) {
                case TOP: {
                    if (this.spot.y > 0 && !grid[this.spot.x][this.spot.y].walls[0]) {
                        this.spot.y = this.spot.y - 1;
                        Spot temp = new Spot(this.spot.x, this.spot.y, this.wAh);
                       this.path.add(temp);
                    }
                    // else add some error rate in order to calculate the fitness function
                    break;
                }
                case RIGHT: {
                    if (this.spot.x < this.cols - 1 && !grid[this.spot.x][this.spot.y].walls[1]) {
                        this.spot.x = this.spot.x + 1;
                        Spot temp = new Spot(this.spot.x, this.spot.y, this.wAh);
                        this.path.add(temp);
                    }
                    break;
                }
                case BOTTOM: {
                    if (this.spot.y < this.rows - 1 && !grid[this.spot.x][this.spot.y].walls[2]) {
                        this.spot.y = this.spot.y + 1;
                        Spot temp = new Spot(this.spot.x, this.spot.y, this.wAh);
                       this.path.add(temp);
                    }
                    break;
                }
                case LEFT: {
                    if (this.spot.x > 0 && !grid[this.spot.x][this.spot.y].walls[3]) {
                        this.spot.x = this.spot.x - 1;
                        Spot temp = new Spot(this.spot.x, this.spot.y, this.wAh);
                       this.path.add(temp);
                    }
                    break;
                }
            }
        }
    }
   
    

    public void calcuateFitness(Spot target) {
        float distance = abs(this.spot.x - target.x) + abs(this.spot.y - target.y);
        this.fitness = 1 / distance;

//        float up = (this.spot.x + this.spot.y);
//        float down = (target.x + target.y) - (this.spot.x + this.spot.y);
//        float fv = up / down;
//        fv = fv * 100;
//        
//        this.fitness = fv;

        if (this.isCompleted) {
        //    this.fitness *= 10;
        }
    }
}
