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

    // private class node to link the tree
    private class Node {
        private Point2D p;      // the point
        private RectHV rRect;   // the axis-aligned rectangle corresponding to this node
        private RectHV lRect;
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
            this.lRect = null;
            this.rRect = null;
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
        root = insert(root, null, p, true, true);
        // need edge case for root node rectangle creation !


        // System.out.println("Sucessfully insert: " + p);
    }

    private static void assignRectanglesToNode(Node insertNode, Node parentNode, Point2D p,
                                               boolean parentIsVerical,
                                               boolean insertAsLeftChild) {

        double xminParent, xmaxParent, yminParent, ymaxParent;

        // case of root node
        if (parentNode == null) {
            xminParent = 0.0;
            xmaxParent = 1.0;
            yminParent = 0.0;
            ymaxParent = 1.0;
        }
        else {
            if (insertAsLeftChild) {
                xminParent = parentNode.lRect.xmin();
                xmaxParent = parentNode.lRect.xmax();
                yminParent = parentNode.lRect.ymin();
                ymaxParent = parentNode.lRect.ymax();
            }
            else {
                xminParent = parentNode.rRect.xmin();
                xmaxParent = parentNode.rRect.xmax();
                yminParent = parentNode.rRect.ymin();
                ymaxParent = parentNode.rRect.ymax();
            }
        }

        if (parentIsVerical) {
            insertNode.lRect = new RectHV(xminParent, yminParent, p.x(), ymaxParent);
            insertNode.rRect = new RectHV(p.x(), yminParent, xmaxParent, ymaxParent);
        }
        else {
            insertNode.lRect = new RectHV(xminParent, yminParent, xmaxParent, p.y());
            insertNode.rRect = new RectHV(xminParent, p.y(), xmaxParent, ymaxParent);
        }


    }

    // helper function for insert
    private Node insert(Node currentNode, Node parentNode, Point2D p, boolean parentIsVertical,
                        boolean insertAsLeftChild) {
        // not yet exist
        if (currentNode == null) {
            this.treeSize++;

            // construct rec here to p

            Node insertNode = new Node(p);

            assignRectanglesToNode(insertNode, parentNode, p, parentIsVertical, insertAsLeftChild);

            if (parentNode != null)
                System.out.println("inserting" + insertNode.p + "with parent:" + parentNode.p);
            else System.out.println("inserting root");

            return insertNode;
        }
        // found already exist
        if (p.x() == currentNode.p.x() && p.y() == currentNode.p.y()) {
            return currentNode;
        }
        // Continue recursive search
        parentNode = currentNode;

        // compare x
        if (parentIsVertical) {
            // go left
            if (p.x() < currentNode.p.x()) {
                currentNode.lb = insert(currentNode.lb, parentNode, p, false, true);
            }
            // go right
            else {
                currentNode.rt = insert(currentNode.rt, parentNode, p, false, false);
            }
        }
        // compare y
        else {
            // go left
            if (p.y() < currentNode.p.y()) {
                currentNode.lb = insert(currentNode.lb, parentNode, p, true, true);
            }
            // go right
            else {
                currentNode.rt = insert(currentNode.rt, parentNode, p, true, false);
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

    private Point2D get(Node currentNode, Point2D p, boolean parentIsVertical) {
        if (p == null) throw new IllegalArgumentException("call get with null point!");
        // not found
        if (currentNode == null) return null;
        // found
        if (p.x() == currentNode.p.x() && p.y() == currentNode.p.y()) return currentNode.p;

        // compare x
        if (parentIsVertical) {

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
        System.out.println("Tree size: " + kdTree.size());

    }
}
