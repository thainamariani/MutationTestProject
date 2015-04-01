/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.operators.mutation;

import java.util.ArrayList;
import java.util.List;
import jmetal.encodings.variable.Binary;
import org.junit.Assert;
import org.junit.Test;

/**
 *
 * @author thaina
 */
public class SwapMutationBinaryTest {

    /**
     * Test of doMutation method, of class SwapMutationBinary.
     */
    @Test
    public void testDoMutation() throws Exception {

        //TEST 1
        //solution [1, 0, 1, 0, 1, 1, 0, 1, 0] 9 bits
        Binary binarySolution = new Binary(9);
        binarySolution.setIth(0, true);
        binarySolution.setIth(1, false);
        binarySolution.setIth(2, true);
        binarySolution.setIth(3, false);
        binarySolution.setIth(4, true);
        binarySolution.setIth(5, true);
        binarySolution.setIth(6, false);
        binarySolution.setIth(7, true);
        binarySolution.setIth(8, false);

        //fixed test and replicated code due to the random variables
        int numberOfBits = binarySolution.getNumberOfBits();
        //solution [1, 0, 1, 0, 1, 1, 0, 1, 0] 9 bits

        List<Integer> selectedTrueGenes = new ArrayList<>(); //positions of genes with 1 value
        List<Integer> selectedFalseGenes = new ArrayList<>();//positions of genes with 0 value

        for (int i = 0; i < numberOfBits; i++) {
            if (binarySolution.getIth(i)) {
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
            binarySolution.setIth(selectedTrueGenes.get(i), false);
            binarySolution.setIth(selectedFalseGenes.get(j), true);
        }

        Assert.assertEquals(true, binarySolution.getIth(0));
        Assert.assertEquals(false, binarySolution.getIth(1));
        Assert.assertEquals(true, binarySolution.getIth(2));
        Assert.assertEquals(false, binarySolution.getIth(3));
        Assert.assertEquals(false, binarySolution.getIth(4)); //changed
        Assert.assertEquals(true, binarySolution.getIth(5));
        Assert.assertEquals(false, binarySolution.getIth(6));
        Assert.assertEquals(true, binarySolution.getIth(7));
        Assert.assertEquals(true, binarySolution.getIth(8)); //changed

        //TEST 2 
        int i = 4;
        int j = 1;
        binarySolution.setIth(selectedTrueGenes.get(i), false);
        binarySolution.setIth(selectedFalseGenes.get(j), true);

        Assert.assertEquals(true, binarySolution.getIth(0));
        Assert.assertEquals(false, binarySolution.getIth(1));
        Assert.assertEquals(true, binarySolution.getIth(2));
        Assert.assertEquals(true, binarySolution.getIth(3));
        Assert.assertEquals(false, binarySolution.getIth(4)); //changed
        Assert.assertEquals(true, binarySolution.getIth(5));
        Assert.assertEquals(false, binarySolution.getIth(6));
        Assert.assertEquals(false, binarySolution.getIth(7));
        Assert.assertEquals(true, binarySolution.getIth(8)); //changed
    }

}
