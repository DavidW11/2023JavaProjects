import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.List;

public class ShortestCommonAncestor {

    // Digraph to represent vertices and edges
    private Digraph G;

    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null) throw new IllegalArgumentException();

        // check that graph has no directed cycles (has topological order)
        Topological topo = new Topological(G);
        if (!topo.hasOrder()) throw new IllegalArgumentException();

        // check that graph is rooted --> only one vertex has outdegree of 0
        int numRoots = 0;
        for (int v = 0; v < G.V(); v++) {
            if (G.outdegree(v) == 0) numRoots++;
            if (numRoots > 1) throw new IllegalArgumentException();
        }

        this.G = new Digraph(G);
    }

    // breadth first search
    // @param temporary queue to store vertices and
    // @param hashmap to store ancestors and distance to vertex
    private void bfs(Queue<Integer> queue,
                     LinearProbingHashST<Integer, Integer> ancestors) {
        while (!queue.isEmpty()) {
            int vertex = queue.dequeue();
            for (int adj : G.adj(vertex)) {
                // enqueue adjacent vertex if not already visited
                if (!ancestors.contains(adj)) queue.enqueue(adj);
                int distance = ancestors.get(vertex) + 1;
                // do not replace distance if vertex in hashmap
                // has smaller distance
                if (!(ancestors.contains(adj) && distance > ancestors.get(adj)))
                    ancestors.put(adj, distance);
            }
        }
    }

    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException();

        Queue<Integer> queue = new Queue<Integer>();

        // use hash map to track ancestors and distance to v
        // uses memory proportional to number of ancestors
        LinearProbingHashST<Integer, Integer> vAncestors =
                new LinearProbingHashST<Integer, Integer>();
        queue.enqueue(v);
        vAncestors.put(v, 0);

        // use breadth first search to explore ancestors of v
        bfs(queue, vAncestors);

        // repeat for w
        LinearProbingHashST<Integer, Integer> wAncestors =
                new LinearProbingHashST<Integer, Integer>();
        queue.enqueue(w);
        wAncestors.put(w, 0);

        bfs(queue, wAncestors);

        // set initial champion distance to positive infinity
        // to ensure that value will be replaced
        double champDist = Double.POSITIVE_INFINITY;
        for (int k : vAncestors.keys()) {
            if (wAncestors.contains(k)) {
                int distance = wAncestors.get(k) + vAncestors.get(k);
                if (distance < champDist) {
                    champDist = distance;
                }
            }
        }
        return (int) champDist;
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= G.V() || w < 0 || w >= G.V())
            throw new IllegalArgumentException();

        Queue<Integer> queue = new Queue<Integer>();

        // use hash map to track ancestors and distance to v
        // uses memory proportional to number of ancestors
        LinearProbingHashST<Integer, Integer> vAncestors =
                new LinearProbingHashST<Integer, Integer>();
        queue.enqueue(v);
        vAncestors.put(v, 0);

        // use breadth first search to explore ancestors of v
        bfs(queue, vAncestors);

        // repeat for w
        LinearProbingHashST<Integer, Integer> wAncestors =
                new LinearProbingHashST<Integer, Integer>();
        queue.enqueue(w);
        wAncestors.put(w, 0);

        bfs(queue, wAncestors);

        // initialize shortest common ancestor to -1, will be replaced
        // if searching in rooted DAG
        int champ = -1;
        // set initial champion distance to positive infinity
        // to ensure that value will be replaced
        double champDist = Double.POSITIVE_INFINITY;
        for (int k : vAncestors.keys()) {
            if (wAncestors.contains(k)) {
                int distance = wAncestors.get(k) + vAncestors.get(k);
                if (distance < champDist) {
                    champ = k;
                    champDist = distance;
                }
            }
        }
        return champ;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA,
                            Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null ||
                !subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException();

        Queue<Integer> queue = new Queue<Integer>();

        // use hash map to track ancestors and distance to v
        // uses memory proportional to number of ancestors
        LinearProbingHashST<Integer, Integer> vAncestors =
                new LinearProbingHashST<Integer, Integer>();
        for (Integer i : subsetA) {
            if (i == null || i < 0 || i >= G.V())
                throw new IllegalArgumentException();
            queue.enqueue(i);
            vAncestors.put(i, 0);
        }

        // use breadth first search to explore ancestors of v
        bfs(queue, vAncestors);

        // repeat for w
        LinearProbingHashST<Integer, Integer> wAncestors =
                new LinearProbingHashST<Integer, Integer>();
        for (Integer i : subsetB) {
            if (i == null || i < 0 || i >= G.V())
                throw new IllegalArgumentException();
            queue.enqueue(i);
            wAncestors.put(i, 0);
        }

        bfs(queue, wAncestors);

        // set initial champion distance to positive infinity
        // to ensure that value will be replaced
        double champDist = Double.POSITIVE_INFINITY;
        for (int k : vAncestors.keys()) {
            if (wAncestors.contains(k)) {
                int distance = wAncestors.get(k) + vAncestors.get(k);
                if (distance < champDist) {
                    champDist = distance;
                }
            }
        }
        return (int) champDist;
    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA,
                              Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null ||
                !subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException();

        Queue<Integer> queue = new Queue<Integer>();

        // use hash map to track ancestors and distance to v
        // uses memory proportional to number of ancestors
        LinearProbingHashST<Integer, Integer> vAncestors =
                new LinearProbingHashST<Integer, Integer>();
        for (Integer i : subsetA) {
            if (i == null || i < 0 || i >= G.V())
                throw new IllegalArgumentException();
            queue.enqueue(i);
            vAncestors.put(i, 0);
        }

        // use breadth first search to explore ancestors of v
        bfs(queue, vAncestors);

        // repeat for w
        LinearProbingHashST<Integer, Integer> wAncestors =
                new LinearProbingHashST<Integer, Integer>();
        for (Integer i : subsetB) {
            if (i == null || i < 0 || i >= G.V())
                throw new IllegalArgumentException();
            queue.enqueue(i);
            wAncestors.put(i, 0);
        }

        bfs(queue, wAncestors);

        // initialize shortest common ancestor to -1, will be replaced
        // if searching in rooted DAG
        int champ = -1;
        // set initial champion distance to positive infinity
        // to ensure that value will be replaced
        double champDist = Double.POSITIVE_INFINITY;
        for (int k : vAncestors.keys()) {
            if (wAncestors.contains(k)) {
                int distance = wAncestors.get(k) + vAncestors.get(k);
                if (distance < champDist) {
                    champ = k;
                    champDist = distance;
                }
            }
        }
        return champ;
    }

    // unit testing (required)
    public static void main(String[] args) {
        System.out.println("Unit Testing on digraph25.txt");
        In inUnitTest = new In("digraph25.txt");
        Digraph gUnitTest = new Digraph(inUnitTest);
        ShortestCommonAncestor scaUnitTest =
                new ShortestCommonAncestor(gUnitTest);

        Integer[] subsetAUnitTest = { 4, 18, 5 };
        Integer[] subsetBUnitTest = { 6, 1, 21 };
        System.out.println("Subset A: " + List.of(subsetAUnitTest));
        System.out.println("Subset B: " + List.of(subsetBUnitTest));

        System.out.println("Ancestor of Subsets: " +
                                   scaUnitTest.ancestorSubset(
                                           List.of(subsetAUnitTest),
                                           List.of(subsetBUnitTest)));
        System.out.println("Distance between Subsets: " +
                                   scaUnitTest.lengthSubset(
                                           List.of(subsetAUnitTest),
                                           List.of(subsetBUnitTest)));

        System.out.println("Noun A: " + 11);
        System.out.println("Noun B: " + 3);
        System.out.println("Ancestor of Nouns: " +
                                   scaUnitTest.ancestor(11, 3));
        System.out.println("Distance between Subsets: " +
                                   scaUnitTest.length(11, 3));


        // Input Any File
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        ShortestCommonAncestor sca = new ShortestCommonAncestor(G);

        // Integer[] subsetEmpty = new Integer[] { };
        // Integer[] subsetNull = null;
        // Integer[] subsetNegative = new Integer[] { -1 };

        Integer[] subsetA = new Integer[] { 0 };
        Integer[] subsetB = new Integer[] { 0 };

        System.out.println(sca.lengthSubset(List.of(subsetA), List.of(subsetB)));
        System.out.println(sca.ancestorSubset(List.of(subsetA), List.of(subsetB)));

        // System.out.println
        // (sca.lengthSubset(List.of(subsetEmpty), List.of(subsetB)));
        // System.out.println
        // (sca.lengthSubset(List.of(subsetNull), List.of(subsetB)));
        // System.out.println
        // (sca.lengthSubset(List.of(subsetNegative), List.of(subsetB)));

        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length = sca.length(v, w);
            int ancestor = sca.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
