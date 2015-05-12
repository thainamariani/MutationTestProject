/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

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
public class MultiObjectiveResults {

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {

        //hypervolume
        List<String> instances = new ArrayList<>();
        instances.add("bisect");
        instances.add("bub");
        instances.add("find");
        instances.add("fourballs");
        instances.add("guizzo_cas");
        instances.add("guizzo_james");
        instances.add("guizzo_save");
        instances.add("guizzo_weatherstation");
        instances.add("mid");
        instances.add("trityp");

        List<String> algorithms = new ArrayList<>();
        algorithms.add("IBEA");
        algorithms.add("NSGAIII");
        algorithms.add("NSGAII");
        algorithms.add("SPEA2");

        int numberOfObjectives = 2;
        int numberOfExecutions = 30;

        //List<Path> paths = ResultsUtil.getPaths(instances, algorithms);
        //ResultsUtil.writeHypervolume(paths, numberOfObjectives, numberOfExecutions);
        //kruskal wallis for tuning
        for (String instance : instances) {
            List<String> instancesKruskal = new ArrayList<>();
            instancesKruskal.add(instance);
            for (String algorithm : algorithms) {
                List<String> algorithmsKruskal = new ArrayList<>();
                algorithmsKruskal.add(algorithm);
                List<Path> paths = ResultsUtil.getPaths(instancesKruskal, algorithmsKruskal);
                ResultsUtil.doKruskalWallisTest(paths, numberOfExecutions, "Hypervolume_");
            }
        }
    }
}
