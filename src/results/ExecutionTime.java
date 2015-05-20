/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author thaina
 */
public class ExecutionTime {

    public static void main(String[] args) {

        List<String> configurations = new ArrayList<>();

        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.005_UniformCrossoverBinary_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.01_SinglePointCrossover_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.01_UniformCrossoverBinary_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.01_SinglePointCrossover_SwapMutationBinary_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.01_SinglePointCrossover_SwapMutationBinary_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.01_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.005_SinglePointCrossover_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.01_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.005_SinglePointCrossover_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.005_UniformCrossoverBinary_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.01_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.8_0.01_SinglePointCrossover_BitFlipMutation_BinaryTournament2_30");
        configurations.add("experiment/find/NSGAIII/F2/50_100_0.9_0.01_UniformCrossoverBinary_BitFlipMutation_BinaryTournament2_30");

        executionTimeAverage(configurations);
    }

    private static void executionTimeAverage(List<String> configurations) {
        Pattern pattern = Pattern.compile("^Total time of execution: (\\d*)$", Pattern.MULTILINE);
        for (String configuration : configurations) {
            double summation = 0;
            int count = 0;
            try (Scanner scanner = new Scanner(new File(configuration + "/SYSTEM_OUTPUT.txt"))) {
                while (scanner.hasNextLine()) {
                    String nextLine = scanner.nextLine();
                    Matcher matcher = pattern.matcher(nextLine);
                    try {
                        matcher.find();
                        summation += Integer.parseInt(matcher.group(1));
                        count++;
                    } catch (Exception ex) {
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(ExecutionTime.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println(configuration + " => " + summation + " / " + count + " = " + (summation / count));
            System.out.println("");
        }
    }

}
