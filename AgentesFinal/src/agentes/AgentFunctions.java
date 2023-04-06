package agentes;

import java.util.Random;

public class AgentFunctions {
    public static final int typeEmpty = 0;
    public static final int typeSample = 1;
    public static final int typeObstacle = 2;
    public static final int typeSpacecraft = 3;
    public static final Random random = new Random(System.nanoTime());

    public static boolean inBounds(int[][] matrix, Position position) {
        return (position.i() >= 0 && position.i() < matrix.length && position.j() >= 0 && position.j() < matrix[0].length);
    }

    public static boolean isType(int[][] matrix, Position position, int type) {
        return inBounds(matrix, position) && matrix[position.i()][position.j()] == type;
    }

    public static Position randomMatrixPosition(int[][] matrix) {
        return new Position(random.nextInt(matrix.length), random.nextInt(matrix[0].length));
    }
}
