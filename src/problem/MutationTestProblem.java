/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package problem;

import java.text.DecimalFormat;
import java.util.List;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import pojo.Mutant;
import pojo.TestCase;
import util.InstanceReader;

/**
 *
 * @author thaina
 *
 */
/**
 * Class representing problem MutationTest. The problem consist of, given a set
 * of test suites, maximizing it coverage and minimizing the number of test
 * suites.
 */
public class MutationTestProblem extends Problem {

//    private final List<TestCase> testCases;
//    private final List<Mutant> mutants;
    private int[][] coverage;

    private int numberOfTestSuite;

    private int numberOfMutants;

    private int fitnessFunction;

    public MutationTestProblem(int numberOfTestSuite, int numberOfMutants, int[][] coverage) {
        this.coverage = coverage;
        this.numberOfMutants = numberOfMutants;
        this.numberOfTestSuite = numberOfTestSuite;

        // JMetal's Settings
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        numberOfVariables_ = 1;
        length_ = new int[numberOfVariables_];
        length_[0] = numberOfTestSuite;
        problemName_ = "Mutant Test Problem";
        solutionType_ = new BinarySolutionType(this);
//        this.testCases = null;
//        this.mutants = null;
    }

    public MutationTestProblem(String filename, int fitnessFunction) {
        // Read Instance's file
        InstanceReader reader = new InstanceReader(filename);

        reader.open();
        this.numberOfTestSuite = reader.readInt();
        this.numberOfMutants = reader.readInt();
        this.coverage = reader.readIntMatrix(numberOfMutants, numberOfTestSuite, " ");
        this.fitnessFunction = fitnessFunction;
        reader.close();

        // JMetal's Settings
        numberOfObjectives_ = 2;
        numberOfConstraints_ = 0;
        numberOfVariables_ = 1;
        length_ = new int[numberOfVariables_];
        length_[0] = numberOfTestSuite;
        problemName_ = "Mutant Test Problem";
        solutionType_ = new BinarySolutionType(this);
//        this.testCases = null;
//        this.mutants = null;
    }

//    public MutationTestProblem(List<TestCase> testCases, List<Mutant> mutants) {
////        this.testCases = testCases;
////        this.mutants = mutants;
//        numberOfObjectives_ = 1;
//        numberOfConstraints_ = 0;
//        numberOfVariables_ = testCases.size();
//        problemName_ = "Mutant Test Problem";
//        solutionType_ = new BinarySolutionType(this);
//    }
    @Override
    public void evaluate(Solution solution) throws JMException {
        //variable = vector positions
        Binary s = (Binary) solution.getDecisionVariables()[0];
//        HashSet<Mutant> killedMutants = new HashSet<>();
//        int numberOfSelectedTestCases = 0;
//
//        for (int i = 0; i < variable.getNumberOfBits(); i++) {
//            boolean isInTestCase = variable.getIth(i);
//            if (isInTestCase) {
//                numberOfSelectedTestCases++;
//                TestCase testCase = testCases.get(i);
//                killedMutants.addAll(testCase.getKilledMutants());
//            }
//        }

        //mutation score calculation
        //int deadMutants = getNumberOfDifferentKilledMutants(s); //dm(p,t)
        //double totalMutants = numberOfMutants; //m(p)
        //double mutationScore = (double) deadMutants / (double) totalMutants; //ms(p,t)
        double mutationScore = getMutantionScore(s);
        double numberOfSelectedTestSuite = getNumberOfSelectedTestSuite(s);

        //fitness function calculation
//        double result;
//        if (fitnessFunction == 1) {
//            result = fitnessFunction(mutationScore, numberOfSelectedTestSuite);
//        } else {
//            result = fitnessFunction(1, 1, mutationScore, numberOfSelectedTestSuite, numberOfTestSuite);
//        }
        //multiobjective
        solution.setObjective(0, mutationScore * -1);
        solution.setObjective(1, numberOfSelectedTestSuite);
    }

    @Override
    public void evaluateConstraints(Solution solution) throws JMException {
        Binary binarySolution = (Binary) solution.getDecisionVariables()[0];
        int numberOfSelectedTestSuite = getNumberOfSelectedTestSuite(binarySolution);
        if (numberOfSelectedTestSuite == 0) {
            int random = PseudoRandom.randInt(0, binarySolution.getNumberOfBits() - 1);
            binarySolution.setIth(random, true);
            evaluate(solution);
        }
    }

    public double getMutantionScore(Binary s) {
        int deadMutants = getNumberOfDifferentKilledMutants(s); //dm(p,t)
        double totalMutants = numberOfMutants; //m(p)
        return (double) deadMutants / (double) totalMutants; //ms(p,t)
    }

    public double fitnessFunction(double mutationScore, double numberOfSelectedTestCases) {
        if (numberOfSelectedTestCases == 0) {
            return 0;
        }
        return mutationScore / numberOfSelectedTestCases;
    }

    public double fitnessFunction(double alfa, double beta, double mutationScore, double numberOfSelectedTestCases, double numberOfTestCases) {
        double minimizationScore = ((1.0 / numberOfTestCases) * numberOfSelectedTestCases);
        return (alfa * mutationScore) - (beta * minimizationScore);
    }

    public int getNumberOfSelectedTestSuite(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                total++;
            }
        }

        return total;
    }

    public int getNumberOfKilledMutants(int idTestSuite) {
        if (idTestSuite >= numberOfTestSuite) {
            throw new IllegalArgumentException("Test Suite'id should be >= number of test suite");
        }

        int total = 0;

        for (int i = 0; i < numberOfMutants; i++) {
            if (coverage[i][idTestSuite] == 1) {
                total++;
            }
        }

        return total;
    }

    public int getNumberOfDifferentKilledMutants(Binary solution) {
        if (solution == null) {
            throw new IllegalArgumentException("Solution cannot be null");
        }

        int[] visited = new int[numberOfMutants];
        int total = 0;

        for (int i = 0; i < solution.getNumberOfBits(); i++) {
            if (solution.getIth(i)) {
                // Test Suite was selected by metaheurist
                for (int j = 0; j < numberOfMutants; j++) {
                    if (coverage[j][i] == 1 && visited[j] == 0) {
                        // Test Suit has not yet been visited
                        visited[j] = 1;
                        total++;
                    }
                }
            }
        }

        return total;
    }
}
