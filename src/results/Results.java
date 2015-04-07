/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package results;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thaina
 */
public class Results {

    public void getResults(int executions, String path) {
        //calculate fitness results
        File file = new File(path);
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
                    bw.write("Best Fitness (FUN_" + bestPosition + "): " + best);
                    bw.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
