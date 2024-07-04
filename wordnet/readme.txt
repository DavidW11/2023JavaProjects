Programming Assignment 6: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the 
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */
To store the information in sysnsets.txt, we created two data structures, a
LinearProbingHashST and a RedBlackBST. The Hash Table mapped the integer index
of a synset with the array of strings that consisted of the synset. This was
implemented using the universial hashing assumption and the java algs4
implementation of the Hash Map. This allowed us to retrieve the synset
associated with an index in linear time. Furthermore, a RedBlackBST was created
that would store each noun String as a node and the value associated with it
wouldbe a queue of integers, with each integer being associated with the
particular  index of the synset that the noun was found in. This allowed us to
keep track of each indivudal string noun in case they appeared in multiple
synsets and quickly determine whihc synsets they were in logarathmic time.


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */
To store the information in hypernyms.txt, we decided to create a digraph with
directed edges pointing towards the hypernym of a synset. This was done by
iterating through each of the lines of hypernym.txt and adding each adjancecy
relationship in one-by-one. We decided to utilize the digraph as it allowed us
to place the digraph into a ShortestCommonAncestors object that we could call
throughout the WordNet program wihtout the need for storing a seperate digraph
object. Furthermore, the digraph data structure allowed us to add adjaecent
edge relationships in a very time and space efficient manner.


/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 *  What is the order of growth of the worst-case running times of
 *  your algorithm? Express your answer as a function of the
 *  number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify
 *  your answer.
 **************************************************************************** */

Description:
We took advantage of the fact that a digraph cannot have a topographical order
if it has any directed cycles. The Topographical object constructor takes
Theta(E+V) time and the hasOrder() method takes constant time. We then
check that the digraph is rooted by confirming that only one vertex
has an outdegree of 0 (meaning there is only one root).
We iterate through V vertices and the outdegree method takes constant time.
Thus, the worst-case running time is Theta(E+V) + Theta(V) = Theta(E+V)

Order of growth of running time: Theta(E+V)


/* *****************************************************************************
 *  Describe concisely your algorithm to compute the shortest common ancestor
 *  in ShortestCommonAncestor. For each method, give the order of growth of
 *  the best- and worst-case running times. Express your answers as functions
 *  of the number of vertices V and the number of edges E in the digraph.
 *  (Do not use other parameters.) Use Big Theta notation to simplify your
 *  answers.
 *
 *  If you use hashing, assume the uniform hashing assumption so that put()
 *  and get() take constant time per operation.
 *
 *  Be careful! If you use a BreadthFirstDirectedPaths object, don't forget
 *  to count the time needed to initialize the marked[], edgeTo[], and
 *  distTo[] arrays.
 **************************************************************************** */

Description:
We first check that the parameters given are within the domain of our digraph.
We used a queue to store vertices for breadth first search. And
we used a HashMap to store the ancestors of each parameter along with its
distance from the vertex of interest.
For each parameter, we add the vertex or set of vertices to both the queue
and HashMap, intializing the value for each key in the HashMap to 0.
We then use a helper method to run breadth first search from each vertex
in the queue. This method explores the adjacent vertices of the next
vertex in the queue, enqueueing the adjacent vertex if it has not
already been visited (and thus put in the hashmap). The method
then puts the vertex and its associated distance (1+previous vertex's distance)
into the HashMap, unless the value stored in the HashMap is smaller.
After the algorithm runs breadth first search on both parameters,
it iterates through the HashMap keys and checks whether a vertex
is contained within both HashMaps, signifying a common ancestor.
The algorithm returns the common ancestor with the smallest
sum distance between the two parameters.

Under the uniform hashing assumption, the put(), get(), and contains() functions
of the HashMap class take constant time. Iterating through the set of vertices
takes Theta(V) time worst case.
Breadth first search takes Theta(E+V) time and is performed twice
(once for each parameter).
Iterating through the HashMap of ancestors takes Theta(V) time.
2*Theta(V) + 2*Theta(E+V) + Theta(V) = Theta(E+V) worst case

However, since our algorithm only searches the ancestors reachable from
each parameter, the average running time is lower.

In the best case, both parameters are the root and breadth first search
has no adjacent vertices to search. Both HashMaps will then only have one key.
Thus, the algorithm takes constant time in the best case.

                                 running time
method                  best case            worst case
--------------------------------------------------------
length()                Theta(1)             Theta(E+V)

ancestor()              Theta(1)             Theta(E+V)

lengthSubset()          Theta(1)             Theta(E+V)

ancestorSubset()        Theta(1)             Theta(E+V)


/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
 No known bugs or limitations to the code.


/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
We encountred issues when working on the ShortestCommonAncestor optimization as
we attempted to move away from BreadthFirstSearch class to our own
implementation. As we tried to implement the code, we initially requeed vertices
that had already been visited and replaced the distances in the hashmap with the
larger distances, which was very inefficient.


/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
We thoroughly enjoyed working on this project as it allowed us to push our
understanding of HashMaps, RedBlackBSTs, and Digraphs. The algorithms were
also very engaging to create and fun to think through. We both really liked
the problem-solving process that this assignment required!