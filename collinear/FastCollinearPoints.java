/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private ArrayList<LineSegment> arrayListLineSegments = new ArrayList<LineSegment>();
    private ArrayList<Point> arrayListPointPair = new ArrayList<Point>();

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null input");

        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("null point exists");
        }


        // copy to prevent modifying original - this will be used in in-place sorting
        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, points.length);


        for (Point p : points) {
            Comparator<Point> slopeComparator = p.slopeOrder();
            Arrays.sort(pointsCopy, slopeComparator);


            // pre-compute slope
            double[] slopesToP = new double[points.length];
            for (int i = 1; i < points.length; i++) slopesToP[i] = p.slopeTo(pointsCopy[i]);
            // search for 3 consecutive points
            // for (int i = 1; i < points.length - 1; i++) {
            int j = 1;
            while (j < points.length - 1) {
                double prevSlope = slopesToP[j - 1];
                double currentSlope = slopesToP[j];
                double nextSlope = slopesToP[j + 1];

                int k = j + 1;
                if ((Double.compare(prevSlope, currentSlope) == 0) && (
                        Double.compare(currentSlope, nextSlope) == 0)) {
                    // find next
                    while ((k < points.length) && (Double.compare(slopesToP[k], currentSlope)
                            == 0)) {
                        k++;
                    }
                    // sort collinear points with its order
                    int nPointForSegment = (k - j + 1);
                    Point[] validPoints = new Point[nPointForSegment + 1];
                    validPoints[0] = p;
                    int count = 1;
                    for (int m = j - 1; m < k; m++) {
                        validPoints[count] = pointsCopy[m];
                        count++;
                    }
                    Arrays.sort(validPoints);
                    arrayListPointPair.add(validPoints[0]);
                    arrayListPointPair.add(validPoints[nPointForSegment]);

                }
                // end of sequence
                j = k;

            }


        }

    }


    // the number of line segments
    public int numberOfSegments() {
        return arrayListLineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {


        ArrayList<Integer> hashedUsedPair = new ArrayList<Integer>();

        // System.out.println("Total Pairs: " + arrayListPointPair.size() / 2);
        for (int i = 0; i < arrayListPointPair.size(); i += 2) {
            Point p = arrayListPointPair.get(i);
            Point q = arrayListPointPair.get(i + 1);

            int hashP = System.identityHashCode(p);
            int hashQ = System.identityHashCode(q);
            int hashCode = 3 * hashP + 7 * hashQ;

            if (!hashedUsedPair.contains(hashCode)) {
                arrayListLineSegments.add(new LineSegment(p, q));
                hashedUsedPair.add(hashCode);
            }

        }

        LineSegment[] segments = new LineSegment[arrayListLineSegments.size()];
        int i = 0;
        for (LineSegment segment : arrayListLineSegments) {
            segments[i] = segment;
            i++;
        }
        return segments;
    }


    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
