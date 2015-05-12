package util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import metric.JaccardCoefficient;
import problem.MutationTestProblem;

public class GenerateJaccardMetric {

	public static void main(String[] args){
		// Open all files in instances folder and generate the jaccard index for
		// all
		File folder = new File("instances/");
		
		for (final File file : folder.listFiles()) {
			generateJaccardCoefficient(file.getName());
		}
	}
	
	public static void generateJaccardCoefficient(String filename){
		// Replace the output by file		
		try {
			PrintStream out = new PrintStream(new FileOutputStream("jaccard/"+filename+"_console"));
			System.setOut(out);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
				
		JaccardCoefficient coe = new JaccardCoefficient();
		
		MutationTestProblem p = new MutationTestProblem("instances/"+filename, 1);
		
		double sum = 0.0;
		double count = 0.0;
		
		double[][] result = new double[p.getNumberOfTestSuite()][p.getNumberOfTestSuite()];
		
		System.out.println("i\tj\tsimilarity");
		
		for(int i=0;i<p.getNumberOfTestSuite();i++){
			for(int j=i;j<p.getNumberOfTestSuite();j++){
				if(i !=j){
					List<String> one = new ArrayList<String>();
					List<String> two = new ArrayList<String>();
					
					for (int h = 0; h < p.getNumberOfMutants(); h++) {
						if (p.isKilled(i, h)) {
							one.add(String.valueOf(h));
						}
						if (p.isKilled(j, h)) {
							two.add(String.valueOf(h));
						}
					}
					double sim = coe.similarity(one, two);
					sum += sim;
					count++;
					result[i][j] = sim;
					System.out.println(i+"\t"+j+"\t"+sim);
				}
			}
		}	
		
		System.out.println("Test Case="+p.getNumberOfTestSuite());
		System.out.println("Mutant="+p.getNumberOfMutants());
		System.out.println("Sum=" + sum);
		System.out.println("Count=" + count);
		System.out.println("Average=" + sum / count);
	}
}
