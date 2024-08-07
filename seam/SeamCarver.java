import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private double[][] energies;

    private boolean inTranspose = false;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("null argument");
        }
        this.pic = new Picture(picture);
        this.energies = this.computeEnergyAll();


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
        if (x < 0 || x >= this.width()) {
            throw new IllegalArgumentException("x outside range");
        }
        if (y < 0 || y >= this.height()) {
            throw new IllegalArgumentException("y outside range");
        }
        return this.energies[x][y];
    }

    // compute energy of pixel at column x and row y
    private double computeEnergyPixel(int x, int y) {
        if (x < 0 || x >= this.width()) {
            throw new IllegalArgumentException("x outside range");
        }
        if (y < 0 || y >= this.height()) {
            throw new IllegalArgumentException("y outside range");
        }
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
    private double[][] computeEnergyAll() {
        double[][] e = new double[this.width()][this.height()];
        for (int col = 0; col < this.width(); col++) { // will col in inner loop faster
            for (int row = 0; row < this.height(); row++) {
                e[col][row] = computeEnergyPixel(col, row);
            }
        }
        return e;
    }

    public int[] findVerticalSeam() {
        return findSeam(true);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findSeam(false);
    }

    // sequence of indices for vertical seam
    private int[] findSeam(boolean vertical) {
        if (vertical) {
            if (inTranspose) turnTransposeOff();
        }
        else {
            if (!inTranspose) turnTransposeOn();
        }

        int[][] edgeTo = new int[this.width()][this.height()];
        double[][] distTo = new double[this.width()][this.height()];
        for (int row = 0; row < this.height(); row++) {
            for (int col = 0; col < this.width(); col++) {
                distTo[col][row] = Double.POSITIVE_INFINITY;
                if (row == 0) distTo[col][row] = this.energies[col][row];
            }
        }
        // topological order must begin row-by-row i.e., column sweep
        for (int row = 0; row < this.height() - 1; row++) { // not to relax last row
            for (int col = 0; col < this.width(); col++) { // left edge
                if (distTo[col][row + 1] > distTo[col][row] + this.energies[col][row + 1]) {
                    distTo[col][row + 1] = distTo[col][row] + this.energies[col][row + 1];
                    edgeTo[col][row + 1] = col;
                }
                if (col - 1 > 0) { // right edge
                    if (distTo[col - 1][row + 1] > distTo[col][row] + this.energies[col - 1][row
                            + 1]) {
                        distTo[col - 1][row + 1] = distTo[col][row] + this.energies[col - 1][row
                                + 1];
                        edgeTo[col - 1][row + 1] = col;
                    }
                }
                if (col + 1 < this.width()) { // interior
                    if (distTo[col + 1][row + 1] > distTo[col][row] + this.energies[col + 1][row
                            + 1]) {
                        distTo[col + 1][row + 1] = distTo[col][row] + this.energies[col + 1][row
                                + 1];
                        edgeTo[col + 1][row + 1] = col;
                    }
                }
            }
        }
        // last row with lowest energy
        int minCol = 0;
        double minE = Double.POSITIVE_INFINITY;
        for (int col = 0; col < this.width(); col++) {
            if (minE > distTo[col][this.height() - 1]) {
                minE = distTo[col][this.height() - 1];
                minCol = col;
            }
        }
        // backtrack to get path
        int[] minESeam = new int[this.height()];
        minESeam[this.height() - 1] = minCol;
        for (int row = (this.height() - 1); row > 0; row--) {
            minCol = edgeTo[minCol][row];
            minESeam[row - 1] = minCol;
        }
        // I DON"T LIKE THIS... HOWEVER, if we don't do this, the image will never return to original state
        if (!vertical) {
            turnTransposeOff();
        }
        return minESeam;
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
        if (!inTranspose) {
            this.pic = getPicTranspose(this.pic);
            this.energies = getEnergyTranspose(this.energies);
        }
        inTranspose = true;
    }

    private void turnTransposeOff() {
        if (inTranspose) {
            this.pic = getPicTranspose(this.pic);
            this.energies = getEnergyTranspose(this.energies);
        }
        inTranspose = false;
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        removeSeam(seam, false);
    }

    public void removeVerticalSeam(int[] seam) {
        removeSeam(seam, true);
    }

    private void removeSeam(int[] seam, boolean vertical) {

        if (seam == null) {
            throw new IllegalArgumentException("null argument");
        }
        // get the picture ready
        // System.out.println("Before condition w h: " + this.width() + " " + this.height());
        if (vertical) {
            if (inTranspose) turnTransposeOff();
            // System.out.println("Calling Vertical");
        }
        else {
            if (!inTranspose) turnTransposeOn();
            // System.out.println("Calling Horizontal");
        }
        // System.out.println("after condition w h: " + this.width() + " " + this.height());

        // check seam more
        if (this.width() <= 1) throw new IllegalArgumentException("width already <= 1");
        if (seam.length != this.height())
            throw new IllegalArgumentException(
                    "seam length is wrong " + seam.length + " expected " + this.height());
        validateSeam(seam);

        Picture newPic = new Picture(this.width() - 1, this.height());

        for (int row = 0; row < seam.length; row++) {
            for (int col = 0; col < this.width(); col++) {
                if (col < seam[row]) {
                    newPic.setRGB(col, row, this.pic.getRGB(col, row));
                }
                else if (col > seam[row]) {
                    newPic.setRGB(col - 1, row, this.pic.getRGB(col, row));
                }
            }
        }

        this.pic = newPic;

        // // shift array
        // for (int row = 0; row < seam.length; row++) {
        //     for (int col = seam[row]; col < (this.width() - 1); col++) {
        //         this.energies[col][row] = this.energies[col + 1][row];
        //     }
        // }
        //
        // // recalculate energy left and right of the seam
        // for (int row = 0; row < seam.length; row++) {
        //     // lhs of old seam
        //     computeEnergyPixel(seam[row] - 1, row);
        //     // rhs of old seam
        //     computeEnergyPixel(seam[row], row);
        // }
        if (!vertical) {
            turnTransposeOff();
        }
    }

    private void validateSeam(int[] seam) {
        for (int i = 0; i < (seam.length - 1); i++) {
            if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                throw new IllegalArgumentException("invalid seam");
            }
        }
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] >= this.width()) {
                throw new IllegalArgumentException("invalid seam");
            }
        }
    }

    // unit testing (optional)
    public static void main(String[] args) {
        // Picture pic = new Picture("./chameleon.png");
        // pic.show();
    }

}
