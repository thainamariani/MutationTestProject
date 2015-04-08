/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators.selection;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import jmetal.core.SolutionSet;
import jmetal.operators.selection.Selection;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;
import jmetal.util.comparators.ObjectiveComparator;

/**
 *
 * @author thaina
 */
public class LinearRanking extends Selection {

    private double probabilitySum = 0;

    public LinearRanking(HashMap<String, Object> parameters) {
        super(parameters);
    }

    @Override
    public Object execute(Object object) throws JMException {
        SolutionSet population = (SolutionSet) object;

        Comparator comparator = new ObjectiveComparator(0, true);
        population.sort(comparator);

        List<Double> populationProbability = calculateProbability(population);

        normalizeProbability(populationProbability);

        return selectSolution(populationProbability, population);
    }

    public Object selectSolution(List<Double> populationProbability, SolutionSet population) {
        double random = PseudoRandom.randDouble(0, 1);
        double fitness = 0;
        for (int i = 0; i < populationProbability.size(); i++) {
            fitness += populationProbability.get(i);
            if (fitness >= random) {
                return population.get(i);
            }
        }
        return null;
    }

    public List<Double> normalizeProbability(List<Double> populationProbability) {
        //normalization
        for (int i = 0; i < populationProbability.size(); i++) {
            populationProbability.set(i, populationProbability.get(i) / probabilitySum);
        }
        return populationProbability;
    }

    //formulation of linear ranking
    public List<Double> calculateProbability(SolutionSet population) {
        probabilitySum = 0;
        //s = selection pressure
        double s = 1.5;
        double value1 = (2 - s) / population.size();
        List<Double> populationProbability = new ArrayList<>();
        for (int i = 0; i < population.size(); i++) {
            double value2 = (2 * (i + 1) * (s - 1)) / (population.size() * (population.size() - 1));
            populationProbability.add(value1 + value2);
            probabilitySum += populationProbability.get(i);
        }
        return populationProbability;
    }
}
