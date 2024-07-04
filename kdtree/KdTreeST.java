import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.Stopwatch;

public class KdTreeST<Value> {

    private Node root;
    private int size;

    private class Node {
        private Point2D p;     // the point
        private Value val;     // the symbol table maps the point to this value
        private RectHV rect;   // the axis-aligned rectangle corresponding to this node
        private Node lb;       // the left/bottom subtree
        private Node rt;       // the right/top subtree

        public Node(Point2D p, Value val, RectHV rect) {
            this.p = p;
            this.val = val;
            this.rect = rect;
        }
    }

    // construct an empty symbol table of points
    public KdTreeST() {
        root = null;
        size = 0;
    }

    // is the symbol table empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // number of points
    public int size() {
        return size;
    }

    // associate the value val with point p
    public void put(Point2D p, Value val) {
        if (p == null || val == null)
            throw new IllegalArgumentException("calls put() with null arguments");
        // orient true = vertical, false = horizontal
        root = put(root, null, p, val, true);
    }

    private Node put(Node x, Node parent, Point2D p, Value val, boolean orient) {

        if (x == null) {
            size++;
            double xmin, xmax, ymin, ymax;
            if (parent == null) {
                // if adding first point (root)
                xmin = Double.NEGATIVE_INFINITY;
                ymin = Double.NEGATIVE_INFINITY;
                xmax = Double.POSITIVE_INFINITY;
                ymax = Double.POSITIVE_INFINITY;
            }
            else if (!orient) {
                // horizontal orientation --> parent has vertical orientation
                if (p.x() < parent.p.x()) {
                    // point is left of parent
                    xmax = parent.p.x();
                    xmin = parent.rect.xmin();
                }
                else {
                    // point is right of parent
                    xmin = parent.p.x();
                    xmax = parent.rect.xmax();
                }
                ymin = parent.rect.ymin();
                ymax = parent.rect.ymax();
            }
            else {
                // vertical orientation --> parent has horizontal orientation
                if (p.y() < parent.p.y()) {
                    // point is below parent
                    ymax = parent.p.y();
                    ymin = parent.rect.ymin();
                }
                else {
                    // point is above parent
                    ymin = parent.p.y();
                    ymax = parent.rect.ymax();
                }
                xmin = parent.rect.xmin();
                xmax = parent.rect.xmax();
            }
            return new Node(p, val, new RectHV(xmin, ymin, xmax, ymax));
        }

        double cmp;
        if (orient) cmp = p.x() - x.p.x();
        else cmp = p.y() - x.p.y();

        if (cmp < 0) x.lb = put(x.lb, x, p, val, !orient);
        else if (x.p.equals(p)) x.val = val;
        else x.rt = put(x.rt, x, p, val, !orient);

        return x;

        /*
        double cmpA;
        double cmpB;
        if (orient) {
            cmpA = p.x() - x.p.x();
            cmpB = p.y() - x.p.y();
        }
        else {
            cmpA = p.y() - x.p.y();
            cmpB = p.x() - x.p.x();
        }

        if (cmpA < 0) x.lb = put(x.lb, x, p, val, !orient);
        else if (cmpA > 0 || cmpB != 0) x.rt = put(x.rt, x, p, val, !orient);
        else x.val = val;
        return x;
        */
    }

    // value associated with point p
    public Value get(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls get() with a null key");
        return get(root, p, true);
    }

    private Value get(Node x, Point2D p, boolean orient) {
        if (x == null) return null;

        double cmp;
        if (orient) cmp = p.x() - x.p.x();
        else cmp = p.y() - x.p.y();

        if (cmp < 0) return get(x.lb, p, !orient);
        else if (x.p.equals(p)) return x.val;
        else return get(x.rt, p, !orient);
    }

    // does the symbol table contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return get(p) != null;
    }

    // all points in the symbol table
    public Iterable<Point2D> points() {
        Queue<Point2D> keys = new Queue<Point2D>();
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            if (x == null) continue;
            keys.enqueue(x.p);
            queue.enqueue(x.lb);
            queue.enqueue(x.rt);
        }
        return keys;
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException("calls range() with a null rect");
        Queue<Point2D> matches = new Queue<Point2D>();
        range(root, rect, matches);
        return matches;
    }

    private void range(Node x, RectHV rect, Queue<Point2D> matches) {
        // if (x == null) throw new IllegalArgumentException("calls range() with a null key");
        if (rect.contains(x.p)) matches.enqueue(x.p);
        if (x.lb != null && rect.intersects(x.lb.rect)) range(x.lb, rect, matches);
        if (x.rt != null && rect.intersects(x.rt.rect)) range(x.rt, rect, matches);
    }

    // a nearest neighbor of point p; null if the symbol table is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("calls nearest() with a null key");
        if (root == null) return null;
        return nearest(root, root, p, true).p;
    }

    private Node nearest(Node x, Node champ, Point2D p, boolean orient) {
        // return champ if reach end of tree
        if (x == null ||
                x.rect.distanceSquaredTo(p) >= champ.p.distanceSquaredTo(p)) return champ;

        // System.out.println(x.p);
        // current node is nearer to point than champ
        if (x.p.distanceSquaredTo(p) < champ.p.distanceSquaredTo(p)) champ = x;

        Node smallChamp;
        Node largeChamp;
        if ((orient && x.p.x() <= p.x()) || (!orient && x.p.y() <= p.y())) {
            smallChamp = nearest(x.lb, champ, p, !orient);
            largeChamp = nearest(x.rt, smallChamp, p, !orient);
        }
        else {
            smallChamp = nearest(x.rt, champ, p, !orient);
            largeChamp = nearest(x.lb, smallChamp, p, !orient);
        }
        return largeChamp;
    }

    // unit testing (required)
    public static void main(String[] args) {
        /*
        KdTreeST<Integer> test = new KdTreeST<Integer>();
        Point2D a = new Point2D(0.7, 0.2);
        Point2D b = new Point2D(0.5, 0.4);
        Point2D c = new Point2D(0.2, 0.3);
        Point2D d = new Point2D(0.4, 0.7);
        Point2D e = new Point2D(0.9, 0.6);
        RectHV rect = new RectHV(0, 0, 2, 2);
        test.put(a, 1);
        test.put(b, 3);
        test.put(c, 2);
        test.put(d, 4);
        test.put(e, 5);
        System.out.println(test.get(new Point2D(0.4, 0.7)));
        System.out.println(test.contains(new Point2D(0.4, 0.7)));
        System.out.println(test.points());
        System.out.println(test.range(rect));
        Point2D f = new Point2D(4, 4);
        System.out.println(test.nearest(f));
         */
        String filename = "input1M.txt";
        In in = new In(filename);

        // initialize the two data structures with point from standard input
        KdTreeST<Integer> kdtree = new KdTreeST<Integer>();
        for (int i = 0; !in.isEmpty(); i++) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.put(p, i);
        }

        Stopwatch stopwatch = new Stopwatch();
        int m = 100;
        for (int n = 0; n < m; n++) {
            Point2D randPoint = new Point2D(StdRandom.uniformDouble(0.0, 1.0),
                                            StdRandom.uniformDouble(0.0, 1.0));
            kdtree.nearest(randPoint);
        }
        System.out.println("nearest calls: " + m);
        System.out.println("elapsed time: " + stopwatch.elapsedTime());
        System.out.println("nearest calls per second: " + m / stopwatch.elapsedTime());
    }
}
