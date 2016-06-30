import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class CircularSuffixArray {

    private static int R = 256;
    private int N;
    private char[] charray;
    private int[] index;    // index[i] read as suffix of rank i

    public CircularSuffixArray(String s) {      // circular suffix array of s

        if (s == null) {
            throw new NullPointerException("string is null.");
        }

        if (s.equals("")) {
            throw new IllegalArgumentException("string is empty.");
        }

        N = s.length();
        charray = s.toCharArray();

        index = new int[N];
        for (int i = 0; i < index.length; ++i) {
            index[i] = i;
        }

//        sortCircularSuffix(0, N-1, 0);

    }

    private void sortCircularSuffix(int lo, int hi, int d) { // use 3-way quick sort

        if (lo >= hi) {return;}

        if (d >= N) {return;}

        char pivot = getChar(index[lo], d);

//        StdOut.println("pivot = " + pivot);

        int i = lo, j = lo, k = hi;

        while (j <= k) {

//            StdOut.print("i=" + i + ":" + getChar(i, d) + " j=" + j + ":" + getChar(j, d) + " k=" + k + ":" + getChar(k, d));

            if      (getChar(index[j], d) < pivot)  {
//                StdOut.print(" " + 1);
                swap(i++, j++);
            }
            else if (getChar(index[j], d) == pivot) {
//                StdOut.print(" " + 2);
                ++j;
            }
            else if (getChar(index[k], d) > pivot)  {
//                StdOut.print(" " + 3);
                --k;
            }
            else if (getChar(index[k], d) == pivot) {
//                StdOut.print(" " + 4);
                swap(j++, k--);
            }
            else    {
//                StdOut.print(" " + 5);
                swap(i++, k); swap(j++, k--);
            }

//            StdOut.print("  ");
//            for (int v = 0; v < N; ++v) {
//                StdOut.print(getChar(index[v], 0));
//            }
//            StdOut.println();
        }

//        StdOut.println("i=" + i + ":" + getChar(index[i], d) + " j=" + j + ":" + getChar(index[j], d) + " k=" + k + ":" + getChar(index[k], d));

        // when out of loop: i->1st elem = pivot, j->1st elem > pivot
        sortCircularSuffix(lo, i-1, d);  // sort  less part on same index
        sortCircularSuffix(j, hi, d);    // sort large part on same index
        sortCircularSuffix(i, j-1, d+1); // sort equal part on next index

//        for (int v = 0; v < N; ++v) {
//            StdOut.print(getChar(index[v], d));
//        }
//        StdOut.println();

    }

    private char getChar(int i, int d) {    // dth char of ith suffix
        if (i + d < N) return charray[i+d];
        return charray[(i+d)%N];
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

        CircularSuffixArray cs = new CircularSuffixArray("ABRACADABRA!");

        StdOut.println(Arrays.toString(cs.index));

        cs.sortCircularSuffix(0, 11, 0);
        StdOut.println(Arrays.toString(cs.index));

    }
}
