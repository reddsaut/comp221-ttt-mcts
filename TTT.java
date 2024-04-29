import java.util.Scanner;
/**
 * Runs a game of Tic Tac Toe
 * @author reddsaut
 */
public class TTT {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        TTT ttt = new TTT();
        int[][] board = new int[3][3];

        while(true) {
            ttt.drawBoard(board);
            int[] r = ttt.collectInput(scanner);
            if(r == null) {
                break;
            }
            int i = r[0], j = r[1];
            if(board[i][j] == 0) {
                board[i][j] = 1;
            }
        }
        ttt.drawBoard(board);
        System.out.println("end");
        scanner.close();
    }

    /**
     * Draws a TTT board, given a @param state stored in a 2d 3x3 array, with 0 being unfilled, 1 being X, and 2 being O
     * @author reddsaut
     */
    private void drawBoard(int[][]state) {
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[0].length; j++) {
                System.out.print(state[i][j] == 0 ? " "
                                :state[i][j] == 1 ? "X"
                                :"O");
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

    private int checkWin(int[][] state) {
        if(state[1][1] != 0 && (state[0][0] == state[1][1] && state[1][1] == state[2][2])||
                                (state[0][2] == state[2][0] && state[1][1] == state[2][0])) {
            return state[1][1];
        }
        for (int i = 0; i < state.length; i++) {
            if(state[i][0] != 0 && state[i][0] == state[i][1] && state[i][1] == state[i][2]) {
                return state[i][0];
            }
            if(state[0][i] != 0 && state[0][i] == state[1][i] && state[1][i] == state[2][i]) {
                return state[0][i];
            }
        }
        return 0;
    }

    private int[] collectInput(Scanner scanner) {
        System.out.println("type your move, ex: 1,2, or stop to stop");
        String inp = scanner.nextLine();
        
        if(inp.equals("stop")) {
            return null;
        }
        int i = 3, j = 3;
        try {
            i = Integer.parseInt(inp.substring(0, 1));
            j = Integer.parseInt(inp.substring(2));
        } catch (Exception e) {
            System.out.println("error!");
            collectInput(scanner);
        }
        if(i > 2 || j > 2) {
            System.out.println("error!");
            collectInput(scanner);
        }
        int[] r = {i,j};
        return r;
    }
}