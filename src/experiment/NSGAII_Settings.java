//  NSGAII_Settings.java 
//
//  Authors:
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
package experiment;

import jmetal.experiments.settings.*;
import jmetal.core.Algorithm;
import jmetal.experiments.Settings;
import jmetal.metaheuristics.nsgaII.NSGAII;
import jmetal.operators.crossover.Crossover;
import jmetal.operators.crossover.CrossoverFactory;
import jmetal.operators.mutation.Mutation;
import jmetal.operators.mutation.MutationFactory;
import jmetal.operators.selection.Selection;
import jmetal.operators.selection.SelectionFactory;
import jmetal.problems.ProblemFactory;
import jmetal.util.JMException;

import java.util.HashMap;
import java.util.Properties;
import jmetal.core.Problem;

/**
 * Settings class of algorithm NSGA-II (real encoding)
 */
public class NSGAII_Settings extends Settings {

    public int populationSize_;
    public int maxEvaluations_;
    public double mutationProbability_;
    public double crossoverProbability_;
    public double mutationDistributionIndex_;
    public double crossoverDistributionIndex_;

    /**
     * Constructor
     */
    public NSGAII_Settings(String problemName, Problem problem) {
        super(problemName);

        problem_ = problem;

        // Default experiments.settings
        populationSize_ = 100;
        maxEvaluations_ = 25000;
        mutationProbability_ = 1.0 / problem_.getNumberOfVariables();
        crossoverProbability_ = 0.9;
        mutationDistributionIndex_ = 20.0;
        crossoverDistributionIndex_ = 20.0;
    } // NSGAII_Settings

    /**
     * Configure NSGAII with default parameter experiments.settings
     *
     * @return A NSGAII algorithm object
     * @throws jmetal.util.JMException
     */
    public Algorithm configure() throws JMException {
        Algorithm algorithm;
        Selection selection;
        Crossover crossover;
        Mutation mutation;

        HashMap parameters; // Operator parameters

    // Creating the algorithm. There are two choices: NSGAII and its steady-
        // state variant ssNSGAII
        algorithm = new NSGAII(problem_);
    //algorithm = new ssNSGAII(problem_) ;

        // Algorithm parameters
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

        // Mutation and Crossover for Real codification
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        parameters.put("distributionIndex", crossoverDistributionIndex_);
        crossover = CrossoverFactory.getCrossoverOperator("SBXCrossover", parameters);

        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        parameters.put("distributionIndex", mutationDistributionIndex_);
        mutation = MutationFactory.getMutationOperator("PolynomialMutation", parameters);

        // Selection Operator
        parameters = null;
        selection = SelectionFactory.getSelectionOperator("BinaryTournament2", parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        return algorithm;
    } // configure

    /**
     * Configure NSGAII with user-defined parameter experiments.settings
     *
     * @return A NSGAII algorithm object
     */
    @Override
    public Algorithm configure(Properties configuration) throws JMException {
        Algorithm algorithm;
        Selection selection;
        Crossover crossover;
        Mutation mutation;

        HashMap parameters; // Operator parameters

    // Creating the algorithm. There are two choices: NSGAII and its steady-
        // state variant ssNSGAII
        algorithm = new NSGAII(problem_);
    //algorithm = new ssNSGAII(problem_) ;

        // Algorithm parameters
        populationSize_ = Integer.parseInt(configuration.getProperty("populationSize", String.valueOf(populationSize_)));
        maxEvaluations_ = Integer.parseInt(configuration.getProperty("maxEvaluations", String.valueOf(maxEvaluations_)));
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

        // Mutation and Crossover for Real codification
        crossoverProbability_ = Double.parseDouble(configuration.getProperty("crossoverProbability", String.valueOf(crossoverProbability_)));
        crossoverDistributionIndex_ = Double.parseDouble(configuration.getProperty("crossoverDistributionIndex", String.valueOf(crossoverDistributionIndex_)));
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        parameters.put("distributionIndex", crossoverDistributionIndex_);
        crossover = CrossoverFactory.getCrossoverOperator(configuration.getProperty("crossoverOperator"), parameters);

        mutationProbability_ = Double.parseDouble(configuration.getProperty("mutationProbability", String.valueOf(mutationProbability_)));
        mutationDistributionIndex_ = Double.parseDouble(configuration.getProperty("mutationDistributionIndex", String.valueOf(mutationDistributionIndex_)));
        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        parameters.put("distributionIndex", mutationDistributionIndex_);
        mutation = MutationFactory.getMutationOperator(configuration.getProperty("mutationOperator"), parameters);

        // Selection Operator
        parameters = null;
        selection = SelectionFactory.getSelectionOperator(configuration.getProperty("selectionOperator"), parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        return algorithm;
    }
    
     /**
     * Configure NSGAII with user-defined parameter experiments.settings
     *
     * @param configuration
     * @return A NSGAII algorithm object
     * @throws jmetal.util.JMException
     */        
    public Algorithm configureHash(HashMap configuration) throws JMException {
        Algorithm algorithm;
        Selection selection;
        Crossover crossover;
        Mutation mutation;

        HashMap parameters; // Operator parameters

    // Creating the algorithm. There are two choices: NSGAII and its steady-
        // state variant ssNSGAII
        algorithm = new NSGAII(problem_);
    //algorithm = new ssNSGAII(problem_) ;

        // Algorithm parameters
        populationSize_ = Integer.parseInt(configuration.get("populationSize").toString());
        maxEvaluations_ = Integer.parseInt(configuration.get("maxEvaluations").toString());
        algorithm.setInputParameter("populationSize", populationSize_);
        algorithm.setInputParameter("maxEvaluations", maxEvaluations_);

        // Mutation and Crossover for Real codification
        crossoverProbability_ = Double.parseDouble(configuration.get("crossoverProbability").toString());
        crossoverDistributionIndex_ = Double.parseDouble(configuration.get("crossoverDistributionIndex").toString());
        parameters = new HashMap();
        parameters.put("probability", crossoverProbability_);
        parameters.put("distributionIndex", crossoverDistributionIndex_);
        crossover = CrossoverFactory.getCrossoverOperator(configuration.get("crossoverOperator").toString(), parameters);

        mutationProbability_ = Double.parseDouble(configuration.get("mutationProbability").toString());
        mutationDistributionIndex_ = Double.parseDouble(configuration.get("mutationDistributionIndex").toString());
        parameters = new HashMap();
        parameters.put("probability", mutationProbability_);
        parameters.put("distributionIndex", mutationDistributionIndex_);
        mutation = MutationFactory.getMutationOperator(configuration.get("mutationOperator").toString(), parameters);

        // Selection Operator
        parameters = null;
        selection = SelectionFactory.getSelectionOperator(configuration.get("selectionOperator").toString(), parameters);

        // Add the operators to the algorithm
        algorithm.addOperator("crossover", crossover);
        algorithm.addOperator("mutation", mutation);
        algorithm.addOperator("selection", selection);

        return algorithm;
    }
} // NSGAII_Settings
