import java.util.ArrayList;

public class Node implements Comparable<Node> {

    int player; // 0 if O's turn has been played, 1 otherwise;
    Node parent;
    int[][] gameState; 
    int move;
    ArrayList<Node> children;
    double numVisits, UCTValue, wins, draws, losses = 0;
    int winner = TTT.GAME_CONTINUES;

    Node(int pl, Node p, int[][] s, int m) {
        player = pl;
        parent = p;
        gameState = new int[s.length][]; 
        for (int i = 0; i < s.length; i++) {
            gameState[i] = s[i].clone();
        }
        move = m;
        children = new ArrayList<>();
    }

    @Override
    public int compareTo(Node other) {
        return Double.compare(other.UCTValue, UCTValue);
    }

    void UCTValue() {
        if (numVisits == 0) UCTValue = Double.MAX_VALUE;
        else UCTValue = ((wins + draws / 2) / numVisits) + Math.sqrt(2) * Math.sqrt(Math.log(parent.numVisits) / numVisits);
    }
}

