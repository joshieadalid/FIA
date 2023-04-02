package agentes;

import javax.swing.*;
import java.util.*;

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
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix[0].length; ++j) {
                if (matrix[i][j] == typeSpacecraft) {
                    return new Position(i, j);
                }
            }
        }
        return null;
    }

    @Override
    public void run() {
        mapSamples();
        this.spacecraftPosition = findSpacecraftPosition(matrix);
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

    private void move(Position position) {
        lastSquare = board[this.agentPosition.i()][this.agentPosition.j()];
        this.agentPosition = position;
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
        return new ArrayList<>(List.of(
                new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1)
        ))
                .stream()
                .filter(pos -> isType(matrix, agentPosition.addPosition(pos), typeEmpty))
                .min(Comparator.comparingInt(pos ->
                        this.spacecraftPosition.subtractPosition(agentPosition.addPosition(pos)).manhattan()
                ))
                .orElseThrow() // Si no hay direcciones válidas, lanza una excepción
                .getDirection();
    }



    private void selectMovement() {
        if (!hasSample) {
            Direction randomDirection = randomDirection();
            Position squarePosition = agentPosition.addDirection(randomDirection);
            if (isType(matrix, squarePosition, typeSample)) {
                grabSample(agentPosition.addDirection(randomDirection));
                move(agentPosition.addDirection(randomDirection));
            } else if (isType(matrix, squarePosition, typeEmpty)) {
                move(agentPosition.addDirection(randomDirection));
            }
        } else {
            move(agentPosition.addDirection(spacecraftDirection())); // Moverse a la nave

            if (agentPosition.subtractPosition(spacecraftPosition).manhattan() <= 1) {
                dropSample();
            }
        }
    }




    @Override
    public String toString() {
        return name;
    }
}
