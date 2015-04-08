/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

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
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 *
 * @author thaina
 */
public class Results {

    //conducted executions
    private static final int executions = 10;

    public static void main(String[] args) throws IOException, FileNotFoundException, InterruptedException {
        List<Path> paths = getPaths();
        for (Path path : paths) {
            //getResults(path);
        }
        selectPathsToKruskal(paths);
    }

    public static void selectPathsToKruskal(List<Path> paths) throws IOException, FileNotFoundException, InterruptedException {
        List<Path> selectedDirectories = new ArrayList<>();
        String actualParentDirectory = paths.get(0).getParent().toString();
        for (int i = 0; i < paths.size(); i++) {
            if (paths.get(i).getParent().toString().equals(actualParentDirectory)) {
                selectedDirectories.add(paths.get(i));
            } else {
                doKruskalWallisTest(selectedDirectories);
                selectedDirectories = new ArrayList<>();
                actualParentDirectory = paths.get(i).getParent().toString();
            }
        }
        doKruskalWallisTest(selectedDirectories);
    }

    public static void doKruskalWallisTest(List<Path> directories) throws FileNotFoundException, IOException, InterruptedException {
        KruskalWallisTest kruskal = new KruskalWallisTest();
        HashMap<String, double[]> values = new HashMap<>();
        for (Path directory : directories) {
            double[] funArray = new double[executions];
            for (int i = 0; i < executions; i++) {
                String sCurrentLine;
                BufferedReader br = new BufferedReader(new FileReader(directory + "/FUN_" + i));
                while ((sCurrentLine = br.readLine()) != null) {
                    if (!"".equals(sCurrentLine)) {
                        funArray[i] = Double.parseDouble(sCurrentLine);
                        break;
                    }
                }
            }
            values.put(directory.toString(), funArray);
        }
        writeKruskalWallisTest(directories, kruskal.test(values));
    }

    public static void writeKruskalWallisTest(List<Path> directories, HashMap<String, HashMap<String, Boolean>> resultKruskal) throws IOException {
        if (!directories.isEmpty()) {
            Path parent = directories.get(0).getParent();
            File writtenFile = new File(parent + "/KruskalWallisResults.txt");
            if (!writtenFile.exists()) {
                writtenFile.createNewFile();
                FileWriter fw = new FileWriter(writtenFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);

                int count = 0;
                for (int i = 0; i < directories.size() - 1; i++) {
                    for (int j = i + 1; j < directories.size(); j++) {
                        Boolean difference = resultKruskal.get(directories.get(i).toString()).get(directories.get(j).toString());
                        bw.write("Configs");
                        bw.newLine();
                        bw.write(directories.get(i).toString());
                        bw.newLine();
                        bw.write(directories.get(j).toString());
                        bw.newLine();
                        bw.write("Differents? " + difference);
                        bw.newLine();
                        bw.newLine();
                        bw.write("------------------------------------------------------");
                        bw.newLine();
                        bw.newLine();
                        count++;
                    }
                }
                bw.close();
            }
        }
    }

    public static List<Path> getPaths() {
        List<String> instances = new ArrayList<>();
        List<String> algorithms = new ArrayList<>();

        //set the instances name
        instances.add("bisect");
        instances.add("bub");
        instances.add("find");
        instances.add("fourballs");
        instances.add("mid");
        instances.add("trityp");

        //set the algorithms name
        algorithms.add("gGa");

        //select the paths
        List<Path> paths = new ArrayList<>();
        for (String instance : instances) {
            for (String algorithm : algorithms) {
                File file = new File("experiment/" + instance + "/" + algorithm + "/F2");
                if (file.isDirectory()) {
                    File[] subDirectory = file.listFiles();
                    if (subDirectory != null) {
                        for (File dir : subDirectory) {
                            paths.add(dir.toPath());
                        }
                    }
                }
            }
        }
        return paths;
    }

    public static void getResults(Path path) {
        //calculate fitness results
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

            //write results
            File writtenFile = new File(path + "/RESULTS");
            if (!writtenFile.exists()) {
                writtenFile.createNewFile();
                FileWriter fw = new FileWriter(writtenFile.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("Average: " + average);
                bw.newLine();
                bw.write("Standard Deviation: " + getStandardDeviation(allFuns));
                bw.newLine();
                bw.write("Best Fitness (FUN_" + bestPosition + "): " + best);
                bw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static double getStandardDeviation(List<Double> fun) {
        double[] funArray = new double[fun.size()];
        for (int i = 0; i < funArray.length; i++) {
            funArray[i] = fun.get(i);
        }

        StandardDeviation sd = new StandardDeviation();
        return sd.evaluate(funArray);
    }
}
