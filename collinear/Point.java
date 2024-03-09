/******************************************************************************
 *  Compilation:  javac Point.java
 *  Execution:    java Point
 *  Dependencies: none
 *
 *  An immutable data type for points in the plane.
 *  For use on Coursera, Algorithms Part I programming assignment.
 *
 ******************************************************************************/

import edu.princeton.cs.algs4.StdDraw;

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;     // x-coordinate of this point
    private final int y;     // y-coordinate of this point

    /**
     * Initializes a new point.
     *
     * @param x the <em>x</em>-coordinate of the point
     * @param y the <em>y</em>-coordinate of the point
     */
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    /**
     * Draws this point to standard draw.
     */
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    /**
     * Draws the line segment between this point and the specified point
     * to standard draw.
     *
     * @param that the other point
     */
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    /**
     * Returns the slope between this point and the specified point.
     * Formally, if the two points are (x0, y0) and (x1, y1), then the slope
     * is (y1 - y0) / (x1 - x0). For completeness, the slope is defined to be
     * +0.0 if the line segment connecting the two points is horizontal;
     * Double.POSITIVE_INFINITY if the line segment is vertical;
     * and Double.NEGATIVE_INFINITY if (x0, y0) and (x1, y1) are equal.
     *
     * @param that the other point
     * @return the slope between this point and the specified point
     */
    public double slopeTo(Point that) {
        /* YOUR CODE HERE */
        int compareX = Integer.compare(this.x, that.x);
        int compareY = Integer.compare(this.y, that.y);

        // degenerate point
        if (compareX == 0 && compareY == 0) return Double.NEGATIVE_INFINITY;
        // horizontal line
        if (compareY == 0) return 0.0;
        // vertical
        if (compareX == 0) return Double.POSITIVE_INFINITY;

        double dx = (double) (that.x - this.x);
        double dy = (double) (that.y - this.y);
        return dy / dx;
    }

    /**
     * Compares two points by y-coordinate, breaking ties by x-coordinate.
     * Formally, the invoking point (x0, y0) is less than the argument point
     * (x1, y1) if and only if either y0 < y1 or if y0 = y1 and x0 < x1.
     *
     * @param that the other point
     * @return the value <tt>0</tt> if this point is equal to the argument
     * point (x0 = x1 and y0 = y1);
     * a negative integer if this point is less than the argument
     * point; and a positive integer if this point is greater than the
     * argument point
     */
    public int compareTo(Point that) {
        /* YOUR CODE HERE */
        int compareX = Integer.compare(this.x, that.x);
        int compareY = Integer.compare(this.y, that.y);

        if (compareY == -1 || (compareY == 0 && compareX == -1)) return -1;
        else if (compareX == 0 && compareY == 0) return 0;
        else return 1;
    }

    /**
     * Compares two points by the slope they make with this point.
     * The slope is defined as in the slopeTo() method.
     *
     * @return the Comparator that defines this ordering on points
     */
    public Comparator<Point> slopeOrder() {
        /* YOUR CODE HERE */
        return new Comparator<Point>() {
            public int compare(Point p1, Point p2) {
                double slopeToP1 = slopeTo(p1);
                double slopeToP2 = slopeTo(p2);
                return Double.compare(slopeToP1, slopeToP2);
            }
        };
    }


    /**
     * Returns a string representation of this point.
     * This method is provide for debugging;
     * your program should not rely on the format of the string representation.
     *
     * @return a string representation of this point
     */
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    private static void testSlopeOrder() {
        Point p = new Point(1, 1);  // This point is used for slope comparison

        // Points with different slopes
        Point p1 = new Point(2, 2);  // Slope = 1 (equal)
        Point p2 = new Point(3, 3);  // Slope = 1 (equal)
        Point p3 = new Point(3, 2);  // Slope = 0 (less than)
        Point p4 = new Point(2, 0);  // Slope = -1 (greater than)
        Comparator<Point> slopeComparator = p.slopeOrder();

        // Test equal slopes (lhs = rhs)
        double slopeComparison12 = slopeComparator.compare(p1, p2);
        System.out.println("Expected 0, get: " + slopeComparison12);

        // Test point with smaller slope (lhs < rhs)
        double slopeComparison13 = slopeComparator.compare(p3, p1);
        System.out.println("Expected -1, get: " + slopeComparison13);

        // Test point with larger slope (lhs > rhs)
        double slopeComparison14 = slopeComparator.compare(p1, p4);
        System.out.println("Expected 1, get: " + slopeComparison14);

        // Test degenerate point (same coordinates)
        Point degenerate = new Point(1, 1);
        double slopeComparisonDegenerate = slopeComparator.compare(p, degenerate);
        System.out.println("Slope comparison (degenerate): " + slopeComparisonDegenerate);
    }


    /**
     * Unit tests the Point data type.
     */
    public static void main(String[] args) {
        /* YOUR CODE HERE */
        System.out.println("====Test 1: Same Point ====");
        Point p1 = new Point(1, 2);
        Point p2 = new Point(1, 2);
        double slope = p1.slopeTo(p2);
        System.out.println("Expected: -inf, get: " + slope);

        System.out.println("====Test 2: Vertical Line ====");
        p1 = new Point(1, 2);
        p2 = new Point(1, 5);
        slope = p1.slopeTo(p2);
        System.out.println("Expected: +inf, get: " + slope);

        System.out.println("====Test 3: Horizontal Line ====");
        p1 = new Point(1, 2);
        p2 = new Point(4, 2);
        slope = p1.slopeTo(p2);
        System.out.println("Expected: 0, get: " + slope);

        System.out.println("====Test 4: Positive Slope  ====");
        p1 = new Point(1, 2);
        p2 = new Point(4, 8);
        slope = p1.slopeTo(p2);
        System.out.println("Expected: 2.0, get: " + slope);


        System.out.println("====Test 5: Negative Slope  ====");
        p1 = new Point(1, 8);
        p2 = new Point(4, 2);
        slope = p1.slopeTo(p2);
        System.out.println("Expected: -2.0, get: " + slope);

        testSlopeOrder();
    }
}
