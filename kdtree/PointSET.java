/* *****************************************************************************
 *  Name: Tharit T.
 *  Date: March 31.
 *  Description: Find all points that is inside quried rectangle and the nearest point to the queried point.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    // construct an empty set of points
    private TreeSet<Point2D> points;

    public PointSET() {
        this.points = new TreeSet<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return points.isEmpty();
    }

    // number of points in the set
    public int size() {
        return points.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        points.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : points) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return points;

    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return points.last();
    }

    // unit testing of the methods (optional)

    public static void main(String[] args) {
        System.out.println("Test running");
        In in = new In(args[0]);
        // Use a Red-Black BST (TreeSet) for efficient ordering
        PointSET pointSet = new PointSET();
        // Read until the end of the file
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            // Create and add Point2D objects
            pointSet.insert(new Point2D(x, y));
        }

        in.close();

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        pointSet.draw();
        StdDraw.show();
    }
}
