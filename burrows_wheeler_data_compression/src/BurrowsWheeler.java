import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class BurrowsWheeler {

    private static int R = 256;

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {

        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);

        int first = -1;
        char[] tail = new char[s.length()];
        for (int i = 0; i < tail.length; ++i) {

            if (csa.index(i) == 0) {
                first = i;
                tail[i] = s.charAt(s.length()-1);
            }
            else {
                tail[i] = s.charAt(csa.index(i)-1);
            }
        }

        BinaryStdOut.write(first, 32);  // must write int in 4 bytes
        for (char c : tail) {
            BinaryStdOut.write(c, 8);   // write char in 1 byte
        }

        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {

        if (BinaryStdIn.isEmpty()) {
            throw new IllegalArgumentException("Empty BiStdIn.");
        }

        int first = BinaryStdIn.readInt();

//        StringBuilder sb = new StringBuilder();
//        while (!BinaryStdIn.isEmpty()) {
//            sb.append(BinaryStdIn.readChar());
//        }
//        char[] tail = sb.toString().toCharArray();

        char[] tail = BinaryStdIn.readString().toCharArray();
        char[] head = new char[tail.length];
        int[] next = new int[tail.length];

        for (int i = 0; i < head.length; ++i) {
            head[i] = tail[i];
        }
        Arrays.sort(head);

//        int first_freq = 0;
//        for (int i = 0; i < tail.length; ++i) {
//            if (tail[i] == tail[first]) {++first_freq;}
//        }

        HashMap<Character, LinkedList<Integer>> map = new HashMap<>();

        for (int i = 0; i < tail.length; ++i) {

//            if (first_freq == 1 && i == first) {                     // the 'first' should be the last one to be added
//                continue;                         // since the last 'head' points to first.
//            }

            if (!map.containsKey(tail[i])) {
                map.put(tail[i], new LinkedList<>());
            }
            map.get(tail[i]).add(i);
        }

//        if (first_freq == 1) {
//            if (!map.containsKey(tail[first])) {
//                map.put(tail[first], new LinkedList<>());
//            }
//            map.get(tail[first]).add(first);
//        }

        for (int i = 0; i < next.length; ++i) {
            next[i] = map.get(head[i]).removeFirst();
        }

//        StdOut.println("[0, 1, 2, 3, 4, 5, 6, 7, 8, 9]");
//        StdOut.println(Arrays.toString(tail));
//        StdOut.println(Arrays.toString(head));
//        StdOut.println(Arrays.toString(next));
//        StdOut.println("First = " + first);

        int cur = first;

        StringBuilder sb = new StringBuilder();

        while(next[cur] != first) {
            sb.append(head[cur]);
            BinaryStdOut.write(head[cur], 8);
            cur = next[cur];
        }
        sb.append(head[cur]);
        BinaryStdOut.write(head[cur], 8);

        if (sb.length() < head.length) {
            int N = head.length/sb.length();
            while(--N > 0) {
                BinaryStdOut.write(sb.toString());
            }
        }


        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {

        if (args[0].equals("-")) {
            encode();
        }
        else if (args[0].equals("+")) {
            decode();
        }
        else {
            throw new IllegalArgumentException("args[0] invalid");
        }
    }
}
