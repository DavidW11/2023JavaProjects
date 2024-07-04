import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    // 2D array of 0s and 1s representing sites in grid
    private int[][] grid;
    // 2 Union-Find data types in which sets represent linked sites
    // the first includes a virtual top and bottom site to determine whether
    // the grid percolates
    private WeightedQuickUnionUF sites;
    // the second only includes a virtual top to determine whether a site
    // is full, while preventing backwash
    private WeightedQuickUnionUF fullSites;
    // total number of open sites - used to calculate percolation threshold
    private int numOpenSites;
    // width of 2D grid array
    private int rowWidth;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // throw exception if size n is not positive
        if (n <= 0) {
            throw new IllegalArgumentException("grid size must be above zero");
        }

        rowWidth = n;
        // initialize grid array to width and length n
        grid = new int[rowWidth][rowWidth];
        // initialize length of Union-Find data type to #sites + 2
        // the extra 2 sets represent a "virtual" top and bottom row,
        // allowing the algorithm to check whether a site in the bottom row
        // connects to a site in the top row
        sites = new WeightedQuickUnionUF((int) Math.pow(rowWidth, 2) + 2);
        // initialize the second union-find data type to include only virtual top
        fullSites = new WeightedQuickUnionUF((int) Math.pow(rowWidth, 2) + 1);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        throwIllegalArgumentException(row, col);

        if (grid[row][col] == 0) {
            int index = getIndex(row, col);
            // set grid value to 1, marking that the site is open
            grid[row][col] = 1;
            // increment number of open sites
            numOpenSites++;

            // union sites in the top row to both virtual tops
            if (row == 0) {
                sites.union(index, (int) Math.pow(rowWidth, 2));
                fullSites.union(index, (int) Math.pow(rowWidth, 2));
            }
            // union sites in the bottom row to only the first union-find data type
            if (row == rowWidth - 1) {
                sites.union(index, (int) Math.pow(rowWidth, 2) + 1);
            }

            // search 4 surrounding blocks and union open cells
            if (isValidIndex(row - 1, col) && isOpen(row - 1, col)) {
                sites.union(index, index - rowWidth);
                fullSites.union(index, index - rowWidth);
            }
            if (isValidIndex(row + 1, col) && isOpen(row + 1, col)) {
                sites.union(index + rowWidth, index);
                fullSites.union(index + rowWidth, index);
            }
            if (isValidIndex(row, col - 1) && isOpen(row, col - 1)) {
                sites.union(index, index - 1);
                fullSites.union(index, index - 1);
            }
            if (isValidIndex(row, col + 1) && isOpen(row, col + 1)) {
                sites.union(index + 1, index);
                fullSites.union(index + 1, index);
            }
        }
    }

    // convert row and column to 1-D index in the Union-Find data type
    private int getIndex(int row, int col) {
        return row * rowWidth + col;
    }

    // return true if row and col are valid indices
    private boolean isValidIndex(int row, int col) {
        return (row >= 0 && row < rowWidth && col >= 0 && col < rowWidth);
    }

    // throw exception if indices out of range
    private void throwIllegalArgumentException(int row, int col) {
        if (!isValidIndex(row, col)) {
            throw new IllegalArgumentException("index out of range");
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        throwIllegalArgumentException(row, col);
        return (grid[row][col] == 1);
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        throwIllegalArgumentException(row, col);
        int index = getIndex(row, col);
        // check if the site shares a parent with the virtual top
        return (fullSites.find((int) Math.pow(rowWidth, 2)) == fullSites.find(index));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        // if the virtual top and bottom rows share a parent, then
        // there is a path of open sites connecting them
        return (sites.find((int) Math.pow(rowWidth, 2)) ==
                sites.find((int) Math.pow(rowWidth, 2) + 1));
    }

    // unit testing (required)
    public static void main(String[] args) {
        Percolation test = new Percolation(3);
        test.open(0, 1);
        test.open(1, 1);
        test.open(1, 2);
        test.open(2, 2);
        System.out.println("Open (true): " + test.isOpen(0, 1));
        System.out.println("Not Open (false): " + test.isOpen(0, 0));
        System.out.println("Full (true): " + test.isFull(1, 1));
        System.out.println("Not Full (false): " + test.isFull(2, 0));
        System.out.println("Number of Open Sites: " + test.numberOfOpenSites());
        System.out.println("Percolates: " + test.percolates());
        // test illegal argument exception
        // test.open(3, 0);
    }
}
