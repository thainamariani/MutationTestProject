/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import util.ResultsUtil;

/**
 *
 * @author thaina
 */
public class MonoObjectiveResults {

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {
        //set the number of executions conducted
        int executions = 10;

        //basic results
        List<Path> paths = getAllResuts(executions);

        //kruskal wallis test
        getKruskalWallisTest(paths, executions);
    }

    private static void getKruskalWallisTest(List<Path> paths, int executions) throws InterruptedException, IOException {
        ResultsUtil.selectConfigurationPaths(paths);
        ResultsUtil.doKruskalWallisTest(MonoObjectiveResults.setDirectories(), executions, "FUN_");
    }

    //main class to calculate the results. Variables must be changed based on the needs.
    private static List<Path> getAllResuts(int executions) {
        List<String> instances = new ArrayList<>();
        List<String> algorithms = new ArrayList<>();
        //set the instances name
        instances.add("bisect");
        //instances.add("bub");
        //instances.add("find");
        //instances.add("fourballs");
        //instances.add("mid");
        //instances.add("trityp");
        //set the algorithms name
        algorithms.add("gGa");
        //algorithms.add("HillClimbing");
        //algorithms.add("HillClimbingAscendent");
        //algorithms.add("HillClimbingAscendentWithReplacement");
        List<Path> paths = ResultsUtil.getPaths(instances, algorithms);
        for (Path path : paths) {
            ResultsUtil.getResults(path, executions);
        }
        return paths;
    }

    //set the directories to be compared in the kruskal wallis test
    public static List<Path> setDirectories() {
        //final bisect
//        List<Path> directories = new ArrayList<>();
//        directories.add(new File("experiment/bisect/gGa/F2/50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament_10").toPath());
//        directories.add(new File("experiment/bisect/HillClimbing/F2/20000_BitFlipMutation_10").toPath());
//        directories.add(new File("experiment/bisect/HillClimbingAscendent/F2/100_BitFlipMutation_10_200").toPath());
//        directories.add(new File("experiment/bisect/HillClimbingAscendentWithReplacement/F2/1000_BitFlipMutation_10_50").toPath());

        //final bub
//        List<Path> directories = new ArrayList<>();
//        directories.add(new File("experiment/bub/gGa/F2/200_100_0.9_0.05_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament_10").toPath());
//        directories.add(new File("experiment/bub/HillClimbing/F2/2000000_BitFlipMutation_10").toPath());
//        directories.add(new File("experiment/bub/HillClimbingAscendent/F2/10000_BitFlipMutation_10_200").toPath());
//        directories.add(new File("experiment/bub/HillClimbingAscendentWithReplacement/F2/10000_BitFlipMutation_10_200").toPath());
//        
        //final find
//        List<Path> directories = new ArrayList<>();
//        directories.add(new File("experiment/find/gGa/F2/50_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament_10").toPath());
//        directories.add(new File("experiment/find/HillClimbing/F2/200000_BitFlipMutation_10").toPath());
//        directories.add(new File("experiment/find/HillClimbingAscendent/F2/1000_BitFlipMutation_10_100").toPath());
//        directories.add(new File("experiment/find/HillClimbingAscendentWithReplacement/F2/10000_BitFlipMutation_10_200").toPath());
        //final fourballs
//        List<Path> directories = new ArrayList<>();
//        directories.add(new File("experiment/fourballs/gGa/F2/100_100_0.8_0.1_UniformCrossoverBinary_SwapMutationBinary_LinearRanking_10").toPath());
//        directories.add(new File("experiment/fourballs/HillClimbing/F2/500000_BitFlipMutation_10").toPath());
//        directories.add(new File("experiment/fourballs/HillClimbingAscendent/F2/1000_BitFlipMutation_10_200").toPath());
//        directories.add(new File("experiment/fourballs/HillClimbingAscendentWithReplacement/F2/1000_BitFlipMutation_10_100").toPath());
//        
        //final mid
//        List<Path> directories = new ArrayList<>();
//        directories.add(new File("experiment/mid/gGa/F2/200_10000_0.9_0.1_TwoPointsCrossoverBinary_SwapMutationBinary_RouletteWheel_10").toPath());
//        directories.add(new File("experiment/mid/HillClimbing/F2/2000000_BitFlipMutation_10").toPath());
//        directories.add(new File("experiment/mid/HillClimbingAscendent/F2/10000_BitFlipMutation_10_200").toPath());
//        directories.add(new File("experiment/mid/HillClimbingAscendentWithReplacement/F2/10000_BitFlipMutation_10_200").toPath());
//        
//final trityp
        List<Path> directories = new ArrayList<>();
        directories.add(new File("experiment/trityp/gGa/F2/100_100_0.9_0.1_UniformCrossoverBinary_SwapMutationBinary_BinaryTournament_10").toPath());
        directories.add(new File("experiment/trityp/HillClimbing/F2/2000000_BitFlipMutation_10").toPath());
        directories.add(new File("experiment/trityp/HillClimbingAscendent/F2/10000_BitFlipMutation_10_200").toPath());
        directories.add(new File("experiment/trityp/HillClimbingAscendentWithReplacement/F2/10000_BitFlipMutation_10_200").toPath());
        return directories;
    }
}
