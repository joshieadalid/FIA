package agentes;

import java.util.Random;

public class AgentFunctions {
    public static final int typeEmpty = 0;
    public static final int typeSample = 1;
    public static final int typeObstacle = 2;
    public static final int typeSpacecraft = 3;
    private static final Random random = new Random(System.nanoTime());

    public static boolean inBounds(int[][] matrix, Position position) {
        return (position.i() >= 0 && position.i() < matrix.length && position.j() >= 0 && position.j() < matrix[0].length);
    }

    public static boolean isType(int[][] matrix, Position position, int type) {
        return inBounds(matrix, position) && matrix[position.i()][position.j()] == type;
    }

    public static Direction randomDirection() {
        int dirSelect = random.nextInt(4);
        return switch (dirSelect) {
            case 0 -> new Direction(-1, 0); // Arriba
            case 1 -> new Direction(+1, 0); // Abajo
            case 2 -> new Direction(0, -1); // Izquierda
            case 3 -> new Direction(0, +1); // Derecha
            default -> null;
        };
    }


    public static Position randomMatrixPosition(int i, int j) {
        return new Position(random.nextInt(i), random.nextInt(j));
    }
}
