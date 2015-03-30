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
public class HillClimbingLS extends LocalSearch {

    /**
     * Stores the problem to solve
     */
    private Problem problem_;

    private int improvementRounds_;

    /**
     * Stores comparators for dealing with dominance checking
     */
    private Comparator dominanceComparator_;

    /**
     * Stores the mutation operator
     */
    private Operator mutationOperator_;

    /**
     * Stores the number of evaluations_ carried out
     */
    int evaluations_;

    /**
     * Constructor. Creates a new local search object.
     *
     * @param parameters The parameters
     *
     */
    public HillClimbingLS(HashMap<String, Object> parameters) {
        super(parameters);

        if (parameters.get("problem") != null) {
            problem_ = (Problem) parameters.get("problem");
        }
        if (parameters.get("improvementRounds") != null) {
            improvementRounds_ = (Integer) parameters.get("improvementRounds");
        }
        if (parameters.get("mutation") != null) {
            mutationOperator_ = (Mutation) parameters.get("mutation");
        }

        evaluations_ = 0;
        dominanceComparator_ = new DominanceComparator();
    }

    @Override
    /**
     * Returns the number of evaluations maded
     */
    public int getEvaluations() {
        return evaluations_;
    } // evaluations

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
    }
    /*
     @Override
     public SolutionSet execute() throws JMException, ClassNotFoundException {
     int maxEvaluations;
     int evaluations;

     Operator mutationOperator;

     Comparator comparator;
     comparator = new ObjectiveComparator(0); // Single objective comparator

     Solution parent[];

     //Read the parameters
     maxEvaluations = ((Integer) this.getInputParameter("maxEvaluations")).intValue();

     mutationOperator = operators_.get("mutation");

     //Initialize the variable
     evaluations = 0;

     // Create the initial solutionSet
     SolutionSet resultSolution = new SolutionSet(1);

     // Create a new solution
     Solution s = new Solution();
        
     s = new Solution(problem_);
     problem_.evaluate(s);
     problem_.evaluateConstraints(s);
     evaluations++;

     while (evaluations < maxEvaluations) {
     SolutionSet rSolution = new SolutionSet(1);
     rSolution.add(s);
            
     Solution r = rSolution.get(0);

     // Mutation - tweak
     mutationOperator.execute(r);

     problem_.evaluate(r);
     problem_.evaluateConstraints(r);

     if (comparator.compare(s, r) < 0) {
     s = r;
     }

     evaluations++;
     } // while

     resultSolution.add(s);
        
     System.out.println("Evaluations: " + evaluations);
     return resultSolution;
     }*/
}
