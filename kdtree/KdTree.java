/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;

public class KdTree {
    private Node root;
    private int treeSize = 0;

    // construct an empty set of points
    public KdTree() {
    }

    private class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
            this.rect = null; // Or calculate the rectangle here based on p
            this.lb = null;
            this.rt = null;
        }
    }


    // is the set empty?
    public boolean isEmpty() {
        if (root == null) {
            assert treeSize == 0;
            return true;
        }
        else {
            return false;
        }
    }

    // number of points in the set
    public int size() {
        return this.treeSize;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null point inserted !");
        // isRed is always true at root node
        root = insert(root, p, true);

        // System.out.println("Sucessfully insert: " + p);
    }

    // helper function for insert
    private Node insert(Node currentNode, Point2D p, boolean isRed) {

        // not yet exist
        if (currentNode == null) {
            this.treeSize++;
            return new Node(p);
        }
        // found already exist
        if (p.x() == currentNode.p.x() && p.y() == currentNode.p.y()) {
            return currentNode;
        }

        // Continue recursive search
        // compare x
        if (isRed) {
            // go left
            if (p.x() < currentNode.p.x()) {
                currentNode.lb = insert(currentNode.lb, p, false);
            }
            // go right
            else {
                currentNode.rt = insert(currentNode.rt, p, false);
            }
        }
        // compare y
        else {
            // go left
            if (p.y() < currentNode.p.y()) {
                currentNode.lb = insert(currentNode.lb, p, true);
            }
            // go right
            else {
                currentNode.rt = insert(currentNode.rt, p, true);
            }
        }

        // not-ever to come here... but if comes make sure it is the point that already there
        assert (p.x() == currentNode.p.x() && p.y() == currentNode.p.y());
        return currentNode;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("null argument !");

        return this.get(p) != null;


    }

    // helper function for get starting from root
    private Point2D get(Point2D p) {
        return get(root, p, true);
    }

    private Point2D get(Node currentNode, Point2D p, boolean isRed) {
        if (p == null) throw new IllegalArgumentException("call get with null point!");
        // not found
        if (currentNode == null) return null;
        // found
        if (p.x() == currentNode.p.x() && p.y() == currentNode.p.y()) return currentNode.p;

        // compare x
        if (isRed) {

            if (p.x() < currentNode.p.x()) {
                return get(currentNode.lb, p, false);
            }
            else {
                return get(currentNode.rt, p, false);
            }
        }

        // compare y
        else {
            if (p.y() < currentNode.p.y()) {
                return get(currentNode.lb, p, true);
            }
            else {
                return get(currentNode.rt, p, true);
            }
        }
    }

    // draw all points to standard draw
    public void draw() {

    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        return new ArrayList<Point2D>();
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        return new Point2D(0, 0);

    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        System.out.println("Test running");
        In in = new In(args[0]);
        KdTree kdTree = new KdTree();

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 1);
        StdDraw.setYscale(0, 1);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.03);

        // Read until the end of the file
        int count = 1;
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            // Create and add Point2D objects
            Point2D p = new Point2D(x, y);
            Point2D q;
            kdTree.insert(p);

            // test get and contains
            if (kdTree.contains(p)) {
                q = kdTree.get(p);
                q.draw();
                StdDraw.text(q.x(), q.y() + 0.05, Integer.toString(count));
                count++;
            }
        }
        in.close();
        StdDraw.show();

    }
}
