import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {

    // array of terms
    private Term[] terms;

    // Initializes the data structure from the given array of terms.
    // sorts array in lexicographic order using mergesort (Arrays.sort())
    public Autocomplete(Term[] terms) {
        if (terms == null) {
            throw new IllegalArgumentException("terms cannot be null");
        }

        // create defensive copy of array and check if elements are null
        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            if (terms[i] == null) {
                throw new IllegalArgumentException("terms cannot be null");
            }
            this.terms[i] = terms[i];
        }

        // array.sort() uses either quicksort or mergesort to ensure
        // a worst-case nlogn number of compares
        // uses compareTo() method in Terms class to sort by lexicographic order
        Arrays.sort(this.terms);
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        Term prefixTerm = new Term(prefix, 0);
        // find index of first and last key in array (2logn)
        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefix.length());
        int firstIndex = BinarySearchDeluxe.firstIndexOf(
                terms, prefixTerm, prefixComparator);

        // return empty array if binary search does not find a match
        if (firstIndex == -1) {
            return new Term[0];
        }
        int lastIndex = BinarySearchDeluxe.lastIndexOf(
                terms, prefixTerm, prefixComparator);

        // iterate through matches and copy to new array (m)
        Term[] matches = new Term[lastIndex - firstIndex + 1];
        for (int i = 0; i <= lastIndex - firstIndex; i++) {
            matches[i] = terms[i + firstIndex];
        }
        // sort matches array by reverse weight (mlogm compares)
        Comparator<Term> weightComparator = Term.byReverseWeightOrder();
        Arrays.sort(matches, weightComparator);
        return matches;
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        // find index of first and last key in array (2logn)
        Comparator<Term> prefixComparator = Term.byPrefixOrder(prefix.length());
        int firstIndex = BinarySearchDeluxe.firstIndexOf(
                terms, new Term(prefix, 0), prefixComparator);
        // return 0  if binary search does not find a match
        if (firstIndex == -1) {
            return 0;
        }
        int lastIndex = BinarySearchDeluxe.lastIndexOf(
                terms, new Term(prefix, 0), prefixComparator);
        // return number of terms that match prefix
        return lastIndex - firstIndex + 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and print the top k
        // matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }
}
