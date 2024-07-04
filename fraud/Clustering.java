import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.Point2D;

import java.util.Iterator;

public class Clustering {

    private CC clusters;
    private int m;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {

        if (locations == null || k > locations.length || k < 1)
            throw new IllegalArgumentException();

        m = locations.length;
        EdgeWeightedGraph graph = new EdgeWeightedGraph(m);

        // initialize weights to Euclidean distances
        for (int i = 1; i < m; i++) {
            double weight = locations[i].distanceTo(locations[i - 1]);
            graph.addEdge(new Edge(i, i - 1, weight));
            if (locations[i] == null) throw new IllegalArgumentException();
        }
        // create MST
        KruskalMST minGraph = new KruskalMST(graph);

        // add m-k edges to clusters graph
        EdgeWeightedGraph clusterGraph = new EdgeWeightedGraph(locations.length);
        // create iterator of edges in MST
        Iterator<Edge> minEdges = minGraph.edges().iterator();
        for (int i = 0; i < m - k; i++) {
            clusterGraph.addEdge(minEdges.next());
        }

        // find connected components
        clusters = new CC(clusterGraph);
    }

    // return the cluster of the ith point
    public int clusterOf(int i) {
        if (i < 0 || i > m - 1) throw new IllegalArgumentException();
        return clusters.id(i);
    }

    // use the clusters to reduce the dimensions of an input
    public double[] reduceDimensions(double[] input) {
        if (input == null || m != input.length) throw new IllegalArgumentException();

        double[] reduced = new double[clusters.count()];
        for (int i = 0; i < m; i++) {
            reduced[clusters.id(i)] += input[i];
        }
        return reduced;
    }

    // unit testing (required)
    public static void main(String[] args) {

    }
}
