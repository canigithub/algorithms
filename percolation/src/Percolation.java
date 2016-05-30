
import edu.princeton.cs.algs4.*;

public class Percolation {

    private int size;
    private boolean percolates;
    private boolean[] open;
    private boolean[] touch_top;
    private boolean[] touch_bot;
    private WeightedQuickUnionUF wquf;


    public Percolation(int N) {             // create N-by-N grid, with all sites blocked

        if (N <= 0) {
            throw new java.lang.IllegalArgumentException("percolation ctor N<=0");
        }

        size = N;
        percolates = false;

        open = new boolean[size*size];
        touch_top = new boolean[size*size];
        touch_bot = new boolean[size*size];

        // abandon virtual top and bot for backwash problem
        wquf = new WeightedQuickUnionUF(size*size);
    }


    private int calc_index(int i, int j) {

        return i*size + j;
    }


    public void open(int i, int j) {        // open site (row i, column j) if it is not open already

        if (i < 1 || i > size || j < 1 || j > size) {
            throw new java.lang.IndexOutOfBoundsException("idx out of bounds");
        }

        if (isOpen(i, j)) {
            return;
        }

        --i; --j;
        open[calc_index(i, j)] = true;

        if (i == 0) {
            touch_top[calc_index(i, j)] = true;
        }

        if (i == size-1) {
            touch_bot[calc_index(i, j)] = true;
        }

        if (i > 0 && isOpen(i-1+1, j+1)) {  // if not 1st row, connect up row
            int root_1 = wquf.find(calc_index(i, j));
            int root_2 = wquf.find(calc_index(i-1, j));

            if (touch_top[root_1] || touch_top[root_2]) {
                touch_top[root_1] = true;
                touch_top[root_2] = true;
            }

            if (touch_bot[root_1] || touch_bot[root_2]) {
                touch_bot[root_1] = true;
                touch_bot[root_2] = true;
            }

            wquf.union(root_1, root_2);
        }

        if (i < size-1 && isOpen(i+1+1, j+1)) {  // if not last row, connect bot row
            int root_1 = wquf.find(calc_index(i, j));
            int root_2 = wquf.find(calc_index(i+1, j));

            if (touch_top[root_1] || touch_top[root_2]) {
                touch_top[root_1] = true;
                touch_top[root_2] = true;
            }

            if (touch_bot[root_1] || touch_bot[root_2]) {
                touch_bot[root_1] = true;
                touch_bot[root_2] = true;
            }

            wquf.union(root_1, root_2);
        }

        if (j > 0 && isOpen(i+1, j-1+1)) {  // if not 1st col, connect left col
            int root_1 = wquf.find(calc_index(i, j));
            int root_2 = wquf.find(calc_index(i, j-1));

            if (touch_top[root_1] || touch_top[root_2]) {
                touch_top[root_1] = true;
                touch_top[root_2] = true;
            }

            if (touch_bot[root_1] || touch_bot[root_2]) {
                touch_bot[root_1] = true;
                touch_bot[root_2] = true;
            }

            wquf.union(root_1, root_2);
        }

        if (j < size-1 && isOpen(i+1, j+1+1)) {  // if not last col, connect right col
            int root_1 = wquf.find(calc_index(i, j));
            int root_2 = wquf.find(calc_index(i, j+1));

            if (touch_top[root_1] || touch_top[root_2]) {
                touch_top[root_1] = true;
                touch_top[root_2] = true;
            }

            if (touch_bot[root_1] || touch_bot[root_2]) {
                touch_bot[root_1] = true;
                touch_bot[root_2] = true;
            }

            wquf.union(root_1, root_2);
        }

        int root = wquf.find(calc_index(i, j));
        if (touch_top[root] && touch_bot[root]) {
            percolates = true;
        }
    }


    public boolean isOpen(int i, int j) {   // is site (row i, column j) open?

        if (i < 1 || i > size || j < 1 || j > size) {
            throw new java.lang.IndexOutOfBoundsException("idx out of bounds");
        }

        --i; --j;
        return open[calc_index(i, j)];
    }

    public boolean isFull(int i, int j) {   // is site (row i, column j) full?

        if (i < 1 || i > size || j < 1 || j > size) {
            throw new java.lang.IndexOutOfBoundsException("idx out of bounds");
        }

        --i; --j;

        int root = wquf.find(calc_index(i, j));
        return touch_top[root];
    }


    public boolean percolates() {           // does the system percolate?

        return percolates;
    }



    public static void main(String[] args) {  // test client (optional)

        In input = new In(args[0]);

        int N = Integer.parseInt(input.readLine());

        Percolation perco = new Percolation(N);

        while (input.hasNextLine()) {
            String s = input.readLine().trim();
            String[] ss = s.split(" +");
            int i = Integer.parseInt(ss[0]);
            int j = Integer.parseInt(ss[1]);
            perco.open(i, j);
        }

        StdOut.println(perco.percolates());

//        StdOut.println(perco.isFull(9, 1));

    }
}

