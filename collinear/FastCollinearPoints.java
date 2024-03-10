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

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null input");

        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("null point exists");
        }


        // copy to prevent modifying original - this will be used in in-place sorting
        Point[] pointsCopy = new Point[points.length];
        System.arraycopy(points, 0, pointsCopy, 0, points.length);

        // repeated point will surely have similar slope to origin
        Point originPoint = new Point(0, 0);
        Comparator<Point> slopeComparatorOrigin = originPoint.slopeOrder();
        Arrays.sort(pointsCopy, slopeComparatorOrigin);
        for (int k = 0; k < pointsCopy.length - 1; k++) {
            if (pointsCopy[k].compareTo(pointsCopy[k + 1]) == 0)
                throw new IllegalArgumentException("Repeated point exists");
        }


        for (Point p : points) {
            Comparator<Point> slopeComparator = p.slopeOrder();
            Arrays.sort(pointsCopy, slopeComparator);
            // find consecutive slopes at least 4 points
            // first index will be itself (-inf)
            for (int i = 1; i < points.length - 3; i++) {
                if (isThreeConsecutiveSlope(i, p, pointsCopy)) {
                    int nPointsForSegment = 4;
                    // if not yet max-out points
                    if (i < points.length - 4) {
                        nPointsForSegment = accumulateMorePointWithSameSlope(i, p, pointsCopy);
                    }

                    // sort collinear points with its order
                    Point[] validPoints = new Point[nPointsForSegment];
                    validPoints[0] = p;
                    for (int m = 1; m < nPointsForSegment; m++) {
                        validPoints[m] = pointsCopy[i + m - 1];
                    }
                    Arrays.sort(validPoints);
                    arrayListLineSegments.add(
                            new LineSegment(validPoints[0], validPoints[nPointsForSegment - 1]));
                }


            }

        }

    }

    private boolean isThreeConsecutiveSlope(int i, Point ref, Point[] points) {
        Point p = points[i];
        Point q = points[i + 1];
        Point r = points[i + 2];

        double slopeToP = ref.slopeTo(p);
        double slopeToQ = ref.slopeTo(q);
        double slopeToR = ref.slopeTo(r);

        return (Double.compare(slopeToP, slopeToQ) == 0 && Double.compare(slopeToP, slopeToR) == 0
        );
    }


    private int accumulateMorePointWithSameSlope(int i, Point ref, Point[] points) {
        int nPointsForSegment = 4;
        double segmentSlope = ref.slopeTo(points[i + 1]);
        int j = i + 3;
        while (j < points.length) {

            if (Double.compare(segmentSlope, ref.slopeTo(points[j])) == 0) {
                nPointsForSegment++;
                j++;
            }
            else break;
        }
        return nPointsForSegment;
    }

    // the number of line segments
    public int numberOfSegments() {
        return arrayListLineSegments.size();
    }

    // the line segments
    public LineSegment[] segments() {
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
