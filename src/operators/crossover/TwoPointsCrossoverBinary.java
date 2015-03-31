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

import jmetal.operators.crossover.*;
import jmetal.core.Solution;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;

/**
 * This class allows to apply a two points crossover operator using two parent
 * solutions. NOTE: the type of the solutions must be Binary..
 */
public class TwoPointsCrossoverBinary extends Crossover {

    /**
     * Valid solution types to apply this operator
     */
    private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class);

    private Double crossoverProbability_ = null;

    /**
     * Constructor Creates a new intance of the two point crossover operator
     */
    public TwoPointsCrossoverBinary(HashMap<String, Object> parameters) {
        super(parameters);

        if (parameters.get("probability") != null) {
            crossoverProbability_ = (Double) parameters.get("probability");
        }
    } // TwoPointsCrossover

    /**
     * Constructor
     *
     * @param A properties containing the Operator parameters Creates a new
     * intance of the two point crossover operator
     */
    //public TwoPointsCrossover(Properties properties) {
    //	this();
    //}
    /**
     * Perform the crossover operation
     *
     * @param probability Crossover probability
     * @param parent1 The first parent
     * @param parent2 The second parent
     * @return Two offspring solutions
     * @throws JMException
     */
    public Solution[] doCrossover(double probability, Solution parent1, Solution parent2) throws JMException {

        Solution[] offspring = new Solution[2];

        //copy parents solutions into offsprings
        offspring[0] = new Solution(parent1);
        offspring[1] = new Solution(parent2);

        if (parent1.getType().getClass() == BinarySolutionType.class) {
            if (PseudoRandom.randDouble() < probability) {
                int numberOfBits = ((Binary) parent1.getDecisionVariables()[0]).getNumberOfBits();

                // STEP 1: Get two cutting points
                //initial point
                int crosspoint1 = PseudoRandom.randInt(1, numberOfBits - 2);
                //final point
                int crosspoint2 = PseudoRandom.randInt(1, numberOfBits - 2);

                while (crosspoint2 == crosspoint1) {
                    crosspoint2 = PseudoRandom.randInt(1, numberOfBits - 2);
                }
                offspring = createOffsprings(parent1, parent2, crosspoint1, crosspoint2, offspring);
            }
        } else {
            Configuration.logger_.severe("TwoPointsCrossoverBinary.doCrossover: invalid "
                    + "type"
                    + parent1.getDecisionVariables()[0].getVariableType());
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doCrossover()");
        }
        
        return offspring;
    }

    public Solution[] createOffsprings(Solution parent1, Solution parent2, int crosspoint1, int crosspoint2, Solution[] offspring) {

        if (crosspoint1 > crosspoint2) {
            int swap;
            swap = crosspoint1;
            crosspoint1 = crosspoint2;
            crosspoint2 = swap;
        }
        // STEP 2: Obtain the first child
        for (int i = crosspoint1; i <= crosspoint2; i++) {
            boolean value = ((Binary) parent2.getDecisionVariables()[0]).getIth(i);
            ((Binary) offspring[0].getDecisionVariables()[0]).setIth(i, value);
        }
        // STEP 3: Obtain the second child
        for (int i = crosspoint1; i <= crosspoint2; i++) {
            boolean value = ((Binary) parent1.getDecisionVariables()[0]).getIth(i);
            ((Binary) offspring[1].getDecisionVariables()[0]).setIth(i, value);
        }
        return offspring;
    }

    /**
     * Executes the operation
     *
     * @param object An object containing an array of two solutions
     * @return An object containing an array with the offSprings
     * @throws JMException
     */
    public Object execute(Object object) throws JMException {
        Solution[] parents = (Solution[]) object;
        Double crossoverProbability;

        if (!(VALID_TYPES.contains(parents[0].getType().getClass())
                && VALID_TYPES.contains(parents[1].getType().getClass()))) {

            Configuration.logger_.severe("TwoPointsCrossoverBinary.execute: the solutions "
                    + "are not of the right type. The type should be 'Binary', but "
                    + parents[0].getType() + " and "
                    + parents[1].getType() + " are obtained");
        }

        crossoverProbability = (Double) getParameter("probability");

        if (parents.length < 2) {
            Configuration.logger_.severe("TwoPointsCrossoverBinary.execute: operator needs two "
                    + "parents");
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        Solution[] offspring = doCrossover(crossoverProbability_,
                parents[0],
                parents[1]);

        return offspring;
    }

}
