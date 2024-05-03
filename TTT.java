import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class TTT {

    static final int EMPTY = -1;
    static final int O = 0;
    static final int X = 1;

    static final int DRAW = 2;
    static final int GAME_CONTINUES = -2;

    ArrayList<ArrayList<Integer>> winningMoves;
    Random rand;

    TTT() {
        rand = new Random();
        setWinningMoves();
    }

    private void setWinningMoves() {
        winningMoves = new ArrayList<>(Arrays.asList(
                new ArrayList<>(Arrays.asList(0, 1, 2)),
                new ArrayList<>(Arrays.asList(3, 4, 5)),
                new ArrayList<>(Arrays.asList(6, 7, 8)),
                new ArrayList<>(Arrays.asList(0, 3, 6)),
                new ArrayList<>(Arrays.asList(1, 4, 7)),
                new ArrayList<>(Arrays.asList(2, 5, 8)),
                new ArrayList<>(Arrays.asList(0, 4, 8)),
                new ArrayList<>(Arrays.asList(2, 4, 6))
        ));
    }

    int fullSimulation(Node n) {
        if (n.winner != GAME_CONTINUES) return n.winner;
        int player = n.player ^ 1;
        int[][] currentGameState = copyBoard(n.gameState);

        while (true) {
            ArrayList<Integer> moves = getAllMoves(currentGameState);
            int randomMoveIndex = rand.nextInt(moves.size());
            int moveToMake = moves.get(randomMoveIndex);
            currentGameState[moveToMake / 3][moveToMake % 3] = player;
            int won = checkWinOrDraw(currentGameState, player);
            if (won != GAME_CONTINUES) return won;
            player ^= 1;
        }
    }

    ArrayList<Integer> getAllMoves(int[][] gameState) {
        ArrayList<Integer> allPossibleMoves = new ArrayList<>();
        for (int row = 0; row < gameState.length; row++) {
            for (int col = 0; col < gameState[row].length; col++) {
                if (gameState[row][col] == EMPTY) {
                    allPossibleMoves.add(row * 3 + col);
                }
            }
        }
        return allPossibleMoves;
    }

    int checkWinOrDraw(int[][] gameState, int player) {
        for (ArrayList<Integer> w : winningMoves) {
            int n = 0;
            for (Integer index : w) {
                int row = index / 3;
                int col = index % 3;
                if (gameState[row][col] != player) {
                    break;
                }
                n++;
            }
            if (n == 3) return player;
        }

        ArrayList<Integer> moves = getAllMoves(gameState);
        if (moves.isEmpty()) return DRAW;

        return GAME_CONTINUES;
    }

    /**
     * Draws a TTT board, given a @param state stored in a 2d 3x3 array, with 0 being unfilled, 1 being X, and 2 being O
     * @author reddsaut
     */
    void drawBoard(int[][] state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                System.out.print(state[i][j] == 0 ? "O"
                                :state[i][j] == 1 ? "X"
                                :" ");
                if(j < state[0].length - 1) {
                    System.out.print(" | ");
                }
            }
            if(i < state.length - 1) {
                System.out.println("\n---------");
            }
        }
        System.out.println();
    }

    public int[][] copyBoard(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }
    
}
