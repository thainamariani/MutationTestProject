package problem;

import org.moeaframework.core.Solution;
import org.moeaframework.core.variable.BinaryVariable;
import org.moeaframework.problem.AbstractProblem;

import util.InstanceReader;

public class MutationTestProblem4MOEA extends AbstractProblem {

	private int[][] coverage;

	private int numberOfTestSuite;

	private int numberOfMutants;

	public MutationTestProblem4MOEA(String filename) {
		super(1, 2, 1);
		// Read Instance's file
		InstanceReader reader = new InstanceReader(filename);

		reader.open();
		this.numberOfTestSuite = reader.readInt();
		this.numberOfMutants = reader.readInt();
		this.coverage = reader.readIntMatrix(numberOfMutants, numberOfTestSuite, " ");
		reader.close();
	}

	public MutationTestProblem4MOEA(int numberOfTestSuite, int numberOfMutants, int[][] coverage) {
		super(1, 2, 1);
		this.coverage = coverage;
		this.numberOfMutants = numberOfMutants;
		this.numberOfTestSuite = numberOfTestSuite;
	}

	@Override
	public void evaluate(Solution solution) {
		BinaryVariable binary = (BinaryVariable)solution.getVariable(0);
		
		double mutationScore = getMutantionScore(binary);
		double numberOfSelectedTestSuite = getNumberOfSelectedTestSuite(binary);
		
		solution.setObjective(0, mutationScore * -1.0);
		solution.setObjective(1, numberOfSelectedTestSuite);
		
		//Constraints
		int sum = 0;

		for (int i = 0; i < numberOfTestSuite; i++) {
			if (binary.get(i)) {
				sum++;
			}
		}

		if (sum == 0) {
			solution.setConstraints(new double[] { 1.0 });
		} else {
			solution.setConstraints(new double[] { 0.0 });
		}		
	}

	public double getMutantionScore(BinaryVariable binary) {
		int deadMutants = getNumberOfDifferentKilledMutants(binary); // dm(p,t)
		double totalMutants = numberOfMutants; // m(p)
		return (double) deadMutants / (double) totalMutants; // ms(p,t)
	}

	public int getNumberOfSelectedTestSuite(BinaryVariable solution) {
		if (solution == null) {
			throw new IllegalArgumentException("Solution cannot be null");
		}

		int total = 0;

		for (int i = 0; i < solution.getNumberOfBits(); i++) {
			if (solution.get(i)) {
				total++;
			}
		}

		return total;
	}

	public int getNumberOfKilledMutants(int idTestSuite) {
		if (idTestSuite >= numberOfTestSuite) {
			throw new IllegalArgumentException(
					"Test Suite'id should be >= number of test suite");
		}

		int total = 0;

		for (int i = 0; i < numberOfMutants; i++) {
			if (coverage[i][idTestSuite] == 1) {
				total++;
			}
		}

		return total;
	}

	public int getNumberOfDifferentKilledMutants(BinaryVariable solution) {
		if (solution == null) {
			throw new IllegalArgumentException("Solution cannot be null");
		}

		int[] visited = new int[numberOfMutants];
		int total = 0;

		for (int i = 0; i < solution.getNumberOfBits(); i++) {
			if (solution.get(i)) {
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

	@Override
	public Solution newSolution() {
		Solution solution = new Solution(1, 2, 1);
		solution.setVariable(0, new BinaryVariable(numberOfTestSuite));
		return solution;
	}
}
