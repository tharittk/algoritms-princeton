import edu.princeton.cs.algs4.Picture;

import java.awt.Color;
import java.util.Arrays;

public class SeamCarver {
    private final Picture pic;
    private Picutre picT;
    private double[][] energies, energiesT;
    
    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.pic = picture;
        this.energies = new double[pic.width()][pic.height()];
        this.computeEergyAll();
	
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
        double minSumE = Double.POSITIVE_INFINITY;

        for (int col = 0; col < this.width(); col++) {
	    double currentSumE = 0;
            currentPath[0] = col;
	    int currentCol = col;
            int rowCount = 1;
	    // fill the path
	    while (rowCount < this.height()) {
                
		if (currentCol == 0){ // left edge
			int[] offsets = {0, 1};
		} else if (currentCol == (this.width() - 1){// right edge
			int[] offsets = {-1, 0};
		} else { // interior
			int[] offsets = {-1, 0, 1};
		}
		// next edge that has the lowest energy
		int minECol = currentCol;
		int E = Integer.MAX_VALUE;
	      	int minE = Integer.MAX_VALUE;
		for (int offset : offsets){
			E = energy(currentCol + offset, rowCount);
			if (E < minE){
				minE = E;
				minECol = currentCol + offset;	
			}
		}
		currentSumE += minE
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


    private Picutre getPicTranpose(Picture pic){
	    Picutre picT = new Picutre(pic.height(), pic.width());
	    for (int col = 0; col < pic.width(); col++){
		    for (int row = 0; row < pic.height(); row++) {
			    picT.setRGB(row, col, pic.getRGB(col, row));
		    }
	    }
	    return picT;
    }


    private double[][] getEnergyTranpose(double[][] energies){
	    double[][] energiesT = new double(energies.length, energies[0].length);
	    for (int col = 0; col < energies.length; col++){
		    for (int row = 0; row < energies[0].length(); row++) {
			    energiesT[row][col] = energies[col][row];
		    }
	    }
	    return energiesT;
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
