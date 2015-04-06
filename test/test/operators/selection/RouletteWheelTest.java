/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package test.operators.selection;

import java.util.HashMap;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.util.IRandomGenerator;
import jmetal.util.PseudoRandom;
import junit.framework.Assert;
import operators.selection.RouletteWheel;
import org.junit.Test;
import problem.MutationTestProblem;

/**
 *
 * @author thaina
 */
public class RouletteWheelTest {

    @Test
    public void testExecute() throws Exception {

        int[][] coverage = {
            {1, 0, 1},
            {0, 1, 0},
            {0, 1, 1}
        };

        //problem created just to instantiate a solution
        MutationTestProblem problem = new MutationTestProblem(9, 10, coverage);

        Solution solution1 = new Solution(problem);
        solution1.setObjective(0, -0.1);

        Solution solution2 = new Solution(problem);
        solution2.setObjective(0, -0.2);

        Solution solution3 = new Solution(problem);
        solution3.setObjective(0, -0.5);

        Solution solution4 = new Solution(problem);
        solution4.setObjective(0, -0.8);

        Solution solution5 = new Solution(problem);
        solution5.setObjective(0, 0.9);

        SolutionSet population = new SolutionSet(5);
        population.add(solution1);
        population.add(solution2);
        population.add(solution3);
        population.add(solution4);
        population.add(solution5);

        //TEST1
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

        HashMap<String, Object> parameters = new HashMap<>();
        RouletteWheel rouletteWheel = new RouletteWheel(parameters);
        Solution solution = (Solution) rouletteWheel.execute(population);
        Assert.assertEquals(solution2, solution);

        //TEST2
        PseudoRandom.setRandomGenerator(new IRandomGenerator() {

            @Override
            public int nextInt(int upperLimit) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public double nextDouble() {
                return 0.5;
            }
        });

        Solution solutions2 = (Solution) rouletteWheel.execute(population);
        Assert.assertEquals(solution4, solutions2);
        Assert.assertEquals(solution4, solutions2);

        //TEST3
        PseudoRandom.setRandomGenerator(new IRandomGenerator() {

            @Override
            public int nextInt(int upperLimit) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public double nextDouble() {
                return 1;
            }
        });

        Solution solutions3 = (Solution) rouletteWheel.execute(population);
        Assert.assertEquals(solution5, solutions3);
        Assert.assertEquals(solution5, solutions3);
    }

}
