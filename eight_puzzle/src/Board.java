import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class Board {

    final private int[]	board;
    final private int 	N;
    final private int 	hamming;
    final private int 	manhattan;

    public Board(int[][] blocks) {
        N = blocks.length;
        board = new int[N*N];
        int k = 0;
        for (int i = 0; i < N; ++i)
            for (int j = 0; j < N; ++j)
                board[k++] = blocks[i][j];
        hamming = hamming(board);
        manhattan = manhattan(board);
    }

    private Board(int[] blocks, int size) {
        assert(blocks.length == size*size);
        N = size;
        board = new int[N*N];
        for (int i = 0; i < N*N; ++i)
            board[i] = blocks[i];
        hamming = hamming(board);
        manhattan = manhattan(board);
    }

    private int hamming(int[] a) {
        int val = 0;
        for (int i = 0; i < a.length; ++i) {
            int n = a[i];
            if (n != 0 && n != i+1) ++val;
        }
        return val;
    }

    private int manhattan(int[] a) {
        int val = 0;
        for (int i = 0; i < a.length; ++i) {
            int n = a[i];
            if (n != 0)
                val += (Math.abs((n-1)/N - i/N) + Math.abs((n-1)%N - i%N));
        }
        return val;
    }

    public int dimension() {return N;}

    public int hamming() {return hamming;}

    public int manhattan() {return manhattan;}

    public boolean isGoal() {return hamming == 0;}

    public Board twin() {
        int[] temp = new int[N*N];
        for (int i = 0; i < temp.length; ++i) temp[i] = board[i];
        for (int i = 0; i < temp.length; i += N) {
            if (temp[i] == 0 || temp[i+1] == 0) continue;
            else {
                swap(temp, i, i+1);
                return new Board(temp, N);
            }
        }
        throw new IllegalArgumentException("wrong board nubmers.");
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public boolean equals(Object y) {
        if (this == y) 		return true;
        if (y == null) 		return false;
        if (this.getClass() != y.getClass()) return false;
        Board that = (Board) y;
        if (N != that.N) 	return false;
        for (int i = 0; i < board.length; ++i)
            if (board[i] != that.board[i]) return false;
        return true;
    }

    public Iterable<Board> neighbors() {
        Queue<Board> queue = new Queue<Board>();
        int idx = -1, row = -1, col = -1;
        for (int i = 0; i < board.length; ++i)
            if (board[i] == 0) {idx = i; row = i/N; col = i%N; break;}
        assert(idx >= 0);
        if (row > 0) {
            int[] temp = new int[N*N];
            for (int i = 0; i < temp.length; ++i) temp[i] = board[i];
            swap(temp, idx, idx-N);
            Board neighbor = new Board(temp, N);
            queue.enqueue(neighbor);
        }
        if (row < N-1) {
            int[] temp = new int[N*N];
            for (int i = 0; i < temp.length; ++i) temp[i] = board[i];
            swap(temp, idx, idx+N);
            Board neighbor = new Board(temp, N);
            queue.enqueue(neighbor);
        }
        if (col > 0) {
            int[] temp = new int[N*N];
            for (int i = 0; i < temp.length; ++i) temp[i] = board[i];
            swap(temp, idx, idx-1);
            Board neighbor = new Board(temp, N);
            queue.enqueue(neighbor);
        }
        if (col < N-1) {
            int[] temp = new int[N*N];
            for (int i = 0; i < temp.length; ++i) temp[i] = board[i];
            swap(temp, idx, idx+1);
            Board neighbor = new Board(temp, N);
            queue.enqueue(neighbor);
        }
        return queue;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                s.append(String.format("%2d ", board[i*N+j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) {
        int[][] blocks = {{0,1},{2,3}};
        Board board = new Board(blocks);
        StdOut.println(board);
        for (Board i : board.neighbors()) {
            StdOut.println(i);
        }
    }

}
