

public class ForceComputation extends Simulation implements Runnable {

    private int bodyNum;

    // computeOneBody itself need not be concurrent
    // only one thread can execute computeOneBody at a time
    synchronized
    public void computeOneBody(int bodyNum, double r, int n) {
        Vector totalForce = new Vector(0,0);
        for (int i = 0; i < n; i++) {
            if (i != bodyNum) {
                Vector firstMassPos = new Vector(xPositions[i], yPositions[i]);
                Vector secondMassPos = new Vector(xPositions[bodyNum], yPositions[bodyNum]);
                Vector separation = firstMassPos.subtract(secondMassPos);
                double currentDistance = Vector.distance(firstMassPos, secondMassPos);
                Vector unitSep = separation.normalize();
                double factor = (20 * (2 * r / Math.pow(currentDistance, 2) - 0.5 * r / Math.pow(currentDistance, 3)));
                totalForce = totalForce.add(unitSep.multiply(factor));
            }
        }
        synchronized (this) {
            bodyForces[bodyNum] = totalForce;
        }
    }

    // make addForces concurrent; get one thread to do each j'th body separately

    public void run() {
        computeOneBody(bodyNum, r, n);
    }


    public void setBodyNum(int bodyNum) {
        this.bodyNum = bodyNum;
    }
}
