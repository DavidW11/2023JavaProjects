Programming Assignment 7: Seam Carving


/* *****************************************************************************
 *  Describe concisely your algorithm to find a horizontal (or vertical)
 *  seam.
 **************************************************************************** */

We used Djikstra's algorithm to find vertical seams, making use of four data
structures:
1) an energy array to represent the energy at each pixel (which
we calculate using a color gradient helper function)
2) a previousVertex array to represent the preceding vertex in the shortest
energy path to each pixel
3) an energyTo array to represent the distance of the shortest energy path
to each pixel
4) a priority queue to track unvisited vertices with the shortest energy path
all data structures are 1D with a length of width*height + 1, with the last
entry reserved for a "virtual bottom" that keeps track of the shortest
energy path from the top row to the bottom row.
all data structures are also column-major indexed to improve data locality.

Our algorithm calculates the energy array by accessing the rbg values at each
pixel. We insert the first "row" of pixels into the energyTo array
and priority queue, with the value being the pixels' energy.
We then intialize all other values in the energyTo array to positive infinity,
including the virtual bottom.
We dequeue the pixel with the minimum energy path and relax each adjacent pixel,
ensuring that adjacent pixels are within the dimensions of the picture.
If the relax helper method finds a shorter path to an adjacent vertex, it
overwrites the energyTo and vertexTo arrays. This method also
adds unvisited vertices to the priority queue and decreases the key of
existing vertices.
Finally, the algorithm traces the shortest energy path backwards, from
the virtual bottom to a pixel in the top row.

To find a horizontal seam, our algorithm transposes the picture before using
the find vertical seam method, and then transposes the picture back to
its original orientation.


/* *****************************************************************************
 *  Describe what makes an image suitable to the seam-carving approach
 *  (in terms of preserving the content and structure of the original
 *  image, without introducing visual artifacts). Describe an image that
 *  would not work well.
 **************************************************************************** */

Images that are suitable to the seam-carving approach generally have a relatively
uniform background that contrasts the primary features of the image.
As a result, the algorithm identifies seams with low contrast to its surrounding
pixels that do not interfere with the important features.
This requirement breaks down when images have either a very busy background or
important features that take up the entire frame. With such high energy images,
even the low energy seams that algorithm removes will introduce visual
artifacts.


/* *****************************************************************************
 *  Perform computational experiments to estimate the running time to reduce
 *  a W-by-H image by one column and one row (i.e., one call each to
 *  findVerticalSeam(), removeVerticalSeam(), findHorizontalSeam(), and
 *  removeHorizontalSeam()). Use a "doubling" hypothesis, where you
 *  successively increase either W or H by a constant multiplicative
 *  factor (not necessarily 2).
 *
 *  To do so, fill in the two tables below. Each table must have 5-10
 *  data points, ranging in time from around 0.25 seconds for the smallest
 *  data point to around 30 seconds for the largest one.
 **************************************************************************** */

(keep W constant)
 W = 2000
 multiplicative factor (for H) =

 H           time (seconds)      ratio       log ratio
------------------------------------------------------
...
...
...
...
...
...


(keep H constant)
 H = 2000
 multiplicative factor (for W) =

 W           time (seconds)      ratio       log ratio
------------------------------------------------------
...
...
...
...
...
...



/* *****************************************************************************
 *  Using the empirical data from the above two tables, give a formula 
 *  (using tilde notation) for the running time (in seconds) as a function
 *  of both W and H, such as
 *
 *       ~ 5.3*10^-8 * W^5.1 * H^1.5
 *
 *  Briefly explain how you determined the formula for the running time.
 *  Recall that with tilde notation, you include both the coefficient
 *  and exponents of the leading term (but not lower-order terms).
 *  Round each coefficient and exponent to two significant digits.
 **************************************************************************** */


Running time (in seconds) to find and remove one horizontal seam and one
vertical seam, as a function of both W and H:


    ~ 
       _______________________________________




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
