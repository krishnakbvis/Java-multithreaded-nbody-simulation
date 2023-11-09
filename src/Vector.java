public class Vector extends Thread{
    double xComp;
    double yComp;

    public Vector(double x, double y) {
        this.xComp = x;
        this.yComp = y;
    }
    public Vector add(Vector v){
        return new Vector(this.xComp + v.xComp, this.yComp + v.yComp);
    }
    public Vector multiply(double lambda){
        return new Vector(lambda*this.xComp, lambda*this.yComp);
    }

    public Vector subtract(Vector v) {
        return this.add(v.multiply(-1));
    }

    public double magnitude() {
        return Math.sqrt(this.dot(this));
    }

    public double dot(Vector v) {
        return (this.xComp)*(v.xComp) + (this.yComp)*(v.yComp);
    }

    public static double distance(Vector v1, Vector v2) {
        Vector separation = new Vector(v1.xComp - v2.xComp, v1.yComp - v2.yComp);
        return separation.magnitude();
    }

    public Vector normalize() {
        return new Vector(this.xComp/this.magnitude(), this.yComp/this.magnitude());
    }

}
