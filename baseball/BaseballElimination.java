package baseball;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private final HashMap<String, TeamData> data;
    private final ArrayList<String> teamNames;
    private final int n;
    private int maxWins;
    private String maxWinsTeam;

    public BaseballElimination(String filename) {
        if (filename == null) throw new IllegalArgumentException();
        In in = new In(filename);
        data = new HashMap<>();
        teamNames = new ArrayList<>();
        n = in.readInt();
        maxWins = Integer.MIN_VALUE;
        maxWinsTeam = null;
        for (int i = 0; i < n; i++) {
            String team = in.readString();
            teamNames.add(team);
            int wins = in.readInt();
            if (wins > maxWins) {
                maxWins = wins;
                maxWinsTeam = team;
            }
            int losses = in.readInt();
            int remaining = in.readInt();
            ArrayList<Integer> matches = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                matches.add(in.readInt());
            }
            data.put(team, new TeamData(i, wins, losses, remaining, matches));
        }
    }

    public int numberOfTeams() { return n; }

    public Iterable<String> teams() {
        return teamNames;
    }

    public int wins(String team) {
        if (team == null || !data.containsKey(team)) throw new IllegalArgumentException();
        return data.get(team).wins;
    }

    public int losses(String team) {
        if (team == null || !data.containsKey(team)) throw new IllegalArgumentException();
        return data.get(team).losses;
    }

    public int remaining(String team) {
        if (team == null || !data.containsKey(team)) throw new IllegalArgumentException();
        return data.get(team).remaining;
    }

    public int against(String team1, String team2) {
        if (team1 == null || team2 == null || !data.containsKey(team1) || !data.containsKey(team2)) throw new IllegalArgumentException();
        return data.get(team1).matches.get(data.get(team2).index);
    }

    public boolean isEliminated(String team) {
        if (team == null || !data.containsKey(team)) throw new IllegalArgumentException();
        TeamData td = data.get(team);
        if (td.eliminated == -1) checkElimintaion(td);

        return td.eliminated == 1;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (team == null || !data.containsKey(team)) throw new IllegalArgumentException();
        TeamData td = data.get(team);
        if (td.eliminated == -1) checkElimintaion(td);

        return td.eliminated == 0 ? null : data.get(team).certificate;
    }

    private void checkElimintaion(TeamData td) {
        int score = td.wins + td.remaining;
        if (score < maxWins) {
            td.eliminated = 1;
            td.addCertificate(maxWinsTeam);
            return;
        }

        td.eliminated = 0;
        // Build FlowNetwork
        // Number of matches
        int matches = (n - 1)*(n - 2) / 2;
        // Number of network vertices
        int v = 2 + (n - 1) + matches;
        FlowNetwork fn = new FlowNetwork(v);
        int[] idx = new int[n];
        int match = 1;
        double inflow = 0;
        for (TeamData other : data.values()) {
            if (td.index == other.index) continue;
            int i = other.index - ((other.index < td.index) ? 0 : 1);
            idx[i] = other.index;
            for (int j = other.index + 1; j < n; j++) {
                if (j == td.index) continue;
                // Add edge from source to match
                fn.addEdge(new FlowEdge(0, match, other.matches.get(j)));
                inflow += other.matches.get(j);
                // Add edges from match to teams
                fn.addEdge(new FlowEdge(match, 1 + matches + i, Double.POSITIVE_INFINITY));
                fn.addEdge(new FlowEdge(match, 1 + matches + j - ((td.index < j) ? 1 : 0), Double.POSITIVE_INFINITY));
                match++;
            }
            // Add edge to target
            fn.addEdge(new FlowEdge(1 + matches + i, v - 1, score - other.wins));
        }
        // Run FordFulkerson to check for elimination
        FordFulkerson ff = new FordFulkerson(fn, 0, v - 1);
        if (ff.value() < inflow) {
            td.eliminated = 1;
            for (int i = 0; i < n - 1; i++) {
                if (ff.inCut(1 + matches + i)) td.addCertificate(teamNames.get(idx[i]));
            }
        }
    }

    private class TeamData {
        int index, wins, losses, remaining, eliminated;
        ArrayList<Integer> matches;
        ArrayList<String> certificate;

        private TeamData(int index, int wins, int losses, int remaining, ArrayList<Integer> matches) {
            this.index = index;
            this.wins = wins;
            this.losses = losses;
            this.remaining = remaining;
            this.matches = matches;
            this.eliminated = -1;
            this.certificate = new ArrayList<>();
        }

        private void addCertificate(String team) {
            this.certificate.add(team);
        }
    }

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
