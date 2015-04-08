/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jmetal.metaheuristics.hillClimbing;

import java.util.Comparator;
import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.core.SolutionSet;
import jmetal.operators.localSearch.LocalSearch;
import jmetal.operators.mutation.Mutation;
import jmetal.util.JMException;
import jmetal.util.comparators.DominanceComparator;
import jmetal.util.comparators.ObjectiveComparator;
import jmetal.util.comparators.OverallConstraintViolationComparator;

/**
 *
 * @author Mariana
 */
public class HillClimbingAscendent extends Algorithm {

    /**
     *
     * Constructor Create a new HillClimbing instance.
     *
     * @param problem Problem to solve.
     */
    public HillClimbingAscendent(Problem problem) {
        super(problem);
    }

    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int rounds = ((Integer) this.getInputParameter("improvementRounds")).intValue();
        int n = ((Integer) this.getInputParameter("tweaks")).intValue();
        int evaluations = 0;

        // Read the operators
        Operator mutationOperator = this.operators_.get("mutation");

        Solution solution = new Solution(problem_); // Initial Solution
        problem_.evaluate(solution); // Executing evalute method to get "fitness" after

        while (evaluations < rounds) {
            // Copy of solution
            Solution r = new Solution(solution);

            // Tweak
            mutationOperator.execute(r);
            problem_.evaluate(r); // Executing evalute method to get "fitness" after
            evaluations++;

            for (int j = 0; j < n - 1; j++) {
                // Copy of solution
                Solution w = new Solution(solution);

                // Tweak
                mutationOperator.execute(w);
                problem_.evaluate(w); // Executing evalute method to get "fitness" after

                // This is: Mutated is best (Minimize)
                if (w.getObjective(0) < r.getObjective(0)) {
                    r = w;
                }
            }

            // This is: Mutated is best (Minimize)
            if (r.getObjective(0) < solution.getObjective(0)) {
                solution = r;
            }
        }

        // Return a population with the best individual
        SolutionSet resultSolution = new SolutionSet(1);
        resultSolution.add(solution);

        return resultSolution;
    }
}
