/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package operators.mutation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import jmetal.core.Solution;
import jmetal.encodings.solutionType.BinarySolutionType;
import jmetal.encodings.variable.Binary;
import jmetal.operators.mutation.Mutation;
import jmetal.util.Configuration;
import jmetal.util.JMException;
import jmetal.util.PseudoRandom;

/**
 *
 * @author thaina
 */
public class SwapMutationBinary extends Mutation {

    private static final List VALID_TYPES = Arrays.asList(BinarySolutionType.class);

    private Double mutationProbability_ = null;

    public SwapMutationBinary(HashMap<String, Object> parameters) {
        super(parameters);
        if (parameters.get("probability") != null) {
            mutationProbability_ = (Double) parameters.get("probability");
        }
    }

    @Override
    public Object execute(Object object) throws JMException {
        Solution solution = (Solution) object;

        if (!VALID_TYPES.contains(solution.getType().getClass())) {
            Configuration.logger_.severe("SwapMutationBinary.execute: the solution "
                    + "is not of the right type. The type should be 'Binary', but " + solution.getType() + " is obtained");

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".execute()");
        }

        this.doMutation(mutationProbability_, solution);
        return solution;
    }

    public void doMutation(Double probability, Solution solution) throws JMException {
        if (solution.getType().getClass() == BinarySolutionType.class) {
            if (PseudoRandom.randDouble() < probability) {

                Binary binarySolution = ((Binary) solution.getDecisionVariables()[0]);
                int numberOfBits = binarySolution.getNumberOfBits();

                List<Integer> selectedTrueGenes = new ArrayList<>(); //positions of genes with 1 value
                List<Integer> selectedFalseGenes = new ArrayList<>();//positions of genes with 0 value

                for (int i = 0; i < numberOfBits; i++) {
                    if (binarySolution.getIth(i)) {
                        selectedTrueGenes.add(i);
                    } else {
                        selectedFalseGenes.add(i);
                    }
                }

                if ((!selectedTrueGenes.isEmpty()) && (!selectedFalseGenes.isEmpty())) {
                    int i = PseudoRandom.randInt(0, selectedTrueGenes.size() - 1);
                    int j = PseudoRandom.randInt(0, selectedFalseGenes.size() - 1);
                    binarySolution.setIth(selectedTrueGenes.get(i), false);
                    binarySolution.setIth(selectedFalseGenes.get(j), true);
                }

            }
        } else {
            Configuration.logger_.severe("SwapMutationBinary.doMutation: invalid type. "
                    + "" + solution.getDecisionVariables()[0].getVariableType());

            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".doMutation()");
        }
    }

}
