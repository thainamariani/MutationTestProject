package experiment;

import util.GenerateInstance;

public class GenerateInstanceTest {

	public static void main(String[] args){
		GenerateInstance gen = new GenerateInstance("instance.txt");
		
		// Settings
		gen.setNumberOfTestSuite(10);
		gen.setNumberOfMutants(5);
		gen.setProbabilityOfKill(0.5);
				
		// Run
		gen.generate();
	}
}
