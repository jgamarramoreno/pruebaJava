/******************************************************************************
 *  Compilation:  javac Point2D.java
 *  Execution:    java Point2D x0 y0 N
 *  Dependencies: StdDraw.java StdRandom.java
 *
 *  Immutable point data type for points in the plane.
 *
 ******************************************************************************/
import java.util.Arrays;
import java.util.Comparator;


/**
 *  The <tt>Point</tt> class is an immutable data type to encapsulate a
 *  two-dimensional point with real-value coordinates.
 *  <p>
 * Note: in order to deal with the difference behavior of double and 
 * Double with respect to -0.0 and +0.0, the Punto2D constructor converts
 * any coordinates that are -0.0 to +0.0.
 */
public final class Punto2D implements Comparable<Punto2D> {

    /**
     * Compares two points by x-coordinate.
     */
    public static final Comparator<Punto2D> ORDEN_X = new OrdenX();

    /**
     * Compares two points by y-coordinate.
     */
    public static final Comparator<Punto2D> ORDEN_Y = new OrdenY();

    /**
     * Compares two points by polar radius.
     */
    public static final Comparator<Punto2D> ORDEN_R = new OrdenR();

    private final double x;    // x coordinate
    private final double y;    // y coordinate

    /**
     * Initializes a new point (x, y).
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @throws IllegalArgumentException if either <tt>x</tt> or <tt>y</tt>
     *    is <tt>Double.NaN</tt>, <tt>Double.POSITIVE_INFINITY</tt> or
     *    <tt>Double.NEGATIVE_INFINITY</tt>
     */
    public Punto2D(double x, double y) {
        if (Double.isInfinite(x) || Double.isInfinite(y))
            throw new IllegalArgumentException("Las coordenadas deben ser finitas");
        if (Double.isNaN(x) || Double.isNaN(y))
            throw new IllegalArgumentException("Cordenadas no pueden ser NaN");
        if (x == 0.0) this.x = 0.0;  // convert -0.0 to +0.0
        else          this.x = x;

        if (y == 0.0) this.y = 0.0;  // convert -0.0 to +0.0
        else          this.y = y;
    }

    /**
     * Returns the x-coordinate.
     * @return the x-coordinate
     */
    public double x() {
        return x;
    }

    /**
     * Returns the y-coordinate.
     * @return the y-coordinate
     */
    public double y() {
        return y;
    }

    /**
     * Returns the polar radius of this point.
     * @return the polar radius of this point in polar coordiantes: sqrt(x*x + y*y)
     */
    public double r() {
        return Math.sqrt(x*x + y*y);
    }

    /**
     * Returns the angle of this point in polar coordinates.
     * @return the angle (in radians) of this point in polar coordiantes (between -pi/2 and pi/2)
     */
    public double theta() {
        return Math.atan2(y, x);
    }

    /**
     * Returns the angle between this point and that point.
     * @return the angle in radians (between -pi and pi) between this point and that point (0 if equal)
     */
    private double anguloHacia(Punto2D that) {
        double dx = that.x - this.x;
        double dy = that.y - this.y;
        return Math.atan2(dy, dx);
    }

