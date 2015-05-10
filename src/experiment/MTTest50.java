package experiment;

import java.util.List;
import jmetal.util.JMException;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;


import problem.MutationTestProblem4NSGAIII;
import util.ExperimentUtil;

//experimental class for jmetal 5.0
public class MTTest50 {

    public static void main(String[] args) throws JMetalException, JMException {

        MutationTest_Parameters mutationParameters = ExperimentUtil.verifyParameters(args);

        //print parameters
        mutationParameters.PrintParameters();

        //select problem
        Problem problem = new MutationTestProblem4NSGAIII(mutationParameters.getInstance(), mutationParameters.getFitnessFunction());

        Algorithm algorithm = ExperimentUtil.algorithmBuilder(mutationParameters, problem);

        //NonDominatedSolutionList nonDominatedSolutions = new NonDominatedSolutionList();
        NonDominatedSolutionListArchive nonDominatedSolutions = new NonDominatedSolutionListArchive();
        String path = "";

        /* Execute the Algorithm */
        for (int i = 0; i < mutationParameters.getExecutions(); i++) {
            NonDominatedSolutionListArchive actualNonDominatedSolutions = new NonDominatedSolutionListArchive();

            System.out.println("Run: " + i);
            AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm).execute();
            List<Solution> population = ((NSGAIII) algorithm).getResult();
            long computingTime = algorithmRunner.getComputingTime();
            System.out.println("Total time of execution: " + computingTime);

            /* Log messages */
            path = String.format("experiment/%s/%s/F%s/%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getFitnessFunction(), mutationParameters.getContext());
            String pathFun = String.format("%s/FUN_%s", path, i);
            String pathVar = String.format("%s/VAR_%s", path, i);

            for (Solution solution : population) {
                nonDominatedSolutions.add(solution);
                actualNonDominatedSolutions.add(solution);
            }

            ExperimentUtil.removeRepeated(actualNonDominatedSolutions);
            System.out.println("Variables values have been writen to file " + pathVar);
            System.out.println("Objectives values have been writen to file " + pathFun);
            new SolutionSetOutput.Printer(actualNonDominatedSolutions.getSolutionList())
                    .setSeparator("\t")
                    .setVarFileOutputContext(new DefaultFileOutputContext(pathVar))
                    .setFunFileOutputContext(new DefaultFileOutputContext(pathFun))
                    .print();

        }

        ExperimentUtil.printFinalSolutions(nonDominatedSolutions, mutationParameters);

//        JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
//        JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
//        JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");
    }
}
