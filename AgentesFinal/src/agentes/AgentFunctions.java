package agentes;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

public class AgentFunctions {
    public static final int tipoVacio = 0;
    public static final int tipoMuestra = 1;
    public static final int tipoObstaculo = 2;
    public static final int tipoNave = 3;

    public static boolean inBounds(int[][] matrix, Position position) {
        return (position.i() >= 0 && position.i() < matrix.length && position.j() >= 0 && position.j() < matrix.length);
    }

    public static boolean isType(int[][] matrix, Position position, int type) {
        if (inBounds(matrix, position)) {
            return matrix[position.i()][position.j()] == type;
        } else {
            return false;
        }
    }

    public static Direction randomDirection() {
        int dirSelect = new Random(System.currentTimeMillis()).nextInt(4) + 1;
        switch (dirSelect) {
            case 1 -> { // Arriba
                return new Direction(-1, 0);
            }
            case 2 -> { // Abajo
                return new Direction(+1, 0);
            }
            case 3 -> { // Izquierda
                return new Direction(0, -1);
            }
            case 4 -> { // Derecha
                return new Direction(0, +1);
            }
        }
        return new Direction(0, 0);
    }

    public static Position randomMatrixPosition(int i, int j) {
        int randomI = new Random(System.currentTimeMillis()).nextInt(i);
        int randomJ = new Random(System.currentTimeMillis()).nextInt(j);
        return new Position(randomI, randomJ);
    }


    public static Direction absPosition(Position position){
        return new Direction(Math.abs(position.i()),Math.abs(position.i()));
    }
}
