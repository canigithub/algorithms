import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class BaseballElimination {


    private      int num_team;
    private String[] team_name;
    private    int[] num_win;
    private    int[] num_lose;
    private    int[] num_rmgm;    // num of remain games
    private  int[][] sche_mat;
    private HashMap<String, Integer> team_id;
    private boolean[] checked;
    private boolean[] elimed;
    private List<List<String>> certification;

    public BaseballElimination(String filename) {                  // create a baseball division from

        if (filename == null) {
            throw new NullPointerException("filename is null");
        }
                                                                   // given filename in format specified below
        In in = new In(filename);

        num_team  = Integer.parseInt(in.readLine());
        team_name = new String[num_team];
        num_win   = new    int[num_team];
        num_lose  = new    int[num_team];
        num_rmgm  = new    int[num_team];
        sche_mat  = new    int[num_team][num_team];
        team_id   = new HashMap<>();
        checked   = new boolean[num_team];
        elimed    = new boolean[num_team];
        certification = new ArrayList<>(num_team);
        for (int i = 0; i < num_team; ++i) {
            certification.add(null);
        }

        for (int i = 0; i < num_team; ++i) {

            String[] line = in.readLine().trim().split(" +");
            team_name[i] = line[0];
            num_win[i]   = Integer.parseInt(line[1]);
            num_lose[i]  = Integer.parseInt(line[2]);
            num_rmgm[i]  = Integer.parseInt(line[3]);
            sche_mat[i] = new int[line.length-4];
            for (int j = 0; j < line.length-4; ++j) {
                sche_mat[i][j] = Integer.parseInt(line[j+4]);
            }
            team_id.put(team_name[i], i);
        }
    }

    public              int numberOfTeams() {                      // number of teams
        return num_team;
    }

    public Iterable<String> teams() {                              // all teams

        List<String> list = new LinkedList<>();

        for (int i = 0; i < team_name.length; ++i) {
            list.add(team_name[i]);
        }

        return list;
    }

    private            void checkTeamName(String team) {

        if (team == null) {
            throw new NullPointerException("team name is null");
        }

        if (!team_id.containsKey(team)) {
            throw new IllegalArgumentException("team is not contained");
        }
    }

    public              int wins(String team) {                    // number of wins for given team
        checkTeamName(team);
        return num_win[team_id.get(team)];
    }

    public              int losses(String team) {                  // number of losses for given team
        checkTeamName(team);
        return num_lose[team_id.get(team)];
    }

    public              int remaining(String team) {               // number of remaining games for given team
        checkTeamName(team);
        return num_rmgm[team_id.get(team)];
    }

    public              int against(String team1, String team2) {  // number of remaining games between team1 and team2
        checkTeamName(team1);
        checkTeamName(team2);
        return sche_mat[team_id.get(team1)][team_id.get(team2)];
    }

    public          boolean isEliminated(String team) {            // is given team eliminated?

        checkTeamName(team);
        int id = team_id.get(team);

        if (!checked[id]) {

            trivialEliminate(id);

            if (!checked[id]) {
                maxflowEliminate(id);
            }
        }

        return elimed[id];
    }

    public Iterable<String> certificateOfElimination(String team) { // subset R of teams that eliminates given team; null if not eliminated

        checkTeamName(team);
        int id = team_id.get(team);

        if (!checked[id]) {

            trivialEliminate(id);

            if (!checked[id]) {
                maxflowEliminate(id);
            }
        }

        return certification.get(id);
    }

    private           void trivialEliminate(int id) {

        if (num_team == 1) {
            checked[id] = true;
            elimed[id] = false;
            certification.set(id, null);
            return;
        }

        int max_id = -1, max_win = Integer.MIN_VALUE;

        for (int i = 0; i < num_team; ++i) {

            if (i != id && num_win[i] > max_win) {
                max_id = i;
                max_win = num_win[i];
            }
        }

        if (num_win[id] + num_rmgm[id] < max_win) {
            checked[id] = true;
            elimed[id] = true;
            List<String> list = new LinkedList<>();
            list.add(team_name[max_id]);
            certification.set(id, list);
        }
    }

    private            void maxflowEliminate(int id) {

        FlowNetwork fn = new FlowNetwork(num_team*(num_team-1)/2 + 2);
        // 0 ~ (n-1)*(n-2)/2 - 1 : game vertex
        // (n-1)*(n-2)/2 ~ n*(n-1)/2 - 1 : team vertex
        // V()-2 : s, V()-1 : t

        // could save this memory by write a private function
        int[] t = new int[num_team-1];
        for (int i = 0; i < t.length; ++i) {
            if (i < id) t[i] = i;
            else t[i] = i+1;
        }

        int i, j, k, offset = (num_team-1)*(num_team-2)/2;

        k = 0;
        for (i = 0; i < t.length; ++i) {
            for (j = i+1; j < t.length; ++j) {

                fn.addEdge(new FlowEdge(fn.V()-2, k, (double) sche_mat[t[i]][t[j]]));
                fn.addEdge(new FlowEdge(k, offset+i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(k, offset+j, Double.POSITIVE_INFINITY));
                ++k;
            }

            fn.addEdge(new FlowEdge(offset+i, fn.V()-1, (double) num_win[id]+num_rmgm[id]-num_win[t[i]]));
        }

        FordFulkerson ff = new FordFulkerson(fn, fn.V()-2, fn.V()-1);

        List<String> list = new ArrayList<>();

        for (i = offset; i < fn.V()-2; ++i) {
            if (ff.inCut(i)) {
                checked[id] = true;
                elimed[id] = true;
                list.add(team_name[t[i-offset]]);
            }
        }


        if (checked[id]) {
            certification.set(id, list);
            return;
        }

        checked[id] = true;
        elimed[id] = false;
        certification.set(id, null);
    }

    //remember to comment main
//    public static void main(String[] args) {
//
//        BaseballElimination division = new BaseballElimination(args[0]);
//
////        for (String team : division.teams()) {
////            if (division.isEliminated(team)) {
////                StdOut.print(team + " is eliminated by the subset R = { ");
////                for (String t : division.certificateOfElimination(team)) {
////                    StdOut.print(t + " ");
////                }
////                StdOut.println("}");
////            }
////            else {
////                StdOut.println(team + " is not eliminated");
////            }
////        }
//
////        StdOut.println(division.certificateOfElimination("Serbia"));
////        StdOut.println(division.certificateOfElimination("Detroit"));
////        StdOut.println(division.certificateOfElimination("Philadelphia"));
//        StdOut.println(division.certificateOfElimination("Team25"));
//    }

}
