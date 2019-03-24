/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package maze.ai.search.genetic;

import java.util.Random;
import maze.ai.maze.Direction;

/**
 *
 * @author Krzys
 */
public class DNA {

    private int lifeSpan;
    Direction genes[];

    private int mutationRate;

    public DNA(int lifeSpan, int mutationRate, Direction newGenes[]) {
        this.mutationRate = mutationRate;
        this.lifeSpan = lifeSpan;
        if (newGenes != null) {
            this.genes = newGenes;
        } else {
            this.genes = new Direction[lifeSpan];
            for (int i = 0; i < this.lifeSpan; i++) {
                this.genes[i] = Direction.values()[new Random().nextInt(Direction.values().length)];
            }
        }

    }

    DNA crossover(DNA parentB) {
        Direction newGenes[] = new Direction[this.lifeSpan];
        Random generator = new Random();
        int midPoint = generator.nextInt(this.lifeSpan);

        for (int i = 0; i < this.lifeSpan; i++) {
            if (i > midPoint) {
                newGenes[i] = this.genes[i];
            } else {
                newGenes[i] = parentB.genes[i];
            }
        }
        return new DNA(this.lifeSpan,this.mutationRate, newGenes);
    }

    void mutation() {
        for (int i = 0; i < this.lifeSpan; i++) {
            Random generatorA = new Random();
            double random = generatorA.nextDouble();
            if (random < (mutationRate / 100)) {
                this.genes[i] = Direction.values()[new Random().nextInt(Direction.values().length)];
            }

        }
    }
}
