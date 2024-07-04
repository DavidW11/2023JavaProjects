import java.util.Comparator;

public class BinarySearchDeluxe {

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(Key[] a, Key key,
                                         Comparator<Key> comparator) {
        // throws error if any of the arguments are null
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException();
        }
        // defines the initial hi and lo index of the search bounds to be the
        // entire array
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            // unsigned bit shift to find midway point between hi and lo index
            int mid = (lo + hi) >>> 1;
            // runs passed comparator functions between mid and key
            int compare = comparator.compare(key, a[mid]);
            // redefines search bounds below mid if key is less than the value
            // at the mid index
            if (compare < 0) hi = mid - 1;
                // redefines search bounds above mid if key is greater than
                // the value at the mid index
            else if (compare > 0) lo = mid + 1;
                // following statements only execute if mid equals the target
                // continues scan if the full lower bounds of the array have not yet
                // been searched
            else if (lo != mid) {
                // ensures that the full lower range is being searched including
                // mid
                hi = mid;
            }
            // if full bounds are searched, then the low variable must hold the
            // index of the first occurrence
            else return lo;
        }
        return -1;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(Key[] a, Key key,
                                        Comparator<Key> comparator) {
        // throws error if any of the arguments are null
        if (a == null || key == null || comparator == null) {
            throw new IllegalArgumentException();
        }
        // defines the initial hi and lo index of the search bounds to be the
        // entire array
        int lo = 0, hi = a.length - 1;
        while (lo <= hi) {
            // unsigned bit shift to find midway point between hi and lo index
            int mid = (lo + hi) >>> 1;
            // runs passed comparator functions between mid and key
            int compare = comparator.compare(key, a[mid]);
            // redefines search bounds below mid if key is less than the value
            // at the mid index
            if (compare < 0) hi = mid - 1;
                // redefines search bounds above mid if key is greater than
                // the value at the mid index
            else if (compare > 0) lo = mid + 1;
                // following statements only execute if mid equals the target
            else if (hi != mid) {
                // prevents infinite loop should the search bound be of size two
                // where lo = mid and hi = lo + 1.
                if (hi - lo == 1) {
                    // if the hi is NOT equal to key, then the mid index must
                    // hold the last occurrence of the key and is thus returned
                    if (comparator.compare(key, a[hi]) != 0) return mid;
                        // executes if the hi index holds the value of key, then
                        // the hi index must be the last occurrence and is returned
                    else return hi;
                }
                // continues the scan to the inclusive upper half of the search
                // boundary to ensure a full scan occurs.
                else {
                    lo = mid;
                }
            }
            // returns the mid if it is the last occurrence should hi == mid,
            // which can only occur when the mid index equal to key is the
            // last occurrence of the key
            else return mid;
        }
        return -1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // initializes an array of terms
        Term[] test = new Term[10];
        // define terms
        test[0] = new Term("A", 1);
        test[1] = new Term("B", 7);
        test[2] = new Term("C", 5);
        test[3] = new Term("D", 6);
        test[4] = new Term("D", 7);
        test[5] = new Term("D", 8);
        test[6] = new Term("E", 3);
        test[7] = new Term("E", 2);
        test[8] = new Term("F", 4);
        test[9] = new Term("G", 5000);

        int dFirstIndex = firstIndexOf(test, new Term("D", 1),
                                       Term.byPrefixOrder(1));
        System.out.println("First Index of D (3): " + dFirstIndex);

        int dLastIndex = lastIndexOf(test, new Term("D", 1),
                                     Term.byPrefixOrder(1));
        System.out.println("Last Index of D (5): " + dLastIndex);

        int eFirstIndex = firstIndexOf(test, new Term("E", 1),
                                       Term.byPrefixOrder(1));
        System.out.println("First Index of E (6): " + eFirstIndex);

        int eLastIndex = lastIndexOf(test, new Term("E", 1),
                                     Term.byPrefixOrder(1));
        System.out.println("Last Index of E (7): " + eLastIndex);

        int gFirstIndex = firstIndexOf(test, new Term("G", 1),
                                       Term.byPrefixOrder(1));
        System.out.println("First Index of G (9): " + gFirstIndex);

        int gLastIndex = lastIndexOf(test, new Term("G", 1),
                                     Term.byPrefixOrder(1));
        System.out.println("Last Index of G (9): " + gLastIndex);
    }
}
