

public class ForceComputation extends Simulation implements Runnable {

    private int chunkNum; // gives the chunk of word that the thread is performing on

    // divide up work such that
    // each thread does computation on one chunk of bodies (given by n/numWorkers)
    synchronized
    public void workChunk(int chunkNum, double r, int n) {
        Vector totalForce = new Vector(0,0);
        int lowerBound = chunkNum*chunkSize+1;
        int upperBound = (chunkNum+1)*chunkSize;
        System.out.println(lowerBound);
        System.out.println(upperBound);

        for (int j = chunkNum*chunkSize+1; j < (chunkNum+1)*chunkSize; j++) {
            for (int i = 0; i < n; i++) {
                if (i != j) {
                    Vector firstMassPos = new Vector(xPositions[i], yPositions[i]);
                    Vector secondMassPos = new Vector(xPositions[j], yPositions[j]);
                    Vector separation = firstMassPos.subtract(secondMassPos);
                    double currentDistance = Vector.distance(firstMassPos, secondMassPos);
                    Vector unitSep = separation.normalize();
                    double factor = (20 * (2 * r / Math.pow(currentDistance, 2) - 0.5 * r / Math.pow(currentDistance, 3)));
                    totalForce = totalForce.add(unitSep.multiply(factor));
                }
            }
            bodyForces[j] = totalForce;
        }
    }


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
        bodyForces[bodyNum] = totalForce;
    }


    // make addForces concurrent; get one thread to do each j'th body separately

    public void run() {
        workChunk(chunkNum, r, n);
    }


    public void setWorkChunk(int chunkNum) {
        this.chunkNum = chunkNum;
    }
}
