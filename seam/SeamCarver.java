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
        int[] seam = findVerticalSeam();
        turnTransposeOff();
        return seam;
    }

    // sequence of indices for vertical seam
    // fail 7x10 && 10x10 -- your approach is somewhat greedy... wrong
    public int[] findVerticalSeam() {
	    int[] path =  new int[this.height()];
	    int[] minEPath = new int[this.height()];
	    double minE = Double.MAX_VALUE;
	    double E;

	    for (int col = 0; col < this.width(); col++ {
		    path[0] = col;
		    E = doDP (col, 1, path);
		    if( E < minE){
			    minE = E;
		    	    minEPath = Arrays.copyOf(path, path.length);
		    }
	    
	    }
	    return minEPath;
    }

    private double doDP (int col, int rowCount, int[] p){
	    if (rowCount == (this.height)){
	    	return 0;
	    }
	    if (col == 0) { // left edge
		//offsets = Arrays.asList(0, 1);

		double e1 = energy(col, rowCount) + doDP(col, rowCount+1, p);
		double e2 = energy(col+1, rowCount) + doDP(col+1, rowCount+1, p);

		if (e1 < e2){
			p[rowCount] = col;
			return e1;
		} else {
			p[rowCount = col + 1;
			return e2;
		}
	    
	    }
            else if (col == (this.width() - 1)) { // right edge
		//offsets = Arrays.asList(-1, 0);

	   	double e1 = energy(col-1, rowCount) + doDP(col-1, rowCount+1, p);
	   	double e2 = energy(col, rowCount) + doDP(col, rowCount+1, p);
	
		if (e1 < e2){
			p[rowCount] = col - 1;
			return e1;
		} else {
			p[rowCount] = col;
			return e2;
		}
	          
	    }
            else { // interior
                //offsets = Arrays.asList(-1, 0, 1);
 
	   	double e1 = energy(col-1, rowCount) + doDP(col-1, rowCount+1, p);
	   	double e2 = energy(col, rowCount) + doDP(col, rowCount+1, p);
		double e3 = energy(col+1, rowCount) + doDP(col+1, rowCount+1, p);
		
		int d1d2 = Double.compare (e1, e2);
		if (d1d2 < 0) { // e1 < e2
			int d1d3 = Double.compare (e1, e3);
			if (d1d3 < 0) { // e1 is minimum
				p[rowCount] = col - 1;
				return e1;
			} else { // e3 is minimum
				p[rowCount] = col + 1;
				return e3;
			}
		} else { // e2 < e3
			int d2d3 = Double.compare (e2, e3);
			if (d2d3 < 0) { // e2 is minimum
				p[rowCount] = col;
				return e2;
			} else { // e3 is minimum
				p[rowCount] = col + 1;
				return e3;
			}
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
        removeVerticalSeam(int[] seam); 
	turnTransposeOff();
    }

    public void removeVerticalSeam(int[] seam) {
	    assert (!inTranspose);
	    // shift array
	    for (int row = 0; row < seam.length; row++){
		    for(int col = seam[row]; col < (this.width() - 1); col++){
			    this.energies[col][row] = this.energies[col + 1][row];
		    }
	    }

	    // recalculate energy left and right of the seam
	    for (int row = 0; row < seam.length; row++){
		    // lhs of old seam
		    computeEnergyPixel(seam[row]-1, row);
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
