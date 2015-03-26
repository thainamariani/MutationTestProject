/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package representation.problem;

import java.util.HashSet;
import java.util.List;
import jmetal.core.Problem;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.util.JMException;
import pojo.Mutant;
import pojo.TestCase;

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

    private final List<TestCase> testCases;
    private final List<Mutant> mutants;

    public MutationTestProblem(List<TestCase> testCases, List<Mutant> mutants) {
        this.testCases = testCases;
        this.mutants = mutants;
        numberOfObjectives_ = 1;
        numberOfConstraints_ = 0;
        numberOfVariables_ = testCases.size();
        problemName_ = "Mutant Test Problem";
        solutionType_ = new BinarySolutionType(this);
    }

    public void evaluate(Solution solution) throws JMException {
        //variable = vector positions
        Binary variable = (Binary) solution.getDecisionVariables()[0];
        HashSet<Mutant> killedMutants = new HashSet<>();
        int numberOfSelectedTestCases = 0;

        for (int i = 0; i < variable.getNumberOfBits(); i++) {
            boolean isInTestCase = variable.getIth(i);
            if (isInTestCase) {
                numberOfSelectedTestCases++;
                TestCase testCase = testCases.get(i);
                killedMutants.addAll(testCase.getKilledMutants());
            }
        }

        //mutation score calculation
        double deadMutants = killedMutants.size(); //dm(p,t)
        double totalMutants = mutants.size(); //m(p)
        double mutationScore = deadMutants / totalMutants; //ms(p,t)

        //fitness function calculation
        double result = fitnessFunction(mutationScore, numberOfSelectedTestCases);
        //double result = fitnessFunction(1, 1, mutationScore, numberOfTestSuites, testSuites.size());
        solution.setObjective(0, result);
    }

    public double fitnessFunction(double mutationScore, double numberOfSelectedTestCases) {
        return mutationScore / numberOfSelectedTestCases;
    }

    public double fitnessFunction(double alfa, double beta, double mutationScore, double numberOfSelectedTestCases, double numberOfTestCases) {
        double minimizationScore = ((1 / numberOfTestCases) * numberOfSelectedTestCases);
        return (alfa * mutationScore) + (beta * minimizationScore);
    }
}
