Programming Assignment 3: Autocomplete


/* *****************************************************************************
 *  Describe how your firstIndexOf() method in BinarySearchDeluxe.java
 *  finds the first index of a key that is equal to the search key.
 **************************************************************************** */
The firstIndexOf() method operates much like a traditional binary search
algorithm with an additional else if statement. The algorithm begins by throwing
an exception if the passed array is null, the key is null, or the comparator is
null, which ensures that the rest of the algirthm can run as expected. The
algorithm begins by defining the low and high search indices at 0 (the beginning
of the array) and the lenght of the array - 1 (the index of the last element in
the array). Then, while the low search index is less than or equal to the high
search index, a variable, mid, is defined as the average of hi and low as
defined by a unsigned right bitshift operator (used to prevent overflow errors).
Next, a variable, compare, is initialized to hold the return value of the
compare function of the passed comparator between the key and the value of the
array at the mid index. If this compare value is negative, then the key value
(if it exists in the array) must be located before the mid index, thus the high
index is redefined to be just below the mid index and the program loops.
However, if this compare value is positive, then the key value (if it exists in
the array) must be located before the mid index, thus the low index is redefined
to  be just above the mid index and the program loops. If the compare value is 0,
then the key is equal to the value of the mid index (but not neccesarily the
first appearence). If the mid index is not equal to the low index (indicating
that the full array has not yet been searched for matches to the key), then
the bounds of the binary search are redefined for the hi to equal the mid
index. This enables further search of the lower half of the array for other
matches to the key. If the mid index is equal to the low index, then the entire
lower portion of the array has been searched and thus the mid key must be the
first occurnece of the key as the search bounds converged on this spot. Thus,
the algorithm returns the mid index at this point. If all of these if statements
are executed as the binary search is performed and no return statement occurs
and the while loop exits with hi > low, then there is no appearence of the key
in the array and -1 is returned.


/* *****************************************************************************
 *  Identify which sorting algorithm (if any) that your program uses in the
 *  Autocomplete constructor and instance methods. Choose from the following
 *  options:
 *
 *    none, selection sort, insertion sort, mergesort, quicksort, heapsort
 *
 *  If you are using an optimized implementation, such as Arrays.sort(),
 *  select the principal algorithm.
 **************************************************************************** */

Arrays.sort() generally uses Timsort to sort large arrays and
reference types, because the stable algorithm preserves the relative
order of elements with an average time complexity of nlogn.

Autocomplete() : Timsort

allMatches() : Timsort

numberOfMatches() : none

/* *****************************************************************************
 *  How many compares (in the worst case) does each of the operations in the
 *  Autocomplete data type make, as a function of both the number of terms n
 *  and the number of matching terms m? Use Big Theta notation to simplify
 *  your answers.
 *
 *  Recall that with Big Theta notation, you should discard both the
 *  leading coefficients and lower-order terms, e.g., Theta(m^2 + m log n).
 **************************************************************************** */

Autocomplete():     Theta(n log n)
Sort n terms in lexographic order using Arrays.sort()/Timsort --> n log n

allMatches():       Theta(log n + m log m)
Binary Search for index of first and last match --> 2 log n
Sort m items by reverse weight using Arrays.sort() --> m log m
2 log n + m log m = Theta(log n + m log m)

numberOfMatches():  Theta(log n)
Binary Search for index of first and last match --> 2 log n


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */

One problem we ecountered was an overuse of comparator calls in the
firstIndexOf() and lastIndexOf() methods. This occured as in these methods we
made a compator call to check if the mid key was equivalent to the previous
index (for firstIndexOf()) or the following index (for lastIndexOf()), which
increased the quantity of calls to this comparator function which was not
optimized. Eventually, we changed our binary search algorithm to only compare
once on each binary search restriction. Both methods now iterate through the
array until it there is only one element left that is equal to the key (if it
exists) at the mid index. This ensures that the number of compator calls in
these methods remains below log2(n) and can employ an optimum number of compares
to  minimize computational load.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** */
We enjoyed working on this assignment as it provided insight into how the
autocomplete features on Google, Instagram, and other apps in our daily lives
operate and improve our quality of life. We really enjoyed how all the
methods came together and built off of one another in the Autocomplete class.
Furthermore, the assignment provided a great supplement to the Binary Search and
Comparator discussions in lectures. Neither of us have implemented these
algorithms or data strcutures before, and we enjoyed learning about them for the
first time through this implementation.
