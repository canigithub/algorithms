import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

/**
 * This class creates a symbol table for string
 *
 */
public class TrieST {

    private static int R = 26;
    private TrieNode root;
//    private static int K = 4;
//    private HashMap<String, TrieNode> history = new HashMap<>(); // store string len == K

    private static class TrieNode {
        boolean isword;
        TrieNode[] next = new TrieNode[R];
    }

    private void validate(String s) {

        if (s == null) {
            throw new NullPointerException("input word is null.");
        }

        if (s.equals("")) {
            throw new IllegalArgumentException("input word is empty.");
        }

        for (char c : s.toCharArray()) {
            if (c < 'A' || c > 'Z') {
                throw new IllegalArgumentException("input word has invalid char.");
            }
        }
    }

    private int index(char c) {
        return c - 'A';
    }

    public void add(String s) {
        validate(s);
        root = add(root, s, 0);
    }

    private TrieNode add(TrieNode rt, String s, int pos) {

        if (rt == null) {rt = new TrieNode();}

//        if (pos == K) {
//            history.put(s.substring(0,K), rt);
//        }

        if (pos == s.length()) {rt.isword = true; return rt;}

        int index = index(s.charAt(pos));
        rt.next[index] = add(rt.next[index], s, pos+1);


        return rt;
    }

    public boolean[] contains(String s) { // [0]:isprefix, [1]:isword
        validate(s);

//        if (s.length() >= K) {
//            String prefix_K = s.substring(0,K);
//            if (history.containsKey(prefix_K)) {
//                return contains(history.get(prefix_K), s, K);
//            }
//        }

        return contains(root, s, 0);
    }

    private boolean[] contains(TrieNode rt, String s, int pos) {

        if (rt == null) {return new boolean[]{false, false};}

        if (pos == s.length()) {return new boolean[]{true, rt.isword};}

        int index = index(s.charAt(pos));

        boolean[] b = contains(rt.next[index], s, pos+1);

        return b;
    }

//    public static void main(String[] args) {
//        TrieST trie = new TrieST();
////        trie.add("ABC");
//        trie.add("ABDC");
////        boolean[] b = trie.contains("ABDC");
////        StdOut.println(b[0] + " " + b[1]);
//    }

}
