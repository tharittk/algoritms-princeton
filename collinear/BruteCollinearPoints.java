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

public class BruteCollinearPoints {
    // dynamically grow in size
    private ArrayList<LineSegment> arrayListLineSegments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException("null input");
        for (Point p : points) {
            if (p == null) throw new IllegalArgumentException("null point exists");
        }

        for (int i = 0; i < points.length; i++) {
            for (int j = i + 1; j < points.length; j++) {

                Point p = points[i];
                Point q = points[j];
                // repeated point check
                int comparePQ = p.compareTo(q);
                if (comparePQ == 0)
                    throw new IllegalArgumentException("repeated points exists");

                for (int k = j + 1; k < points.length; k++) {
                    Point r = points[k];
                    // repeated point check
                    int comparePR = p.compareTo(r);
                    int compareQR = q.compareTo(r);
                    if (comparePR == 0 || compareQR == 0)
                        throw new IllegalArgumentException("repeated points exists");

                    for (int m = k + 1; m < points.length; m++) {

                        Point s = points[m];
                        // repeated point check
                        int comparePS = p.compareTo(s);
                        int compareQS = q.compareTo(s);
                        int compareRS = r.compareTo(s);

                        if (comparePS == 0 || compareQS == 0 || compareRS == 0)
                            throw new IllegalArgumentException("repeated points exists");

                        double slopePQ = p.slopeTo(q);
                        double slopePR = p.slopeTo(r);
                        double slopePS = p.slopeTo(s);

                        // avoid numerical precision issue (not using ==)
                        int comparePQPR = Double.compare(slopePQ, slopePR);
                        int comparePQPS = Double.compare(slopePQ, slopePS);
                        int comparePRPS = Double.compare(slopePQ, slopePS);

                        if (comparePQPR == 0 && comparePQPS == 0 && comparePRPS == 0) {
                            // sort to
                            Point[] validFourPoints = new Point[4];
                            validFourPoints[0] = p;
                            validFourPoints[1] = q;
                            validFourPoints[2] = r;
                            validFourPoints[3] = s;
                            Arrays.sort(validFourPoints);

                            arrayListLineSegments.add(
                                    new LineSegment(validFourPoints[0], validFourPoints[3]));
                        }


                    }
                }
            }
        } // end of n4 loop

    }

    // the number of line segments
    public int numberOfSegments() {
        return arrayListLineSegments.size();
    }

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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
