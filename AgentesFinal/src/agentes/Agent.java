package agentes;

import javax.swing.*;
import java.util.*;

import static agentes.AStar.printPath;
import static agentes.AgentFunctions.*;

public class Agent extends Thread {
    private static final Random waitingTime = new Random(System.nanoTime());
    private static final Map<String, Integer> OBJECT_TYPES = Map.of("Sample", typeSample, "Obstacle", typeObstacle, "Spacecraft", typeSpacecraft);
    private static final Position[] DIRECTIONS = {new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1)};
    private final String name;
    private final ImageIcon agentIcon;
    private final int[][] matrix;
    private final JLabel[][] board;

    private Position agentPosition;
    private Position spacecraftPosition;
    private boolean hasSample;
    private JLabel lastSquare;

    public Agent(String name, ImageIcon agentIcon, int[][] matrix, JLabel[][] board) {
        this.name = name;
        this.agentIcon = agentIcon;
        this.matrix = matrix;
        this.board = board;
        this.hasSample = false;
        this.agentPosition = randomMatrixPosition(matrix.length, matrix[0].length);
        board[agentPosition.i()][agentPosition.j()].setIcon(agentIcon);
    }

    private static Position findSpacecraftPosition(int[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[0].length; j++) {
                if (matrix[i][j] == typeSpacecraft) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    public static void printMatrix(int[][] matrix) {
        int numRows = matrix.length;
        int numCols = matrix[0].length;

        System.out.print("   ");
        for (int j = 0; j < numCols; j++) {
            System.out.printf("%2d ", j);
        }
        System.out.println();

        for (int i = 0; i < numRows; i++) {
            System.out.printf("%2d ", i);
            for (int j = 0; j < numCols; j++) {
                System.out.printf("%2d ", matrix[i][j]);
            }
            System.out.println();
        }
    }


    @Override
    public void run() {
        mapSamples();
        this.spacecraftPosition = findSpacecraftPosition(matrix);
        while (true) {
            // Algoritmo para que el robot únicamente se mueva en cruz (arriba-abajo, izquierda-derecha)
            behaviorMove();
            try {
                Thread.sleep(250 + waitingTime.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
    private List<Position> path = new ArrayList<>();
    private int i = 1;
    private void mapSamples() {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                String objectName = board[i][j].getName();
                if (objectName != null) {
                    Integer objectType = OBJECT_TYPES.get(objectName);
                    if (objectType != null) {
                        matrix[i][j] = objectType;
                    }
                }
            }
        }
    }

    public synchronized void refresh() {
        lastSquare.setIcon(null); // Elimina su figura de la casilla anterior
        board[agentPosition.i()][agentPosition.j()].setIcon(agentIcon); // Pone su figura en la nueva casilla
    }

    private void moveTo(Position position) {
        lastSquare = board[this.agentPosition.i()][this.agentPosition.j()];
        this.agentPosition = position;
        refresh();
    }

    private void grabSampleFrom(Position position) {
        System.out.println(name + ": Sample grabbed");
        hasSample = true;
        matrix[position.i()][position.j()] = 0;
    }

    private void dropSample() {
        System.out.println(name + ": Sample dropped");
        hasSample = false;
    }

    private Direction spacecraftDirection() {
        List<Position> directions = Arrays.asList(new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1));

        Collections.shuffle(directions);

        Optional<Position> direction = directions.stream().parallel().filter(pos -> isType(matrix, agentPosition.plus(pos), typeEmpty)).min(Comparator.comparingInt(pos -> this.spacecraftPosition.minus(agentPosition.plus(pos)).manhattan()));

        return direction.map(Position::getDirection).orElse(null);
    }

    public Direction samplesDirection() {
        List<Position> directions = Arrays.asList(new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1));

        Collections.shuffle(directions);

        return directions.parallelStream().filter(dir -> isType(matrix, agentPosition.plus(dir), typeSample) || isType(matrix, agentPosition.plus(dir), typeEmpty)).sorted((dir1, dir2) -> {
            boolean dir1IsSample = isType(matrix, agentPosition.plus(dir1), typeSample);
            boolean dir2IsSample = isType(matrix, agentPosition.plus(dir2), typeSample);
            return Boolean.compare(dir2IsSample, dir1IsSample);
        }).map(Position::getDirection).findFirst().orElse(null);
    }

    private void moveToSpacecraft(List<Position> path, int i) {
        moveTo(path.get(i));
        dropSample();
    }

    private void behaviorMove() {


        if (!hasSample) {
            Position movingTo = agentPosition.plus(samplesDirection());
            if (isType(matrix, movingTo, typeSample)) {
                grabSampleFrom(movingTo);
                path = new AStar(matrix, agentPosition, spacecraftPosition).findPath();
                printPath(path);
            }
            moveTo(movingTo);
        } else {
            System.out.println("Con muestra");
            if (i < path.size() - 1) {
                moveTo(path.get(i));
                ++i;
            } else {
                i = 1;
                path = null;
                dropSample();
            }
        }
    }
}
