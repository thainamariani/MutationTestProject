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
        "instances/bisect.txt", //"instances/bub.txt",
    //        "instances/find.txt",
    //        "instances/fourballs.txt",
    //        "instances/mid.txt",
    //        "instances/trityp.txt"
    };

    public static final MutationMetaheuristic[] ALGORITHMS = {
        MutationMetaheuristic.gGa, //MutationMetaheuristic.ssGa,
    //MutationMetaheuristic.HillClimbingA
    };

    public static final int[] POPULATION_SIZE = {
        100, //200
    };

    public static final int[] GENERATIONS = {
        1000/*,
     10000,
     100000*/

    };

    public static final double[] CROSSOVER_PROBABILITY = {
        //0.8,
        0.95
    };

    public static final double[] MUTATION_PROBABILITY = {
        //0.005,
        0.1
    };

    public static final String[] CROSSOVER_OPERATORS = {
        //"SinglePointCrossover"
        //"TwoPointsCrossoverBinary"
        "UniformCrossoverBinary"
    };

    public static final String[] MUTATION_OPERATORS = {
        "BitFlipMutation"
    };

    public static final String[] SELECTION_OPERATORS = {
        "BinaryTournament"
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

    public static final int EXECUTIONS = 1;
}
