import edu.princeton.cs.algs4.*;

import java.util.LinkedList;
import java.util.List;

public class SAP {


    private Digraph DG;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {

        if (G == null) {
            throw new NullPointerException("null graph");
        }

        DG = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        List<Integer> vv = new LinkedList<>(); vv.add(v);
        List<Integer> ww = new LinkedList<>(); ww.add(w);
        return lengthAndAncestor(vv, ww)[0];
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        List<Integer> vv = new LinkedList<>(); vv.add(v);
        List<Integer> ww = new LinkedList<>(); ww.add(w);
        return lengthAndAncestor(vv, ww)[1];
    }


    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null) {
            throw new NullPointerException("null iterable");
        }

        return lengthAndAncestor(v, w)[0];
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {

        if (v == null || w == null) {
            throw new NullPointerException("null iterable");
        }

        return lengthAndAncestor(v, w)[1];
    }

    private int[] lengthAndAncestor(Iterable<Integer> v, Iterable<Integer> w) {

        BreadthFirstDirectedPaths bfsp_v = new BreadthFirstDirectedPaths(DG, v);
        BreadthFirstDirectedPaths bfsp_w = new BreadthFirstDirectedPaths(DG, w);

        int min_dist = Integer.MAX_VALUE;
        int ret = -1;

        for (int i = 0; i < DG.V(); ++i) {

            if (bfsp_v.hasPathTo(i) && bfsp_w.hasPathTo(i)) {

                int dist = bfsp_v.distTo(i) + bfsp_w.distTo(i);

                if (dist < min_dist) {
                    min_dist = dist;
                    ret = i;
                }
            }
        }

        min_dist = (min_dist == Integer.MAX_VALUE) ? -1 : min_dist;

        return new int[] {min_dist, ret};   // return: length, ancestor
    }


    // do unit testing of this class
    public static void main(String[] args) {

        In file = new In(args[0]);
        SAP sap = new SAP(new Digraph(file));
        sap.DG.addEdge(3,0);
        int m = 0;
        int n = 3;
        StdOut.println(sap.length(m, n));
        StdOut.println(sap.ancestor(m, n));
    }


}
