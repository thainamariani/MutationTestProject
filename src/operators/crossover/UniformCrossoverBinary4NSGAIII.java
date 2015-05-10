//  TwoPointsCrossover.java
//
//  Author:
//       Antonio J. Nebro <antonio@lcc.uma.es>
//       Juan J. Durillo <durillo@lcc.uma.es>
//
//  Copyright (c) 2011 Antonio J. Nebro, Juan J. Durillo
//
//  This program is free software: you can redistribute it and/or modify
//  it under the terms of the GNU Lesser General Public License as published by
//  the Free Software Foundation, either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU Lesser General Public License for more details.
// 
//  You should have received a copy of the GNU Lesser General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
package operators.crossover;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import org.uma.jmetal.operator.CrossoverOperator;
import org.uma.jmetal.solution.BinarySolution;
import org.uma.jmetal.solution.impl.GenericBinarySolution;

/**
 * This class allows to apply a two points crossover operator using two parent
 * solutions. NOTE: the type of the solutions must be Binary..
 */
public class UniformCrossoverBinary4NSGAIII implements CrossoverOperator<List<BinarySolution>, List<BinarySolution>>{

	private static final long serialVersionUID = -7026629145234446379L;
	
	protected UniformCrossoverBinary crossover;
	
	protected double crossoverProbability_;
	
	public UniformCrossoverBinary4NSGAIII(HashMap<String, Object> parameters) {
		crossover = new UniformCrossoverBinary(parameters);
	
		if (parameters.get("probability") != null) {
            crossoverProbability_ = (Double) parameters.get("probability");
        }
	}
	
	 /**
     * Constructor
     *
     * @param A properties containing the Operator parameters Creates a new
     * intance of the uniform crossover operator
     */
    /**
     * Perform the crossover operation
     *
     * @param probability Crossover probability
     * @param parent1 The first parent
     * @param parent2 The second parent
     * @return Two offspring solutions
     * @throws JMException
     */
    public List<BinarySolution> doCrossover(double probability, BinarySolution parent1, BinarySolution parent2) throws JMException {

    	BinarySolution[] offspring = new BinarySolution[2];

        //copy parents solutions into offsprings
        offspring[0] = (BinarySolution) parent1.copy();
        offspring[1] = (BinarySolution) parent2.copy();

        if (parent1.getClass() == GenericBinarySolution.class) {
            if (PseudoRandom.randDouble() < probability) {
                createOffsprings(parent1, parent2, offspring);
            }
        } else {
            Configuration.logger_.severe("UniformCrossoverBinary.doCrossover: invalid "
                    + "type"
                    + parent1.getVariableValue(0).toString());
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doCrossover()");
        }

        List<BinarySolution> result = new ArrayList<BinarySolution>();
        
        result.add(offspring[0]);
        result.add(offspring[1]);
        
        return result;
    }

    private void createOffsprings(BinarySolution parent1, BinarySolution parent2, BinarySolution[] offspring) {
//        int numberOfBits = ((Binary) parent1.getDecisionVariables()[0]).getNumberOfBits();
    	int numberOfBits = parent1.getVariableValue(0).getBinarySetLength();

        for (int i = 0; i < numberOfBits; i++) {
        	boolean geneParent1 = parent1.getVariableValue(0).get(i);
        	boolean geneParent2 = parent2.getVariableValue(0).get(i);
//            boolean geneParent1 = ((Binary) parent1.getDecisionVariables()[0]).getIth(i);
//            boolean geneParent2 = ((Binary) parent2.getDecisionVariables()[0]).getIth(i);
            int random = PseudoRandom.randInt(0, 1);
            insertValues(random, offspring, i, geneParent2, geneParent1);
        }
    }

    public void insertValues(int random, BinarySolution[] offspring, int i, boolean geneParent2, boolean geneParent1) {
        if (random == 1.0) {
//            ((Binary) offspring[0].getDecisionVariables()[0]).setIth(i, geneParent2);
//            ((Binary) offspring[1].getDecisionVariables()[0]).setIth(i, geneParent1);
        	offspring[0].getVariableValue(0).set(i, geneParent2);
        	offspring[1].getVariableValue(0).set(i, geneParent1);
        }
    }

	@Override
	public List<BinarySolution> execute(List<BinarySolution> solution) {
		if (solution.size() < 2) {
            Configuration.logger_.severe("UniformCrossoverBinary.execute: operator needs two " + "parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            System.err.println("Exception in " + name + ".execute()");
        }
		
		List<BinarySolution> offspring = null;
	
		try {
			offspring = doCrossover(crossoverProbability_, solution.get(0), solution.get(1));
		} catch (JMException e) {
			e.printStackTrace();
		}
				
		return offspring;
	}
}
