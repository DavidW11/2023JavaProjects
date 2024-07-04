/* *****************************************************************************
 *  Operating system: OS X Ventura 13.3
 *
 *  Compiler: 17.0.8+7-b1000.8 aarch64
 *
 *  Text editor / IDE: IntelliJ 2023.2
 *
 *  Have you taken (part of) this course before: No
 *  Have you taken (part of) the Coursera course Algorithms, Part I or II: No
 *
 *  Hours to complete assignment (optional): 7
 *
 **************************************************************************** */

Programming Assignment 1: Percolation


/* *****************************************************************************
 *  Describe the data structures (i.e., instance variables) you used to
 *  implement the Percolation API.
 **************************************************************************** */
I used a 2D array of integers to represent the grid of sites, in which
zeros represent blocked sites and ones represent open sites.

In both union-find data types, each index represents a site and each
non-singleton set represents a series of linked open sites.
In the first union-find instance, named "sites,"
the last two indices represent a "virtual" top and bottom, allowing the
algorithm to check whether sites in the bottom row share a parent to sites in
the top row.

The second union-find instance, named "fullSites,"
includes only a virtual top, allowing the algorithm to check whether a site
is full, while preventing the "backwash" that occurs when the virtual top and
bottom sets are union-ed in a grid that percolates.

The numOpenSites instance variable counts the total number of open sites.

The rowWidth variable is equivalent to the width (n) of the n by n grid.

/* *****************************************************************************
 *  Briefly describe the algorithms you used to implement each method in
 *  the Percolation API.
 **************************************************************************** */
open():
If the site is not already open, I change the value in the grid to 1,
signifying an open site. After incrementing the number of open sites, I check
whether the site is in either the top or bottom row.
If the site is in the top row, I union the set containing the site's index with
the virtual top of both union-find data structures.
And if the site is in the bottom row, I union it to the virtual bottom of the
"sites" union-find data structure used to check for percolation.
I then check the four neighboring sites, union-ing any neighboring sites
that are open. For open(), isOpen(), and isFull(), I used a helper method
throwIllegalArgumentException() at the beginning of each
method to throw an exception if the row and column indices fall out of range.

isOpen():
Returns true if the grid value equals 1, false if 0.

isFull():
Returns true if the site shares a parent with the virtual top in the "fullSites"
union-find data structure.

numberOfOpenSites():
Returns the instance variable numOpenSites.

percolates():
Returns true if the virtual bottom shares a parent with the virtual top
(in the "sites" union-find data structure).
The only way in which the virtual top and bottom would be in the same set
is if a series of neighboring sites exists between the top and bottom row.

/* *****************************************************************************
 *  First, implement Percolation using QuickFindUF.
 *  What is the largest values of n that PercolationStats can handle in
 *  less than one minute on your computer when performing T = 100 trials?
 *
 *  Fill in the table below to show the values of n that you used and the
 *  corresponding running times. Use at least 5 different values of n.
 **************************************************************************** */

 T = 100

 n          time (seconds)
--------------------------
200         17.813
400         285.495
300         90.309
250         43.93
275         63.471
270         59.3

/* *****************************************************************************
 *  Describe the strategy you used for selecting the values of n.
 **************************************************************************** */
I started in the range of 200-400. Since 60 seconds falls within this range,
the next n value I tried was the midpoint of 200-400. I repeated this process
for the following trials, taking the midpoint of the new n value and the
previous upper or lower bound, depending on whether the run time fell above or
below 1 minute.


/* *****************************************************************************
 *  Next, implement Percolation using WeightedQuickUnionUF.
 *  What is the largest values of n that PercolationStats can handle in
 *  less than one minute on your computer when performing T = 100 trials?
 *
 *  Fill in the table below to show the values of n that you used and the
 *  corresponding running times. Use at least 5 different values of n.
 **************************************************************************** */

 T = 100

 n          time (seconds)
--------------------------
1000        3.839
4000        173.294
2000        29.927
3000        89.245
2500        55.883
2600        61.597
2550        58.973
2570        59.86
 

/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */

My solution to backwash uses a second union-find data structure, which brings
the total memory of the Percolation class to above 17 n^2 + 128 n + 1024 bytes,
the threshold required to pass tests 1a-d. Without the functionality to prevent
backwash, the program passes tests 1a-d. With more time, I'd like to find a
solution to backwash that does not require a second union-find data structure.

The instance methods of the Percolation class contain Θ(1) calls to
union() and find(). However, the union() and find() methods of the
weighted-quick-union class take at worst Θ(log(n)) time.
And the union() method of the quick-find class takes Θ(n) time,
severely increasing the run time of larger user inputs.
I'm curious about the idea of using a weighted-quick-union data type
with path compression, decreasing the union() time complexity to nearly constant.

It's also worth noting that my PercolationStats class does not check for valid
arguments (two arguments that can be parsed into integers).

/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */

One issue that I worked to resolve was "backwash," in which open sites in the
bottom row were marked as "full," even though they were not connected to the
top row. This "backwash" occurs in grids that percolate, because the virtual top
and bottom rows share the same parent. In order to resolve this issue, I created
a second union-find data structure that contains only a virtual top.
When the grid percolates, open sites in the virtual bottom of this "fullSites"
data structure do not connect to a virtual top.
The "fullSites" data structure is used to determine full sites, while the
"sites" data structure is used to determine percolation.
With more time, I'd like to search for a more memory-efficient solution to
backwash.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** */

For my first COS 226 coding assignment, I made the mistake of ignoring the
checklist before starting on my code. As a result, I spent a lot of time
thinking about how I could most efficiently use the union-find data type
to check for percolation. In the end, I drew up a strategy very similar to the
"virtual" top and bottom concept I later discovered in the checklist.
While I might have saved some time with the checklist, I actually enjoyed
the trial and error and problem-solving process.
And while I've learned about the concept of time complexity in the past, this
was my first experience optimizing the efficiency of one of my programs.

