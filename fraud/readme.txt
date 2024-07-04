Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */


/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */

Our algorithm loops through all possible values of the dimension, sign, and
value predictor. However, rather than checking our decision stump against
every point in the input array, thus making the algorithm Theta(kn^2), we
updated the number of correct predictions in order of increasing value.
We used a minimum oriented index priority queue to remove the minimum value
in log(n) time, as well as to keep track of the index associated with each
value. After every iteration of the value, we adjusted the total weight
of correct predictions based on whether the new point agreed with our
sign predictor. Finally, we would compare the weight of correct predictions
to the "champion" and update the values of our instance variables
accordingly. Thus, our algorithm takes Theta(knlogn) running time.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------

/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
