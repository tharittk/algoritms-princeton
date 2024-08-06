import edu.princeton.cs.algs4.Picture;
import edu.princeton.cs.algs4.Queue;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private Picture pic, picT;
    private double[][] energies, energiesT;

    private boolean inTranspose = false;
    private boolean hasTranspose = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.pic = picture;
        this.energies = new double[pic.width()][pic.height()];
        this.computeEnergyAll();

    }

    // current picture
    public Picture picture() {
        return this.pic;
    }

    // width of current picture
    public int width() {
        return this.pic.width();
    }

    // height of current picture
    public int height() {
        return this.pic.height();
    }

    // get energy of pixel at column x and row y
    public double energy(int x, int y) {
        return this.energies[x][y];
    }

    // compute energy of pixel at column x and row y
    private double computeEnergyPixel(int x, int y) {
        // border
        if (x == 0 || x == (this.width() - 1) || y == 0 || y == (this.height() - 1)) {
            return 1000;
        }

        Color top, bottom, left, right;
        int rx, gx, bx, ry, gy, by, dx2, dy2;
        top = pic.get(x, y - 1);
        bottom = pic.get(x, y + 1);
        left = pic.get(x - 1, y);
        right = pic.get(x + 1, y);

        rx = right.getRed() - left.getRed();
        gx = right.getGreen() - left.getGreen();
        bx = right.getBlue() - left.getBlue();

        ry = bottom.getRed() - top.getRed();
        gy = bottom.getGreen() - top.getGreen();
        by = bottom.getBlue() - top.getBlue();

        dx2 = (rx * rx) + (gx * gx) + (bx * bx);
        dy2 = (ry * ry) + (gy * gy) + (by * by);

        return Math.sqrt(dx2 + dy2); // no casting need ?
    }

    // compute energy of at all pixels
    private void computeEnergyAll() {
        for (int col = 0; col < this.width(); col++) { // will col in inner loop faster
            for (int row = 0; row < this.height(); row++) {
                this.energies[col][row] = computeEnergyPixel(col, row);
            }
        }
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        if (!inTranspose) {
            turnTransposeOn();
        }
        int[] seam = findVerticalSeam();
        turnTransposeOff();
        return seam;
    }

    // sequence of indices for vertical seam
    // fail 7x10 && 10x10 -- your approach is somewhat greedy... wrong
    public int[] findVerticalSeam() {
        int[] minEPath = new int[this.height()];
        int[] path = new int[this.height()];

        double minE = Double.POSITIVE_INFINITY;

        double[][] eTo = new double[this.width()][this.height()];
        int[][] colTo = new int[this.width()][this.height()];


        for (int src = 0; src < this.width(); src++) {
            eTo[src][0] = 1000;
            // init eTo
            for (int col = 0; col < this.width(); col++) {
                for (int row = 1; row < this.height(); row++) {
                    eTo[col][row] = Double.POSITIVE_INFINITY;
                }
            }
            // relax sub node
            Queue<Integer> q = new Queue<Integer>();
            int rowCount = 0;
            q.enqueue(src);
            while (rowCount < (this.height() - 1)) {
                int col = q.dequeue();
                relax(col, rowCount, eTo, colTo);

                if (col == 0) {
                    q.enqueue(col);
                    q.enqueue(col + 1);
                }
                else if (col == (this.width() - 1)) {
                    q.enqueue(col - 1);
                    q.enqueue(col);
                }
                else {
                    q.enqueue(col - 1);
                    q.enqueue(col);
                    q.enqueue(col + 1);
                }
                rowCount++;
            }

            // pick min path from last row and track back
            int minCol = 0;
            double pathE = Double.POSITIVE_INFINITY;

            for (int col = 0; col < this.width(); col++) {
                double e = eTo[col][this.height() - 1];
                // System.out.println("eTo" + col + " |  " + (this.height() - 1) + "::" + e);
                if (e < pathE) {
                    pathE = e;
                    minCol = col;
                }
            }
            // get path
            path[this.height() - 1] = minCol;
            for (int row = (this.height() - 1); row > 0; row--) {
                path[row - 1] = colTo[minCol][row];
                minCol = colTo[minCol][row];
            }
            // compare to other src
            if (pathE < minE) {
                minE = pathE;
                minEPath = Arrays.copyOf(path, path.length);
            }
        }
        return minEPath;
    }

    private void relax(int col, int row, double[][] eTo, int[][] colTo) {
        if (col == 0) { // left edge
            double e1 = energy(col, row + 1) + eTo[col][row];
            double e2 = energy(col + 1, row + 1) + eTo[col][row];

            if (e1 < eTo[col][row + 1]) {
                eTo[col][row + 1] = e1;
                colTo[col][row + 1] = col;
            }
            if (e2 < eTo[col + 1][row + 1]) {
                eTo[col + 1][row + 1] = e2;
                colTo[col + 1][row + 1] = col;
            }
        }
        else if (col == (this.width() - 1)) { // right edge
            double e1 = energy(col - 1, row + 1) + eTo[col][row];
            double e2 = energy(col, row + 1) + eTo[col][row];

            if (e1 < eTo[col - 1][row + 1]) {
                eTo[col - 1][row + 1] = e1;
                colTo[col - 1][row + 1] = col;
            }
            if (e2 < eTo[col][row + 1]) {
                eTo[col][row + 1] = e2;
                colTo[col][row + 1] = col;
            }
        }
        else {
            double e1 = energy(col - 1, row + 1) + eTo[col][row];
            double e2 = energy(col, row + 1) + eTo[col][row];
            double e3 = energy(col + 1, row + 1) + eTo[col][row];

            if (e1 < eTo[col - 1][row + 1]) {
                eTo[col - 1][row + 1] = e1;
                colTo[col - 1][row + 1] = col;
            }
            if (e2 < eTo[col][row + 1]) {
                eTo[col][row + 1] = e2;
                colTo[col][row + 1] = col;
            }
            if (e3 < eTo[col + 1][row + 1]) {
                eTo[col + 1][row + 1] = e3;
                colTo[col + 1][row + 1] = col;
            }
        }
    }

    private Picture getPicTranspose(Picture picture) {
        Picture pictureT = new Picture(picture.height(), picture.width());
        for (int col = 0; col < picture.width(); col++) {
            for (int row = 0; row < picture.height(); row++) {
                pictureT.setRGB(row, col, picture.getRGB(col, row));
            }
        }
        return pictureT;
    }

    private double[][] getEnergyTranspose(double[][] e) {
        double[][] eT = new double[e[0].length][e.length];
        for (int col = 0; col < e.length; col++) {
            for (int row = 0; row < e[0].length; row++) {
                eT[row][col] = e[col][row];
            }
        }
        return eT;
    }

    private void turnTransposeOn() {
        // switching to transpose by exchanging pointer
        if (!hasTranspose) {
            this.picT = getPicTranspose(this.pic);
            this.energiesT = getEnergyTranspose(this.energies);
            hasTranspose = true;
        }

        if (!inTranspose) {
            swapPicAndEPointer();
            inTranspose = true;
        }
    }

    private void turnTransposeOff() {
        assert (hasTranspose);
        // swap pointer
        if (inTranspose) {
            swapPicAndEPointer();
            inTranspose = false;
        }
    }

    // swap Pic and Energy pointer so that everything in findVerticalSeam works
    // flawlessly
    private void swapPicAndEPointer() {
        double[][] tmpE = this.energies;
        Picture tmpPic = this.pic;

        this.energies = this.energiesT;
        this.energiesT = tmpE;

        this.pic = this.picT;
        this.picT = tmpPic;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (!inTranspose) {
            turnTransposeOn();
        }
        removeVerticalSeam(seam);
        turnTransposeOff();
    }

    public void removeVerticalSeam(int[] seam) {
        assert (!inTranspose);
        // shift array
        for (int row = 0; row < seam.length; row++) {
            for (int col = seam[row]; col < (this.width() - 1); col++) {
                this.energies[col][row] = this.energies[col + 1][row];
            }
        }

        // recalculate energy left and right of the seam
        for (int row = 0; row < seam.length; row++) {
            // lhs of old seam
            computeEnergyPixel(seam[row] - 1, row);
            // rhs of old seam
            computeEnergyPixel(seam[row], row);
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // Picture pic = new Picture("./chameleon.png");
        // pic.show();
    }

}
