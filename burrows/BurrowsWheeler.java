package burrows;

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private static final int R = 256;

    public static void transform() {
        String text = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(text);
        int first = 0;
        while (first < csa.length() && csa.index(first) != 0)
            first++;
        BinaryStdOut.write(first);

        for (int i = 0; i < csa.length(); i++)
            BinaryStdOut.write(text.charAt(Math.floorMod(csa.index(i) - 1, text.length())));
        BinaryStdOut.close();
    }

    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String text = BinaryStdIn.readString();
        int[] count = new int[R + 1], next = new int[text.length()];
        int minChar = Integer.MAX_VALUE, maxChar = Integer.MIN_VALUE;
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            count[c + 1]++;
            if ((int) c < minChar) minChar = c;
            if ((int) c > maxChar) maxChar = c;
        }
        for (int i = minChar; i < maxChar; i++)
            count[i + 1] += count[i];
        for (int i = 0; i < text.length(); i++)
            next[count[text.charAt(i)]++] = i;
        for (int i = 0, c = next[first]; i < text.length(); c = next[c], i++)
            BinaryStdOut.write(text.charAt(c), 8);
        BinaryStdOut.close();
    }

    public static void main(String[] args) {
        if      (args[0].equals("-")) transform();
        else if (args[0].equals("+")) inverseTransform();
        else throw new IllegalArgumentException("Illegal command line argument");
    }
}
