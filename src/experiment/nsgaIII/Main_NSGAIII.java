package experiment.nsgaIII;

import java.util.HashMap;
import java.util.List;

import jmetal.operators.crossover.SinglePointCrossover;
import jmetal.operators.crossover.TwoPointsCrossover;
import operators.crossover.TwoPointsCrossoverBinary;
import operators.crossover.UniformCrossoverBinary;
import operators.crossover.UniformCrossoverBinary4NSGAIII;
import operators.mutation.SwapMutationBinary4NSGAIII;
import operators.selection.BinaryTournament24NSGAIII;

import org.uma.jmetal.algorithm.Algorithm;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIII;
import org.uma.jmetal.algorithm.multiobjective.nsgaiii.NSGAIIIBuilder;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.operator.MutationOperator;
import org.uma.jmetal.operator.SelectionOperator;
import org.uma.jmetal.operator.impl.crossover.SBXCrossover;
import org.uma.jmetal.operator.impl.mutation.BitFlipMutation;
import org.uma.jmetal.operator.impl.mutation.PolynomialMutation;
import org.uma.jmetal.operator.impl.selection.BinaryTournamentSelection;
import org.uma.jmetal.problem.Problem;
import org.uma.jmetal.solution.Solution;
import org.uma.jmetal.util.AlgorithmRunner;
import org.uma.jmetal.util.JMetalException;
import org.uma.jmetal.util.JMetalLogger;
import org.uma.jmetal.util.ProblemUtils;
import org.uma.jmetal.util.fileoutput.SolutionSetOutput;
import org.uma.jmetal.util.fileoutput.impl.DefaultFileOutputContext;

import problem.MutationTestProblem;
import problem.MutationTestProblem4NSGAIII;

public class Main_NSGAIII {

	public static void main(String[] args) throws JMetalException {
	    Problem problem;
	    Algorithm algorithm;
	    CrossoverOperator crossover;
	    MutationOperator mutation;
	    SelectionOperator selection;

	    //String problemName = "org.uma.jmetal.problem.multiobjective.dtlz.DTLZ3" ;

	    //problem = ProblemUtils.loadProblem(problemName);
	    
	    problem = new MutationTestProblem4NSGAIII("instances/guizzo_cas.txt",1);

	    double crossoverProbability = 0.8 ;
	    double crossoverDistributionIndex = 20.0 ;
//	    crossover = new SBXCrossover(crossoverProbability, crossoverDistributionIndex) ;
//	    crossover = new org.uma.jmetal.operator.impl.crossover.SinglePointCrossover(crossoverProbability) ;
	    HashMap param = new HashMap();
	    param.put("probability", 0.8);
	    crossover = new UniformCrossoverBinary4NSGAIII(param);

//	    double mutationProbability = 1.0 / problem.getNumberOfVariables() ;
	    double mutationProbability = 0.005 ;
	    double mutationDistributionIndex = 20.0 ;
	    mutation = new SwapMutationBinary4NSGAIII(mutationProbability) ;

	    selection = new BinaryTournament24NSGAIII();

	    algorithm = new NSGAIIIBuilder(problem)
	            .setCrossoverOperator(crossover)
	            .setMutationOperator(mutation)
	            .setSelectionOperator(selection)
	            .setPopulationSize(100)
	            .setMaxEvaluations(100000)
	            .setDivisions(12)
	            .build() ;

	    AlgorithmRunner algorithmRunner = new AlgorithmRunner.Executor(algorithm)
	            .execute() ;

	    List<Solution> population = ((NSGAIII)algorithm).getResult() ;
	    long computingTime = algorithmRunner.getComputingTime() ;

	    new SolutionSetOutput.Printer(population)
	            .setSeparator("\t")
	            .setVarFileOutputContext(new DefaultFileOutputContext("VAR"))
	            .setFunFileOutputContext(new DefaultFileOutputContext("FUN"))
	            .print();

	    JMetalLogger.logger.info("Total execution time: " + computingTime + "ms");
	    JMetalLogger.logger.info("Objectives values have been written to file FUN.tsv");
	    JMetalLogger.logger.info("Variables values have been written to file VAR.tsv");

	  }
}
