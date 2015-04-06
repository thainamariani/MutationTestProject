/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators.selection;

import java.util.HashMap;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.Selection;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 *
 * @author thaina
 */
public class RouletteWheel extends Selection {

    public RouletteWheel(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object execute(Object object) throws JMException {
        SolutionSet population = (SolutionSet) object;
        double fitnessSum = 0;
        for (int i = 0; i < population.size(); i++) {
            fitnessSum += Math.abs(population.get(i).getObjective(0));
        }
        double random = PseudoRandom.randDouble(0, fitnessSum);
        double fitness = 0;
        for (int i = 0; i < population.size(); i++) {
            fitness += Math.abs(population.get(i).getObjective(0));
            if (fitness >= random) {
                return population.get(i);
            }
        }
        return null;
    }
}
