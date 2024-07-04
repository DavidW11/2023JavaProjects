import java.util.Comparator;

public class Term implements Comparable<Term> {

    // query string
    private String query;
    // weight - greater weight = greater frequency of use
    private long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null || weight < 0) {
            throw new IllegalArgumentException();
        }
        this.query = query;
        this.weight = weight;
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new ReverseWeightComparator();
    }

    // compares two terms in reverse weight order
    private static class ReverseWeightComparator implements Comparator<Term> {

        // returns positive number if t1 has smaller weight
        // negative number if t1 has greater weight, 0 if equal
        public int compare(Term t1, Term t2) {
            return (int) (t2.weight - t1.weight);
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        if (r < 0) {
            throw new IllegalArgumentException();
        }
        return new PrefixComparator(r);
    }

    // compares first r characters of two terms in lexicographic order
    private static class PrefixComparator implements Comparator<Term> {

        // length of prefix (number of characters to compare)
        private int r;

        // initialize r (length of prefix)
        public PrefixComparator(int r) {
            this.r = r;
        }

        // returns positive number if first r characters of
        // t1 is lexographically greater than first r characters of t2
        // if one query is a substring of the other, the shorter
        // string will have a lesser value
        public int compare(Term t1, Term t2) {

            for (int i = 0; i < r; i++) {
                // return if prefix length exceeds one or both of queries
                if (i >= t1.query.length() && i >= t2.query.length()) {
                    return 0;
                }
                else if (i >= t1.query.length()) {
                    return -1;
                }
                else if (i >= t2.query.length()) {
                    return 1;
                }
                int diff = t1.query.charAt(i) - t2.query.charAt(i);
                if (diff != 0) return diff;
            }
            // return 0 if t1 and t2 have same prefix
            return 0;
        }
    }

    // Compares the two terms in lexicographic order by query using the string
    // compareTo() method
    public int compareTo(Term that) {
        return query.compareTo(that.query);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return (weight + "\t" + query);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term apple = new Term("apple", 10);
        Term orange = new Term("orange", 11);
        Term orangejuice = new Term("orangejuice", 11);
        System.out.println(apple);
        System.out.println(orange);
        System.out.println(apple.compareTo(orange));
        System.out.println(byReverseWeightOrder().compare(apple, orange));
        System.out.println(byPrefixOrder(10).compare(apple, orange));
        System.out.println(byPrefixOrder(10).compare(orange, orangejuice));
        System.out.println(byPrefixOrder(10).compare(orange, orange));
    }
}