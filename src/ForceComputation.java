

public class ForceComputation extends Simulation implements Runnable {

    private int bodyNum;
    Vector totalForce;

    // computeOneBody itself need not be concurrent
    synchronized // only one thread can execute computeOneBody at a time
    public void computeOneBody(int bodyNum, double r, int n) {
        for (int i = 0; i < bodyNum; i++) {
            Vector firstMassPos = new Vector(xPositions[i], yPositions[i]);
            Vector secondMassPos = new Vector(xPositions[bodyNum], yPositions[bodyNum]);
            Vector separation = firstMassPos.subtract(secondMassPos);
            double currentDistance = Vector.distance(firstMassPos, secondMassPos);
            Vector unitSep = separation.normalize();
            double factor = (20*(2*r/Math.pow(currentDistance, 2) - 0.5*r/Math.pow(currentDistance,3)));
            totalForce = totalForce.add(unitSep.multiply(factor));
        }

        for (int i = bodyNum+1; i < n; i++) {
            Vector firstMassPos = new Vector(xPositions[i], yPositions[i]);
            Vector secondMassPos = new Vector(xPositions[bodyNum], yPositions[bodyNum]);
            Vector separation = firstMassPos.subtract(secondMassPos);
            double currentDistance = Vector.distance(firstMassPos, secondMassPos);
            Vector unitSep = separation.normalize();
            double factor = (20*(2*r/Math.pow(currentDistance, 2) - 0.5*r/Math.pow(currentDistance,3)));
            totalForce = totalForce.add(unitSep.multiply(factor));
        }
    }

    // make addForces concurrent; get one thread to do each j'th body separately

    public void run() {
        computeOneBody(bodyNum, r, n);
        bodyForces[bodyNum] = totalForce;
    }


    public void setBodyNum(int bodyNum) {
        this.bodyNum = bodyNum;
    }
}
