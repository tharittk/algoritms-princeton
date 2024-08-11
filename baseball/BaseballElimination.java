import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class BaseballElimination {
    // create a baseball division from given filename
    private static final int WIN_COL = 1;
    private static final int LOSE_COL = 2;
    private static final int REMAIN_COL = 3;
    private static final int GAME_COL = 4;

    private final int n;
    private final int[] w, lose, r;
    private final int[][] g;
    private final HashMap<String, Integer> teamToId;
    private FlowNetwork fn;
    private int sId, tId;
    private ArrayList<String> subset;

    public BaseballElimination(String filename) {
        In fIn = new In(filename);
        n = Integer.parseInt(fIn.readLine());
        w = new int[n];
        lose = new int[n];
        r = new int[n];
        g = new int[n][n];

        teamToId = new HashMap<String, Integer>();
        int id = 0;
        while (fIn.hasNextLine()) {
            String line = fIn.readLine();
            String[] tokens = line.trim().split("\\s+");
            teamToId.put(tokens[0], id);
            w[id] = Integer.parseInt(tokens[WIN_COL]);
            lose[id] = Integer.parseInt(tokens[LOSE_COL]);
            r[id] = Integer.parseInt(tokens[REMAIN_COL]);
            for (int i = 0; i < n; i++) {
                g[id][i] = Integer.parseInt(tokens[GAME_COL + i]);
            }
            id++;
        }
    }

    private FlowNetwork constructFlowNetwork(String teamToCheck) {
        // Construct full network while having the teamToCheck floats with no edge connected
        // this simplifies the number system
        int ng = (n) * (n - 1) / 2; // game vertices
        int nt = n;
        sId = nt + ng;
        tId = nt + ng + 1;
        fn = new FlowNetwork(nt + ng + 2); // include s, t
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
        return fn;
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
        if (!teamToId.containsKey(team)) throw new IllegalArgumentException("invalid team");
        return w[teamToId.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!teamToId.containsKey(team)) throw new IllegalArgumentException("invalid team");
        return lose[teamToId.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!teamToId.containsKey(team)) throw new IllegalArgumentException("invalid team");
        return r[teamToId.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!teamToId.containsKey(team1) || !teamToId.containsKey(team2))
            throw new IllegalArgumentException("invalid team");
        return g[teamToId.get(team1)][teamToId.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!teamToId.containsKey(team)) throw new IllegalArgumentException("invalid team");
        this.subset = new ArrayList<String>();
        // trivial elimination
        for (String other : teams()) {
            if ((wins(team) + remaining(team)) < wins(other)) {
                this.subset.add(other);
                return true;
            }
        }
        this.fn = constructFlowNetwork(team);
        FordFulkerson ff = new FordFulkerson(fn, sId, tId);
        // team can win if all edge leaving s is full
        int capacity = 0;
        for (FlowEdge e : fn.adj(sId)) {
            capacity += e.capacity();
        }
        if (ff.value() != capacity) {
            for (String other : teams()) {
                if (ff.inCut(teamToId.get(other))) this.subset.add(other);
            }
            return true;
        }
        else return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!teamToId.containsKey(team)) throw new IllegalArgumentException("invalid team");
        if (isEliminated(team)) return this.subset;
        else return null;
    }

    // unit test
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
