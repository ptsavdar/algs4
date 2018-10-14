package burrows;

public class CircularSuffixArray {
    private static final int CUTOFF =  15;
    private final int[] sortedIdx;
    private final int N;

    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException();
        N = s.length();
        sortedIdx = new int[N];
        for (int i = 0; i < N; i++)
            sortedIdx[i] = i;
        sort(s, 0, N - 1, 0);
    }

    public int length() {
       return N;
    }

    public int index(int i) {
        if (i < 0 || i > N - 1) throw new IllegalArgumentException();
        return sortedIdx[i];
    }

    // return the dth character of s, -1 if d = length of s
    private int charAt(String s, int suffix, int d) {
        if (d == N) return -1;
        return s.charAt((suffix + d) % N);
    }


    // 3-way string quicksort a[lo..hi] starting at dth character
    private void sort(String s, int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi - lo <= CUTOFF) {
            insertion(s, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(s, sortedIdx[lo], d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(s, sortedIdx[i], d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else              i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(s, lo, lt-1, d);
        if (v >= 0) sort(s, lt, gt, d+1);
        sort(s, gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(String s, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(s, sortedIdx[j], sortedIdx[j-1], d); j--)
                exch(j, j-1);
    }

    // exchange a[i] and a[j]
    private void exch(int i, int j) {
        int temp = sortedIdx[i];
        sortedIdx[i] = sortedIdx[j];
        sortedIdx[j] = temp;
    }

    private boolean less(String s, int v, int w, int d) {
        for (int i = d; i < N; i++) {
            if (charAt(s, v, i) < charAt(s, w, i)) return true;
            if (charAt(s, v, i) > charAt(s, w, i)) return false;
        }
        return false;
    }

    public static void main(String[] args) {
        CircularSuffixArray cf = new CircularSuffixArray(args[0]);
        for (int i = 0; i < cf.length(); i++) {
            System.out.println(cf.index(i));
        }
    }
}
