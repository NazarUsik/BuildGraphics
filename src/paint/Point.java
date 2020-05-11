package paint;

import java.io.Serializable;

/**
 * Class Point storage coordinates
 * <p></p>
 * x and y coordinate to represent point in 2D space
 */

public class Point implements Comparable, Serializable {
    private double x;
    private double y;

    public Point() {

    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return ("x: " + this.x + "" +
                "\ny: " + this.y);
    }

    public Point(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    @Override
    public int compareTo(Object o) {
        Point p = (Point) o;
        return Double.compare(this.y, p.y);
    }
}