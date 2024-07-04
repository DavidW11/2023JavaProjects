import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.RedBlackBST;

public class PointST<Value> {

    // red black binary search tree to represent point value pairs
    private RedBlackBST<Point2D, Value> tree;

    // construct an empty symbol table of points
    public PointST() {
        tree = new RedBlackBST<Point2D, Value>();
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return tree.size() == 0;
    }

    // number of points
    public int size() {
        return tree.size();
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("calls put() with null arguments");
        tree.put(p, val);
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls get() with a null key");
        return tree.get(p);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return tree.contains(p);
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        return tree.keys();
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("calls range() with a null rect");
        Queue<Point2D> matches = new Queue<Point2D>();
        // iterate through points and add to queue if within range
        for (Point2D point : points()) {
            if (rect.contains(point)) matches.enqueue(point);
        }
        return matches;
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls nearest() with a null key");
        Iterable<Point2D> points = points();
        // set champ to first point
        Point2D champ = points.iterator().next();
        // iterate through points and update champ
        for (Point2D point : points()) {
            if (point.distanceSquaredTo(p) < champ.distanceSquaredTo(p)) {
                champ = point;
            }
        }
        return champ;
    }

    // unit testing (required)
    public static void main(String[] args) {
        PointST<Integer> bruteForce = new PointST<Integer>();

    }

}
