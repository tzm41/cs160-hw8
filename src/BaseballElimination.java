import java.util.HashMap;
import java.util.HashSet;

/**
 * Name: Zhuoming Tan
 * Login: ztan
 * <p/>
 * Created by colin on 11/27/14.
 */
public class BaseballElimination {

    private int teamNum;
    private HashMap<String, Integer> name_ID = new HashMap<String, Integer>();
    private HashMap<Integer, teamData> ID_Data = new HashMap<Integer, teamData>();

    private class teamData {
        private String name;
        private int wins, losses, remains;
        private int[] games;

        public teamData(String name, int wins, int losses, int remains, int[] games) {
            this.name = name;
            this.wins = wins;
            this.losses = losses;
            this.remains = remains;
            this.games = games;
        }
    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        // parse file
        In in = new In(filename);
        teamNum = Integer.parseInt(in.readLine());
        name_ID = new HashMap<String, Integer>(teamNum);
        ID_Data = new HashMap<Integer, teamData>(teamNum);

        int i = 0;
        while (!in.isEmpty()) {
            String[] tokens = in.readLine().split("\\s+");
            name_ID.put(tokens[0], i);
            int[] games = new int[teamNum];
            for (int j = 0; j < teamNum; j++)
                games[j] = Integer.parseInt(tokens[4 + j]);
            ID_Data.put(i, new teamData(tokens[0], Integer.parseInt(tokens[1]),
                    Integer.parseInt(tokens[2]), Integer.parseInt(tokens[3]), games));
            i++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return teamNum;
    }

    // all teams
    public Iterable<String> teams() {
        return name_ID.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!name_ID.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return ID_Data.get(name_ID.get(team)).wins;
    }

    // number of losses for given team
    public int losses(String team) {
        if (!name_ID.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return ID_Data.get(name_ID.get(team)).losses;
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!name_ID.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return ID_Data.get(name_ID.get(team)).remains;
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!name_ID.containsKey(team1) || !name_ID.containsKey(team2))
            throw new java.lang.IllegalArgumentException();
        return ID_Data.get(name_ID.get(team1)).games[name_ID.get(team2)];
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (!name_ID.containsKey(team))
            throw new java.lang.IllegalArgumentException();

        // trivial elimination
        boolean trivial = trivialElimination(team);
        if (trivial) return true;

        // nontrivial elimination
        nontrivialElimination(team);
        return false;
    }

    // trivial elimination criteria
    private boolean trivialElimination(String team) {
        int ID = name_ID.get(team);
        // current wins plus remaining games, aka the biggest possible wins
        int currentPower = ID_Data.get(ID).wins + ID_Data.get(ID).remains;
        for (int i = 0; i < teamNum; i++)
            if (currentPower < ID_Data.get(i).wins)
                return true;
        return false;
    }

    private void nontrivialElimination(String team) {
        // construct flow network
        int ID = name_ID.get(team);
        // the flow network excludes the current querying team
//        int teamNum = this.teamNum - 1;
        // node number = s + t + team number + game number
        int nodeNum = 2 + teamNum + teamNum * (teamNum - 1) / 2;
        // edge number = edges between team vertices and t
        // + edges between game vertices and s
        // + 2 for each game vertices as they are connected to two teams
        // int edgeNum = teamNum + 3 * teamNum * (teamNum - 1) / 2;
        FlowNetwork fn = new FlowNetwork(nodeNum);
        // flow vertices numbered by:
        // team vertices = team IDs
        int s = nodeNum - 2, t = nodeNum - 1;
        // game vertices: fill up the numbers between
        int gameNum = teamNum;
        for (int i = 0; i < teamNum - 1; i++) {
            if (i != ID) {
                for (int j = i + 1; j < teamNum; j++) {
                    if (j != ID) {
                        // from s to game vertices
                        fn.addEdge(new FlowEdge(s, gameNum, (double) ID_Data.get(i).games[j]));
                        // from game vertices to team vertices
                        fn.addEdge(new FlowEdge(gameNum, i, Double.POSITIVE_INFINITY));
                        fn.addEdge(new FlowEdge(gameNum, j, Double.POSITIVE_INFINITY));
                        gameNum++;
                    }
                }
            }
        }
        for (int i = 0; i < teamNum; i++) {
            // from team vertices to t
            if (i != ID)
                 fn.addEdge(new FlowEdge(i, t, (double) (ID_Data.get(ID).wins + ID_Data.get(ID).remains - ID_Data.get(i).wins)));
        }

        // compute using Ford Fulkerson
        FordFulkerson ff = new FordFulkerson(fn, s, t);
        StdOut.println(ff.value());
        StdOut.println(ff.inCut(ID));
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        if (!name_ID.containsKey(team))
            throw new java.lang.IllegalArgumentException();
        return new HashSet<String>();
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            } else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
