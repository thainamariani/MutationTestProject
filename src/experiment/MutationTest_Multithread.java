package experiment;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MutationTest_Multithread {

    private static volatile int MAX_THREADS = 10;
    private static volatile int RUNNING_THREADS = 0;
    private static volatile List<Thread> ACTIVE_THREADS;
    private static volatile List<Thread> QUEUED_THREADS;
    private static volatile List<String> FINISHED_THREADS;
    private static volatile Thread console;
    private static volatile String consoleToken = ">";

    private static final String[] INSTANCES = {
        "instances/bisect.txt",
        "instances/bub.txt"
    };

    private static final String[] ALGORITHMS = {
        "gGa",
        "ssGa"
    };

    private static final int[] POPULATION_SIZE = {
        50,
        100,
        200
    };

    private static final int[] GENERATIONS = {
        1000
    };

    private static final double[] CROSSOVER_PROBABILITY = {
        0.95
    };

    private static final double[] MUTATION_PROBABILITY = {
        0.1
    };

    private static final String[] CROSSOVER_OPERATORS = {
        "SinglePointCrossover"
    };

    private static final String[] MUTATION_OPERATORS = {
        "BitFlipMutation"
    };

    private static final String[] SELECTION_OPERATORS = {
        "BinaryTournament"
    };

    private static final int[] FITNESS_FUNCTIONS = {
        1,
        2
    };

    private static final int EXECUTIONS = 30;

    private static synchronized void initialize() {
        ACTIVE_THREADS = new ArrayList<>();
        QUEUED_THREADS = new ArrayList<>();
        FINISHED_THREADS = new ArrayList<>();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Inform the max number of threads (min = 1, max = 10), leave in blank for default (4):");
        printConsoleToken();
        do {
            try {
                String nextLine = scanner.nextLine();
                if (nextLine.trim().isEmpty()) {
                    throw new Exception();
                }
                MAX_THREADS = Integer.valueOf(nextLine);
                if (MAX_THREADS < 1 || MAX_THREADS > 10) {
                    throw new NumberFormatException();
                }
                break;
            } catch (NumberFormatException ex) {
                System.out.println("Number higher/lower than max/min or format invalid! Type again:");
                printConsoleToken();
            } catch (Exception ex) {
                MAX_THREADS = 4;
                break;
            }
        } while (true);

        System.out.println("Inform the path for the file containing the name of the experiments you want to execute, leave in blank for default (all):");
        printConsoleToken();
        try {
            File experimentsName = new File(scanner.nextLine());
            while (!experimentsName.exists()) {
                if (experimentsName.getPath().trim().isEmpty()) {
                    throw new Exception();
                }
                System.out.println("File not found! Type again:");
                printConsoleToken();
                experimentsName = new File(scanner.nextLine());
            }
            Scanner fileScan = new Scanner(experimentsName);
            while (fileScan.hasNext()) {
                String context = fileScan.nextLine();
                createThread(context);
            }
        } catch (Exception ex) {
            for (final String instance : INSTANCES) {
                for (final String algorithm : ALGORITHMS) {
                    for (final int fitnessFunction : FITNESS_FUNCTIONS) {
                        for (final int populationSize : POPULATION_SIZE) {
                            for (final int generations : GENERATIONS) {
                                for (final double crossoverProbability : CROSSOVER_PROBABILITY) {
                                    for (final double mutationProbability : MUTATION_PROBABILITY) {
                                        for (final String crossoverOperator : CROSSOVER_OPERATORS) {
                                            for (final String mutationOperator : MUTATION_OPERATORS) {
                                                for (final String selectionOperator : SELECTION_OPERATORS) {
                                                    createThread(instance, algorithm, populationSize, generations, crossoverProbability, mutationProbability, crossoverOperator, mutationOperator, EXECUTIONS, fitnessFunction, selectionOperator);

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Inform the path for the file to store information, leave in blank for default (RunningInfo.txt in the work directory):");
        printConsoleToken();

        try {
            String fileName = scanner.nextLine();

            if (fileName.trim().isEmpty()) {
                fileName = System.getProperty("user.dir") + "/RunningInfo.txt";
            }
            final File runningInfoFile = new File(fileName);
            if (!runningInfoFile.exists()) {
                if (!runningInfoFile.getParentFile().exists()) {
                    runningInfoFile.getParentFile().mkdirs();
                }
                runningInfoFile.createNewFile();
            }
            new Thread(new Runnable() {

                @Override
                public void run() {

                    while (true) {
                        try (final FileWriter runningInfo = new FileWriter(runningInfoFile, false)) {
                            runningInfo.append("# This file presents the current status of the execution.\n# It is updated every 10s.");
                            runningInfo.append("\n");
                            runningInfo.append("\n");
                            runningInfo.append("Number of Max Threads = " + String.valueOf(MAX_THREADS));
                            runningInfo.append("\n");
                            runningInfo.append("Number of Running Threads = " + String.valueOf(RUNNING_THREADS));
                            runningInfo.append("\n");
                            runningInfo.append("Number of Remaining Threads = " + String.valueOf(getQueuedThreadsSize()));
                            runningInfo.append("\n");
                            runningInfo.append("Number of Successfully Finished Threads = " + String.valueOf(getFinishedThreadsSize()));
                            runningInfo.append("\n");
                            runningInfo.append("\n");
                            runningInfo.append("Active Threads = " + getActiveThreads().toString());
                            runningInfo.append("\n");
                            runningInfo.append("\n");
                            runningInfo.append("Queued Threads = " + getQueuedThreads().toString());
                            runningInfo.append("\n");
                            runningInfo.append("\n");
                            runningInfo.append("Finished Threads = " + getFinishedThreads().toString());
                        } catch (Exception ex) {
                        }
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(MutationTest_Multithread.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }

                }
            }).start();
        } catch (IOException ex) {
            System.err.println("There was a problem creating the file to store information. Skipping it.");
        }

        console = new Thread(new Runnable() {

            @Override
            public void run() {
                Scanner scanner = new Scanner(System.in);
                while (true) {
                    System.out.println("");
                    printConsoleToken();
                    String line = scanner.nextLine();
                    switch (line) {
                        case "+":
                            incrementMaxThreads();
                            break;
                        case "-":
                            decrementMaxThreads();
                            break;
                        case "m":
                            System.out.println(MAX_THREADS);
                            break;
                        case "P":
                            System.out.println("There are " + getQueuedThreadsSize() + " threads in queued right now. They are:");
                            for (Thread thread : getQueuedThreads()) {
                                System.out.println(thread.getName());
                            }
                            System.out.println("");
                        case "p":
                            System.out.println("There are " + getActiveThreadsSize() + " threads active right now. They are:");
                            for (Thread thread : getActiveThreads()) {
                                System.out.println(thread.getName());
                            }
                            break;
                        case "q":
                            System.out.println("Are you sure? (y or n):");
                            printConsoleToken();
                            String sure = scanner.nextLine();
                            if ("y".equals(sure)) {
                                exit();
                            }
                            break;
                        case "c":
                            for (int i = 0; i < 50; i++) {
                                System.out.println();
                            }
                            try {
                                Runtime.getRuntime().exec("clear");
                            } catch (IOException ex) {
                            }
                            try {
                                Runtime.getRuntime().exec("cls");
                            } catch (IOException ex) {
                            }
                            break;
                        case "r":
                            System.out.println("There are " + getQueuedThreadsSize() + " remainign to be executed.");
                            break;
                        case "t":
                            System.out.println("Inform the new console token:");
                            printConsoleToken();
                            consoleToken = scanner.nextLine();
                            if (consoleToken.trim().isEmpty()) {
                                consoleToken = ">";
                            }
                            break;
                        case "f":
                            System.out.println("There are " + getFinishedThreadsSize() + " successfully finished threads. They are:");
                            for (String string : getFinishedThreads()) {
                                System.out.println(string);
                            }
                            break;
                        case "h":
                        default:
                            System.out.println("Select one of the following:");
                            System.out.println("\t\"+\" - Increments the max number of threads;");
                            System.out.println("\t\"-\" - Decrements the max number of threads;");
                            System.out.println("\t\"m\" - Prints the number of max threads;");
                            System.out.println("\t\"p\" - Prints the current running threads;");
                            System.out.println("\t\"P\" - Prints the remaining and current running threads;");
                            System.out.println("\t\"r\" - Prints the number of remaining threads;");
                            System.out.println("\t\"f\" - Prints the finished threads;");
                            System.out.println("\t\"c\" - Clear the console;");
                            System.out.println("\t\"h\" - Help;");
                            System.out.println("\t\"t\" - Change the console token;");
                            System.out.println("\t\"q\" - Quits program and kills all running threads;");
                    }
                }
            }

        });
        System.out.println("Console started. You can now type your commands:");
        console.start();
    }

    private static synchronized void printConsoleToken() {
        System.out.print(consoleToken + " ");
    }

    private static synchronized void createThread(final String context) {
        //instance_algorithms_populationSize_generations_crossoverProbability_mutationProbability_crossoverOperator_mutationOperator_executions
        String[] split = context.split("_");
        final String instance = String.valueOf(split[1]);
        final String algorithm = String.valueOf(split[2]);
        final int populationSize = Integer.valueOf(split[3]);
        final int generations = Integer.valueOf(split[4]);
        final double crossoverProbability = Double.valueOf(split[5]);
        final double mutationProbability = Double.valueOf(split[6]);
        final String crossoverOperator = String.valueOf(split[7]);
        final String mutationOperator = String.valueOf(split[8]);
        final int executions = Integer.valueOf(split[9]);
        final int fitnessFunction = Integer.valueOf(split[10]);
        createThread(instance, algorithm, populationSize, generations, crossoverProbability, mutationProbability, crossoverOperator, mutationOperator, executions, fitnessFunction, context);
    }

    private static synchronized void createThread(final String instance, final String algorithm, final int populationSize, final int generations, final double crossoverProbability, final double mutationProbability, final String crossoverOperator, final String mutationOperator, final int executions, final int fitnessFunction, final String selectionOperator) {
        final String context = getContext(populationSize, generations, crossoverProbability, mutationProbability, crossoverOperator, mutationOperator, executions, selectionOperator);
        //String.valueOf(maxEvaluations)
        //String.valueOf(mutationProbability), style
        //);
        createThread(instance, algorithm, populationSize, generations, crossoverProbability, mutationProbability, crossoverOperator, mutationOperator, executions, fitnessFunction, context, selectionOperator);
    }

    private static synchronized void createThread(final String instance, final String algorithm, final int populationSize, final int generations, final double crossoverProbability, final double mutationProbability, final String crossoverOperator, final String mutationOperator, final int executions, final int fitnessFunction, final String context, final String selectionOperator) {
        final Thread thread = new Thread(new Runnable() {

            private Process process = null;

            @Override
            public void run() {
                try {
                    ProcessBuilder builder = new ProcessBuilder("java", "-XX:MaxPermSize=1G", "-Xmx5G", "-classpath", "dist/MutationTestProject.jar", "experiment.MTTest",
                            "" + instance,
                            "" + algorithm,
                            "" + populationSize,
                            "" + generations,
                            "" + crossoverProbability,
                            "" + mutationProbability,
                            "" + crossoverOperator,
                            "" + mutationOperator,
                            "" + executions,
                            "" + context,
                            "" + fitnessFunction,
                            "" + selectionOperator
                    );
                    final File destination = new File("experiment/" + getInstanceName(instance) + "/" + algorithm + "/F" + fitnessFunction + "/" + context + "/SYSTEM_OUTPUT.txt");
                    final File errorDestination = new File("experiment/" + getInstanceName(instance) + "/" + algorithm + "/F" + fitnessFunction + "/" + context + "/SYSTEM_ERROR.txt");
                    {
                        final File parentFile = destination.getParentFile();
                        if (!parentFile.exists()) {
                            parentFile.mkdirs();
                        }
                    }
                    destination.createNewFile();
                    builder.redirectOutput(destination);
                    errorDestination.createNewFile();
                    builder.redirectError(errorDestination);
                    process = builder.start();
                    int exitStatus = process.waitFor();
                    if (exitStatus != 0) {
                        threadError(this, context, exitStatus);
                    } else {
                        FINISHED_THREADS.add(context);
                    }
                } catch (IOException | InterruptedException ex) {
                    Logger.getLogger(MutationTest_Multithread.class.getName()).log(Level.SEVERE, null, ex);
                } finally {
                    if (process != null) {
                        process.destroy();
                    }
                    removeThread(context);
                    decrementRunningThreads();
                }
            }
        }, context);
        addThreadToQueue(thread);
    }

    public static synchronized void decrementRunningThreads() {
        RUNNING_THREADS--;
    }

    public static synchronized void incrementRunningThreads() {
        RUNNING_THREADS++;
    }

    public static synchronized void decrementMaxThreads() {
        MAX_THREADS--;
    }

    public static synchronized void incrementMaxThreads() {
        MAX_THREADS++;
    }

    private static synchronized void exit() {
        try {
            RUNNING_THREADS = Integer.MAX_VALUE;
            for (Thread thread : ACTIVE_THREADS) {
                thread.stop();
            }
        } finally {
            System.exit(0);
        }
    }

    private static synchronized void addThread(Thread thread) {
        ACTIVE_THREADS.add(thread);
    }

    private static synchronized void removeThread(String name) {
        for (int i = 0; i < ACTIVE_THREADS.size(); i++) {
            Thread thread = ACTIVE_THREADS.get(i);
            if (thread.getName().equals(name)) {
                ACTIVE_THREADS.remove(i);
                return;
            }
        }
    }

    private static synchronized void threadError(Runnable runnable, String name, int exitStatus) {
        System.err.println("Thread " + name + " stopped with error.\nIt has been added at the end of the queue.\nExit status: " + exitStatus + "\n");
        printConsoleToken();
        addThreadToQueue(new Thread(runnable, name));
    }

    private static synchronized void addThreadToQueue(Thread thread) {
        QUEUED_THREADS.add(thread);
    }

    private static synchronized int getActiveThreadsSize() {
        return ACTIVE_THREADS.size();
    }

    private static synchronized int getQueuedThreadsSize() {
        return QUEUED_THREADS.size();
    }

    private static synchronized int getFinishedThreadsSize() {
        return FINISHED_THREADS.size();
    }

    private static synchronized List<Thread> getActiveThreads() {
        return new ArrayList<>(ACTIVE_THREADS);
    }

    private static synchronized List<Thread> getQueuedThreads() {
        return new ArrayList<>(QUEUED_THREADS);
    }

    private static synchronized List<String> getFinishedThreads() {
        return new ArrayList<>(FINISHED_THREADS);
    }

    public static synchronized String getContext(final int populationSize, final int generations, final double crossoverProbability, final double mutationProbability, final String crossoverOperator, final String mutationOperator, final int executions, final String selectionOperator) {
        return populationSize + "_" + generations + "_" + crossoverProbability + "_" + mutationProbability + "_" + crossoverOperator + "_" + mutationOperator + "_" + selectionOperator + "_" + executions;
    }

    public static void main(String[] args) throws InterruptedException {
        initialize();

        while (!ACTIVE_THREADS.isEmpty() || !QUEUED_THREADS.isEmpty()) {
            if (!QUEUED_THREADS.isEmpty()) {
                Thread thread = QUEUED_THREADS.get(0);
                while (RUNNING_THREADS >= MAX_THREADS) {
                    Thread.sleep(1000);
                }
                QUEUED_THREADS.remove(0);
                incrementRunningThreads();
                addThread(thread);
                thread.start();
            } else {
                Thread.sleep(1000);
            }
        }
        System.out.println("FIM de execução!");
        System.exit(0);
    }

    public static String getInstanceName(String path) {
        int end = path.indexOf(".txt");
        return path.substring(10, end);
    }

}
