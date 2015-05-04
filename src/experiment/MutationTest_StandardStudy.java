/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jmetal.core.Algorithm;
import jmetal.core.Operator;
import jmetal.core.Problem;
import jmetal.experiments.Experiment;
import jmetal.experiments.Settings;
import jmetal.experiments.settings.GDE3_Settings;
import jmetal.experiments.settings.MOCell_Settings;
import jmetal.experiments.settings.SMPSO_Settings;
import jmetal.experiments.settings.SPEA2_Settings;
import jmetal.experiments.studies.StandardStudy2;
import jmetal.experiments.util.Friedman;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.SelectionFactory;
import jmetal.util.JMException;
import problem.MutationTestProblem;

/**
 *
 * @author Prado Lima
 */
public class MutationTest_StandardStudy extends Experiment {

    private static HashMap[] parameters_;
    private static int parameterPosition;

    /**
     * Configures the algorithms in each independent run
     *
     * @param problemName The problem to solve
     * @param problemIndex
     * @throws ClassNotFoundException
     */
    public void algorithmSettings(String problemName,
            int problemIndex,
            Algorithm[] algorithm) throws ClassNotFoundException {
        try {
            Problem problem = new MutationTestProblem(parameters_[parameterPosition].get("instance").toString(), 0);
            int numberOfAlgorithms = algorithmNameList_.length;

            /*
             for (int i = 0; i < numberOfAlgorithms; i++) {
             parameters_[i] = new HashMap();
             } // for

             if (!(paretoFrontFile_[problemIndex] == null) && !paretoFrontFile_[problemIndex].equals("")) {
             for (int i = 0; i < numberOfAlgorithms; i++) {
             parameters_[i].put("paretoFrontFile_", paretoFrontFile_[problemIndex]);
             }
             } // if
             */
            algorithm[0] = new NSGAII_Settings(problemName, problem).configureHash(parameters_[parameterPosition]);

            //algorithm[0] = new NSGAII_Settings(problemName).configure(parameters_[parameterPosition]);
            //algorithm[1] = new SPEA2_Settings(problemName).configure(parameters_[parameterPosition]);
            //algorithm[2] = new MOCell_Settings(problemName).configure(parameters_[parameterPosition]);
            //algorithm[3] = new SMPSO_Settings(problemName).configure(parameters_[parameterPosition]);
            //algorithm[4] = new GDE3_Settings(problemName).configure(parameters_[parameterPosition]);
        } catch (IllegalArgumentException ex) {
            Logger.getLogger(StandardStudy2.class.getName()).log(Level.SEVERE, null, ex);
        } /*catch (IllegalAccessException ex) {
         Logger.getLogger(StandardStudy2.class.getName()).log(Level.SEVERE, null, ex);
         }*/ catch (JMException ex) {
         Logger.getLogger(StandardStudy2.class.getName()).log(Level.SEVERE, null, ex);
         }

    } // algorithmSettings

