/**
 * Zhuoming Tan
 * Created by colin on 12/2/14.
 */
public class BaseballEliminationTester {
    public static void main(String[] args) {
//        BaseballElimination be = new BaseballElimination("test/teams5.txt");
//        StdOut.println("Number of teams: " + be.numberOfTeams());
//        for(String team: be.teams()){
//            StdOut.println("Team: " + team);
//            StdOut.println("Is eliminated: " + be.isEliminated(team));
//            StdOut.println("Eliminators: " + be.certificateOfElimination(team));
//        }
        BaseballElimination division = new BaseballElimination("teams.txt");
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
