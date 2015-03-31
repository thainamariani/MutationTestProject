/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import jmetal.core.Algorithm;
import jmetal.core.Problem;
import jmetal.metaheuristics.hillClimbing.HillClimbingA;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.gGA;
import jmetal.metaheuristics.singleObjective.geneticAlgorithm.ssGA;

/**
 * Class that contains the parameters encapsulated
 * @author Prado Lima
 */
public class MutationTest_Parameters {

    private String instance;
    private MutationMetaheuristic algo;
    private int populationSize;
    private int generations;
    private double crossoverProbability;
    private double mutationProbability;
    private String crossoverOperator;
    private String mutationOperator;
    private String selectionOperator;
    private int executions;
    private String context;
    private int fitnessFunction;
    private int improvementRounds;

    //<editor-fold defaultstate="collapsed" desc="Methods">
    
    public Algorithm getAlgorithmInstance(Problem problem) {
        switch (getAlgo()) {
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

    public void PrintParameters() {
        System.out.println("Parameters Information");
        System.out.println("----------------------------------------------------");
        System.out.println("Instance: " + getInstance());
        System.out.println("Algorithm: " + getAlgo());
        System.out.println("Fitness Function: F" + getFitnessFunction());
        System.out.println("Population: " + getPopulationSize());
        System.out.println("maxEvaluations: " + getPopulationSize() * getGenerations());
        System.out.println("crossoverProbability: " + getCrossoverProbability());
        System.out.println("mutationsProbability; " + getMutationProbability());
        System.out.println("crossoverOperator: " + getCrossoverOperator());
        System.out.println("mutationOperator: " + getMutationOperator());
        System.out.println("selectionOperator: " + getSelectionOperator());
        System.out.println("executions: " + getExecutions());
        System.out.println("improvementRounds: " + getImprovementRounds());
        System.out.println("----------------------------------------------------");
    }
    
    public static synchronized String generateAlgorithmId(final MutationMetaheuristic algorithm, final int populationSize, final int generations, final double crossoverProbability, final double mutationProbability, final String crossoverOperator, final String mutationOperator, final int executions, final String selectionOperator, final int improvementRounds) {
        switch (algorithm) {
            case HillClimbingA:
                return String.format("%s_%s_%s", improvementRounds, mutationOperator, executions);
            default:
                return String.format("%s_%s_%s_%s_%s_%s_%s_%s", populationSize, generations, crossoverProbability, mutationProbability, crossoverOperator, mutationOperator, selectionOperator, executions);
        }
    }
    
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters/Setters">
    /**
     * @return the instance
     */
    public String getInstance() {
        return instance;
    }

    /**
     * @param instance the instance to set
     */
    public void setInstance(String instance) {
        this.instance = instance;
    }

    /**
     * @return the algo
     */
    public MutationMetaheuristic getAlgo() {
        return algo;
    }

    /**
     * @param algo the algo to set
     */
    public void setAlgo(MutationMetaheuristic algo) {
        this.algo = algo;
    }

    /**
     * @return the populationSize
     */
    public int getPopulationSize() {
        return populationSize;
    }

    /**
     * @param populationSize the populationSize to set
     */
    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

    /**
     * @return the generations
     */
    public int getGenerations() {
        return generations;
    }

    /**
     * @param generations the generations to set
     */
    public void setGenerations(int generations) {
        this.generations = generations;
    }

    /**
     * @return the crossoverProbability
     */
    public double getCrossoverProbability() {
        return crossoverProbability;
    }

    /**
     * @param crossoverProbability the crossoverProbability to set
     */
    public void setCrossoverProbability(double crossoverProbability) {
        this.crossoverProbability = crossoverProbability;
    }

    /**
     * @return the mutationProbability
     */
    public double getMutationProbability() {
        return mutationProbability;
    }

    /**
     * @param mutationProbability the mutationProbability to set
     */
    public void setMutationProbability(double mutationProbability) {
        this.mutationProbability = mutationProbability;
    }

    /**
     * @return the crossoverOperator
     */
    public String getCrossoverOperator() {
        return crossoverOperator;
    }

    /**
     * @param crossoverOperator the crossoverOperator to set
     */
    public void setCrossoverOperator(String crossoverOperator) {
        this.crossoverOperator = crossoverOperator;
    }

    /**
     * @return the mutationOperator
     */
    public String getMutationOperator() {
        return mutationOperator;
    }

    /**
     * @param mutationOperator the mutationOperator to set
     */
    public void setMutationOperator(String mutationOperator) {
        this.mutationOperator = mutationOperator;
    }

    /**
     * @return the selectionOperator
     */
    public String getSelectionOperator() {
        return selectionOperator;
    }

    /**
     * @param selectionOperator the selectionOperator to set
     */
    public void setSelectionOperator(String selectionOperator) {
        this.selectionOperator = selectionOperator;
    }

    /**
     * @return the executions
     */
    public int getExecutions() {
        return executions;
    }

    /**
     * @param executions the executions to set
     */
    public void setExecutions(int executions) {
        this.executions = executions;
    }

    /**
     * @return the context
     */
    public String getContext() {
        return context;
    }

    /**
     * @param context the context to set
     */
    public void setContext(String context) {
        this.context = context;
    }

    /**
     * @return the fitnessFunction
     */
    public int getFitnessFunction() {
        return fitnessFunction;
    }

    /**
     * @param fitnessFunction the fitnessFunction to set
     */
    public void setFitnessFunction(int fitnessFunction) {
        this.fitnessFunction = fitnessFunction;
    }

    /**
     * @return the improvementRounds
     */
    public int getImprovementRounds() {
        return improvementRounds;
    }

    /**
     * @param improvementRounds the improvementRounds to set
     */
    public void setImprovementRounds(int improvementRounds) {
        this.improvementRounds = improvementRounds;
    }
      //</editor-fold>
}