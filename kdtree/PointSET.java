package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (contains(p)) return;

        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();

        return points.contains(p);
    }

    public void draw() {
        if (isEmpty()) return;
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        for (Point2D p : points) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> included = new ArrayList<>();
        for (Point2D p : points)
            if (rect.contains(p)) included.add(p);

        return included;
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        if (this.contains(p)) return p;

        double minDist = Double.POSITIVE_INFINITY;
        Point2D minP = null;
        for (Point2D pt : points) {
            double dist = p.distanceSquaredTo(pt);
            if (dist < minDist) {
                minDist = dist;
                minP = pt;
            }
        }

        return minP;
    }
}
