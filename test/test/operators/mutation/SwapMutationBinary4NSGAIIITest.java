/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.operators.mutation;

import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.binarySet.BinarySet;
import problem.MutationTestProblem4NSGAIII;

/**
 *
 * @author thaina
 */
public class SwapMutationBinary4NSGAIIITest {

    public SwapMutationBinary4NSGAIIITest() {
    }

    @Test
    public void testDoMutation() throws Exception {

        //fixed test and replicated code due to the random variables
        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 1}
        };

        //problem created just to instantiate a solution
        Problem problem = new MutationTestProblem4NSGAIII(9, 10, coverage);

        //TEST 1
        //solution [1, 0, 1, 0, 1, 1, 0, 1, 0] 9 bits
        BinarySolution solution = (BinarySolution) problem.createSolution();
        solution.getVariableValue(0).set(0, true);
        solution.getVariableValue(0).set(1, false);
        solution.getVariableValue(0).set(2, true);
        solution.getVariableValue(0).set(3, false);
        solution.getVariableValue(0).set(4, true);
        solution.getVariableValue(0).set(5, true);
        solution.getVariableValue(0).set(6, false);
        solution.getVariableValue(0).set(7, true);
        solution.getVariableValue(0).set(8, false);

        BinarySet binarySolution = solution.getVariableValue(0);
        int numberOfBits = binarySolution.getBinarySetLength();

        List<Integer> selectedTrueGenes = new ArrayList<>(); //positions of genes with 1 value
        List<Integer> selectedFalseGenes = new ArrayList<>();//positions of genes with 0 value

        for (int i = 0; i < numberOfBits; i++) {
            if (binarySolution.get(i)) {
                selectedTrueGenes.add(i);
            } else {
                selectedFalseGenes.add(i);
            }
        }

        Assert.assertEquals(5, selectedTrueGenes.size());
        Assert.assertEquals(0, selectedTrueGenes.get(0).intValue());
        Assert.assertEquals(2, selectedTrueGenes.get(1).intValue());
        Assert.assertEquals(4, selectedTrueGenes.get(2).intValue());
        Assert.assertEquals(5, selectedTrueGenes.get(3).intValue());
        Assert.assertEquals(7, selectedTrueGenes.get(4).intValue());

        Assert.assertEquals(4, selectedFalseGenes.size());
        Assert.assertEquals(1, selectedFalseGenes.get(0).intValue());
        Assert.assertEquals(3, selectedFalseGenes.get(1).intValue());
        Assert.assertEquals(6, selectedFalseGenes.get(2).intValue());
        Assert.assertEquals(8, selectedFalseGenes.get(3).intValue());

        if ((!selectedTrueGenes.isEmpty()) && (!selectedFalseGenes.isEmpty())) {
            int i = 2;
            int j = 3;
            binarySolution.set(selectedTrueGenes.get(i), false);
            binarySolution.set(selectedFalseGenes.get(j), true);
        }

        //
        Assert.assertEquals(true, binarySolution.get(0));
        Assert.assertEquals(false, binarySolution.get(1));
        Assert.assertEquals(true, binarySolution.get(2));
        Assert.assertEquals(false, binarySolution.get(3));
        Assert.assertEquals(false, binarySolution.get(4)); //changed
        Assert.assertEquals(true, binarySolution.get(5));
        Assert.assertEquals(false, binarySolution.get(6));
        Assert.assertEquals(true, binarySolution.get(7));
        Assert.assertEquals(true, binarySolution.get(8)); //changed

        //TEST 2 
        int i = 4;
        int j = 1;
        binarySolution.set(selectedTrueGenes.get(i), false);
        binarySolution.set(selectedFalseGenes.get(j), true);

        Assert.assertEquals(true, binarySolution.get(0));
        Assert.assertEquals(false, binarySolution.get(1));
        Assert.assertEquals(true, binarySolution.get(2));
        Assert.assertEquals(true, binarySolution.get(3));
        Assert.assertEquals(false, binarySolution.get(4)); //changed
        Assert.assertEquals(true, binarySolution.get(5));
        Assert.assertEquals(false, binarySolution.get(6));
        Assert.assertEquals(false, binarySolution.get(7));
        Assert.assertEquals(true, binarySolution.get(8)); //changed
    }

}
