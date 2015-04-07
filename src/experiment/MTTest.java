package experiment;

import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import problem.MutationTestProblem;
import results.Results;

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

    public static void main(String[] args) throws JMException, ClassNotFoundException {
        
        MutationTest_Parameters mutationParameters = VerifyParameters(args);
        //experiments configurations
        Problem problem = new MutationTestProblem(mutationParameters.getInstance(), mutationParameters.getFitnessFunction());

        //select algorithm
        Algorithm algorithm = mutationParameters.getAlgorithmInstance(problem);

        //print parameters
        mutationParameters.PrintParameters();

        // Algorithm params
        algorithm.setInputParameter("populationSize", mutationParameters.getPopulationSize());
        algorithm.setInputParameter("maxEvaluations", mutationParameters.getPopulationSize() * mutationParameters.getGenerations());
        algorithm.setInputParameter("improvementRounds", mutationParameters.getImprovementRounds());

        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator

        HashMap parameters; // Operator parameters
        // Mutation and Crossover for Real codification
        parameters = new HashMap();
        parameters.put("probability", mutationParameters.getCrossoverProbability());
        crossover = CrossoverFactory.getCrossoverOperator(mutationParameters.getCrossoverOperator(), parameters);
        parameters = new HashMap();
        parameters.put("probability", mutationParameters.getMutationProbability());
        mutation = MutationFactory.getMutationOperator(mutationParameters.getMutationOperator(), parameters);

        /* Selection Operator */
        parameters = null;
        selection = SelectionFactory.getSelectionOperator(mutationParameters.getSelectionOperator(), parameters);

        /* Add the operators to the algorithm*/
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        String path = "";
        /* Execute the Algorithm */
        for (int i = 0; i < mutationParameters.getExecutions(); i++) {
            System.out.println("Run: " + i);
            long initTime = System.currentTimeMillis();
            SolutionSet population = algorithm.execute();
            long estimatedTime = System.currentTimeMillis() - initTime;
            System.out.println("Total time of execution: " + estimatedTime);
            /* Log messages */
            path = String.format("experiment/%s/%s/F%s/%s", getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getFitnessFunction(), mutationParameters.getContext());
            String pathFun = String.format("%s/FUN_%s", path, i);
            String pathVar = String.format("%s/VAR_%s", path, i);

            System.out.println("Objectives values have been writen to file " + pathFun);
            population.printObjectivesToFile(pathFun);

            System.out.println("Variables values have been writen to file " + pathVar);
            population.printVariablesToFile(pathVar);
        }

    }

    private static MutationTest_Parameters VerifyParameters(String[] args) {
        // The parameters are optionals
        /*if (args.length < 10) {
         System.out.println("You need to inform the following parameters:");
         System.out.println("\t1 - Instance (String);"
         + "\t2 - Algorithm (String);"
         + "\t3 - Population Size (Integer);"
         + "\n\t4 - Generations (Integer);"
         + "\n\t5 - Crossover Probability (Double);"
         + "\n\t6 - Mutation Probability (Double);"
         + "\n\t7 - Crossover Operator (String);"
         + "\n\t8 - Mutation Operator (String);"
         + "\n\t8 - Context (String);"
         + "\n\t10 - Executions (Integer);"
         + "\n\t11 - Fitness Function (Integer);"
         + "\n\t12 - Selection Operator (String)"
         );
         System.exit(0);
         }*/

        MutationTest_Parameters mutationParameters = new MutationTest_Parameters();
        //instance
        if (args[0] != null && !args[0].trim().equals("")) {
            mutationParameters.setInstance(args[0]);
        }

        //algorithm
        if (args[1] != null && !args[1].trim().equals("")) {
            mutationParameters.setAlgo(MutationMetaheuristic.valueOf(args[1]));
        }

        //populationSize
        if (args[2] != null && !args[2].trim().equals("")) {
            try {
                mutationParameters.setPopulationSize(Integer.valueOf(args[2]));
            } catch (NumberFormatException ex) {
                System.out.println("Population size argument not integer.");
                System.exit(1);
            }
        }

        //generations
        if (args[3] != null && !args[3].trim().equals("")) {
            try {
                mutationParameters.setGenerations(Integer.valueOf(args[3]));
            } catch (NumberFormatException ex) {
                System.out.println("Generations argument not integer.");
                System.exit(1);
            }
        }

        //crossoverProbability
        mutationParameters.setCrossoverProbability(0.0);
        if (args[4] != null && !args[4].trim().equals("")) {
            try {
                mutationParameters.setCrossoverProbability(Double.valueOf(args[4]));
            } catch (NumberFormatException ex) {
                System.out.println("Crossover probability argument not double.");
                System.exit(1);
            }
        }

        //mutationProbability
        mutationParameters.setMutationProbability(0.0);

        if (args[5] != null && !args[5].trim().equals("")) {
            try {
                mutationParameters.setMutationProbability(Double.valueOf(args[5]));
            } catch (NumberFormatException ex) {
                System.out.println("Mutation probability argument not double.");
                System.exit(1);
            }
        }

        //crossoverOperator
        if (args[6] != null && !args[6].trim().equals("")) {
            mutationParameters.setCrossoverOperator(args[6]);
        }

        //mutationOperator
        if (args[7] != null && !args[7].trim().equals("")) {
            mutationParameters.setMutationOperator(args[7]);
        }

        //executions
        if (args[8] != null && !args[8].trim().equals("")) {
            try {
                mutationParameters.setExecutions(Integer.valueOf(args[8]));
            } catch (NumberFormatException ex) {
                System.out.println("Executions argument not double.");
                System.exit(1);
            }
        }

        //context
        if (args[9] != null && !args[9].trim().equals("")) {
            mutationParameters.setContext(args[9]);
        }

        //fitness function
        if (args[10] != null && !args[10].trim().equals("")) {
            try {
                mutationParameters.setFitnessFunction(Integer.valueOf(args[10]));
            } catch (NumberFormatException ex) {
                System.out.println("Fitness Function argument not integer.");
                System.exit(1);
            }
        }

        //selection operator
        if (args[11] != null && !args[11].trim().equals("")) {
            mutationParameters.setSelectionOperator(args[11]);
        }

        //improvement rounds
        if (args[12] != null && !args[12].trim().equals("")) {
            mutationParameters.setImprovementRounds(Integer.valueOf(args[12]));
        }

        return mutationParameters;
    }

    public static String getInstanceName(String path) {
        int end = path.indexOf(".txt");
        return path.substring(10, end);
    }
}
