package agentes;

import javax.swing.*;
import java.util.Random;

public class Agente extends Thread {
    String nombre;
    ImageIcon icon;
    int[][] matrix;
    JLabel[][] tablero;
    Position position;

    public Agente(String nombre, ImageIcon icon, int[][] matrix, JLabel[][] tablero, Position posicionInicial) {
        this.nombre = nombre;
        this.icon = icon;
        this.matrix = matrix;
        this.tablero = tablero;
        this.position = posicionInicial;

        tablero[position.getI()][position.getJ()].setIcon(icon);
    }

    public void run() {
        int iDir = 1;
        int jDir = 1;

        while (true) {
            iDir = reverseDirection(position.getI(), iDir, matrix.length);
            jDir = reverseDirection(position.getJ(), jDir, matrix[0].length);
            moverAgente(iDir, jDir);

            try {
                sleep(10 + new Random(System.currentTimeMillis()).nextInt(500));
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            System.out.printf("%s -> [%d, %d]%n", nombre, position.getI(), position.getJ());
        }
    }

    private int reverseDirection(int axisPosition, int momentum, int limit) {
        if (axisPosition + momentum >= limit || axisPosition + momentum < 0) {
            return -momentum;
        }
        return momentum;
    }

    private void moverAgente(int iDir, int jDir) {
        tablero[position.getI()][position.getJ()].setIcon(null);
        position.setI(position.getI() + iDir);
        position.setJ(position.getJ() + jDir);
        tablero[position.getI()][position.getJ()].setIcon(icon);
    }
}
