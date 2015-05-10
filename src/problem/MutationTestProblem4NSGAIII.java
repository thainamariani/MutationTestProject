package problem;

import java.util.BitSet;

import jmetal.encodings.variable.Binary;
import jmetal.util.PseudoRandom;
import org.uma.jmetal.problem.ConstrainedProblem;
import org.uma.jmetal.problem.impl.AbstractBinaryProblem;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.util.JMetalException;



import util.InstanceReader;

public class MutationTestProblem4NSGAIII extends AbstractBinaryProblem implements ConstrainedProblem<BinarySolution> {

	private static final long serialVersionUID = 7488414018042022225L;
	
	private int[][] coverage;

    private int numberOfTestSuite;

    private int numberOfMutants;
	
	public MutationTestProblem4NSGAIII(String filename, int fitnessFunction) {
		// Read Instance's file
        InstanceReader reader = new InstanceReader(filename);

        reader.open();
        this.numberOfTestSuite = reader.readInt();
        this.numberOfMutants = reader.readInt();
        this.coverage = reader.readIntMatrix(numberOfMutants, numberOfTestSuite, " ");       
        reader.close();
        
        setNumberOfVariables(1);
        setNumberOfObjectives(2);
        setName("MutationTestProblem4NSGAIII");
	}
        
        public MutationTestProblem4NSGAIII(int numberOfTestSuite, int numberOfMutants, int[][] coverage) {
        this.coverage = coverage;
        this.numberOfMutants = numberOfMutants;
        this.numberOfTestSuite = numberOfTestSuite;

        setNumberOfVariables(1);
        setNumberOfObjectives(2);
        setName("MutationTestProblem4NSGAIII");
    }

	@Override
	public void evaluate(BinarySolution solution) {
		// TODO Auto-generated method stub
		
		//Binary s = (Binary) solution.getDecisionVariables()[0];
		BitSet bitset = solution.getVariableValue(0) ;
		
		Binary s = new Binary(numberOfTestSuite);
		s.bits_ = bitset;
		
		
		double mutationScore = getMutantionScore(s);
        double numberOfSelectedTestSuite = getNumberOfSelectedTestSuite(s);
        
        solution.setObjective(0, mutationScore * -1.0);
        solution.setObjective(1, numberOfSelectedTestSuite);
	}

	@Override
	protected int getBitsPerVariable(int index) {
		if (index != 0) {
	  		throw new JMetalException("Problem MutationTestProblem4NSGAIII has only a variable. Index = " + index) ;
	  	}
	  	return numberOfTestSuite ;
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

		@Override
		public void evaluateConstraints(BinarySolution solution) {
			BitSet bitset = solution.getVariableValue(0) ;
			
			Binary binary = new Binary(numberOfTestSuite);
			binary.bits_ = bitset;
			
	        int numberOfSelectedTestSuite = getNumberOfSelectedTestSuite(binary);
	        if (numberOfSelectedTestSuite == 0) {
	            int random = PseudoRandom.randInt(0, binary.getNumberOfBits() - 1);
	            binary.setIth(random, true);
	            evaluate(solution);
	        }
			
		}
}
