package agentes;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public class Agente extends Thread {
    String nombre;
    Position position;
    int iNave;
    int jNave;
    boolean tieneMuestra;
    boolean[] casillaLibre = new boolean[4];
    ImageIcon icon;
    ImageIcon obstacle;
    ImageIcon object;
    ImageIcon motherShip;
    int[][] matrix;
    JLabel[][] tablero;
    JLabel casillaAnterior;
    Random aleatorio = new Random(System.currentTimeMillis());

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
        this.position = new Position(aleatorio.nextInt(matrix.length), aleatorio.nextInt(matrix.length));
        tablero[position.getI()][position.getJ()].setIcon(icon);
    }

    public void run() {
        mapSamples();
        buscarPosicionNave();

        while (true) {

            //Algoritmo para que el robot únicamente se mueva en cruz (arriba-abajo, izquierda-derecha)
            movement();
            try {
                sleep(100 + aleatorio.nextInt(100));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    // Métodos auxiliares
    public synchronized void refrescar() {
        casillaAnterior.setIcon(null); // Elimina su figura de la casilla anterior
        tablero[position.getI()][position.getJ()].setIcon(icon); // Pone su figura en la nueva casilla
    }

    private boolean sensor(Position dir) {
        if (position.getI() + dir.getI() >= 0 && position.getI() + dir.getI() < matrix.length && position.getJ() + dir.getJ() >= 0 && position.getJ() + dir.getJ() < matrix.length) {
            int type = matrix[position.getI() + dir.getI()][position.getJ() + dir.getJ()];
            return type != 1 && type != 2 && type != 3;
        } else {
            return false;
        }
    }

    private boolean isSample(Position dir) {
        if (position.getI() + dir.getI() >= 0 && position.getI() + dir.getI() < matrix.length && position.getJ() + dir.getJ() >= 0 && position.getJ() + dir.getJ() < matrix.length) {
            return matrix[position.getI() + dir.getI()][position.getJ() + dir.getJ()] == 1;
        } else {
            return false;
        }
    }

    private void mover(Position dir) {
        casillaAnterior = tablero[position.getI()][position.getJ()];
        position.setI(position.getI() + dir.getI());
        position.setJ(position.getJ() + dir.getJ());
        refrescar();
    }

    void agarrarMuestra(Position dir) {
        System.out.println(nombre + ": Muestra agarrada");
        tieneMuestra = true;
        matrix[position.getI() + dir.getI()][position.getJ() + dir.getJ()] = 0;
    }

    Position movimientoNave() {
        //System.out.printf("%s(%d, %d) -> nave(%d, %d)%n", nombre, position.getI(), position.getJ(), iNave, jNave);
        ArrayList<DireccionDistancia> distancias = new ArrayList<>();
        final Position arriba = new Position(-1, 0);
        final Position abajo = new Position(1, 0);
        final Position derecha = new Position(0, 1);
        final Position izquierda = new Position(0, -1);

        if (sensor(arriba)) {
            double distArriba = sqrt(pow(iNave - (position.getI() + arriba.getI()), 2) + pow((jNave - (position.getJ() + arriba.getJ())), 2));
            distancias.add(new DireccionDistancia("Arriba", distArriba));
        }
        if (sensor(abajo)) {
            double distAbajo = sqrt(pow((iNave - (position.getI() + abajo.getI())), 2) + pow((jNave - (position.getJ() + abajo.getJ())), 2));
            distancias.add(new DireccionDistancia("Abajo", distAbajo));
        }
        if (sensor(derecha)) {
            double distDerecha = sqrt(pow((iNave - (position.getI() + derecha.getI())), 2) + pow(jNave - (position.getJ() + (derecha.getJ())), 2));
            distancias.add(new DireccionDistancia("Derecha", distDerecha));
        }
        if (sensor(izquierda)) {
            double distIzquierda = sqrt(pow((iNave - (position.getI() + izquierda.getI())), 2) + pow((jNave - (position.getJ() + abajo.getJ())), 2));
            distancias.add(new DireccionDistancia("Izquierda", distIzquierda));
        }

        distancias.sort(Comparator.comparingDouble(DireccionDistancia::distancia));

        if (distancias.get(0).distancia() <= sqrt(2)) {
            tieneMuestra = false;
            System.out.println(nombre + ": Muestra soltada");
            return new Position(0, 0);
        }
        if (distancias.get(0).direccion().equals("Arriba")) {
            return arriba;
        }

        if (distancias.get(0).direccion().equals("Abajo")) {
            return abajo;
        }

        if (distancias.get(0).direccion().equals("Derecha")) {
            return derecha;
        }

        if (distancias.get(0).direccion().equals("Izquierda")) {
            return izquierda;
        }
        return new Position(0, 0);
    }


    private void movement() {
        int dirSelect = aleatorio.nextInt(4) + 1;
        Position direction = new Position(0, 0);
        switch (dirSelect) {
            case 1 -> // Arriba
                    direction = new Position(-1, 0);
            case 2 -> // Abajo
                    direction = new Position(+1, 0);
            case 3 -> // Derecha
                    direction = new Position(0, +1);
            case 4 -> // Izquierda
                    direction = new Position(0, -1);
        }

        if (isSample(direction)) if (!tieneMuestra) {
            agarrarMuestra(direction);
            mover(direction);
        }

        if (tieneMuestra) {
            mover(movimientoNave());
        } else {
            if (sensor(direction)) {
                mover(direction);
            }
        }


    }

    private void buscarPosicionNave() {
        for (int k = 0; k < matrix.length; k++) {
            for (int l = 0; l < matrix.length; l++) {
                if (matrix[k][l] == 3) {
                    //Es una nave
                    iNave = k;
                    jNave = l;
                }
            }
        }
    }

    private void mapSamples() {
        for (int k = 0; k < matrix.length; k++) {
            for (int l = 0; l < matrix.length; l++) {
                if (tablero[k][l].getName() != null) {
                    switch (tablero[k][l].getName()) {
                        case "Obst" -> matrix[k][l] = 2;
                        case "Muestra" -> matrix[k][l] = 1;
                        case "Nave" -> matrix[k][l] = 3;
                        default -> {
                        }
                    }
                }
            }
        }
    }
}
