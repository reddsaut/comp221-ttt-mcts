// trying to add in player gets to move
// currently not working 

import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class newMCTS {

    TTTSimulator simulator;
    Node rootNode;
    Node bestMove;
    Scanner scanner;

    newMCTS() {
        simulator = new TTTSimulator();
        scanner = new Scanner(System.in);
    }

    Node selection() {
        Node currentNode = rootNode;

        while (true) {
            if (currentNode.winner != TTTSimulator.GAME_CONTINUES) return currentNode;
            if (currentNode.children.isEmpty()) {
                expansion(currentNode);
                return currentNode.children.get(0);
            } else {
                for (Node child : currentNode.children) {
                    child.UCTValue();
                }
                Collections.sort(currentNode.children);
                currentNode = currentNode.children.get(0);
                if (currentNode.numVisits == 0) {
                    return currentNode;
                }
            }
        }
    }

    void expansion(Node n) {
        ArrayList<Integer> moves = simulator.getAllMoves(n.gameState);
        for (Integer i : moves) {
            int[][] nextGameState = deepCopy(n.gameState);
            int row = i / 3;
            int col = i % 3;
            nextGameState[row][col] = n.player ^ 1;
            Node child = new Node(n.player ^ 1, n, nextGameState, i);
            child.winner = simulator.checkWinOrDraw(child.gameState, child.player);
            n.children.add(child);
        }
    }

    void backpropagate(Node n, int won) {
        Node current = n;
        while (current != null) {
            current.numVisits++;
            if (won == TTTSimulator.DRAW) current.draws += 1;
            else if (current.player == won) {
                current.wins += 1;
            } else current.losses += 1;
            current = current.parent;
        }
    }

    void simulation(int numIterations) {
        for (int i = 0; i < numIterations; i++) {
            Node leafToRollOutFrom = selection();
            int won = simulator.fullSimulation(leafToRollOutFrom);
            backpropagate(leafToRollOutFrom, won);
        }

        double numVisits = 0;
        for (Node child : rootNode.children) {
            if (child.numVisits > numVisits) {
                bestMove = child;
                numVisits = child.numVisits;
            }
        }
        System.out.println();
        simulator.drawBoard(bestMove.gameState);
        System.out.println();
    }

    void playerMove(int row, int col) {
        int index = row * 3 + col;
        for (Node child : rootNode.children) {
            if (child.move == index) {
                rootNode = child;
                return;
            }
        }
        System.out.println("Invalid move.");
    }

    private int[][] deepCopy(int[][] original) {
        if (original == null) return null;
        int[][] copy = new int[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = original[i].clone();
        }
        return copy;
    }

    public static void main(String[] args) {
        newMCTS mcts = new newMCTS();
        TTTSimulator ttt = new TTTSimulator();
        int[][] board = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = TTTSimulator.EMPTY;
            }
        }

        int iter = 0;
        while (true) {
            if (iter == 0) {
                mcts.rootNode = new Node(TTTSimulator.O, null, board, -1);
            }             
            ttt.drawBoard(board);
            System.out.println("Type your move (row,col), e.g., 1,2, or type 'stop' to end:");
            String inp = mcts.scanner.nextLine();
            if (inp.equals("stop")) {
                break;
            }
            int i = 3, j = 3;
            try {
                i = Integer.parseInt(inp.substring(0, 1));
                j = Integer.parseInt(inp.substring(2));
            } catch (Exception e) {
                System.out.println("Error! Please enter a valid move.");
                continue;
            }
            if (i < 0 || i > 2 || j < 0 || j > 2) {
                System.out.println("Error! Please enter a valid move.");
                continue;
            }
            if (board[i][j] == TTTSimulator.EMPTY) {
                board[i][j] = TTTSimulator.X;
                mcts.playerMove(i, j);
                mcts.simulation(1000); // Simulate opponent move
            } else {
                System.out.println("Error! That position is already occupied. Please choose another position.");
            }
            iter++;
        }
        mcts.scanner.close();
        ttt.drawBoard(board);
        System.out.println("Game ended.");
    }
}