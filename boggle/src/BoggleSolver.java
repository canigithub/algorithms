import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.*;

/**
 *  possible optimizing strategy:
 *  1. char[] of length 16 instead of String (to avoid string concatenation);
 *  2. convert a char board[][] into an int board[][];
 *  3. implement TrieNode containsPrefix(): return the node if true;
 *  4. implement boolean containsWord(TrieNode , String s);
 *  5. TrieNode could keep more info! for instance: boolean isFound;
 *  6. Use a stack to save the <String, Node> on the path;
 *
 *  Problem: is there a way to avoid copy visited[][] for each dfs?
 */


public class BoggleSolver {


    private TrieST dictionary = new TrieST();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        if (dictionary == null) {
            throw new NullPointerException("dictionary is null.");
        }

        // use a 26-way trie
        for (String s : dictionary) {
            this.dictionary.add(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        if (board == null) {
            throw new NullPointerException("board is null.");
        }

        HashSet<String> valid_words = new HashSet<>();


        for (int r = 0; r < board.rows(); ++r) {
            for (int c = 0; c < board.cols(); ++c) {

                boolean[][] visited = new boolean[board.rows()][board.cols()];
                dfs(board, r, c, "", visited, valid_words);
            }
        }

        return valid_words;
    }


    private void dfs(BoggleBoard board, int r, int c, String path, boolean[][] visited, HashSet<String> set) {

        if (visited[r][c]) {return;}

        visited[r][c] = true;

        if (board.getLetter(r, c) == 'Q') path += "QU";
        else path += board.getLetter(r, c);

        boolean[] isword = dictionary.contains(path);   // [0]: isprefix, [1]:isword

        if (!isword[0]) {return;}

//        if (isword[1]) set.add(path);
//        if (isword[1] && path.length() >= 3) set.add(path);

        for (int k = 0; k < 8; ++k) {   // 8 directions to go

            boolean[][] v = new boolean[board.rows()][board.cols()];
            for (int i = 0; i < board.rows(); ++i) {
                for (int j = 0; j < board.cols(); ++j){
                    v[i][j] = visited[i][j];
                }
            }

            switch (k) {
                case 0:
                    if (r > 0) dfs(board, r-1, c, path, v, set);
                    break;
                case 1:
                    if (r > 0 && c < board.cols()-1) dfs(board, r-1, c+1, path, v, set);
                    break;
                case 2:
                    if (c < board.cols()-1) dfs(board, r, c+1, path, v, set);
                    break;
                case 3:
                    if (r < board.rows()-1 && c < board.cols()-1) dfs(board, r+1, c+1, path, v, set);
                    break;
                case 4:
                    if (r < board.rows()-1) dfs(board, r+1, c, path, v, set);
                    break;
                case 5:
                    if (r < board.rows()-1 && c > 0) dfs(board, r+1, c-1, path, v, set);
                    break;
                case 6:
                    if (c > 0) dfs(board, r, c-1, path, v, set);
                    break;
                case 7:
                    if (r > 0 && c > 0) dfs(board, r-1, c-1, path, v, set);
                    break;
                default:
                    throw new RuntimeException("9th direction to go?");
            }
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {

        if (word == null) {
            throw new NullPointerException("null word.");
        }

        if (!dictionary.contains(word)[1]) {
            return 0;
        }

        switch (word.length()) {
            case 0:case 1:case 2:
                return 0;
            case 3:case 4:
                return 1;
            case 5:
                return 2;
            case 6:
                return 3;
            case 7:
                return 5;
            default:
                return 11;
        }
    }

    public static void main(String[] args) {

        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);

        while (true) {
            long start = System.currentTimeMillis();
            int score = 0;
            for (String word : solver.getAllValidWords(board)) {
//            StdOut.println(word);
                score += solver.scoreOf(word);
            }
            long end = System.currentTimeMillis();
//        StdOut.println("Score = " + score);
//        StdOut.println("Time = " + (end-start));
        }

//        while (true) {
////            solver.getAllValidWords(board);
//            boolean[][] visited = new boolean[4][4];
//            HashSet<String> set = new HashSet<>();
//            solver.dfs(board, 0, 0, "", visited, set);
//        }

    }
}
