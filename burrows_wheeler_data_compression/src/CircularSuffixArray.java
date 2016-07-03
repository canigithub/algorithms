import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private int N;
    private String string;
    private int[] index;    // index[i] read as suffix of rank i

    public CircularSuffixArray(String s) {      // circular suffix array of s

        if (s == null) {
            throw new NullPointerException("string is null.");
        }

        N = s.length();
        string = s;

        index = new int[N];
        for (int i = 0; i < index.length; ++i) {
            index[i] = i;
        }

        sortCircularSuffix(0, N-1, 0);

    }

    private void sortCircularSuffix(int lo, int hi, int d) { // 3-way quick sort

        if (lo >= hi) {
            return;
        }

        if (d >= N) {
            return;
        }

        char pivot = getChar(index[lo], d);

        int i = lo, j = lo, k = hi;

        while (j <= k) {

            if (getChar(index[j], d) < pivot)  {
                swap(i++, j++);
            }
            else if (getChar(index[j], d) == pivot) {
                ++j;
            }
            else if (getChar(index[k], d) > pivot)  {
                --k;
            }
            else if (getChar(index[k], d) == pivot) {
                swap(j++, k--);
            }
            else {
                swap(i++, k);
                swap(j++, k--);
            }

        }

        // when out of loop: i->1st elem = pivot, j->1st elem > pivot
        sortCircularSuffix(lo, i-1, d);  // sort  less part on same level
        sortCircularSuffix(j, hi, d);    // sort large part on same level
        sortCircularSuffix(i, j-1, d+1); // sort equal part on next level

    }

    private char getChar(int i, int d) {    // dth char of ith suffix
        if (i + d < N) return string.charAt(i+d);
        return string.charAt((i+d)%N);
    }

    private void swap(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }

    public int length() {                       // length of s
        return N;
    }

    public int index(int i) {                   // returns index of ith sorted suffix

        if (i < 0 || i > N-1) {
            throw new IndexOutOfBoundsException("out of string boundary.");
        }

        return index[i];
    }

    public static void main(String[] args) {    // unit testing of the methods (optional)

        CircularSuffixArray cs = new CircularSuffixArray("");

        StdOut.println(Arrays.toString(cs.index));

        cs.sortCircularSuffix(0, 11, 0);
        StdOut.println(Arrays.toString(cs.index));

    }
}
