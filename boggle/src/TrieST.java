import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

/**
 * This class creates a symbol table for string
 *
 */
public class TrieST {

    private static int R = 26;
    private TrieNode root;
    private TrieNode last_node;
    private String last_word = "";

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
//        validate(s);
        root = add(root, s, 0);
    }

    private TrieNode add(TrieNode rt, String s, int pos) {

        if (rt == null) {rt = new TrieNode();}

        if (pos == s.length()) {rt.isword = true; return rt;}

        int index = index(s.charAt(pos));
        rt.next[index] = add(rt.next[index], s, pos+1);

        return rt;
    }

    public boolean[] contains(String s) { // [0]:isprefix, [1]:isword
//        validate(s);

        if (last_node != null && last_word.equals(s.substring(0, s.length()-1))) {
            last_word = s;
            return contains(last_node, s, s.length()-1);
        }

        last_word = s;
        return contains(root, s, 0);
    }

    private boolean[] contains(TrieNode cur, String s, int pos) {


        if (cur == null) {last_node = null; return new boolean[]{false, false};}

        if (pos == s.length()) {last_node = cur; return new boolean[]{true, cur.isword};}

        int index = index(s.charAt(pos));

        return contains(cur.next[index], s, pos+1);

    }

    public static void main(String[] args) {
        TrieST trie = new TrieST();
//        trie.add("ABC");
        trie.add("ABDC");
//        boolean[] b = trie.contains("ABDC");
//        StdOut.println(b[0] + " " + b[1]);
    }

}
