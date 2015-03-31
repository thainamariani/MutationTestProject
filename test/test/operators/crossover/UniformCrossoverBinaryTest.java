/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.operators.crossover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;
import jmetal.util.IRandomGenerator;
import jmetal.util.PseudoRandom;
import jmetal.util.RandomGenerator;
import operators.crossover.UniformCrossoverBinary;
import org.junit.Assert;
import org.junit.Test;
import problem.MutationTestProblem;

/**
 *
 * @author thaina
 */
public class UniformCrossoverBinaryTest {

    /**
     * Test of doCrossover method, of class UniformCrossoverBinary.
     */
    @Test
    public void testDoCrossover() throws Exception {

        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 1}
        };

        //problem created just to instantiate a solution
        MutationTestProblem problem = new MutationTestProblem(9, 10, coverage);

        //Parent1 [1, 0, 1, 0, 1, 1, 0, 1, 0] 9 bits
        Solution parent1 = new Solution(problem);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(0, true);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(1, false);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(2, true);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(3, false);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(4, true);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(5, true);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(6, false);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(7, true);
        ((Binary) parent1.getDecisionVariables()[0]).setIth(8, false);

        //Parent2 [0, 0, 1, 1, 0, 0, 0, 1, 1] 9 bits
        Solution parent2 = new Solution(problem);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(0, false);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(1, false);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(2, true);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(3, true);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(4, false);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(5, false);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(6, false);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(7, true);
        ((Binary) parent2.getDecisionVariables()[0]).setIth(8, true);

        Solution[] offspring = new Solution[2];
        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);

        HashMap<String, Object> parameters = new HashMap<>();
        UniformCrossoverBinary crossover = new UniformCrossoverBinary(parameters);

        //copy from createOffspring class
        int numberOfBits = ((Binary) parent1.getDecisionVariables()[0]).getNumberOfBits();

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
            boolean geneParent1 = ((Binary) parent1.getDecisionVariables()[0]).getIth(i);
            boolean geneParent2 = ((Binary) parent2.getDecisionVariables()[0]).getIth(i);

            crossover.insertValues(randomList.get(i), offspring, i, geneParent2, geneParent1);
        }

        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(0));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(1));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(2));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(3));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(4));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(5));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(6));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(7));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(8));

        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(0));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(1));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(2));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(3));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(4));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(5));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(6));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(7));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(8));
    }

}
