/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.operators.selection;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.IRandomGenerator;
import jmetal.util.PseudoRandom;
import jmetal.util.comparators.ObjectiveComparator;
import operators.selection.LinearRanking;
import org.junit.Assert;
import org.junit.Test;
import problem.MutationTestProblem;

/**
 *
 * @author thaina
 */
public class LinearRankingTest {

    public LinearRankingTest() {
    }

    @Test
    public void testExecute() throws Exception {
        //TEST 1
        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 1}
        };

        //problem created just to instantiate a solution
        MutationTestProblem problem = new MutationTestProblem(9, 10, coverage);

        Solution solution1 = new Solution(problem);
        solution1.setObjective(0, -0.5);

        Solution solution2 = new Solution(problem);
        solution2.setObjective(0, -0.2);

        Solution solution3 = new Solution(problem);
        solution3.setObjective(0, -0.5);

        Solution solution4 = new Solution(problem);
        solution4.setObjective(0, -0.8);

        Solution solution5 = new Solution(problem);
        solution5.setObjective(0, -0.9);

        SolutionSet population = new SolutionSet(5);
        population.add(solution3);
        population.add(solution2);
        population.add(solution1);
        population.add(solution4);
        population.add(solution5);

        Comparator comparator = new ObjectiveComparator(0, true);
        population.sort(comparator);

        HashMap<String, Object> parameters = new HashMap<>();
        LinearRanking linearRanking = new LinearRanking(parameters);

        List<Double> populationProbability = linearRanking.calculateProbability(population);
        Assert.assertEquals(0.15, populationProbability.get(0), 0.01);
        Assert.assertEquals(0.2, populationProbability.get(1), 0.01);
        Assert.assertEquals(0.25, populationProbability.get(2), 0.01);
        Assert.assertEquals(0.3, populationProbability.get(3), 0.01);
        Assert.assertEquals(0.35, populationProbability.get(4), 0.01);

        List<Double> normalizeProbability = linearRanking.normalizeProbability(populationProbability);
        Assert.assertEquals(0.12, normalizeProbability.get(0), 0.01);
        Assert.assertEquals(0.16, normalizeProbability.get(1), 0.01);
        Assert.assertEquals(0.2, normalizeProbability.get(2), 0.01);
        Assert.assertEquals(0.24, normalizeProbability.get(3), 0.01);
        Assert.assertEquals(0.28, normalizeProbability.get(4), 0.01);

        PseudoRandom.setRandomGenerator(new IRandomGenerator() {

            @Override
            public int nextInt(int upperLimit) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public double nextDouble() {
                return 0.1;
            }
        });

        Solution selectedSolution = (Solution) linearRanking.selectSolution(normalizeProbability, population);
        Assert.assertEquals(solution2, selectedSolution);

        PseudoRandom.setRandomGenerator(new IRandomGenerator() {

            @Override
            public int nextInt(int upperLimit) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public double nextDouble() {
                return 0.2;
            }
        });

        selectedSolution = (Solution) linearRanking.selectSolution(normalizeProbability, population);
        Assert.assertEquals(solution3, selectedSolution);

        PseudoRandom.setRandomGenerator(new IRandomGenerator() {

            @Override
            public int nextInt(int upperLimit) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public double nextDouble() {
                return 0.4;
            }
        });

        selectedSolution = (Solution) linearRanking.selectSolution(normalizeProbability, population);
        Assert.assertEquals(solution1, selectedSolution);

        PseudoRandom.setRandomGenerator(new IRandomGenerator() {

            @Override
            public int nextInt(int upperLimit) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public double nextDouble() {
                return 0.8;
            }
        });

        selectedSolution = (Solution) linearRanking.selectSolution(normalizeProbability, population);
        Assert.assertEquals(solution5, selectedSolution);

    }

}
