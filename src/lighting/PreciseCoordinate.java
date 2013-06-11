package lighting;

/**
 *
 * @author Travis
 */
public class PreciseCoordinate {
    double x;
    double y;
    
    PreciseCoordinate(double x, double y){
        this.x = x;
        this.y = y;
    }
    
    public double getX() {
        return x;
    }
    
    public double getY() {
        return y;
    }
    
    void addToX(double incrementor){
        x = x + incrementor;
    }
    
    void addToY(double incrementor) {
        y = y + incrementor;
    }
    
    public String toString() {
        return "(" + x + "," + y + ")";
    }
}
