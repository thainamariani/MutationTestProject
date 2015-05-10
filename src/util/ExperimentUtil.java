/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import experiment.MutationMetaheuristic;
import experiment.MutationTest_Parameters;
import java.util.HashMap;
import java.util.List;
import jmetal.util.JMException;
import jmetal.util.NonDominatedSolutionList;
import operators.crossover.UniformCrossoverBinary4NSGAIII;
import operators.mutation.SwapMutationBinary4NSGAIII;
import operators.selection.BinaryTournament24NSGAIII;
import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.archive.impl.NonDominatedSolutionListArchive;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

/**
 *
 * @author thaina
 */
public class ExperimentUtil {

    public static MutationTest_Parameters verifyParameters(String[] args) {

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

        //tweaks
        if (args[13] != null && !args[13].trim().equals("")) {
            mutationParameters.setTweaks(Integer.valueOf(args[13]));
        }
        return mutationParameters;
    }

    public static String getInstanceName(String path) {
        int end = path.indexOf(".txt");
        return path.substring(10, end);
    }

    //for jmetal 4.5
    public static void removeRepeated(NonDominatedSolutionList nonDominatedSolutions) {
        for (int i = 0; i < nonDominatedSolutions.size() - 1; i++) {
            String solucao = nonDominatedSolutions.get(i).getDecisionVariables()[0].toString();
            for (int j = i + 1; j < nonDominatedSolutions.size(); j++) {
                String solucaoB = nonDominatedSolutions.get(j).getDecisionVariables()[0].toString();
                if (solucao.equals(solucaoB)) {
                    nonDominatedSolutions.remove(j);
                    j--;
                }
            }
        }
    }

    //for jmetal 5.0
    public static void removeRepeated(NonDominatedSolutionListArchive nonDominatedSolutions) {
        List<Solution> listSolutions = nonDominatedSolutions.getSolutionList();
        for (int i = 0; i < listSolutions.size() - 1; i++) {
            String solucao = listSolutions.get(i).getVariableValue(0).toString();
            for (int j = i + 1; j < listSolutions.size(); j++) {
                String solucaoB = listSolutions.get(j).getVariableValue(0).toString();
                if (solucao.equals(solucaoB)) {
                    listSolutions.remove(j);
                    j--;
                }
            }
        }
    }

    public static Algorithm algorithmBuilder(MutationTest_Parameters mutationParameters, Problem problem) throws JMException {

        Algorithm algorithm = null;

        CrossoverOperator crossover = getCrossoverOperator(mutationParameters);
        System.out.println("CrossoverOperator: " + crossover);
        
        MutationOperator mutation = getMutationOperator(mutationParameters);
        System.out.println("mutationOperator:" + mutation);
        
        SelectionOperator selection = getSelectionOperator(mutationParameters);
        System.out.println("selectionOperator:" + selection);
        
        int maxEvaluations = mutationParameters.getPopulationSize() * mutationParameters.getGenerations();

        if (mutationParameters.getAlgo().name().equalsIgnoreCase("NSGAIII")) {
            algorithm = new NSGAIIIBuilder(problem)
                    .setCrossoverOperator(crossover)
                    .setMutationOperator(mutation)
                    .setSelectionOperator(selection)
                    .setPopulationSize(mutationParameters.getPopulationSize())
                    .setMaxEvaluations(maxEvaluations)
                    .setDivisions(12)
                    .build();
        }
        return algorithm;
    }

    public static SelectionOperator getSelectionOperator(MutationTest_Parameters mutationParameters) throws JMException {
        SelectionOperator selection;
        selection = selectSelectionOperator(mutationParameters);
        return selection;
    }

    public static MutationOperator getMutationOperator(MutationTest_Parameters mutationParameters) throws JMException {
        MutationOperator mutation;
        mutation = selectMutationOperator(mutationParameters);
        return mutation;
    }

    public static CrossoverOperator getCrossoverOperator(MutationTest_Parameters mutationParameters) throws JMException {
        CrossoverOperator crossover;
        HashMap parameters = new HashMap();
        parameters.put("probability", mutationParameters.getCrossoverProbability());
        crossover = selectCrossoverOperator(parameters, mutationParameters);
        return crossover;
    }

    public static void printFinalSolutions(NonDominatedSolutionList nonDominatedSolutions, MutationTest_Parameters mutationParameters) {
        String path;
        ExperimentUtil.removeRepeated(nonDominatedSolutions);
        path = String.format("experiment/%s/%s/F%s/%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getFitnessFunction(), mutationParameters.getContext());
        String pathFunAll = path + "/FUN_All";
        String pathVarAll = path + "/VAR_All";
        nonDominatedSolutions.printObjectivesToFile(pathFunAll);
        nonDominatedSolutions.printVariablesToFile(pathVarAll);
    }

    public static void printFinalSolutions(NonDominatedSolutionListArchive nonDominatedSolutions, MutationTest_Parameters mutationParameters) {
        String path;
        ExperimentUtil.removeRepeated(nonDominatedSolutions);
        path = String.format("experiment/%s/%s/F%s/%s", ExperimentUtil.getInstanceName(mutationParameters.getInstance()), mutationParameters.getAlgo(), mutationParameters.getFitnessFunction(), mutationParameters.getContext());
        String pathFunAll = path + "/FUN_All";
        String pathVarAll = path + "/VAR_All";
        new SolutionSetOutput.Printer(nonDominatedSolutions.getSolutionList())
                .setSeparator("\t")
                .setVarFileOutputContext(new DefaultFileOutputContext(pathVarAll))
                .setFunFileOutputContext(new DefaultFileOutputContext(pathFunAll))
                .print();
    }

    public static MutationOperator selectMutationOperator(MutationTest_Parameters mutationParameters) {
        if (mutationParameters.getMutationOperator().equals("SwapMutationBinary4NSGAIII")) {
            return new SwapMutationBinary4NSGAIII(mutationParameters.getMutationProbability());
        }
        return null;
    }

    public static CrossoverOperator selectCrossoverOperator(HashMap parameters, MutationTest_Parameters mutationParameters) {
        if (mutationParameters.getCrossoverOperator().equals("UniformCrossoverBinary4NSGAIII")) {
            return new UniformCrossoverBinary4NSGAIII(parameters);
        }
        return null;
    }

    public static SelectionOperator selectSelectionOperator(MutationTest_Parameters mutationParameters) {
        if (mutationParameters.getSelectionOperator().equals("BinaryTournament24NSGAIII")) {
            return new BinaryTournament24NSGAIII();
        }
        return null;
    }

}
