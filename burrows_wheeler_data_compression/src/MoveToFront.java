
import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int R = 256;

    private static class MoveToFrontPQ {        // origial symbol is 0-based.

        private char[] table = new char[R+1];   // 1-based index binary heap (1-based st)
        private  int[] index = new  int[R+1];   // index of symbol. symbol is in range 0~size-1

        public MoveToFrontPQ() {
            for (int i = 1; i < index.length; ++i) {
                table[i] = (char) (i & 0xffff);
                index[i] = i;
            }
        }

        public int getIndex(char c) {           // c is 0-based symbol;
            int idx = index[c+1];               // 1-based index
            promote(idx);                       // a getIndex must be followed by promote.
            return idx-1;                       // return 0-based index
        }

        public void promote(int idx) {         // move item to front, 1-based index
            while (idx > 1) {
                swap(idx, idx/2);
                idx = idx/2;
            }
        }

        private void swap(int i, int j) {
            char a = table[i], b = table[j];
            table[i] = b; table[j] = a;
            index[a] = j; index[b] = i;
        }
    }

    // do not initiate
    private MoveToFront() {}

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {

        MoveToFrontPQ st = new MoveToFrontPQ();

        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar();
            int index = st.getIndex(c) & 0xff;
            BinaryStdOut.write(index, 8);
        }

        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

        MoveToFrontPQ st = new MoveToFrontPQ();     // use 1-based index

        while (!BinaryStdIn.isEmpty()) {
            int i = BinaryStdIn.readChar();         // 0-based index
            char c = (char) (st.table[i+1] - 1);
            st.promote(i+1);                        // promote operates on 1-based index

            BinaryStdOut.write(c, 8);
        }

        BinaryStdOut.close();
    }


    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
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
