/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import jmetal.core.SolutionSet;
import jmetal.qualityIndicator.util.MetricsUtil;
import jmetal.util.NonDominatedSolutionList;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import results.HypervolumeCalculator;
import results.KruskalWallisTest;

/**
 *
 * @author thaina
 */
public class ResultsUtil {

    //return the standard deviation
    public static double getStandardDeviation(List<Double> fun) {
        double[] funArray = new double[fun.size()];
        for (int i = 0; i < funArray.length; i++) {
            funArray[i] = fun.get(i);
        }
        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(funArray);
    }

    //return the average
    public static double getAverage(List<Double> fun) {
        double[] funArray = new double[fun.size()];
        for (int i = 0; i < funArray.length; i++) {
            funArray[i] = fun.get(i);
        }
        Mean mean = new Mean();
        return mean.evaluate(funArray);
    }

    //return the number of killed mutants of the solution passed
    public static int getNumberOfDifferentKilledMutants(String solution, Path path) {
        Path instanceDirectory = path.getParent().getParent().getParent();
        String instanceName = instanceDirectory.getFileName().toString();
        InstanceReader reader = new InstanceReader("instances/" + instanceName + ".txt");
        reader.open();
        int numberOfTestSuite = reader.readInt();
        int numberOfMutants = reader.readInt();
        int[][] coverage = reader.readIntMatrix(numberOfMutants, numberOfTestSuite, " ");
        reader.close();
        int[] visited = new int[numberOfMutants];
        int total = 0;
        for (int i = 0; i < solution.length(); i++) {
            if (solution.charAt(i) == '1') {
                for (int j = 0; j < numberOfMutants; j++) {
                    if (coverage[j][i] == 1 && visited[j] == 0) {
                        visited[j] = 1;
                        total++;
                    }
                }
            }
        }
        return total;
    }

    //return the number of test cases in the solution passed
    public static int getNumberOfTestCases(String solution) {
        int count = 0;
        for (int i = 0; i < solution.length(); i++) {
            if (solution.charAt(i) == '1') {
                count++;
            }
        }
        return count;
    }

