package agentes;

import java.util.Random;

public class AgentFunctions {
    public static final int typeEmpty = 0;
    public static final int typeSample = 1;
    public static final int typeObstacle = 2;
    public static final int typeSpacecraft = 3;

    public static boolean inBounds(int[][] matrix, Position position) {
        return (position.i() >= 0 && position.i() < matrix.length && position.j() >= 0 && position.j() < matrix[0].length);
    }

    public static boolean isType(int[][] matrix, Position position, int type) {
        return inBounds(matrix, position) && matrix[position.i()][position.j()] == type;
    }


    public static Direction randomDirection() {
        int dirSelect = new Random(System.currentTimeMillis()).nextInt(4) + 1;
        return switch (dirSelect) {
            case 1 -> new Direction(-1, 0); // Arriba
            case 2 -> new Direction(+1, 0); // Abajo
            case 3 -> new Direction(0, -1); // Izquierda
            case 4 -> new Direction(0, +1); // Derecha
            default -> new Direction(0, 0);
        };
    }

    public static Position randomMatrixPosition(int i, int j) {
        Random r = new Random(System.currentTimeMillis());
        return new Position(r.nextInt(i), r.nextInt(j));
    }


    public static Direction absPosition(Position position) {
        return new Direction(Math.abs(position.i()), Math.abs(position.i()));
    }
}
