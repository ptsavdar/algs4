package kdtree;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private static final boolean X_COORD = true;
    private Node root;
    private int size;

    private static class Node {
        private Point2D point;
        private Node left, right;
        private final boolean coord;

        public Node(Point2D p, boolean coord) {
            this.point = p;
            this.coord = coord;
        }
    }

    private static class Pair {
        double dist;
        Point2D point;

        private Pair(double dist, Point2D point) {
            this.dist = dist;
            this.point = point;
        }
    }

    public KdTree() {
        root = null;
        size = 0;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        root = put(root, p, X_COORD);
    }

    private Node put(Node h, Point2D p, boolean coord) {
        if (h == null)  {
            size++;
            return new Node(p, coord);
        }
        if (p.equals(h.point)) return h;
        int cmp = h.coord ? Double.compare(p.x(), h.point.x()) : Double.compare(p.y(), h.point.y());
        if (cmp < 0) h.left = put(h.left, p, !h.coord);
        else h.right = put(h.right, p, !h.coord);

        return h;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        Node h = root;
        while (h != null) {
            if (p.equals(h.point)) return true;
            int cmp = h.coord ? Double.compare(p.x(), h.point.x()) : Double.compare(p.y(), h.point.y());
            if (cmp < 0) h = h.left;
            else h = h.right;
        }

        return false;
    }

    public void draw() {
        if (isEmpty()) return;
        draw(root, new RectHV(0, 0, 1, 1));
    }

    private void draw(Node node, RectHV container) {
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        if (node.coord) {
            StdDraw.setPenColor(StdDraw.RED);
        }
        getIntersectingLine(node, container).draw();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        if (node.left != null) draw(node.left, getContainer(node, container, true));
        if (node.right != null) draw(node.right, getContainer(node, container, false));
    }

    private RectHV getContainer(Node node, RectHV parent, boolean left) {
        if (left) return new RectHV(
            parent.xmin(),
            parent.ymin(),
            node.coord ? node.point.x() : parent.xmax(),
            !node.coord ? node.point.y() : parent.ymax()
        );

        return new RectHV(
            node.coord ? node.point.x() : parent.xmin(),
            !node.coord ? node.point.y() : parent.ymin(),
            parent.xmax(),
            parent.ymax()
        );
    }

    private RectHV getIntersectingLine(Node node, RectHV container) {
        if (node.coord) return new RectHV(node.point.x(), container.ymin(), node.point.x(), container.ymax());
        return new RectHV(container.xmin(), node.point.y(), container.xmax(), node.point.y());
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> included = new ArrayList<>();
        range(rect, root, new RectHV(0, 0, 1, 1), included);

        return included;
    }

    private void range(RectHV rect, Node node, RectHV container, List<Point2D> points) {
        if (node == null) return;
        boolean intersects = false;
        if (rect.contains(node.point)) {
            points.add(node.point);
            intersects = true;
        }
        intersects = intersects || rect.intersects(getIntersectingLine(node, container));
        int cmp = intersects ? 0 : node.coord ? Double.compare(rect.xmin(), node.point.x()) : Double.compare(rect.ymin(), node.point.y());
        if (intersects || cmp < 0) range(rect, node.left, getContainer(node, container, true), points);
        if (intersects || cmp > 0) range(rect, node.right, getContainer(node, container, false), points);
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        if (this.contains(p)) return p;
        Pair nearest = new Pair(Double.POSITIVE_INFINITY, null);
        nearest(root, p, new RectHV(0, 0, 1, 1), nearest);

        return nearest.point;
    }

    private void nearest(Node node, Point2D target, RectHV container, Pair nearest) {
        if (node == null || container.distanceSquaredTo(target) > nearest.dist) return;
        double tmpDist = node.point.distanceSquaredTo(target);
        if (tmpDist < nearest.dist) {
            nearest.dist = tmpDist;
            nearest.point = node.point;
        }
        int cmp = node.coord ? Double.compare(target.x(), node.point.x()) : Double.compare(target.y(), node.point.y());
        nearest((cmp < 0) ? node.left : node.right, target, getContainer(node, container, cmp < 0), nearest);
        nearest((cmp < 0) ? node.right : node.left, target, getContainer(node, container, cmp >= 0), nearest);
    }

    public static void main(String[] args) {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        StdDraw.setYscale(-0.0001, 1.0001);
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        StdDraw.clear();
        kdtree.draw();
    }
}
