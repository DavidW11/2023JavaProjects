import edu.princeton.cs.algs4.IndexMinPQ;
import edu.princeton.cs.algs4.StdOut;

public class WeakLearner {
    // stores the dimension predictor
    private int dPred;
    // stores the value predictor
    private double vPred;
    // stores the sign predictor
    private int sPred;
    // stores the value of k
    private int k;

    // train the weak learner
    public WeakLearner(double[][] input, double[] weights, int[] labels) {

        // corner cases
        if (input == null || weights == null || labels == null ||
                input.length != weights.length || input.length != labels.length)
            throw new IllegalArgumentException();
        int n = input.length;
        for (int i = 0; i < n; i++) {
            if (input[i] == null) throw new IllegalArgumentException();
        }
        k = input[0].length;

        // initialize predictors and champion number of correct predictions
        dPred = 0;
        vPred = input[0][0];
        sPred = 0;
        double champ = 0;

        // sign predictor
        for (int s = 0; s <= 1; s++) {
            // dimension predictor
            for (int d = 0; d < k; d++) {
                // create min priority queue and add all possible values
                IndexMinPQ<Double> pq = new IndexMinPQ<Double>(input.length);
                double zeros = 0;
                double ones = 0;
                for (int i = 0; i < n; i++) {
                    pq.insert(i, input[i][d]);
                    // track total weight of ones and zeros
                    if (labels[i] == 0) zeros += weights[i];
                    else ones += weights[i];
                }

                double correctWeights;
                if (s == 0) correctWeights = ones;
                else correctWeights = zeros;

                for (int i = 0; i < n; i++) {

                    int minIndex = pq.minIndex();
                    double v = pq.minKey();
                    pq.delMin();

                    // add or subtract weight of current value
                    if (labels[minIndex] == s)
                        correctWeights += weights[minIndex];
                    else correctWeights -= weights[minIndex];

                    // check for repeated coordinates
                    while (pq.size() > 0 && pq.minKey() == v) {
                        minIndex = pq.minIndex();
                        v = pq.minKey();
                        pq.delMin();

                        if (labels[minIndex] == s)
                            correctWeights += weights[minIndex];
                        else correctWeights -= weights[minIndex];
                        i++;
                    }


                    // check for ties
                    if (correctWeights == champ) {
                        if (d < this.dPred) update(v, d, s);
                        else if (d == this.dPred) {
                            if (v < this.vPred) update(v, d, s);
                            else if (v == this.vPred) {
                                if (s < this.sPred) update(v, d, s);
                            }
                        }
                    }
                    // update optimal predictors
                    else if (correctWeights > champ) {
                        update(v, d, s);
                        champ = correctWeights;
                    }
                }
            }
        }
    }

    // updates the global value, dimension, and sign predictors
    private void update(double v, int d, int s) {
        this.vPred = v;
        this.dPred = d;
        this.sPred = s;
    }

    // predicts the label of a sample for a given input of a value, dimension,
    // and sign predictor
    private int predict(double[] sample, double v, int d, int s) {
        if (sample == null || sample.length != k)
            throw new IllegalArgumentException();
        if (sample[d] <= v) return s;
        return 1 - s;
    }

    // return the prediction of the learner for a new sample using the global
    // value, dimension, and sign predictor
    public int predict(double[] sample) {
        if (sample == null || sample.length != k)
            throw new IllegalArgumentException();
        return predict(sample, vPred, dPred, sPred);
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return dPred;
    }

    // return the value the learner uses to separate the data
    public double valuePredictor() {
        return vPred;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return sPred;
    }

    // unit testing (required)
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet("princeton_training.txt");
        DataSet test = new DataSet("princeton_test.txt");

        // Clustering c = new Clustering()

        double[] weights = new double[training.input.length];
        for (int i = 0; i < weights.length; i++) {
            weights[i] = 1;
        }
        // train the model
        WeakLearner model =
                new WeakLearner(training.input, weights, training.labels);

        System.out.println("Value Predictor: " + model.valuePredictor());
        System.out.println("Dimension Predictor: " +
                                   model.dimensionPredictor());
        System.out.println("Sign Predictor: " + model.signPredictor());

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
        StdOut.println("Test accuracy of model: " + testAccuracy);
    }
}

