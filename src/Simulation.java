import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Simulation {
    static Random rand = new Random();

    double time = 0;
    double deltaT = Math.pow(10, -4);

    static final int n = 200;
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



    public static void multiThreaded() {
        ArrayList<Thread> computeThreads = new ArrayList<>();

        for (int bodyNum = 0; bodyNum < n; bodyNum++) {
            ForceComputation forceComp = new ForceComputation();
            forceComp.setBodyNum(bodyNum);
            computeThreads.add(new Thread(forceComp));
        }

        for (Thread t : computeThreads) {
            t.start();
        }

        for (Thread t : computeThreads){
            try {
                t.join();
            } catch (InterruptedException e){
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

        final long startTime = System.currentTimeMillis();
        initializeArrays();

        //multiThreaded();
        sequential();

        final long endTime = System.currentTimeMillis();
        System.out.println("Total execution time: " + (endTime - startTime));
        System.out.println(Arrays.toString(bodyForces));

    }

}

