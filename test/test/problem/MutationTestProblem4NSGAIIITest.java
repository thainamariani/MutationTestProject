/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.problem;

import jmetal.encodings.variable.Binary;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.BinarySolution;
import problem.MutationTestProblem;
import problem.MutationTestProblem4NSGAIII;

/**
 *
 * @author thaina
 */
public class MutationTestProblem4NSGAIIITest {

    @Test
    public void testShouldCountNumberOfKilledMutantsCorrectly() {
        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 1}
        };

        MutationTestProblem4NSGAIII p = new MutationTestProblem4NSGAIII(3, 3, coverage);

        assertEquals(1, p.getNumberOfKilledMutants(0));
        assertEquals(2, p.getNumberOfKilledMutants(1));
        assertEquals(2, p.getNumberOfKilledMutants(2));
    }

    @Test
    public void testShouldCountNumberOfDifferentKilledMutantsCorrectly() {
        int[][] coverage = {
            {1, 0, 1, 0},
            {0, 1, 0, 1},
            {0, 1, 1, 1},
            {1, 0, 0, 1},
            {1, 1, 1, 1}
        };

        MutationTestProblem4NSGAIII p = new MutationTestProblem4NSGAIII(4, 5, coverage);

        // Solution[1, 0, 0, 1]
        Binary solution = new Binary(4);
        solution.setIth(0, true);
        solution.setIth(1, false);
        solution.setIth(2, false);
        solution.setIth(3, true);

        assertEquals(5, p.getNumberOfDifferentKilledMutants(solution));

        // Solution[1, 1, 1, 1]
        solution = new Binary(4);
        solution.setIth(0, true);
        solution.setIth(1, true);
        solution.setIth(2, true);
        solution.setIth(3, true);

        assertEquals(5, p.getNumberOfDifferentKilledMutants(solution));

        // Solution[1, 0, 0, 0]
        solution = new Binary(4);
        solution.setIth(0, true);
        solution.setIth(1, false);
        solution.setIth(2, false);
        solution.setIth(3, false);

        assertEquals(3, p.getNumberOfDifferentKilledMutants(solution));

        // Solution[1, 0, 1, 0]
        solution = new Binary(4);
        solution.setIth(0, true);
        solution.setIth(1, false);
        solution.setIth(2, true);
        solution.setIth(3, false);

        assertEquals(4, p.getNumberOfDifferentKilledMutants(solution));

        // Solution[0, 1, 0, 1]
        solution = new Binary(4);
        solution.setIth(0, true);
        solution.setIth(1, false);
        solution.setIth(2, true);
        solution.setIth(3, false);

        assertEquals(4, p.getNumberOfDifferentKilledMutants(solution));
    }

    @Test
    public void FitnessFunctionsAreCorrect() {
        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {1, 0, 1},
            {1, 0, 0},};

        MutationTestProblem4NSGAIII p = new MutationTestProblem4NSGAIII(3, 4, coverage);

        //Solution[1, 1, 1]
        Binary solution = new Binary(3);
        solution.setIth(0, true);
        solution.setIth(1, true);
        solution.setIth(2, true);

        double mutantionScore = p.getMutantionScore(solution);
        assertEquals(1, mutantionScore, 0);
        assertEquals(0.33, p.fitnessFunction(mutantionScore, 3), 0.01);
        assertEquals(0, p.fitnessFunction(1, 1, mutantionScore, 3, 3), 0);

        //Solution[1, 1, 0]
        solution = new Binary(3);
        solution.setIth(0, true);
        solution.setIth(1, true);
        solution.setIth(2, false);

        mutantionScore = p.getMutantionScore(solution);
        assertEquals(1, mutantionScore, 0);
        assertEquals(0.5, p.fitnessFunction(mutantionScore, 2), 0);
        assertEquals(0.33, p.fitnessFunction(1, 1, mutantionScore, 2, 3), 0.01);

        //Solution[1, 0, 0]
        solution = new Binary(3);
        solution.setIth(0, true);
        solution.setIth(1, false);
        solution.setIth(2, false);

        mutantionScore = p.getMutantionScore(solution);
        assertEquals(0.75, mutantionScore, 0);
        assertEquals(0.75, p.fitnessFunction(mutantionScore, 1), 0);
        assertEquals(0.41, p.fitnessFunction(1, 1, mutantionScore, 1, 3), 0.01);

        //Solution[0, 1, 0]
        solution = new Binary(3);
        solution.setIth(0, false);
        solution.setIth(1, true);
        solution.setIth(2, false);

        mutantionScore = p.getMutantionScore(solution);
        assertEquals(0.25, mutantionScore, 0);
        assertEquals(0.25, p.fitnessFunction(mutantionScore, 1), 0);
        assertEquals(-0.08, p.fitnessFunction(1, 1, mutantionScore, 1, 3), 0.01);
    }
}
