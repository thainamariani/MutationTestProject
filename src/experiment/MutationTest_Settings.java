/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

/**
 * Class that contains the configuration of Mutation Test
 *
 * @author Prado Lima
 */
public class MutationTest_Settings {

    public static final String[] INSTANCES = {
        "instances/bisect.txt",
        //"instances/bub.txt",
        //"instances/find.txt",
        //"instances/fourballs.txt",
        //"instances/mid.txt",
        //"instances/trityp.txt"
    };

    public static final MutationMetaheuristic[] ALGORITHMS = {
        //MutationMetaheuristic.gGa, //MutationMetaheuristic.ssGa,
        MutationMetaheuristic.HillClimbing,
        MutationMetaheuristic.HillClimbingAscendent,
        MutationMetaheuristic.HillClimbingAscendentWithReplacement
    };

    public static final int[] POPULATION_SIZE = {
        //50,
        100,
        //200
    };

    public static final int[] GENERATIONS = {
        100,
        //1000,
        //10000
    };

    public static final double[] CROSSOVER_PROBABILITY = {
        0.8,
        //0.9
    };

    public static final double[] MUTATION_PROBABILITY = {
        0.05,
        //0.1
    };

    public static final String[] CROSSOVER_OPERATORS = {
        "SinglePointCrossover",
        //"TwoPointsCrossoverBinary",
        //"UniformCrossoverBinary"
    };

    public static final String[] MUTATION_OPERATORS = {
        "BitFlipMutation",
        //"SwapMutationBinary"
    };

    public static final String[] SELECTION_OPERATORS = {
        //"BinaryTournament",
        //"RouletteWheel",
        "LinearRanking"
    };

    public static final int[] FITNESS_FUNCTIONS = {
        //1,
        2
    };

    // Hill Climbing
    public static final int[] IMPROVEMENT_ROUNDS = {
    10
    //10,
    //100
    };
    
     // Hill Climbing for Ascendent and ascendent with replacement
    public static final int[] TWEAKS = {
        10
    //10,
    //100
    };

    public static final int EXECUTIONS = 10;
}
