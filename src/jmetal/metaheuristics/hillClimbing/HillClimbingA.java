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
 * @author Prado Lima
 */
public class HillClimbingA extends Algorithm {

    /**
     *
     * Constructor Create a new HillClimbing instance.
     *
     * @param problem Problem to solve.
     */
    public HillClimbingA(Problem problem) {
        super(problem);
    }

    /*
     @Override
     public Object execute(Object object) throws JMException {
     int rounds = improvementRounds_;
     Solution solution = (Solution) object; // Initial Solution

     if (rounds <= 0) {
     return new Solution(solution);
     }

     int i = 0; // Iterator
     evaluations_ = 0;

     while (i < rounds) {
     i++;

     // Copy of solution
     Solution mutatedSolution = new Solution(solution);

     // Tweak
     mutationOperator_.execute(mutatedSolution);
     problem_.evaluate(mutatedSolution);
     evaluations_++;

     // This is: Mutated is best
     if (dominanceComparator_.compare(mutatedSolution, solution) == -1) {
     solution = mutatedSolution;
     }
     }

     return new Solution(solution);
     }*/
    @Override
    public SolutionSet execute() throws JMException, ClassNotFoundException {
        int rounds = ((Integer) this.getInputParameter("improvementRounds")).intValue();
        int i = 0;

        // Read the operators
        Operator mutationOperator = this.operators_.get("mutation");

        Solution solution = new Solution(problem_); // Initial Solution
        problem_.evaluate(solution); // Executing evalute method to get "fitness" after

        while (i < rounds) {
            i++;
            
            // Copy of solution
            Solution mutatedSolution = new Solution(solution);

            // Tweak
            mutationOperator.execute(mutatedSolution);
            problem_.evaluate(mutatedSolution); // Executing evalute method to get "fitness" after

            // This is: Mutated is best
            if (solution.getObjective(0) > mutatedSolution.getObjective(0)) {
                solution = mutatedSolution;
            }
        }

        // Return a population with the best individual
        SolutionSet resultSolution = new SolutionSet(1);
        resultSolution.add(solution);
        
        return resultSolution;
    }
}
