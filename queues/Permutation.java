import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    // @param k number of strings to be printed
    // takes sequence of strings as standard input
    // outputs k strings in random order
    public static void main(String[] args) {

        // number of strings to be printed
        int k = Integer.parseInt(args[0]);
        // construct new randomized queue
        RandomizedQueue<String> randomQueue = new RandomizedQueue<String>();

        // read in all strings and add to randomized queue
        while (!StdIn.isEmpty()) {
            String string = StdIn.readString();
            randomQueue.enqueue(string);
        }

        // dequeue k random strings
        for (int i = 0; i < k; i++) {
            StdOut.println(randomQueue.dequeue());
        }
    }
}
