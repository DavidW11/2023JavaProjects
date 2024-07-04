Programming Assignment 5: K-d Trees


/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */
The Node data type has 6 instance variables associated with it: a Point2d object,
a Value object, a reference to the left and right children, an integer value
that contians information about the level of the Node within the tree (for use
in determining orientation), and lastly a RectHV object for us ein optimizing
the nearest neighbor search and range search. These nodes were used to create a
Kd Search Tree.


/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */
The range search begins by declaring and initializing a new queue that Point2D
objects can then be added to. Then, the range seach begins a recursion sequence
starting at the root node using the global reference to the root node held in
the class. This recursion sequence first checks if the node reference is null,
indicating that end of the tree has been reached and breaks the recursion
sequence. If the node is not null, then the method  checks if the particular
point's rectHV and the rectangle's range intersect before continuing down the
tree. Then, the method determines if the rectangle range includes the point,
and if so, it enqueues the point into the matches queue. Regardless of the
previous if statement, the recursion loop continues with the left subtree being
searched before the right subtree.


/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */
Our nearest() method uses a recursive algorithm to search our KdTree and
continually update the "champion node," representing the point that is
closest to the query point. Using the distanceSquaredTo method of the
Point2D class, we update the champion if the current node is closer
to the query point then the current champion. We then recursively find
the champion of the left and right subtrees, comparing both champion distances
to the query point and returning the closer of the two. The algorithm searches
the subtree that is closer to the query point first, comparing x coordinates if
on an even level and y coordinates if on an odd level.
As a second pruning optimization, the algorithm begins by checking whether the
rectangle contained by the subtree is further away than the current champion.
If so, the algorithm returns the current champion and does not search the
subtree.
A slight optimization to this algorithm would be to use the left champion
as a parameter for finding the right champion, which would automatically
compare the two champions and save the better candidate as right champion
(vice versa if searching the right subtree first). By using general variable
names such as smallChamp and largeChamp, we could return the second champion
found and eliminate the need to explicitly compare the left and right champions.



/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:        10000000               38.7              25.8

KdTreeST:       10000000               16.2              615763.5

Note: more calls per second indicates better performance.


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
None.

/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
Implementing the nearest function as part of the Kd-Tree was very difficult as
we struggled to implement the function recursively wihtout the use of global
variables. We initially created the method with global variables that tracked
the Node of the nearest champion. After trial and error, we eventually
discovered a way to pass the object recursively. Another problem that we
encountered was how to track the orientation of the Node object. This was
ultimately done by assinging an instance variable, level, that would the level
of the node within the tree. This was tracked by keeping track of the number of
recursions involved in the put method, with each recursion being equivalent to
going down a level within the tree.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
We believe that the assignment was incredibly helpful in cementing our
understanding of KdSearchTrees and seeing the vast perfromance benefits they
can have over a brute force method like that found in PointST. We certainly
enjoyed this assignment, especialyl by playing with the visualizers as they
allowed us to see our code working visually (which helped with debugging and
code verificantion)