    /**
     * Returns true if a->b->c is a counterclockwise turn.
     * @param a first point
     * @param b second point
     * @param c third point
     * @return { -1, 0, +1 } if a->b->c is a { clockwise, collinear; counterclocwise } turn.
     */
    public static int giroHorario(Punto2D a, Punto2D b, Punto2D c) {
        double area2 = (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
        if      (area2 < 0) return -1;
        else if (area2 > 0) return +1;
        else                return  0;
    }

    /**
     * Returns twice the signed area of the triangle a-b-c.
     * @param a first point
     * @param b second point
     * @param c third point
     * @return twice the signed area of the triangle a-b-c
     */
    public static double area2(Punto2D a, Punto2D b, Punto2D c) {
        return (b.x-a.x)*(c.y-a.y) - (b.y-a.y)*(c.x-a.x);
    }

    /**
     * Returns the Euclidean distance between this point and that point.
     * @param otro the other point
     * @return the Euclidean distance between this point and that point
     */
    public double distanciaHacia(Punto2D otro) {
        double dx = this.x - otro.x;
        double dy = this.y - otro.y;
        return Math.sqrt(dx*dx + dy*dy);
    }

    /**
     * Returns the square of the Euclidean distance between this point and that point.
     * @param otro the other point
     * @return the square of the Euclidean distance between this point and that point
     */
    public double distanciaAlCuadradoHacia(Punto2D otro) {
        double dx = this.x - otro.x;
        double dy = this.y - otro.y;
        return dx*dx + dy*dy;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * @param otro the other point
     * @return the value <tt>0</tt> if this string is equal to the argument
     *         string (precisely when <tt>equals()</tt> returns <tt>true</tt>);
     *         a negative integer if this point is less than the argument
     *         point; and a positive integer if this point is greater than the
     *         argument point
     */
    public int compareTo(Punto2D otro) {
        if (this.y < otro.y) return -1;
        if (this.y > otro.y) return +1;
        if (this.x < otro.x) return -1;
        if (this.x > otro.x) return +1;
        return 0;
    }

    /**
     * Compares two points by polar angle (between 0 and 2pi) with respect to this point.
     *
     * @return the comparator
     */
    public Comparator<Punto2D> ordenPolar() {
        return new OrdenPolar();
    }

    /**
     * Compares two points by atan2() angle (between -pi and pi) with respect to this point.
     *
     * @return the comparator
     */
    public Comparator<Punto2D> ordenAtan2() {
        return new OrdenAtan2();
    }

    /**
     * Compares two points by distance to this point.
     *
     * @return the comparator
     */
    public Comparator<Punto2D> ordenDistanciaHacia() {
        return new OrdenDistanciaHacia();
    }

    // compare points according to their x-coordinate
    private static class OrdenX implements Comparator<Punto2D> {
        public int compare(Punto2D p, Punto2D q) {
            if (p.x < q.x) return -1;
            if (p.x > q.x) return +1;
            return 0;
        }
    }

    // compare points according to their y-coordinate
    private static class OrdenY implements Comparator<Punto2D> {
        public int compare(Punto2D p, Punto2D q) {
            if (p.y < q.y) return -1;
            if (p.y > q.y) return +1;
            return 0;
        }
    }

    // compare points according to their polar radius
    private static class OrdenR implements Comparator<Punto2D> {
        public int compare(Punto2D p, Punto2D q) {
            double delta = (p.x*p.x + p.y*p.y) - (q.x*q.x + q.y*q.y);
            if (delta < 0) return -1;
            if (delta > 0) return +1;
            return 0;
        }
    }
 
    // compare other points relative to atan2 angle (bewteen -pi/2 and pi/2) they make with this Point
    private class OrdenAtan2 implements Comparator<Punto2D> {
        public int compare(Punto2D q1, Punto2D q2) {
            double angulo1 = anguloHacia(q1);
            double angulo2 = anguloHacia(q2);
            if      (angulo1 < angulo2) return -1;
            else if (angulo1 > angulo2) return +1;
            else                      return  0;
        }
    }

    // compare other points relative to polar angle (between 0 and 2pi) they make with this Point
    private class OrdenPolar implements Comparator<Punto2D> {
        public int compare(Punto2D q1, Punto2D q2) {
            double dx1 = q1.x - x;
            double dy1 = q1.y - y;
            double dx2 = q2.x - x;
            double dy2 = q2.y - y;

            if      (dy1 >= 0 && dy2 < 0) return -1;    // q1 above; q2 below
            else if (dy2 >= 0 && dy1 < 0) return +1;    // q1 below; q2 above
            else if (dy1 == 0 && dy2 == 0) {            // 3-collinear and horizontal
                if      (dx1 >= 0 && dx2 < 0) return -1;
                else if (dx2 >= 0 && dx1 < 0) return +1;
                else                          return  0;
            }
            else return -giroHorario(Punto2D.this, q1, q2);     // both above or below

            // Note: ccw() recomputes dx1, dy1, dx2, and dy2
        }
    }

    // compare points according to their distance to this point
    private class OrdenDistanciaHacia implements Comparator<Punto2D> {
        public int compare(Punto2D p, Punto2D q) {
            double dist1 = distanciaAlCuadradoHacia(p);
            double dist2 = distanciaAlCuadradoHacia(q);
            if      (dist1 < dist2) return -1;
            else if (dist1 > dist2) return +1;
            else                    return  0;
        }
    }


    /**       
     * Compares this point to the specified point.
     *       
     * @param  otro the other point
     * @return <tt>true</tt> if this point equals <tt>other</tt>;
     *         <tt>false</tt> otherwise
     */
    @Override
    public boolean equals(Object otro) {
        if (otro == this) return true;
        if (otro == null) return false;
        if (otro.getClass() != this.getClass()) return false;
        Punto2D ese = (Punto2D) otro;
        return this.x == ese.x && this.y == ese.y;
    }

    /**
     * Return a string representation of this point.
     * @return a string representation of this point in the format (x, y)
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Returns an integer hash code for this point.
     * @return an integer hash code for this point
     */
    @Override
    public int hashCode() {
        int hashX = ((Double) x).hashCode();
        int hashY = ((Double) y).hashCode();
        return 31*hashX + hashY;
    }

    /**
     * Plot this point using standard dibujar.
     */
    public void dibujar() {
        StdDraw.point(x, y);
    }

    /**
     * Plot a line from this point to that point using standard dibujar.
     * @param that the other point
     */
    public void dibujarHacia(Punto2D that) {
        StdDraw.line(this.x, this.y, that.x, that.y);
    }


    /**
     * Unit tests the point data type.
     */
    public static void main(String[] args) {
        int x0 = 2;
        int y0 = 2;
        int N = 3;

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);
        StdDraw.setPenRadius(.005);
        Punto2D[] points = new Punto2D[N];
        for (int i = 0; i < N; i++) {
            int x = StdRandom.uniform(100);
            int y = StdRandom.uniform(100);
            points[i] = new Punto2D(x, y);
            points[i].dibujar();
        }

        // dibujar p = (x0, x1) in red
        Punto2D p = new Punto2D(x0, y0);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.02);
        p.dibujar();


        // dibujar line segments from p to each point, one at a time, in polar order
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.GREEN);
        Arrays.sort(points, p.ordenPolar());
        for (int i = 0; i < N; i++) {
            p.dibujarHacia(points[i]);
            StdDraw.show(100);
        }
    }
}


