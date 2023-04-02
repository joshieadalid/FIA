package agentes;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static agentes.AgentFunctions.*;

public class Agent extends Thread {
    private static final Random waitingTime = new Random(System.nanoTime());
    private static final Map<String, Integer> OBJECT_TYPES = Map.of(
            "Sample", typeSample,
            "Obstacle", typeObstacle,
            "Spacecraft", typeSpacecraft
    );
    private final String name;
    private final ImageIcon agentIcon;
    private final int[][] matrix;
    private final JLabel[][] board;
    private Position position;
    private Position spacecraftPosition;
    private boolean hasSample;
    private JLabel lastSquare;

    public Agent(String name, ImageIcon agentIcon, int[][] matrix, JLabel[][] board) {
        this.name = name;
        this.agentIcon = agentIcon;
        this.matrix = matrix;
        this.board = board;
        this.hasSample = false;

        this.position = randomMatrixPosition(matrix.length, matrix[0].length);
        board[position.i()][position.j()].setIcon(agentIcon);
    }

    private static Position spacecraftPosition(int[][] matrix) {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == 3) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    @Override
    public void run() {
        mapSamples();
        this.spacecraftPosition = spacecraftPosition(matrix);
        while (true) {
            // Algoritmo para que el robot únicamente se mueva en cruz (arriba-abajo, izquierda-derecha)
            selectMovement();
            try {
                Thread.sleep(100 + waitingTime.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

    public synchronized void refresh() {
        lastSquare.setIcon(null); // Elimina su figura de la casilla anterior
        board[position.i()][position.j()].setIcon(agentIcon); // Pone su figura en la nueva casilla
    }

    private void move(Position position) {
        lastSquare = board[this.position.i()][this.position.j()];
        this.position = position;
        refresh();
    }

    private void grabSample(Position position) {
        System.out.println(name + ": Sample grabbed");
        hasSample = true;
        matrix[position.i()][position.j()] = 0;
    }

    private void dropSample() {
        System.out.println(name + ": Sample dropped");
        hasSample = false;
    }

    public Direction spacecraftDirection() {
        List<Position> availableDirections = Stream.of(
                        new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1)
                ).filter(pos -> isType(matrix, position.addPosition(pos), typeEmpty))
                .collect(Collectors.toList());

        Comparator<Position> manhattanComparator = Comparator.comparingInt(pos ->
                this.spacecraftPosition.subtractPosition(position.addPosition(pos)).manhattan()
        );

        availableDirections.sort(manhattanComparator);
        System.out.println("Direccion corta: " + availableDirections.get(0).manhattan());
        return availableDirections.get(0).getDirection();
    }

    private void selectMovement() {
        if (!hasSample) {
            Direction randomDirection = randomDirection();
            Position squarePosition = position.addDirection(randomDirection);
            if (isType(matrix, squarePosition, typeSample)) {
                grabSample(position.addDirection(randomDirection));
                move(position.addDirection(randomDirection));
            } else if (isType(matrix, squarePosition, typeEmpty)) {
                move(position.addDirection(randomDirection));
            }
        } else {
            move(position.addDirection(spacecraftDirection())); // Moverse a la nave

            if (position.subtractPosition(spacecraftPosition).manhattan() <= 1) {
                dropSample();
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


    @Override
    public String toString() {
        return name;
    }
}
