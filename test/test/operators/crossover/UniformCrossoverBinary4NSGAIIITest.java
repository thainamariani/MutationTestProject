/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.operators.crossover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jmetal.encodings.variable.Binary;
import operators.crossover.UniformCrossoverBinary;
import operators.crossover.UniformCrossoverBinary4NSGAIII;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.Solution;
import problem.MutationTestProblem;
import problem.MutationTestProblem4NSGAIII;

/**
 *
 * @author thaina
 */
public class UniformCrossoverBinary4NSGAIIITest {
    
    public UniformCrossoverBinary4NSGAIIITest() {
    }
    
    @Test
    public void testDoCrossover() throws Exception {

        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 1}
        };

        //problem created just to instantiate a solution
        Problem problem = new MutationTestProblem4NSGAIII(9, 10, coverage);

        //Parent1 [1, 0, 1, 0, 1, 1, 0, 1, 0] 9 bits
        BinarySolution parent1 = (BinarySolution) problem.createSolution();
        parent1.getVariableValue(0).set(0, true);
        parent1.getVariableValue(0).set(1, false);
        parent1.getVariableValue(0).set(2, true);
        parent1.getVariableValue(0).set(3, false);
        parent1.getVariableValue(0).set(4, true);
        parent1.getVariableValue(0).set(5, true);
        parent1.getVariableValue(0).set(6, false);
        parent1.getVariableValue(0).set(7, true);
        parent1.getVariableValue(0).set(8, false);

        //Parent2 [0, 0, 1, 1, 0, 0, 0, 1, 1] 9 bits
        BinarySolution parent2 = (BinarySolution) problem.createSolution();
        parent2.getVariableValue(0).set(0, false);
        parent2.getVariableValue(0).set(1, false);
        parent2.getVariableValue(0).set(2, true);
        parent2.getVariableValue(0).set(3, true);
        parent2.getVariableValue(0).set(4, false);
        parent2.getVariableValue(0).set(5, false);
        parent2.getVariableValue(0).set(6, false);
        parent2.getVariableValue(0).set(7, true);
        parent2.getVariableValue(0).set(8, true);
        
        BinarySolution[] offspring = new BinarySolution[2];

        //copy parents solutions into offsprings
        offspring[0] = (BinarySolution) parent1.copy();
        offspring[1] = (BinarySolution) parent2.copy();

        HashMap<String, Object> parameters = new HashMap<>();
        UniformCrossoverBinary4NSGAIII crossover = new UniformCrossoverBinary4NSGAIII(parameters);

        //copy from createOffspring class
        int numberOfBits = parent1.getVariableValue(0).getBinarySetLength();

        List<Integer> randomList = new ArrayList<>();
        randomList.add(0); //0
        randomList.add(0); //1
        randomList.add(1); //2
        randomList.add(0); //3
        randomList.add(1); //4
        randomList.add(1); //5
        randomList.add(1); //6
        randomList.add(0); //7
        randomList.add(1); //8

        //PseudoRandom.setRandomGenerator(new RandomGenerator(0));
        for (int i = 0; i < numberOfBits; i++) {
            boolean geneParent1 = parent1.getVariableValue(0).get(i);
            boolean geneParent2 = parent2.getVariableValue(0).get(i);

            crossover.insertValues(randomList.get(i), offspring, i, geneParent2, geneParent1);
        }

        Assert.assertTrue(offspring[0].getVariableValue(0).get(0));
        Assert.assertFalse(offspring[0].getVariableValue(0).get(1));
        Assert.assertTrue(offspring[0].getVariableValue(0).get(2));
        Assert.assertFalse(offspring[0].getVariableValue(0).get(3));
        Assert.assertFalse(offspring[0].getVariableValue(0).get(4));
        Assert.assertFalse(offspring[0].getVariableValue(0).get(5));
        Assert.assertFalse(offspring[0].getVariableValue(0).get(6));
        Assert.assertTrue(offspring[0].getVariableValue(0).get(7));
        Assert.assertTrue(offspring[0].getVariableValue(0).get(8));

        Assert.assertFalse(offspring[1].getVariableValue(0).get(0));
        Assert.assertFalse(offspring[1].getVariableValue(0).get(1));
        Assert.assertTrue(offspring[1].getVariableValue(0).get(2));
        Assert.assertTrue(offspring[1].getVariableValue(0).get(3));
        Assert.assertTrue(offspring[1].getVariableValue(0).get(4));
        Assert.assertTrue(offspring[1].getVariableValue(0).get(5));
        Assert.assertFalse(offspring[1].getVariableValue(0).get(6));
        Assert.assertTrue(offspring[1].getVariableValue(0).get(7));
        Assert.assertFalse(offspring[1].getVariableValue(0).get(8));
    }

    
}
