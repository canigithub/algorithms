
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class Solver {
    private boolean 	 solvable = true;
    private int			 moves;
    private Stack<Board> solution;

    public Solver(Board initial) {
        MinPQ<Node> minpq 	   = new MinPQ<Node>();
        MinPQ<Node> minpq_twin = new MinPQ<Node>();
        Node init 	   = new Node(initial, null, 0);
        Node init_twin = new Node(initial.twin(), null, 0);
        minpq.insert(init);
        minpq_twin.insert(init_twin);
        Node out 	  = minpq.delMin();
        Node out_twin = minpq_twin.delMin();
        while (!out.board.isGoal()) {
            if (out_twin.board.isGoal()) {solvable = false; moves = -1; solution = null; break;}

            for (Board b : out.board.neighbors())
                if (out.prvNode == null || !b.equals(out.prvNode.board)) {
                    Node nn = new Node(b, out, out.moves+1);
                    minpq.insert(nn);
                }
            out = minpq.delMin();

            for (Board b : out_twin.board.neighbors())
                if (out_twin.prvNode == null || !b.equals(out_twin.prvNode.board)) {
                    Node nn_twin = new Node(b, out_twin, out_twin.moves+1);
                    minpq_twin.insert(nn_twin);
                }
            out_twin = minpq_twin.delMin();

        }

        if (solvable) {
            moves = out.moves;
            solution = new Stack<Board>();
            while (out != null) {
                solution.push(out.board);
                out = out.prvNode;
            }
        }
    }

    public boolean isSolvable() {return solvable;}

    public int moves() {return moves;}

    public Iterable<Board> solution() {return solution;}

    private class Node implements Comparable<Node>{
        final Board	board;
        final Node	prvNode;
        final int	moves;
        final int   priority;

        public Node(Board cur, Node prv, int m) {
            board = cur;
            prvNode = prv;
            moves = m;
            priority = cur.manhattan()+moves;
        }

        public int compareTo(Node that) {
            return priority - that.priority;
        }
    }

    public static void main(String[] args) {
        assert(args.length >= 1);
        In in = new  In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        Solver solver = new Solver(initial);
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            for (Board board : solver.solution())
                StdOut.println(board);
            StdOut.println("Minimum number of moves = " + solver.moves());
        }
    }
}


