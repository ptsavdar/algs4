package collinear;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private ArrayList<LineSegment> segments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) throw new IllegalArgumentException();
        checkNullPoints(points);
        segments = new ArrayList<>();
        // Sort points to reduce cost of equal points check
        Point[] sorted = sortPointsCopy(points);
        // Check for equal points
        checkEqualPoints(sorted);
        // Find all 4 or more collinear points
        findSegments(sorted);
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

    private void findSegments(Point[] points) {
        if (points.length < 4) return;
        ArrayList<Pair> pairs = new ArrayList<>();
        for (int i = 0; i < points.length - 3; i++) {
            Point p = points[i];
            // Order by slopes
            Arrays.sort(points, i + 1, points.length, p.slopeOrder());
            Double prevSlope = Double.NEGATIVE_INFINITY;
            int pointsCnt = 2;
            for (int j = i + 1; j < points.length; j++) {
                Point q = points[j];
                Double s = p.slopeTo(q);
                if (s.equals(prevSlope)) pointsCnt++;
                if (j == points.length - 1 || !s.equals(prevSlope)) {
                    if (pointsCnt > 3) {
                        Pair pr = new Pair(prevSlope, p, s.equals(prevSlope) ? q : points [j - 1]);
                        /*if (!pairs.contains(pr)) {
                            pairs.add(pr);
                            segments.add(new LineSegment(pr.s, pr.e));
                        }*/
                        pairs.add(pr);
                    }
                    pointsCnt = 2;
                    prevSlope = s;
                }
            }
            Arrays.sort(points, i + 1, points.length);
        }
        uniqueSegments(pairs);
    }

    private void uniqueSegments(ArrayList<Pair> pairs) {
        pairs.sort(Pair::compareTo);
        Pair prev = null;
        for (Pair pair : pairs) {
            if (pair.equals(prev)) continue;
            segments.add(new LineSegment(pair.s, pair.e));
            prev = pair;
        }
    }

    private class Pair implements Comparable<Pair> {
        Double slope;
        Point s, e;
        private Pair(Double slope, Point s, Point e) {
            this.slope = slope;
            this.s = s;
            this.e = e;
        }

        @Override
        public int compareTo(Pair pair) {
            if (!this.slope.equals(pair.slope)) return this.slope.compareTo(pair.slope);

            if (!this.e.equals(pair.e)) return this.e.compareTo(pair.e);

            return this.s.compareTo(pair.s);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;

            if (o == null) return false;

            if (o.getClass() != this.getClass()) return false;

            Pair that = (Pair) o;
            if (!this.slope.equals(that.slope)) return false;
            if (!this.e.equals(that.e)) return false;

            return true;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
