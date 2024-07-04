Programming Assignment 2: Deques and Randomized Queues


/* *****************************************************************************
 *  Explain briefly how you implemented the randomized queue and deque.
 *  Which data structure did you choose (array, linked list, etc.)
 *  and why?
 **************************************************************************** */

I implemented the deque using a doubly-linked list, because we only need to
modify the first and last elements of the deque. By maintaining pointers
to the first and next-available nodes, we can add or remove the first or last
elements in constant time, without having to resize an underlying array.
I used a doubly-linked list because in order to remove the last item, the
algorithm needs to be able to access the previous node.

I implemented the randomized queue using a resizing array, in order to access
elements in the middle of the queue in constant time. The class still
keeps indices to the front and back of the queue for several reasons.
The resize() method (either doubling or halving the capacity),
re-arranges the elements in front-back order.
The enqueue() method adds items to the back of the queue,
wrapping the index around to the front of the array if necessary.
And the dequeue() method generates a random index using the
StdRandom.uniformInt() method, swapping the random element with the front of
the queue before removing it, to ensure that null elements do not separate
items in the queue. The iterator copies the underlying array
(excluding null elements) to a new array in front-back order.
The RandomizedQueueIterator then shuffles the copy array before iterating
through each index.


/* *****************************************************************************
 *  How much memory (in bytes) do your data types use to store n items
 *  in the worst case? Use the 64-bit memory cost model from Section
 *  1.4 of the textbook and use tilde notation to simplify your answer.
 *  Briefly justify your answers and show your work.
 *
 *  Do not include the memory for the items themselves (as this
 *  memory is allocated by the client and depends on the item type)
 *  or for any iterators, but do include the memory for the references
 *  to the items (in the underlying array or linked list).
 **************************************************************************** */

For a queue of N items, the worst memory case is that the underlying array
storing n items has a length of 4(n-1), right before we would resize the array
to half its capacity. N objects take up (16 bytes overhead + 8 bytes reference)
24N bytes of memory. 3N null references take up (3*8) 24N bytes.
Thus, the total memory usage is 24N+24N = 48N bytes.
Randomized Queue:   ~  48N  bytes

For a doubly-linked list of N nodes, each node contains one reference to
an Item object and two references to next and previous nodes (3*8 = 24).
Including object overhead (16 bytes) and extra overhead for a nested,
non-static class (8 bytes), the final memory usage is 24+16+8 = 48N bytes.
Deque:              ~  48N  bytes


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */


/* *****************************************************************************
 *  Describe any serious problems you encountered.                    
 **************************************************************************** */

My first implementation of the Deque class used a singly-linked list, which
worked for all methods except removeLast(). I was hesitant to use a
doubly-linked list (and add 8N bytes of memory for each previous Node reference)
for only one method, but I settled on this implementation after trying a
variety of other methods.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback   
 *  on how much you learned from doing the assignment, and whether    
 *  you enjoyed doing it.                                             
 **************************************************************************** */
Similar to how the percolation assignment gave me the chance to maximize the
time efficiency of my program, this assignment taught me to appply my knowledge
of memory usage to a real coding scenario.
