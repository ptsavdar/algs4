package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private ArrayList<LineSegment> segments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        checkNullPoints(points);
        segments = new ArrayList<>();
        // Sort points to reduce cost of equal points check
        Point[] sorted = sortPointsCopy(points);
        // Check for equal points
        checkEqualPoints(sorted);
        // Find all 4 collinear points
        find4SizeSegments(sorted);
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        return segments.toArray(new LineSegment[0]);
    }

    private void checkNullPoints(Point[] points) {
        for (Point point : points) {
            if (point == null) throw new IllegalArgumentException();
        }
    }

    private void checkEqualPoints(Point[] points) {
        if (points.length < 2) return;
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].equals(points[i + 1])) throw new IllegalArgumentException();
        }
    }

    private Point[] sortPointsCopy(Point[] points) {
        Point[] tmp = Arrays.copyOf(points, points.length);
        Arrays.sort(tmp);

        return tmp;
    }

    private void find4SizeSegments(Point[] points) {
        if (points.length < 4) return;
        for(int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            for (int j = i + 1; j < points.length - 2; j++) {
                Point q = points[j];
                Double s1 = p.slopeTo(q);
                for (int k = j + 1; k < points.length - 1; k++) {
                    Point r = points[k];
                    Double s2 = p.slopeTo(r);
                    if (!s1.equals(s2)) continue;
                    for (int l = k + 1; l < points.length; l++) {
                        Point s = points[l];
                        Double s3 = p.slopeTo(s);
                        if (s1.equals(s3)) {
                            segments.add(new LineSegment(p, s));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
