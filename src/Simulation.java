import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Simulation {
    static Random rand = new Random();

    double time = 0;
    double deltaT = Math.pow(10, -4);

    static final int n = 100000;
    static int chunkSize = 10000;
    static int numWorkers = n/chunkSize;
    static final double r = 0.3;

    static double[] xPositions = new double[n];
    static double[] yPositions = new double[n];
    static double[] xVelocities = new double[n];
    static double[] yVelocities = new double[n];
    static Vector[] bodyForces = new Vector[n];

    public static void initializeArrays() {
        for (int i = 0; i < n; i++) {
            xPositions[i] = i;
            yPositions[i] = i;
            xVelocities[i] = 0;
            yVelocities[i] = 0;
            bodyForces[i] = new Vector(0,0);
        }
    }


    public static void doublyThreaded() {
        Thread[][] computeThreads = new Thread[numWorkers][numWorkers];
        for (int indexY = 0; indexY < numWorkers; indexY++) {
            for (int indexX = 0; indexX < numWorkers; indexX++) {
                ForceComputation forceComp = new ForceComputation();
                forceComp.setThreadIdx(indexX, indexY);
                computeThreads[indexX][indexY] = new Thread(forceComp); // fill computeThreads array
            }
        }


        //start threads
        for (int i = 0; i < numWorkers; i++) {
            for (int j = 0; j < numWorkers; j++) {
                computeThreads[i][j].start();
            }
        }


        // synchronize threads
        for (int i = 0; i < numWorkers; i++) {
            for (int j = 0; j < numWorkers; j++) {
                try {
                    computeThreads[i][j].join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void singlyThreaded() {
        Thread[] computeThreads = new Thread[numWorkers];
        for (int indexY = 0; indexY < numWorkers; indexY++) {
            ForceComputation forceComp = new ForceComputation();
            forceComp.setThreadIdx(indexY);
            computeThreads[indexY] = new Thread(forceComp); // fill computeThreads array

        }

        //start threads
        for (int i = 0; i < numWorkers; i++) {
            computeThreads[i].start();
        }

        // synchronize threads
        for (int i = 0; i < numWorkers; i++) {
            try {
                computeThreads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public static void sequential() {
        for (int bodyNum = 0; bodyNum < n; bodyNum++) {
            ForceComputation forceComp = new ForceComputation();
            forceComp.computeOneBody(bodyNum, r, n);
        }
    }

    public static void main(String[] args) {
        initializeArrays();
        final long startTime2 = System.currentTimeMillis();
        doublyThreaded();
//        sequential();
//        singlyThreaded();
        final long endTime2 = System.currentTimeMillis();
        System.out.println("fully threaded force computation time: " + (endTime2 - startTime2));

    }

}

