/**
 * Zhuoming Tan
 * Created by colin on 12/2/14.
 */
public class BaseballEliminationTester {
    public static void main(String[] args) {
        BaseballElimination be = new BaseballElimination("test/teams4.txt");
        StdOut.print(be.numberOfTeams());
    }
}