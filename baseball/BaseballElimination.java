import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.In;

import java.util.HashMap;
import java.util.Set;

public class BaseballElimination {
    // create a baseball division from given filename
    private int n;
    private int[] w, l, r;
    private int[][] g;
    private HashMap<String, Integer> teamToId;
    private FlowNetwork fn;
    private int sId, tId;

    public BaseballElimination(String filename) {
        In fIn = new In(filename);
        n = Integer.parseInt(fIn.readLine());
        w = new int[n];
        l = new int[n];
        r = new int[n];
        g = new int[n][n];

        teamToId = new HashMap<String, Integer>();
        int id = 0;
        while (fIn.hasNextLine()) {
            String line = fIn.readLine();
            String[] tokens = line.trim().split("\\s+");
            teamToId.put(tokens[0], id);
            w[id] = Integer.parseInt(tokens[1]);
            l[id] = Integer.parseInt(tokens[2]);
            r[id] = Integer.parseInt(tokens[3]);
            for (int i = 0; i < n; i++) {
                g[id][i] = Integer.parseInt(tokens[4 + i]);
            }
            id++;
        }
    }

    private void constructFlowNetwork(String teamToCheck) {
        // Construct full network while having the teamToCheck floats with no edge connected
        // this simplifies the number system
        int ng = (n) * (n - 1) / 2; // game vertices
        int nt = n;
        sId = nt + ng;
        tId = nt + ng + 1;
        fn = new FlowNetwork(nt + ng + 2); // include s, t
        FlowEdge fe;
        int idExcl = teamToId.get(teamToCheck);
        int vId = nt;
        // source to game-left edge
        for (int i = 0; i < n - 1; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i != j && i != idExcl && j != idExcl) {
                    fn.addEdge(new FlowEdge(sId, vId, g[i][j]));
                    // game-left edge to competing teams
                    fn.addEdge(new FlowEdge(vId, i, Double.POSITIVE_INFINITY));
                    fn.addEdge(new FlowEdge(vId, j, Double.POSITIVE_INFINITY));
                }
                vId++;
            }
        }
        // team to sink t
        for (int i = 0; i < nt; i++) {
            if (i != idExcl) {
                fn.addEdge(new FlowEdge(i, tId, wins(teamToCheck) + remaining(teamToCheck) - w[i]));
            }
        }
        // System.out.println(fn.toString());
    }

    // number of teams
    public int numberOfTeams() {
        return n;
    }

    // all teams
    public Iterable<String> teams() {
        Set<String> keys = teamToId.keySet();
        return keys;
    }

    // number of wins for given team
    public int wins(String team) {
        return w[teamToId.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return l[teamToId.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return r[teamToId.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return g[teamToId.get(team1)][teamToId.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        constructFlowNetwork(team);

        return true;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {

        return null;
    }

    // unit test
    public static void main(String[] args) {
        String filename = args[0];
        BaseballElimination be = new BaseballElimination(filename);
        be.isEliminated("Boston");


    }
}
