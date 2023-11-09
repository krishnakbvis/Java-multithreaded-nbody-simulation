import java.util.ArrayList;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Simulation {
    double time = 0;
    double deltaT = 10^-4;

    static int n = 50;
    double r = 0.3;

    double[] xPositions = new double[n];
    double[] yPositions = new double[n];
    double[] xVelocities = new double[n];
    double[] yVelocities = new double[n];

    Vector[] bodyForces = new Vector[n];


    public static void main(String[] args) {

        ArrayList<Thread> computeThreads = new ArrayList<>();


        for (int bodyNum = 0; bodyNum < n; bodyNum++) {
            ForceComputation forceComp = new ForceComputation();
            forceComp.setBodyNum(bodyNum);
            forceComp.totalForce = new Vector(0,0);
            Thread forceCompute = new Thread(forceComp);

            computeThreads.add(forceCompute);
        }

        for (Thread t : computeThreads) {
            t.start();
        }




    }

}

