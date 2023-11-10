public class ForceComputation extends Simulation implements Runnable {

    private int indexX; // gives the chunk of word that the thread is performing on
    private int indexY;

    private int bodyNum;
    // divide up work such that
    // each thread does computation on one chunk of bodies (given by n/numWorkers)

    // make thread[outer][inner] work on a section of the total force computation;
    public void doublyThreaded(int indexY, int indexX, double r, int n) {
        int lowerOuter = indexY*chunkSize;
        int upperOuter = (indexY+1)*chunkSize;
        for (int j = lowerOuter; j < upperOuter; j++) {
            Vector totalForce = new Vector(0,0);
            int lowerInner = indexX*chunkSize;
            int upperInner = (indexX + 1)*chunkSize;
            for (int i = lowerInner; i < upperInner; i++) {
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
            bodyForces[j] = bodyForces[j].add(totalForce);
        }
    }


    public void singlyThreaded(int indexY, double r, int n) {
        int lowerOuter = indexY*chunkSize;
        int upperOuter = (indexY+1)*chunkSize;
        for (int j = lowerOuter; j < upperOuter; j++) {
            Vector totalForce = new Vector(0,0);
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
        doublyThreaded(indexY, indexX, r, n);
//        singlyThreaded(indexY, r, n);
    }

    public void setThreadIdx(int indexY) {
        this.indexY = indexY;
    }
    public void setThreadIdx(int indexX, int indexY) {
        this.indexX = indexX;
        this.indexY = indexY;
    }
}
