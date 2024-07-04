import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.Picture;

public class SeamCarver2 {
    private Picture p;
    // TODO: Unless you are optimizing your program to update only the energy values that change after removing a seam, you should not need to maintain the energy values in an instance variable
    private double[] energy;
    private int[] colors;


    // create a seam carver object based on the given picture
    public SeamCarver2(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException();
        }
        p = new Picture(picture);
        calculateEnergy();
    }

    private void calculateEnergy() {
        colors = new int[height() * width()];

        // populate colors
        for (int i = 0; i < height() * width(); i++) {
            colors[i] = p.getRGB(reverseConvertCol(i), reverseConvertRow(i));

        }

        energy = new double[width() * height() + 1];
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                energy[convert(row, col)] = energy(col, row);
            }
        }
        energy[height() * width()] = 0; // virtual bottom
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x >= width() || y < 0 || y >= height()) {
            throw new IllegalArgumentException();
        }

        double xSquareColor = squareGradientX(x, y);
        double ySquareColor = squareGradientY(x, y);
        return Math.sqrt(xSquareColor + ySquareColor);
    }

    private double squareGradientX(int x, int y) {
        int forward = convert(y, (x + 1) % width());
        int backward = convert(y, (((x - 1) % width()) + width()) % width());
        int redDifference = colors[forward] >> 16 - colors[backward] >> 16;
        int greenDifference = colors[forward] >> 8 - colors[backward] >> 8;
        int blueDifference = colors[forward] - colors[backward];

        return Math.pow(redDifference, 2) + Math.pow(blueDifference, 2) + Math.pow(greenDifference,
                                                                                   2);
    }

    private double squareGradientY(int x, int y) {
        int forward = convert((y + 1) % height(), x);
        int backward = convert((((y - 1) % height()) + height()) % height(), x);
        int redDifference = colors[forward] >> 16 - colors[backward] >> 16;
        int greenDifference = colors[forward] >> 8 - colors[backward] >> 8;
        int blueDifference = colors[forward] - colors[backward];
        return Math.pow(redDifference, 2) + Math.pow(blueDifference, 2) + Math.pow(greenDifference,
                                                                                   2);
    }

    // current picture
    public Picture picture() {
        return new Picture(p);
    }

    // width of current picture
    public int width() {
        return p.width();
    }

    // height of current picture
    public int height() {
        return p.height();
    }

    // converts row and column into a column-major based index for linear array
    private int convert(int row, int col) {
        return row + height() * (col);
    }

    private int reverseConvertRow(int i) {
        return i % height();
    }

    private int reverseConvertCol(int i) {
        return i / height();
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        transpose();
        int[] horizontalSeam = findVerticalSeam();
        transpose();
        return horizontalSeam;
    }

    private void transpose() {
        // create new picture object of transposed height and width
        Picture temp = new Picture(height(), width());
        // transpose the image
        // iterate through pixels of the original image
        for (int col = 0; col < width(); col++) {
            for (int row = 0; row < height(); row++) {
                temp.setRGB(row, col, colors[convert(row, col)]);
            }
        }
        p = temp;
        // recalcuate energies to update the array
        calculateEnergy();
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        // virtual top pixel of energy 0
        // virtual bottom of energy 0
        // source-destination Djikstra between top and bottom
        // access in column major order

        int[] previousVertex = new int[height() * width() + 1];
        double[] energyTo = new double[height() * width() + 1];
        IndexMinPQ<Double> pq = new IndexMinPQ<Double>(height() * width() + 1);

        for (int i = 0; i < width(); i++) {
            energyTo[convert(0, i)] = energy[convert(0, i)];
            pq.insert(convert(0, i), energy[convert(0, i)]);
        }
        for (int i = 0; i < width(); i++) {
            for (int j = 1; j < height(); j++) {
                energyTo[convert(j, i)] = Double.POSITIVE_INFINITY;
            }
        }
        energyTo[height() * width()] = Double.POSITIVE_INFINITY;

        while (!pq.isEmpty() ^ pq.contains(height() * width())) {
            int v = pq.delMin();
            int col = reverseConvertCol(v);
            int row = reverseConvertRow(v);
            // reached destination
            if (row + 1 == height()) {
                relax(v, height() * width(), previousVertex, energyTo, pq);
            }
            // bound on both right and left side
            else if ((col - 1 < 0 && col + 1 == width())) {
                relax(v, convert(row + 1, col), previousVertex, energyTo, pq); // relax pixel below
            }
            // bound on left side
            else if (col - 1 < 0) {
                relax(v, convert(row + 1, col), previousVertex, energyTo, pq); // relax pixel below
                relax(v, convert(row + 1, col + 1), previousVertex, energyTo,
                      pq); // relax pixel below and to right
            }
            // bound on right side
            else if (col + 1 == width()) {
                relax(v, convert(row + 1, col), previousVertex, energyTo, pq); // relax pixel below
                relax(v, convert(row + 1, col - 1), previousVertex, energyTo,
                      pq); // relax pixel below and to left
            }
            // else it is unbounded and all vertices below can be relaxed
            else {
                relax(v, convert(row + 1, col), previousVertex, energyTo, pq); // relax pixel below
                relax(v, convert(row + 1, col + 1), previousVertex, energyTo,
                      pq); // relax pixel below and to right
                relax(v, convert(row + 1, col - 1), previousVertex, energyTo,
                      pq); // relax pixel below and to left
            }
        }
        int[] path = new int[height()];
        int prevVertex = height() * width();
        for (int i = path.length - 1; i >= 0; i--) {
            path[i] = previousVertex[prevVertex] / height();
            prevVertex = previousVertex[prevVertex];
        }
        return path;


    }

    private void relax(int source, int to, int[] previousVertex, double[] energyTo,
                       IndexMinPQ<Double> pq) {
        if (energyTo[to] > energyTo[source] + energy[to]) {
            energyTo[to] = energyTo[source] + energy[to];
            previousVertex[to] = source;
            if (!pq.contains(to)) pq.insert(to, energyTo[to]);
            else pq.decreaseKey(to, energyTo[to]);
        }
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        // TODO: are these error checks redundant if checking in vertical?
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (height() == 1 || seam.length != width() || seam[0] >= height() || seam[0] < 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] >= height() || seam[i] < 0 || Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException();
        }
        if (width() == 1 || seam.length != height() || seam[0] >= width() || seam[0] < 0) {
            throw new IllegalArgumentException();
        }
        for (int i = 1; i < seam.length; i++) {
            if (seam[i] >= width() || seam[i] < 0 || Math.abs(seam[i] - seam[i - 1]) > 1) {
                throw new IllegalArgumentException();
            }
        }

        Picture temp = new Picture(width() - 1, height());
        for (int row = 0; row < height(); row++) {
            // keep all pixels prior to seam the same
            for (int col = 0; col < seam[row]; col++) {
                temp.setRGB(col, row, colors[convert(row, col)]);
            }
            // change all pixels from after the same
            for (int col = seam[row]; col < width() - 1; col++) {
                temp.setRGB(col, row, colors[convert(row, col + 1)]);
            }
        }
        p = temp;
        calculateEnergy();
    }

    //  unit testing (required)
    public static void main(String[] args) {
        System.out.println("Randomly generating 200x400 picture:");
        Picture test = SCUtility.randomPicture(200, 400);
        SeamCarver2 sc = new SeamCarver2(test);
        System.out.println("Width (200): " + sc.width());
        System.out.println("Height (400): " + sc.height());

        System.out.print("Displaying Picture: ");
        sc.picture().show();
        System.out.println("Complete");

        System.out.println("Energy of Pixel at (153, 20): " + sc.energy(153, 20));
        System.out.println("First Vertical Seam: ");
        int[] seam1 = sc.findVerticalSeam();
        for (int i = 0; i < seam1.length; i++) {
            System.out.print(seam1[i] + ", ");
        }

        System.out.println("\nFirst Horizontal Seam: ");
        int[] seam2 = sc.findHorizontalSeam();
        for (int i = 0; i < seam2.length; i++) {
            System.out.print(seam2[i] + ", ");
        }

        System.out.println("Removing 20 Horizontal Seams and 30 Vertical Seams");
        for (int i = 0; i < 20; i++) {
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
        }
        for (int i = 0; i < 30; i++) {
            sc.removeVerticalSeam(sc.findVerticalSeam());
        }

        System.out.println("New Width (170): " + sc.width());
        System.out.println("New Height (380): " + sc.height());

        System.out.print("Displaying New Picture: ");
        sc.picture().show();
        System.out.println("Complete");
    }

}