    /**
     * Main method
     *
     * @param args
     * @throws JMException
     * @throws IOException
     */
    public static void main(String[] args) throws JMException, IOException {
        setParameters();

        for (parameterPosition = 0; parameterPosition < parameters_.length; parameterPosition++) {
            MutationTest_StandardStudy exp = new MutationTest_StandardStudy();

            exp.experimentName_ = "MutationTest_StandardStudy";

            String[] algorithmNameList = new String[MutationTest_Settings.ALGORITHMS.length];

            for (int i = 0; i < MutationTest_Settings.ALGORITHMS.length; i++) {
                algorithmNameList[i] = MutationTest_Settings.ALGORITHMS[i].toString();
            }
            exp.algorithmNameList_ = algorithmNameList;

            String[] problemList = new String[MutationTest_Settings.INSTANCES.length];

            for (int i = 0; i < MutationTest_Settings.INSTANCES.length; i++) {
                problemList[i] = getInstanceName(MutationTest_Settings.INSTANCES[i]);
            }

            exp.problemList_ = problemList;

            exp.paretoFrontFile_ = new String[18]; // Space allocation for 18 fronts

            exp.indicatorList_ = new String[]{"HV", "SPREAD", "EPSILON"};

            int numberOfAlgorithms = exp.algorithmNameList_.length;

            exp.experimentBaseDirectory_ = System.getProperty("user.dir") + "/" + exp.experimentName_ + "/" + parameters_[parameterPosition].get("experimentCompositName").toString();
            exp.paretoFrontDirectory_ = ""; // This directory must be empty

            exp.algorithmSettings_ = new Settings[numberOfAlgorithms];

            exp.independentRuns_ = MutationTest_Settings.EXECUTIONS;

            exp.initExperiment();

            // Run the experiments
            int numberOfThreads;
            exp.runExperiment(numberOfThreads = 4);
            exp.generateQualityIndicators();

            // Generate latex tables
            exp.generateLatexTables();

            /*
             // Configure the R scripts to be generated
             int rows;
             int columns;
             String prefix;
             String[] problems;
             boolean notch;

             // Configure scripts for DTLZ
             rows = 3;
             columns = 3;
             prefix = new String("DTLZ");
             problems = new String[]{"DTLZ1", "DTLZ2", "DTLZ3", "DTLZ4", "DTLZ5",
             "DTLZ6", "DTLZ7"};

             exp.generateRBoxplotScripts(rows, columns, problems, prefix, notch = false, exp);
             exp.generateRWilcoxonScripts(problems, prefix, exp);

             // Applying Friedman test
             Friedman test = new Friedman(exp);
             test.executeTest("EPSILON");
             test.executeTest("HV");
             test.executeTest("SPREAD");
             */
        }
    } // main

    public static void setParameters() {
        int size = MutationTest_Settings.INSTANCES.length
                * MutationTest_Settings.ALGORITHMS.length
                * MutationTest_Settings.POPULATION_SIZE.length
                * MutationTest_Settings.GENERATIONS.length
                * MutationTest_Settings.CROSSOVER_PROBABILITY.length
                * MutationTest_Settings.MUTATION_PROBABILITY.length
                * MutationTest_Settings.CROSSOVER_OPERATORS.length
                * MutationTest_Settings.MUTATION_OPERATORS.length
                * MutationTest_Settings.SELECTION_OPERATORS.length;

        System.out.println("Size: " + size);
        parameters_ = new HashMap[size];
        int executions = MutationTest_Settings.EXECUTIONS;
        int i = 0;

        for (final String instance : MutationTest_Settings.INSTANCES) {
            for (final int populationSize : MutationTest_Settings.POPULATION_SIZE) {
                for (final int generations : MutationTest_Settings.GENERATIONS) {
                    for (final double crossoverProbability : MutationTest_Settings.CROSSOVER_PROBABILITY) {
                        for (final double mutationProbability : MutationTest_Settings.MUTATION_PROBABILITY) {
                            for (final String crossoverOperator : MutationTest_Settings.CROSSOVER_OPERATORS) {
                                for (final String mutationOperator : MutationTest_Settings.MUTATION_OPERATORS) {
                                    for (final String selectionOperator : MutationTest_Settings.SELECTION_OPERATORS) {
                                        parameters_[i] = new HashMap();
                                        // Algorithm params
                                        parameters_[i].put("populationSize", populationSize);
                                        parameters_[i].put("maxEvaluations", populationSize * generations);

                                        // Paramter for MOEA/D and MOEA/DD
                                        parameters_[i].put("dataDirectory", System.getProperty("user.dir") + "/src/weight");

                                        // Crossover operator
                                        parameters_[i].put("crossoverProbability", crossoverProbability);
                                        parameters_[i].put("crossoverDistributionIndex", 30.0);
                                        parameters_[i].put("crossoverOperator", crossoverOperator);

                                        // Mutation operator
                                        parameters_[i].put("mutationProbability", mutationProbability);
                                        parameters_[i].put("mutationDistributionIndex", 20.0);
                                        parameters_[i].put("mutationOperator", mutationOperator);

                                        // Selection Operator 
                                        parameters_[i].put("selectionOperator", selectionOperator);

                                        parameters_[i].put("instance", instance);
                                        parameters_[i].put("experimentCompositName", String.format("%s_%s_%s_%s_%s_%s_%s_%s", populationSize, generations, crossoverProbability, mutationProbability, crossoverOperator, mutationOperator, selectionOperator, executions));

                                        i++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static String getInstanceName(String path) {
        int end = path.indexOf(".txt");
        return path.substring(10, end);
    }
}
