import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private final Picture pic;
    private double[][] energies;

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
        return new int[2];
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        int[] currentPath = new int[this.height()];
        int[] minEPath = new int[this.height()];
        double minE = Double.POSITIVE_INFINITY;
        double currentSumE = 0;
        for (int col = 0; col < this.width(); col++) {
            int rowCount = 1;
            int currentCol = col;
            minEPath[rowCount] = currentCol;
            while (rowCount < this.height()) {
                // beware of edge (only two neighbor ! -- fix later)
                int minId = findSmallest(this.energies[currentCol - 1][rowCount],
                                         this.energies[currentCol][rowCount],
                                         this.energies[currentCol + 1][rowCount]);

                if (minId == 1) {
                    currentSumE += this.energies[currentCol - 1][rowCount];
                    currentCol = currentCol - 1;
                }
                else if (minId == 2) {
                    currentSumE += this.energies[currentCol][rowCount];
                    // maintain same current cor
                }
                else if (minId == 3) {
                    currentSumE += this.energies[currentCol + 1][rowCount];
                    currentCol = currentCol + 1;
                }
                else {
                    throw new RuntimeException("SP not in 3 descendent !");
                }
                currentPath[rowCount] = currentCol;
                rowCount++;
            }
            if (currentSumE < minE) {
                minE = currentSumE;
                minEPath = Arrays.copyOf(currentPath, currentPath.length); // we reused currentPath
            }
        }
        return minEPath;
    }


    private static int findSmallest(double a, double b, double c) {
        double min = Math.min(a, b);
        double acutalMin = Math.min(min, c);

        if (Double.compare(acutalMin, a) == 0) {
            return 1;
        }
        else if (Double.compare(acutalMin, b) == 0) {
            return 2;
        }
        else if (Double.compare(acutalMin, c) == 0) {
            return 3;
        }
        else {
            throw new RuntimeException("Comparison of 3 broken !");
        }
    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (optional)
    public static void main(String[] args) {
        // Picture pic = new Picture("./chameleon.png");
        // pic.show();
    }

}
