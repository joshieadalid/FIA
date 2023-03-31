package pozole;

import java.util.ArrayList;

public class State {
    int[][] board;
    int[] size;
    int posI;
    int posJ;
    char movement = 'n';

    public State(int[] boardArray, int[] boardSize) {
        int n = 0;
        this.size = boardSize;
        this.board = new int[boardSize[0]][boardSize[1]];
        for (int i = 0; i < boardSize[0]; i++)
            for (int j = 0; j < boardSize[1]; j++) {
                board[i][j] = boardArray[n];
                if (board[i][j] == 0) {
                    posI = i;
                    posJ = j;
                }
                n++;
            }
    }

    public State(int[][] board, char movement, int[] boardSize) {
        this.movement = movement;
        size = boardSize;
        this.board = new int[size[0]][size[1]];

        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                this.board[i][j] = board[i][j];
                if (board[i][j] == 0) {
                    posI = i;
                    posJ = j;
                }
            }
        }
    }

    public static void printBoard(int[][] board) {
        for (int[] ints : board) {
            System.out.print("+");
            for (int j = 0; j < board[0].length; j++) {
                System.out.print("---+");
            }
            System.out.println();
            System.out.print("|");
            for (int j = 0; j < board[0].length; j++) {
                System.out.printf(" %2d |", ints[j]);
            }
            System.out.println();
        }
        System.out.print("+");
        for (int j = 0; j < board[0].length; j++) {
            System.out.print("---+");
        }
        System.out.println();
    }

    public int[][] getBoard() {
        return board;
    }

    public int getI() {
        return posI;
    }

    public int getJ() {
        return posJ;
    }

    public void show() {
        switch (movement) {
            case 'u' -> System.out.println("Move UP");
            case 'd' -> System.out.println("Move DOWN");
            case 'l' -> System.out.println("Move LEFT");
            case 'r' -> System.out.println("Move RIGHT");
            case 'n' -> System.out.println("START");
        }
        printBoard(board);
    }

    public void swap(int i, int j) {
        int aux = board[i][j];
        board[i][j] = 0;
        board[posI][posJ] = aux;
        posI = i;
        posJ = j;
    }


    public ArrayList<State> nextStates() {
        ArrayList<State> next = new ArrayList<>();
        int[][] newBoard = new int[size[0]][size[1]];

        // Clone board
        for (int i = 0; i < board.length; ++i) {
            System.arraycopy(this.board[i], 0, newBoard[i], 0, size[0]);
        }

        // Move up
        if (posI > 0) {
            int newI = posI - 1;
            State newState = new State(newBoard, 'u', size);
            newState.swap(newI, posJ); // 
            next.add(newState);
        }

        // Move down
        if (posI < 2) {
            int newI = posI + 1;
            State newState = new State(newBoard, 'd', size);
            newState.swap(newI, posJ);
            next.add(newState);
        }

        // Move left
        if (posJ > 0) {
            int newJ = posJ - 1;
            State newState = new State(newBoard, 'l', size);
            newState.swap(posI, newJ);
            next.add(newState);
        }

        // Mover right
        if (posJ < 2) {
            int newJ = posJ + 1;
            State newState = new State(newBoard, 'r', size);
            newState.swap(posI, newJ);
            next.add(newState);
        }

        return next;
    }

    public char getMovement() {
        return movement;
    }

    public boolean isEqual(State s) {
        int[][] sBoard = s.getBoard();
        for (int i = 0; i < sBoard.length; i++) {
            for (int j = 0; j < sBoard[0].length; j++) {
                if (sBoard[i][j] != board[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }
}
