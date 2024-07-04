import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {

    // defensive copy of input picture
    private Picture picture;
    // H x W array of pixel energies
    private double[][] energy;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.picture = new Picture(picture);

        // initialize energy 2D array
        energy = new double[height()][width()];
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                energy[row][col] = energy(col, row);
            }
        }
    }

    // current picture
    public Picture picture() {
        return picture;
    }

    // width of current picture
    public int width() {
        return picture.width();
    }

    // height of current picture
    public int height() {
        return picture.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        // TODO GET RGB INSTEAD OF CREATING COLOR OBJECT
        double xGrad = colorGrad(picture.get((x - 1 + width()) % width(), y),
                                 picture.get((x + 1) % width(), y));

        double yGrad = colorGrad(picture.get(x, (y - 1 + height()) % height()),
                                 picture.get(x, (y + 1) % height()));
        return (Math.sqrt(xGrad + yGrad));
    }

    // helper method to find color gradient
    private double colorGrad(Color a, Color b) {
        int red = a.getRed() - b.getRed();
        int green = a.getGreen() - b.getGreen();
        int blue = a.getBlue() - b.getBlue();
        return (Math.pow(red, 2) + Math.pow(green, 2) + Math.pow(blue, 2));
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        return findVerticalSeam();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        int size = width() * height();

        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(size);

        // keep extra slot in distance array for a virtual bottom
        // allowing the algorithm to keep track of the "champion" index
        // with the shortest path to the top
        double[] distTo = new double[size + 1];
        int[] vertTo = new int[size];

        // initialize all distances to infinity
        for (int i = 0; i < size; i++) {
            distTo[i] = Double.POSITIVE_INFINITY;
        }

        // add top row of pixels to priority queue and distance array
        for (int col = 0; col < width(); col++) {
            double e = energy[0][col];
            pq.insert(colMajor(0, col), e);
            distTo[colMajor(0, col)] = e;
        }

        // champ index keeps track of vertex in bottom row with
        // shortest path to the top row
        int champ = distTo.length - 1;
        distTo[champ] = Double.POSITIVE_INFINITY;

        while (!pq.isEmpty()) {
            int v = pq.delMin();

            int mid = v + 1;
            if (validIndex(mid)) champ = relax(v, mid, distTo, vertTo, pq, champ);

            int left = v + 1 - height();
            if (validIndex(left)) champ = relax(v, left, distTo, vertTo, pq, champ);

            int right = v + 1 + height();
            if (validIndex(right)) champ = relax(v, right, distTo, vertTo, pq, champ);
        }

        int[] shortestSeam = new int[height()];
        // find column of champ index and insert into shortest seam array
        int col = champ / height();
        shortestSeam[shortestSeam.length - 1] = col;
        for (int i = shortestSeam.length - 2; i >= 0; i--) {
            // find column major index of next vertex in seam
            int nextVert = vertTo[colMajor(i + 1, shortestSeam[i + 1])];
            // insert column number of next vertex
            shortestSeam[i] = nextVert / height();
        }
        return shortestSeam;
    }

    // relax edge - update distance array if find shorter path
    private int relax(int v, int w, double[] distTo, int[] vertTo,
                      IndexMinPQ<Double> pq, int champ) {
        double newDist = distTo[v] + energy[w % height()][w / height()];
        if (distTo[w] > newDist) {
            distTo[w] = newDist;
            vertTo[w] = v;
            if (!pq.contains(w)) pq.insert(w, distTo[w]);
            else pq.decreaseKey(w, distTo[w]);
        }
        // update champ if a vertex in the last row has a shorter path to the top
        if (lastRow(w) && distTo[w] < distTo[champ]) {
            champ = w;
        }
        return champ;
    }

    // check if column major index is in last row of picture
    private boolean lastRow(int v) {
        if ((v + 1) % height() == 0) return true;
        return false;
    }

    // check that index is within bounds of picture
    private boolean validIndex(int v) {
        if (v < 0 || v >= width() * height()) return false;
        return true;
    }

    // convert row and column in 2D array to index in column major 1D array
    private int colMajor(int row, int col) {
        return (col * height() + row);
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

    }

    //  unit testing (required)
    public static void main(String[] args) {

    }

}