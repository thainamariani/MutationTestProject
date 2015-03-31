/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators.crossover;

import java.util.HashMap;
import jmetal.core.Solution;
import jmetal.encodings.variable.Binary;
import org.junit.Assert;
import org.junit.Test;
import problem.MutationTestProblem;

/**
 *
 * @author thaina
 */
public class TwoPointsCrossoverBinaryTest {

    /**
     * Test of doCrossover method, of class TwoPointsCrossoverBinary.
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
        TwoPointsCrossoverBinary crossover = new TwoPointsCrossoverBinary(parameters);

        //test 1
        offspring = crossover.createOffsprings(parent1, parent2, 7, 3, offspring);

        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(0));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(1));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(2));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(3));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(4));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(5));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(6));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(7));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(8));

        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(0));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(1));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(2));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(3));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(4));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(5));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(6));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(7));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(8));

        //test 2
        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);
        offspring = crossover.createOffsprings(parent1, parent2, 7, 1, offspring);

        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(0));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(1));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(2));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(3));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(4));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(5));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(6));
        Assert.assertTrue(((Binary) offspring[0].getDecisionVariables()[0]).getIth(7));
        Assert.assertFalse(((Binary) offspring[0].getDecisionVariables()[0]).getIth(8));

        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(0));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(1));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(2));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(3));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(4));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(5));
        Assert.assertFalse(((Binary) offspring[1].getDecisionVariables()[0]).getIth(6));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(7));
        Assert.assertTrue(((Binary) offspring[1].getDecisionVariables()[0]).getIth(8));
    }

}