    //write the final results
    public static void getResults(Path path, int executions) {
        List<Double> allFuns = new ArrayList<>();
        try {
            for (int i = 0; i < executions; i++) {
                String sCurrentLine;
                BufferedReader br = new BufferedReader(new FileReader(path + "/FUN_" + i));
                while ((sCurrentLine = br.readLine()) != null) {
                    if (!"".equals(sCurrentLine)) {
                        double fun = Double.valueOf(sCurrentLine);
                        allFuns.add(fun);
                    }
                }
                br.close();
            }
            double sum = 0;
            double best = 0;
            int bestPosition = 0;
            for (int i = 0; i < allFuns.size(); i++) {
                double currentFun = allFuns.get(i);
                if ((i == 0) || (currentFun < best)) {
                    best = currentFun;
                    bestPosition = i;
                }
                sum += currentFun;
            }
            double average = sum / allFuns.size();
            BufferedReader brVar = new BufferedReader(new FileReader(path + "/VAR_" + bestPosition));
            String sCurrentLine;
            String var = "";
            while ((sCurrentLine = brVar.readLine()) != null) {
                if (!"".equals(sCurrentLine)) {
                    var = sCurrentLine;
                }
            }
            brVar.close();
            int varDeadMutants = getNumberOfDifferentKilledMutants(var, path);
            int varTestCases = getNumberOfTestCases(var);
            File writtenFile = new File(path + "/RESULTS");
            if (!writtenFile.exists()) {
                writtenFile.createNewFile();
            }
            FileWriter fw = new FileWriter(writtenFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("Average: " + average);
            bw.newLine();
            bw.write("Standard Deviation: " + getStandardDeviation(allFuns));
            bw.newLine();
            bw.write("Best Fitness (FUN_" + bestPosition + "): " + best);
            bw.newLine();
            bw.write("VAR_" + bestPosition + ": " + var);
            bw.newLine();
            bw.write("Number of Test Cases: " + varTestCases);
            bw.newLine();
            bw.write("Number of Dead Mutants: " + varDeadMutants);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //write for each instance and algorithm, the best fitness value and SD found, and all the experiments which found such values
    public static void writeResults(List<Path> directories) throws FileNotFoundException, IOException, InterruptedException {
        List<Double> averages = new ArrayList<>();
        List<Double> standardDeviations = new ArrayList<>();
        for (Path directory : directories) {
            BufferedReader br = new BufferedReader(new FileReader(directory + "/RESULTS"));
            String sCurrentLine = br.readLine();
            if (!"".equals(sCurrentLine)) {
                String average = sCurrentLine.substring(9, sCurrentLine.length());
                averages.add(Double.parseDouble(average));
                sCurrentLine = br.readLine();
                String standardDeviation = sCurrentLine.substring(20, sCurrentLine.length());
                standardDeviations.add(Double.parseDouble(standardDeviation));
            }
            br.close();
        }
        List<String> bestAveragesDirectory = new ArrayList<>();
        double best = averages.get(0);
        for (Double result : averages) {
            if (result < best) {
                best = result;
            }
        }
        List<Double> bestAveragesSD = new ArrayList<>();
        for (int i = 0; i < averages.size(); i++) {
            if (averages.get(i) == best) {
                bestAveragesDirectory.add(directories.get(i).toString());
                bestAveragesSD.add(standardDeviations.get(i));
            }
        }
        double minStandardDeviation = bestAveragesSD.get(0);
        for (Double standardDeviation : bestAveragesSD) {
            if (standardDeviation < minStandardDeviation) {
                minStandardDeviation = standardDeviation;
            }
        }
        List<String> bestStandardDeviationDirectory = new ArrayList<>();
        for (int i = 0; i < bestAveragesSD.size(); i++) {
            if (bestAveragesSD.get(i) == minStandardDeviation) {
                bestStandardDeviationDirectory.add(bestAveragesDirectory.get(i));
            }
        }
        System.out.println("Best Value: " + best);
        System.out.println("Min Standard Deviation: " + minStandardDeviation);
        for (String bestDirectory : bestStandardDeviationDirectory) {
            System.out.println(bestDirectory);
        }
    }
    
    public static void selectConfigurationPaths(List<Path> paths) throws IOException, FileNotFoundException, InterruptedException {
        List<Path> selectedDirectories = new ArrayList<>();
        String actualParentDirectory = paths.get(0).getParent().toString();
        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getParent().toString().equals(actualParentDirectory)) {
                selectedDirectories.add(paths.get(i));
            } else {
                writeResults(selectedDirectories);
                selectedDirectories = new ArrayList<>();
                actualParentDirectory = paths.get(i).getParent().toString();
            }
        }
        writeResults(selectedDirectories);
    }

    //return the paths containing the results based on the instances and algorithms passed
    public static List<Path> getPaths(List<String> instances, List<String> algorithms) {
        List<Path> paths = new ArrayList<>();
        for (String instance : instances) {
            for (String algorithm : algorithms) {
                File file = new File("experiment/" + instance + "/" + algorithm + "/F2");
                if (file.isDirectory()) {
                    File[] subDirectory = file.listFiles();
                    if (subDirectory != null) {
                        for (File dir : subDirectory) {
                            if (!dir.toPath().toString().contains("KruskalWallisResults") && !dir.toPath().toString().contains("Hypervolume_Results")) {
                                paths.add(dir.toPath());
                            }
                        }
                    }
                }
            }
        }
        return paths;
    }

    //write the hypervolume results for each experiment passed
    public static void writeHypervolume(List<Path> paths, int numberOfObjectives, int numberOfExecutions) throws IOException {
        HypervolumeCalculator hypervolumeCalculator = new HypervolumeCalculator(numberOfObjectives);
        
        for (Path path : paths) {
            for (int i = 0; i < numberOfExecutions; i++) {
                if (!path.toString().endsWith("KruskalWallisResults") && !path.toString().endsWith("Hypervolume_Results")) {
                    hypervolumeCalculator.addParetoFront(path.toString() + "/FUN_" + i);
                }
            }
        }
        try (final FileWriter fileWriter = new FileWriter(paths.get(0).getParent().getParent().getParent().toString() + "/Hypervolume_Results")) {
            for (Path path : paths) {
                if (!path.toString().endsWith("KruskalWallisResults") && !path.toString().endsWith("Hypervolume_Results")) {
                    List<Double> allHypervolumes = new ArrayList<>();
                    for (int i = 0; i < numberOfExecutions; i++) {
                        try (FileWriter fileWriterIndividual = new FileWriter(path + "/Hypervolume_" + i)) {
                            double hypervolume = hypervolumeCalculator.execute(path.toString() + "/FUN_" + i);
                            fileWriterIndividual.write("" + hypervolume);
                            allHypervolumes.add(hypervolume);
                        }
                    }
                    
                    try (final FileWriter fileWriter2 = new FileWriter(path + "/Hypervolume_Results")) {
                        //write the hypervolume average and standard deviation in a file for each algorithm
                        fileWriter2.write("Average: " + getAverage(allHypervolumes) + "\n");
                        fileWriter2.write("Standard Deviation: " + getStandardDeviation(allHypervolumes) + "\n");

                        //write the hypervolume average and standard deviation in a file common for all algorithms
                        fileWriter.write("Path: " + path.toString() + "\n");
                        fileWriter.write("Average: " + getAverage(allHypervolumes) + "\n");
                        fileWriter.write("Standard Deviation: " + getStandardDeviation(allHypervolumes) + "\n\n");
                        fileWriter.write("----------------------------------------------------------------------------------\n\n");
                    }
                }
            }
        }
    }
    
    public static void writeKruskalWallisTest(List<Path> directories, HashMap<String, HashMap<String, Boolean>> resultKruskal) throws IOException {
        if (!directories.isEmpty()) {
            Path parent = directories.get(0).getParent().getParent().getParent();
            File writtenFile = new File(parent + "/KruskalWallisResults_" + directories.get(0).getParent().getParent().getFileName());
            if (!writtenFile.exists()) {
                writtenFile.createNewFile();
            }
            FileWriter fw = new FileWriter(writtenFile.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            for (int i = 0; i < directories.size() - 1; i++) {
                for (int j = i + 1; j < directories.size(); j++) {
                    Boolean difference = resultKruskal.get(directories.get(i).toString()).get(directories.get(j).toString());
                    bw.write("Configs");
                    bw.newLine();
                    bw.write(directories.get(i).toString());
                    bw.newLine();
                    bw.write(directories.get(j).toString());
                    bw.newLine();
                    bw.write("Different? " + difference);
                    bw.newLine();
                    bw.newLine();
                    bw.write("------------------------------------------------------");
                    bw.newLine();
                    bw.newLine();
                }
            }
            bw.close();
            fw.close();
        }
    }

    //must be passed as parameters the directories with the experiments to be compared and the number of executions performed
    public static void doKruskalWallisTest(List<Path> directories, int executions, String archiveSuffix) throws FileNotFoundException, IOException, InterruptedException {
        KruskalWallisTest kruskal = new KruskalWallisTest();
        HashMap<String, double[]> values = new HashMap<>();
        for (Path directory : directories) {
            double[] funArray = new double[executions];
            for (int i = 0; i < executions; i++) {
                String sCurrentLine;
                BufferedReader br = new BufferedReader(new FileReader(directory + "/" + archiveSuffix + i));
                while ((sCurrentLine = br.readLine()) != null) {
                    if (!"".equals(sCurrentLine)) {
                        funArray[i] = Double.parseDouble(sCurrentLine);
                        break;
                    }
                }
                br.close();
            }
            values.put(directory.toString(), funArray);
        }
        writeKruskalWallisTest(directories, kruskal.test(values));
    }
    
    public static void createPFTrue(List<Path> paths) throws FileNotFoundException, IOException {
        MetricsUtil metricsUtil = new MetricsUtil();
        NonDominatedSolutionList nonDominatedSolutionList = new NonDominatedSolutionList();

        //calculate PFtrue
        for (Path path : paths) {
            SolutionSet solutions = metricsUtil.readNonDominatedSolutionSet(path + "/FUN_All");
            for (int i = 0; i < solutions.size(); i++) {
                nonDominatedSolutionList.add(solutions.get(i));
            }
        }
        
        ExperimentUtil.removeRepeated(nonDominatedSolutionList);

        //read PFtrue
        double[][] trueFront = nonDominatedSolutionList.writeObjectivesToMatrix();

        //write PFtrue
        try (FileWriter writer = new FileWriter(paths.get(0).getParent().getParent().getParent() + "/Pareto.txt")) {
            writer.write(nonDominatedSolutionList.size() + "\n\n");

            //write PFknown
            for (Path path : paths) {
                int count = 0;
                double[][] solutions = metricsUtil.readFront(path + "/FUN_All");
                for (double[] solution : solutions) {
                    double distance = metricsUtil.distanceToClosedPoint(solution, trueFront);
                    if (distance == 0) {
                        count++;
                    }
                }
                writer.write(path.getParent().toString() + ": ");
                writer.write(solutions.length + " (" + count + ")\n\n");
            }
        }
        
    }
}
