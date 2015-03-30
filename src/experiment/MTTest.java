package experiment;

import static experiment.MutationMetaheuristic.HillClimbingA;
import java.util.HashMap;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.core.SolutionSet;
import jmetal.metaheuristics.hillClimbing.HillClimbingA;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.ssGA;
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

    public static String instance;
    public static MutationMetaheuristic algo;
    public static int populationSize;
    public static int generations;
    public static double crossoverProbability;
    public static double mutationProbability;
    public static String crossoverOperator;
    public static String mutationOperator;
    public static String selectionOperator;
    public static int executions;
    public static String context;
    public static int fitnessFunction;
    public static int improvementRounds;

    public static void main(String[] args) throws JMException, ClassNotFoundException {

        //getLocalparameters();

        verifyParameters(args);
        //experiments configurations
        Problem problem = new MutationTestProblem(instance, fitnessFunction);

        //select algorithm
        Algorithm algorithm = algorithmFactory(problem);

        //print parameters
        printParameters(algorithm);

        // Algorithm params
        algorithm.setInputParameter("populationSize", populationSize);
        algorithm.setInputParameter("maxEvaluations", populationSize * generations);
        algorithm.setInputParameter("improvementRounds", improvementRounds);

        Operator crossover;         // Crossover operator
        Operator mutation;         // Mutation operator
        Operator selection;         // Selection operator

        HashMap parameters; // Operator parameters
        // Mutation and Crossover for Real codification
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability);
        crossover = CrossoverFactory.getCrossoverOperator(crossoverOperator, parameters);

        parameters = new HashMap();
        parameters.put("probability", mutationProbability);
        mutation = MutationFactory.getMutationOperator(mutationOperator, parameters);

        /* Selection Operator */
        parameters = null;
        selection = SelectionFactory.getSelectionOperator(selectionOperator, parameters);

        /* Add the operators to the algorithm*/
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        /* Execute the Algorithm */
        for (int i = 0; i < executions; i++) {
            System.out.println("Run: " + i);
            long initTime = System.currentTimeMillis();
            SolutionSet population = algorithm.execute();
            long estimatedTime = System.currentTimeMillis() - initTime;
            System.out.println("Total time of execution: " + estimatedTime);
            /* Log messages */
            String path = "experiment/" + getInstanceName(instance) + "/" + algo + "/F" + fitnessFunction + "/" + context;
            System.out.println("Objectives values have been writen to file " + path + "/" + "FUN_" + i);
            population.printObjectivesToFile(path + "/" + "FUN_" + i);
            System.out.println("Variables values have been writen to file " + path + "/" + "FUN_" + i);
            population.printVariablesToFile(path + "/" + "VAR_" + i);
        }

    }

    private static void getLocalparameters() {

        instance = "instances/bisect.txt";
        algo = MutationMetaheuristic.HillClimbingA;
        populationSize = 1;
        generations = 1;
        crossoverProbability = 0.0;
        mutationProbability = 0.0;
        crossoverOperator = "SinglePointCrossover";
        mutationOperator = "BitFlipMutation";
        executions = 1;
        context = "teste";
        fitnessFunction = 1;
        selectionOperator = "BinaryTournament";
        improvementRounds = 10;
    }

    private static void verifyParameters(String[] args) {
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

        //instance
        if (args[0] != null && !args[0].trim().equals("")) {
            instance = args[0];
        }

        //algorithm
        if (args[1] != null && !args[1].trim().equals("")) {
            algo = MutationMetaheuristic.valueOf(args[1]);
        }

        //populationSize
        if (args[2] != null && !args[2].trim().equals("")) {
            try {
                populationSize = Integer.valueOf(args[2]);
            } catch (NumberFormatException ex) {
                System.out.println("Population size argument not integer.");
                System.exit(1);
            }
        }

        //generations
        if (args[3] != null && !args[3].trim().equals("")) {
            try {
                generations = Integer.valueOf(args[3]);
            } catch (NumberFormatException ex) {
                System.out.println("Generations argument not integer.");
                System.exit(1);
            }
        }

        //crossoverProbability
        crossoverProbability = 0.0;
        if (args[4] != null && !args[4].trim().equals("")) {
            try {
                crossoverProbability = Double.valueOf(args[4]);
            } catch (NumberFormatException ex) {
                System.out.println("Crossover probability argument not double.");
                System.exit(1);
            }
        }

        //mutationProbability
        mutationProbability = 0.0;

        if (args[5] != null && !args[5].trim().equals("")) {
            try {
                mutationProbability = Double.valueOf(args[5]);
            } catch (NumberFormatException ex) {
                System.out.println("Mutation probability argument not double.");
                System.exit(1);
            }
        }

        //crossoverOperator
        if (args[6] != null && !args[6].trim().equals("")) {
            crossoverOperator = args[6];
        }

        //mutationOperator
        if (args[7] != null && !args[7].trim().equals("")) {
            mutationOperator = args[7];
        }

        //executions
        if (args[8] != null && !args[8].trim().equals("")) {
            try {
                executions = Integer.valueOf(args[8]);
            } catch (NumberFormatException ex) {
                System.out.println("Executions argument not double.");
                System.exit(1);
            }
        }

        //context
        if (args[9] != null && !args[9].trim().equals("")) {
            context = args[9];
        }

        //fitness function
        if (args[10] != null && !args[10].trim().equals("")) {
            try {
                fitnessFunction = Integer.valueOf(args[10]);
            } catch (NumberFormatException ex) {
                System.out.println("Fitness Function argument not integer.");
                System.exit(1);
            }
        }

        //selection operator
        if (args[11] != null && !args[11].trim().equals("")) {
            selectionOperator = args[11];
        }

        //improvement rounds
        if (args[12] != null && !args[12].trim().equals("")) {
            improvementRounds = Integer.valueOf(args[12]);
        }

    }

    private static void printParameters(Algorithm algorithm) {
        System.out.println("Parameters Information");
        System.out.println("----------------------------------------------------");
        System.out.println("Instance: " + instance);
        System.out.println("Algorithm: " + algorithm.toString());
        System.out.println("Fitness Function: F" + fitnessFunction);
        System.out.println("Population: " + populationSize);
        System.out.println("maxEvaluations: " + populationSize * generations);
        System.out.println("crossoverProbability: " + crossoverProbability);
        System.out.println("mutationsProbability; " + mutationProbability);
        System.out.println("crossoverOperator: " + crossoverOperator);
        System.out.println("mutationOperator: " + mutationOperator);
        System.out.println("selectionOperator: " + selectionOperator);
        System.out.println("executions: " + executions);
        System.out.println("improvementRounds: " + improvementRounds);
        System.out.println("----------------------------------------------------");
    }

    private static Algorithm algorithmFactory(Problem problem) {
        switch (algo) {
            case gGa:
                return new gGA(problem);
            case ssGa:
                return new ssGA(problem);
            case HillClimbingA:
                return new HillClimbingA(problem);
            default:
                throw new AssertionError();
        }
    }

    public static String getInstanceName(String path) {
        int end = path.indexOf(".txt");
        return path.substring(10, end);
    }
}
