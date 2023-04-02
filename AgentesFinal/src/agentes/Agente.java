package agentes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static agentes.AgentFunctions.*;

public class Agente extends Thread {
    String nombre;
    Position position;
    Position positionNave;
    boolean tieneMuestra;
    boolean[] casillaLibre = new boolean[4];
    ImageIcon icon;
    ImageIcon obstacle;
    ImageIcon object;
    ImageIcon motherShip;
    int[][] matrix;
    JLabel[][] tablero;
    JLabel casillaAnterior;

    public Agente(String nombre, ImageIcon icon, ImageIcon obstacle, ImageIcon object, ImageIcon mothership, int[][] matrix, JLabel[][] tablero) {
        this.nombre = nombre;
        this.icon = icon;
        this.obstacle = obstacle;
        this.object = object;
        this.motherShip = mothership;
        this.matrix = matrix;
        this.tablero = tablero;
        this.tieneMuestra = false;
        this.casillaLibre[0] = false;
        this.casillaLibre[1] = false;
        this.casillaLibre[2] = false;
        this.casillaLibre[3] = false;
        this.position = randomMatrixPosition(matrix.length, matrix[0].length);
        tablero[position.i()][position.j()].setIcon(icon);
    }

    public void run() {
        mapSamples();
        buscarPosicionNave();
        Random waitingTime = new Random(System.currentTimeMillis());
        while (true) {
            //Algoritmo para que el robot únicamente se mueva en cruz (arriba-abajo, izquierda-derecha)
            selectMovement();
            try {
                Thread.sleep(500 + waitingTime.nextInt(100));
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }

    }

    public synchronized void refresh() {
        casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
        tablero[position.i()][position.j()].setIcon(icon); // Pone su figura en la nueva casilla
    }

    private void move(Position position) {
        casillaAnterior = tablero[this.position.i()][this.position.j()];
        this.position = position;
        refresh();
    }

    void grabSample(Position position) {
        System.out.println(nombre + ": Muestra agarrada");
        tieneMuestra = true;
        matrix[position.i()][position.j()] = 0;
    }

    public Direction direccionNave() {
        ArrayList<DireccionDistancia> listaDistancias = new ArrayList<>();

        final Direction arriba = new Direction(-1, 0);
        final Direction abajo = new Direction(1, 0);
        final Direction izquierda = new Direction(0, -1);
        final Direction derecha = new Direction(0, 1);



        if (isType(matrix, position.sumDirection(arriba), tipoVacio)) {
            Position newAgentPosition = position.sumDirection(arriba);
            Direction diffNave = absPosition(positionNave.subtractPosition(newAgentPosition));
            double manhattanDist = diffNave.i() + diffNave.j();
            listaDistancias.add(new DireccionDistancia("Arriba", manhattanDist));
        }
        if (isType(matrix, position.sumDirection(abajo), tipoVacio)) {
            Position newAgentPosition = position.sumDirection(abajo);
            Direction diffNave = absPosition(positionNave.subtractPosition(newAgentPosition));
            double manhattanDist = diffNave.i() + diffNave.j();
            listaDistancias.add(new DireccionDistancia("Abajo", manhattanDist));
        }
        if (isType(matrix, position.sumDirection(izquierda), tipoVacio)) {
            Position newAgentPosition = position.sumDirection(izquierda);
            Direction diffNave = absPosition(positionNave.subtractPosition(newAgentPosition));
            double distance = diffNave.i() + diffNave.j();
            listaDistancias.add(new DireccionDistancia("Izquierda", distance));
        }
        if (isType(matrix, position.sumDirection(derecha), tipoVacio)) {
            Position newAgentPosition = position.sumDirection(derecha);
            Direction diffNave = absPosition(positionNave.subtractPosition(newAgentPosition));
            double manhattanDist = diffNave.i() + diffNave.j();
            listaDistancias.add(new DireccionDistancia("Derecha", manhattanDist));
        }

        System.out.println("Distancias: " + this + ": " + listaDistancias);
        listaDistancias.sort(Comparator.comparingDouble(DireccionDistancia::distancia));

        return switch (listaDistancias.get(0).direccion()) {
            case "Arriba" -> arriba;
            case "Abajo" -> abajo;
            case "Izquierda" -> izquierda;
            case "Derecha" -> derecha;
            default -> new Direction(0, 0);
        };
    }

    private void selectMovement() {
        if (!tieneMuestra) {
            Direction direccionAleatoria = randomDirection();
            if (isType(matrix, position.sumDirection(direccionAleatoria), tipoMuestra)) {
                grabSample(position.sumDirection(direccionAleatoria));
                move(position.sumDirection(direccionAleatoria));
            } else if (isType(matrix, position.sumDirection(direccionAleatoria), tipoVacio)) {
                move(position.sumDirection(direccionAleatoria));
            }
        } else {
            move(position.sumDirection(direccionNave())); // Moverse a la nave

            Direction diff = new Direction(Math.abs(this.position.i() - this.positionNave.i()), Math.abs(this.position.j() - this.positionNave.j()));
            System.out.println(this + ": " + diff);
            if (diff.i() + diff.j() <= 1) {
                tieneMuestra = false;
                System.out.println(nombre + ": Muestra soltada");
            }
        }
    }

    private void buscarPosicionNave() {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                if (matrix[i][j] == 3) {
                    //Es una nave
                    positionNave = new Position(i, j);
                }
            }
        }
    }

    private void mapSamples() {
        for (int i = 0; i < matrix.length; ++i) {
            for (int j = 0; j < matrix.length; ++j) {
                if (tablero[i][j].getName() != null) {
                    switch (tablero[i][j].getName()) {
                        case "Muestra" -> matrix[i][j] = tipoMuestra;
                        case "Obst" -> matrix[i][j] = tipoObstaculo;
                        case "Nave" -> matrix[i][j] = tipoNave;
                        default -> {
                        }
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return nombre;
    }
}
