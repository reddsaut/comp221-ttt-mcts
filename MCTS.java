import java.util.ArrayList;
import java.util.Collections;

public class MCTS {

    TTTSimulator simulator;
    Node rootNode;
    Node bestMove;

    MCTS() {
        simulator = new TTTSimulator();
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
        System.out.println("invalid move");
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
        MCTS mcts = new MCTS();
        int numberOfIterations = 1000;
        int[][] initGameState = new int[3][3];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                initGameState[i][j] = TTTSimulator.EMPTY;
            }
        }
        int iter = 0;
        while (true) {
            if (iter == 0) {
                mcts.rootNode = new Node(TTTSimulator.O, null, initGameState, -1);
            } else {
                if (mcts.bestMove.winner == TTTSimulator.O || mcts.bestMove.winner == TTTSimulator.X) {
                    System.out.println("increase the number of iterations");
                    break;
                }
                mcts.rootNode = new Node(mcts.bestMove.player, null, mcts.bestMove.gameState, mcts.bestMove.move);
            }
            if (!mcts.simulator.getAllMoves(mcts.rootNode.gameState).isEmpty()) mcts.simulation(numberOfIterations);
            else break;
            iter++;
        }
    }
}