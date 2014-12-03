/**
 * Zhuoming Tan
 * Created by colin on 12/2/14.
 */
public class BaseballEliminationTester {
    public static void main(String[] args) {
        BaseballElimination be = new BaseballElimination("test/teams4.txt");
        StdOut.println(be.numberOfTeams());
        be.isEliminated("Atlanta");
        be.isEliminated("Philadelphia");
        be.isEliminated("New_York");
        be.isEliminated("Montreal");
    }
}
