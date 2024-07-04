import edu.princeton.cs.algs4.LinkedStack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;

public class BoostingAlgorithm {
    // Stack of each of the weakLearners that are added to the boosting
    private LinkedStack<WeakLearner> weakLearners;
    // weights of each of the input points
    private double[] weights;
    // labels of each of the input points
    private int[] labels;
    // stores reference to input array
    private double[][] input;
    // clustering object c used to reduce dimensions
    private Clustering c;

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(double[][] input, int[] labels,
                             Point2D[] locations, int k) {
        // corner cases
        if (input == null || labels == null || locations == null
                || k < 1 || k > locations.length
                || input.length != labels.length)
            throw new IllegalArgumentException();
        for (Point2D p : locations) {
            if (p == null) throw new IllegalArgumentException();
        }

        // assigns variables
        int n = input.length;

        this.input = new double[n][k];
        this.labels = new int[n];
        this.weights = new double[n];

        // reduce input dimensions
        c = new Clustering(locations, k);
        for (int i = 0; i < n; i++) {
            // more corner cases
            if (input[i] == null || (labels[i] != 0 && labels[i] != 1))
                throw new IllegalArgumentException();
            this.input[i] = c.reduceDimensions(input[i]);
            this.labels[i] = labels[i];
            // initialize and normalize all weights
            weights[i] = (double) 1 / n;
        }

        // initialized the stack of WeakLearner objects
        weakLearners = new LinkedStack<WeakLearner>();
    }

    // return the current weights
    public double[] weights() {
        return weights;
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        // creates new weak learner and adds to stack
        WeakLearner learner = new WeakLearner(input, weights, labels);
        weakLearners.push(learner);
        double weightSum = 0;

        // iterates through the input and doubles weight if the new learner
        // is incorrect
        for (int i = 0; i < input.length; i++) {
            if (learner.predict(input[i]) != labels[i]) weights[i] *= 2;
            weightSum += weights[i];
        }

        // re-normalizes array
        for (int i = 0; i < input.length; i++) {
            weights[i] = weights[i] / weightSum;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(double[] sample) {
        // corner cases
        if (sample == null)
            throw new IllegalArgumentException();

        // reduce dimensions
        double[] sampleReduced = c.reduceDimensions(sample);

        int zeros = 0;
        int ones = 0;
        // iterates through all WeakLearner objects in stack and logs its
        // prediction for the sample input
        for (WeakLearner learner : weakLearners) {
            if (learner.predict(sampleReduced) == 0) zeros++;
            else ones++;
        }

        // output zero for tie and return the more common prediction from the
        // weak learners
        if (zeros >= ones) return 0;
        else return 1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet("princeton_training.txt");
        DataSet test = new DataSet("princeton_test.txt");
        int k = 5;
        int iterations = 100;

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(
                training.input, training.labels, training.locations, k);
        System.out.println("Iterating Model");
        for (int t = 0; t < iterations; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.n; i++)
            if (model.predict(training.input[i]) == training.labels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.n;

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < test.n; i++)
            if (model.predict(test.input[i]) == test.labels[i])
                testAccuracy += 1;
        testAccuracy /= test.n;

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model:     " + testAccuracy);

        System.out.println("Weights for the Boosted Model:");
        for (double weight : model.weights()) {
            System.out.println(weight);
        }
    }
}