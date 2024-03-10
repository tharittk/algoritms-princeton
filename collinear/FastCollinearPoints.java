/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

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


        for (Point p : points) {

            Comparator<Point> slopeComparator = p.slopeOrder();
            Arrays.sort(pointsCopy, slopeComparator);
            // first index will be itself (-inf)
            // find consecutive slopes at least 4 points
            for (int i = 1; i < points.length - 3; i++) {
                if (isThreeConsecutiveSlope(i, p, pointsCopy)) {
                    int nPointsForSegment = 4;
                    // if not yet max-out points
                    if (i < points.length - 4) {
                        nPointsForSegment = accumulateMorePointWithSameSlope(i, p, pointsCopy);
                    }
                }
                // sort collinear points with its order


            }

        }

    }

    private boolean isThreeConsecutiveSlope(int i, Point ref, Point[] points) {
        Point p = points[i];
        Point q = points[i + 1];
        Point r = points[i + 2];

        // repeated point check
        int compareRefP = ref.compareTo(p);
        int compareRefQ = ref.compareTo(q);
        int compareRefR = ref.compareTo(r);

        int comparePQ = p.compareTo(q);
        int comparePR = p.compareTo(r);
        int compareQR = q.compareTo(r);

        if (comparePQ == 0 || comparePR == 0 || compareQR == 0
                || compareRefP == 0 || compareRefQ == 0
                || compareRefR == 0)
            throw new IllegalArgumentException("repeated points exists");


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

            // check consecutive points
            Point p = points[i];
            Point q = points[i + 1];
            Point r = points[i + 2];
            int compareRefJ = ref.compareTo(points[j]);
            int comparePQ = p.compareTo(q);
            int comparePR = p.compareTo(r);
            int compareQR = q.compareTo(r);

            if (comparePQ == 0 || comparePR == 0 || compareQR == 0
                    || compareRefP == 0 || compareRefQ == 0
                    || compareRefR == 0)
                throw new IllegalArgumentException("repeated points exists");

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

    }
}
