/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze.ai.search.genetic;

import java.util.List;
import java.util.Random;
import java.util.Vector;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import maze.ai.maze.Spot;

/**
 *
 * @author Krzys
 */

public class Population {
    public Agent agent[];
    public Spot target;

    public int populationSize;

    public List<Agent> matingPool;
    private int sizeDNA;
    private int mutationRate;

    float wAh;
    int cols;
    int rows;

    public Population(int populationSize, float wAh, int cols, int rows, Spot target, int sizeDNA, int mutationRate) {
        this.mutationRate = mutationRate;
        this.sizeDNA = sizeDNA;
        this.cols = cols;
        this.rows = rows;
        this.wAh = wAh;
        this.target = target;
        this.populationSize = populationSize;
        this.agent = new Agent[populationSize];
        this.matingPool = new Vector<Agent>();
        for (int i = 0; i < this.populationSize; i++) {
            this.agent[i] = new Agent(sizeDNA, mutationRate, wAh, cols, rows, null);
        }
    }

    public void evaluate() {
        float maxFit = 0;                                               // in order to normalize and set the fit value beettwen (0, 1)
        for (int i = 0; i < this.populationSize; i++) {
            this.agent[i].calcuateFitness(this.target);
            if (this.agent[i].fitness > maxFit) {
                maxFit = this.agent[i].fitness;
            }
        }

        this.matingPool.clear();                                        // in order to create new population
        for (int i = 0; i < this.populationSize; i++) {
            this.agent[i].fitness /= maxFit;                            // in order to normalize and set the fit value bettwen (0, 1)
            System.out.print(this.agent[i].fitness + "\n");
        }

        for (int i = 0; i < this.populationSize; i++) {                 // possibility
            float temp = this.agent[i].fitness * 100;
            for (int j = 0; j < (int) temp; j++) {
                this.matingPool.add(this.agent[i]);
            }
        }
    }

    public Pair<Boolean, Integer> checkFitness() {
        for (int i = 0; i < this.populationSize; i++) {
            if (this.agent[i].fitness == 1 || this.agent[i].isCompleted) {
                return new Pair<Boolean, Integer>(true, i);
            }
        }
        return new Pair<Boolean, Integer>(false, 0);
    }

    public void selection() {
        Agent[] newAgents = new Agent[this.populationSize];

        for (int i = 0; i < this.agent.length; i++) {
            Random generatorA = new Random();
            int randomIndexA = generatorA.nextInt(this.matingPool.size());
            DNA parentA = this.matingPool.get(randomIndexA).dna;

            Random generatorB = new Random();
            int randomIndexB = generatorB.nextInt(this.matingPool.size());
            DNA parentB = this.matingPool.get(randomIndexB).dna;

            DNA child = parentA.crossover(parentB);
            child.mutation();
            newAgents[i] = new Agent(this.sizeDNA, this.mutationRate, this.wAh, this.cols, this.rows, child);
        }
        this.agent = newAgents;
    }

    public void drawPopulation(GraphicsContext gc, int spotNumber, float wAh, Color elipseColor, int wAhOfTile) {
        for (int i = 0; i < this.populationSize; i++) {
            this.agent[i].draw(gc, i, wAh, elipseColor, 6);
        }
    }

    public void drawPath(GraphicsContext gc, float wAh, Color color, int wAhOfTile, int agentNumber, boolean onePath) throws InterruptedException {
        if (onePath) {
             this.agent[agentNumber].drawPath(gc, wAh, color, wAhOfTile);
        } else {
            for (int i = 0; i < this.populationSize; i++) {
                this.agent[i].drawPath(gc, wAh, color, wAhOfTile);
            }
        }
    }

    public void updatePopulation(int countPopulation, Spot[][] grid) {
        for (int i = 0; i < this.populationSize; i++) {
            this.agent[i].update(countPopulation, this.target, grid);
        }
    }

    public boolean checkIsCompleted(){
        boolean returnedValue = false;
        for (int i = 0; i < this.populationSize; i++) {
            if (this.agent[i].isCompleted) {
                returnedValue = true;
                break;
            }
        }
        return  returnedValue;
    }
}