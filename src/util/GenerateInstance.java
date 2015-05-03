package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import jmetal.util.PseudoRandom;

/**
 * Generate a instance file which will be used on Mutation Test Problem
 * 
 * @author Thiago Nascimento
 * @since 2015-05-03
 * @version 1.0
 */
public class GenerateInstance {

	protected int numberOfTestSuite;

	protected int numberOfMutants;
	
	protected double probabilityOfKill;

	protected String filename;
	
	private String separator;

	/**
	 * Constructor
	 * 
	 * @param filename The filename which will be used to save the instance
	 */
	public GenerateInstance(String filename) {
		this.filename = filename;
		this.probabilityOfKill = 0.5;
		this.numberOfMutants = 5;
		this.numberOfTestSuite = 5;
		this.setSeparator(" ");
	}

	public int getNumberOfTestSuite() {
		return numberOfTestSuite;
	}

	public void setNumberOfTestSuite(int numberOfTestSuite) {
		if (numberOfTestSuite <= 0) {
			throw new IllegalArgumentException("The number of test suite should be more than 0");
		}
		this.numberOfTestSuite = numberOfTestSuite;
	}

	public int getNumberOfMutants() {
		return numberOfMutants;
	}

	public void setNumberOfMutants(int numberOfMutants) {
		if (numberOfMutants <= 0) {
			throw new IllegalArgumentException("The number of mutants should be more than 0");
		}
		this.numberOfMutants = numberOfMutants;
	}

	public double getProbabilityOfKill() {
		return probabilityOfKill;
	}

	public void setProbabilityOfKill(double probabilityOfKill) {
		if (probabilityOfKill < 0.0 || probabilityOfKill > 1.0) {
			throw new IllegalArgumentException("The probability of kill should be between 0 and 1");
		}
		this.probabilityOfKill = probabilityOfKill;
	}

	public String getSeparator() {
		return separator;
	}

	public void setSeparator(String separator) {
		if (separator == null) {
			throw new IllegalArgumentException("The separator cannot be null");
		}
		this.separator = separator;
	}
	
	/**
	 * Generate the instance file
	 */
	public void generate() {
		System.out.println("Generating...");
		
		BufferedWriter output = null;

		try {
			// Create the file
			File file = new File(filename);
			output = new BufferedWriter(new FileWriter(file));

			// Generate header informations
			output.write(numberOfTestSuite + "\n");
			output.write(numberOfMutants + "\n");

			// Generate the coverage matrix
			for (int i = 0; i < numberOfMutants; i++) {
				for (int j = 0; j < numberOfTestSuite; j++) {
					if (PseudoRandom.randDouble() <= probabilityOfKill) {
						output.write("1");
					} else {
						output.write("0");
					}
					if (j + 1 != numberOfTestSuite) {
						output.write(getSeparator());
					}
				}
				output.write("\n");
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				output.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}

		System.out.println("Done!");
	}
}
