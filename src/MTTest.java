import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import problem.MutationTestProblem;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * Main class
 * 
 * @author thiagodnf
 */
public class MTTest {
    
    public static void main(String[] args) throws JMException, ClassNotFoundException{
        Problem problem = new MutationTestProblem("instances/bisect.txt");
        
        Algorithm algorithm = new gGA(problem);
    
         // Algorithm params
        algorithm.setInputParameter("populationSize",100);
        algorithm.setInputParameter("maxEvaluations",100000);
        
        Operator  crossover ;         // Crossover operator
        Operator  mutation  ;         // Mutation operator
        Operator  selection ;         // Selection operator

        HashMap  parameters ; // Operator parameters
        // Mutation and Crossover for Real codification
        parameters = new HashMap() ;
        parameters.put("probability", 0.95) ;
        crossover = CrossoverFactory.getCrossoverOperator("SinglePointCrossover", parameters);
        
        parameters = new HashMap() ;
        parameters.put("probability", 0.2) ;
        mutation = MutationFactory.getMutationOperator("BitFlipMutation", parameters);                    

        /* Selection Operator */
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament", parameters) ;                            

        /* Add the operators to the algorithm*/
        algorithm.addOperator("crossover",crossover);
        algorithm.addOperator("mutation",mutation);
        algorithm.addOperator("selection",selection);
        
        /* Execute the Algorithm */
        long initTime = System.currentTimeMillis();
        SolutionSet population = algorithm.execute();
        long estimatedTime = System.currentTimeMillis() - initTime;
        System.out.println("Total time of execution: "+estimatedTime);

        /* Log messages */
        System.out.println("Objectives values have been writen to file FUN");
        population.printObjectivesToFile("FUN");
        System.out.println("Variables values have been writen to file VAR");
        population.printVariablesToFile("VAR");  
    }
}
