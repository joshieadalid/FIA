package agentes;

import javax.swing.*;
import java.util.*;

import static agentes.AgentFunctions.*;

public class Agent extends Thread {
    private static final Random waitingTime = new Random(System.nanoTime());
    private static final Map<String, Integer> OBJECT_TYPES = Map.of("Sample", typeSample, "Obstacle", typeObstacle, "Spacecraft", typeSpacecraft);
    private final String name;
    private final ImageIcon agentIcon;
    private final int[][] matrix;
    private final JLabel[][] board;

    private Position agentPosition;
    private Position spacecraftPosition;
    private boolean hasSample;
    private JLabel lastIcon;
    private List<Position> path = new ArrayList<>();
    private int currentPathIndex = 1;

    public Agent(String name, ImageIcon agentIcon, int[][] matrix, JLabel[][] board) {
        this.name = name;
        this.agentIcon = agentIcon;
        this.matrix = matrix;
        this.board = board;
        this.hasSample = false;
        this.agentPosition = randomMatrixPosition(matrix);
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
            moveAgent();
            try {
                Thread.sleep(350 + waitingTime.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

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
        board[agentPosition.i()][agentPosition.j()].setIcon(agentIcon);
    }

    private void moveTo(Position position) {
        board[agentPosition.i()][agentPosition.j()].setIcon(null);
        this.agentPosition = position;
        refresh();
    }


    private void grabSampleFrom(Position position) {
        hasSample = true;
        matrix[position.i()][position.j()] = 0;
    }

    private void dropSample() {
        hasSample = false;
    }

    public Position samplesDirection() {
        List<Position> directions = Arrays.asList(new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1));
        Collections.shuffle(directions);

        return directions.stream()
                .filter(dir -> isType(matrix, agentPosition.plus(dir), typeSample) || isType(matrix, agentPosition.plus(dir), typeEmpty)).min(Comparator.comparingInt(dir -> isType(matrix, agentPosition.plus(dir), typeSample) ? 0 : 1))
                .orElse(null);
    }

    private void moveAgent() {
        if (!hasSample) {
            moveTowardsSample();
        } else {
            followPathToSpacecraft();
        }
    }

    private void moveTowardsSample() {
        Position targetPosition = agentPosition.plus(samplesDirection());
        if (isType(matrix, targetPosition, typeSample)) {
            grabSampleFrom(targetPosition);
            // Elige todas las posiciones menos la primera y la última
            path = new AStar(matrix, agentPosition, spacecraftPosition).findPath();
            path = path.subList(1, path.size() - 1);
        }
        moveTo(targetPosition);

    }

    private void followPathToSpacecraft() {
        if (currentPathIndex < path.size()) {
            moveTo(path.get(currentPathIndex));
            currentPathIndex++;
        } else {
            currentPathIndex = 1;
            path = null;
            dropSample();
        }
    }

}
