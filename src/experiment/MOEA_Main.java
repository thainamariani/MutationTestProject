package experiment;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jmetal.util.Configuration;

import org.moeaframework.Executor;
import org.moeaframework.core.NondominatedPopulation;
import org.moeaframework.core.Solution;

import problem.MutationTestProblem4MOEA;

public class MOEA_Main {
	
	public static void main(String[] args) {
		// solve using NSGA-II
		NondominatedPopulation result = new Executor()
				.withProblemClass(MutationTestProblem4MOEA.class, "instances/guizzo_cas.txt")
				.withAlgorithm("NSGAIII")
				.withMaxEvaluations(2000000)
				.withProperty("populationSize", 200)
				.withProperty("operator", "hux+bf")
				.withProperty("ux.rate", "0.9")
				.withProperty("swap.rate", "0.1")
                                .withProperty("divisions", "12")
				.distributeOnAllCores()
				.run();
		
		printObjectivesToFile(result, "FUN");
		printVariablesToFile(result, "VAR");		
	}
	
	public static void printObjectivesToFile(NondominatedPopulation result, String path) {
		try {
			/* Open the file */
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			for (int i = 0; i < result.size(); i++) {
				Solution solution = result.get(i);
				double[] objectives = solution.getObjectives();
				bw.write(String.valueOf(objectives[0]));
				bw.write(" ");
				bw.write(String.valueOf(objectives[1]));
				bw.newLine();
			}

			/* Close the file */
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printObjectivesToFile

	  /**
	   * Writes the decision encodings.variable values of the <code>Solution</code>
	   * solutions objects into the set in a file.
	   * @param path The output file name
	   */
	public static void printVariablesToFile(NondominatedPopulation result, String path) {
		try {
			FileOutputStream fos = new FileOutputStream(path);
			OutputStreamWriter osw = new OutputStreamWriter(fos);
			BufferedWriter bw = new BufferedWriter(osw);

			if (result.size() > 0) {
				int numberOfVariables = result.get(0).getNumberOfVariables();
				for (Solution aSolutionsList_ : result) {
					for (int j = 0; j < numberOfVariables; j++){
						bw.write(aSolutionsList_.getVariable(j).toString() + " ");
					}
					bw.newLine();
				}
			}
			bw.close();
		} catch (IOException e) {
			Configuration.logger_.severe("Error acceding to the file");
			e.printStackTrace();
		}
	} // printVariablesToFile
}
