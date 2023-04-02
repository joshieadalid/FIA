package agentes;

import javax.swing.*;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static agentes.AgentFunctions.*;

public class Agent extends Thread {
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
        Random waitingTime = new Random(System.currentTimeMillis());
        while (true) {
            //Algoritmo para que el robot únicamente se mueva en cruz (arriba-abajo, izquierda-derecha)
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
        List<Position> distancesList = Stream.of(
                        new Position(-1, 0), new Position(1, 0), new Position(0, -1), new Position(0, 1)
                ).filter(pos -> isType(matrix, position.sumPosition(pos), typeEmpty))
                .collect(Collectors.toList());

        Comparator<Position> sumComparator = Comparator.comparingInt(Position::manhattan);
        distancesList.sort(sumComparator);

        return new Direction(distancesList.get(0).i(), distancesList.get(0).j());
    }


    private void selectMovement() {
        if (!hasSample) {
            Direction randomDirection = randomDirection();
            Position squarePosition = position.sumDirection(randomDirection);
            if (isType(matrix, squarePosition, typeSample)) {
                grabSample(position.sumDirection(randomDirection));
                move(position.sumDirection(randomDirection));
            } else if (isType(matrix, squarePosition, typeEmpty)) {
                move(position.sumDirection(randomDirection));
            }
        } else {
            move(position.sumDirection(spacecraftDirection())); // Moverse a la nave
            Direction diff = absPosition(position.subtractPosition(spacecraftPosition));
            if (diff.i() + diff.j() <= 1) {
                dropSample();
            }
        }
    }

    private void mapSamples() {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                if (board[i][j].getName() != null) {
                    switch (board[i][j].getName()) {
                        case "Sample" -> matrix[i][j] = typeSample;
                        case "Obstacle" -> matrix[i][j] = typeObstacle;
                        case "Spacecraft" -> matrix[i][j] = typeSpacecraft;
                        default -> {
                        }
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
