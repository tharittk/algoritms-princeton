import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

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
        return findVerticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] currentPath = new int[this.height()];
        int[] minEPath = new int[this.height()];
        double minSumE = Double.POSITIVE_INFINITY;

        for (int col = 0; col < this.width(); col++) {
            double currentSumE = 0;
            currentPath[0] = col;
            int currentCol = col;
            int rowCount = 1;
            List<Integer> offsets;
            // fill the path
            while (rowCount < this.height()) {

                if (currentCol == 0) { // left edge
                    offsets = Arrays.asList(0, 1);
                }
                else if (currentCol == (this.width() - 1)) { // right edge
                    offsets = Arrays.asList(-1, 0);
                }
                else { // interior
                    offsets = Arrays.asList(-1, 0, 1);
                }
                // next edge that has the lowest energy
                int minECol = currentCol;
                double e;
                double minE = Integer.MAX_VALUE;
                for (int offset : offsets) {
                    e = energy(currentCol + offset, rowCount);
                    if (e < minE) {
                        minE = e;
                        minECol = currentCol + offset;
                    }
                }
                currentSumE += minE;
                currentPath[rowCount] = minECol;
                // for next iteration
                currentCol = minECol;
                rowCount++;
            }
            if (currentSumE < minSumE) {
                minSumE = currentSumE;
                minEPath = Arrays.copyOf(currentPath, currentPath.length); // we reused currentPath
            }
        }
        return minEPath;
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

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    // unit testing (optional)
    public static void main(String[] args) {
        // Picture pic = new Picture("./chameleon.png");
        // pic.show();
    }

}
