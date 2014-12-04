import java.util.HashMap;

/**
 * Created by colin on 12/4/14.
 */
public class ReadGames {
    public static void main(String args[]) {
        HashMap<String, Integer> teams = new HashMap<String, Integer>();
        In inRanks = new In("rank.txt");
        while (!inRanks.isEmpty()) {
            String[] tokens = inRanks.readLine().split("\\s+");
            teams.put(tokens[1], Integer.parseInt(tokens[0]) - 1);
        }
        for (String name : teams.keySet()) {
            In inGames = new In("games.txt");
            HashMap<Integer, Integer> matches = new HashMap<Integer, Integer>();
            for (int i = 0; i < 20; i++)
                matches.put(i, 0);
            while (!inGames.isEmpty()) {
                String[] tokens = inGames.readLine().split("\\svs\\s");
//            for (String name : teams.keySet()) {
                if (tokens[0].equals(name)) {
                    int match = matches.get(teams.get(tokens[1]));
                    match++;
                    matches.put(teams.get(tokens[1]), match);
                }
                if (tokens[1].equals(name)) {
                    int match = matches.get(teams.get(tokens[0]));
                    match++;
                    matches.put(teams.get(tokens[0]), match);
                }
            }
            StdOut.print(name + ": ");
            for (int i = 0; i < 20; i++)
                StdOut.print(matches.get(i) + " ");
            StdOut.println();
        }
    }
}
