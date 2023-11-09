import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Simulation {
    Random rand = new Random();

    double time = 0;
    double deltaT = 10^-4;

    static int n = 10;
    double r = 0.3;

    double[] xPositions = new double[n];
    double[] yPositions = new double[n];
    double[] xVelocities = new double[n];
    double[] yVelocities = new double[n];


    public void initializeArrays() {
        for (int i = 0; i < n; i++) {
            xPositions[i] = rand.nextDouble();
            yPositions[i] = rand.nextDouble();
            xVelocities[i] = rand.nextDouble();
            yVelocities[i] = rand.nextDouble();
        }
    }

    Vector[] bodyForces = new Vector[n];


    public static void main(String[] args) {
        Simulation sim = new Simulation();
        sim.initializeArrays();
        ArrayList<Thread> computeThreads = new ArrayList<>();


        for (int bodyNum = 0; bodyNum < n; bodyNum++) {
            ForceComputation forceComp = new ForceComputation();
            forceComp.setBodyNum(bodyNum);
            forceComp.totalForce = new Vector(0,0);
            Thread forceCompute = new Thread(forceComp, "body #" + bodyNum + ": running ");

            computeThreads.add(forceCompute);
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

        System.out.println(Arrays.toString(sim.bodyForces));



    }

}

