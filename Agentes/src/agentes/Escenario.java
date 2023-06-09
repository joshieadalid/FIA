/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Escenario extends JFrame {

    private JLabel[][] tablero;
    private int[][] matrix;
    private final int dim = 12;
    int i;
    int j;

    private ImageIcon obstacleIcon;
    private ImageIcon sampleIcon;
    private ImageIcon actualIcon;
    private ImageIcon motherIcon;

    private Agente wallE;
    private Agente eva;

    private final JMenu settings = new JMenu("Editar");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");

    public Escenario() {
        BackGroundPanel fondo = new BackGroundPanel(new ImageIcon("imagenes/surface.jpg"));
        this.setContentPane(fondo);
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(50, 50, dim * 50 + 35, dim * 50 + 85);
        initComponents();
    }

    private void initComponents() {
        ButtonGroup settingsOptions = new ButtonGroup();
        settingsOptions.add(sample);
        settingsOptions.add(obstacle);
        settingsOptions.add(motherShip);

        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("Archivo");
        JMenuItem run = new JMenuItem("Run");

        JMenuItem exit = new JMenuItem("Exit");

        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(motherShip);
        settings.add(obstacle);
        settings.add(sample);

        ImageIcon robot1 = new ImageIcon("imagenes/wall-e.png");
        robot1 = new ImageIcon(robot1.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        ImageIcon robot2 = new ImageIcon("imagenes/eva.png");
        robot2 = new ImageIcon(robot2.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        obstacleIcon = new ImageIcon("imagenes/bomb.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        sampleIcon = new ImageIcon("imagenes/sample.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        motherIcon = new ImageIcon("imagenes/mothership.png");
        motherIcon = new ImageIcon(motherIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        this.setLayout(null);
        formaPlano();

        exit.addActionListener(this::gestionaSalir);
        run.addActionListener(this::gestionaRun);
        obstacle.addItemListener(this::gestionaObstacle);
        sample.addItemListener(this::gestionaSample);
        motherShip.addItemListener(this::gestionaMotherShip);

        class MyWindowAdapter extends WindowAdapter {

            public void windowClosing(WindowEvent eventObject) {
                goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());

        // Crea 2 agentes
        wallE = new Agente("Wall-E", robot1, obstacleIcon, sampleIcon, motherIcon, matrix, tablero);
        eva = new Agente("Eva", robot2, obstacleIcon, sampleIcon, motherIcon, matrix, tablero);

    }

    private void gestionaSalir(ActionEvent eventObject) {
        goodBye();
    }

    private void goodBye() {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?", "Aviso", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void formaPlano() {
        tablero = new JLabel[dim][dim];
        matrix = new int[dim][dim];

        //int i, j;
        for (i = 0; i < dim; i++) {
            for (j = 0; j < dim; j++) {
                matrix[i][j] = 0;
                tablero[i][j] = new JLabel();
                tablero[i][j].setBounds(j * 50 + 12, i * 50 + 12, 50, 50);
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false);
                this.add(tablero[i][j]);

                tablero[i][j].addMouseListener(new MouseAdapter() // Este listener nos ayuda a agregar poner objetos en la rejilla
                {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        insertaObjeto(e);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        insertaObjeto(e);
                    }

                });

            }
        }
    }

    private void gestionaObstacle(ItemEvent eventObject) {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if (opt.isSelected()) {
            actualIcon = obstacleIcon;
        } else {
            actualIcon = null;
        }
    }

    private void gestionaSample(ItemEvent eventObject) {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if (opt.isSelected()) {
            actualIcon = sampleIcon;
        } else {
            actualIcon = null;
        }
    }

    private void gestionaMotherShip(ItemEvent eventObject) {
        JRadioButtonMenuItem opt = (JRadioButtonMenuItem) eventObject.getSource();
        if (opt.isSelected()) {
            actualIcon = motherIcon;
        } else {
            actualIcon = null;
        }
    }

    private void gestionaRun(ActionEvent eventObject) {
        if (!wallE.isAlive()) {
            wallE.start();
        }
        if (!eva.isAlive()) {
            eva.start();
        }
        settings.setEnabled(false);
    }

    public void insertaObjeto(MouseEvent e) {
        JLabel casilla = (JLabel) e.getSource();
        if (actualIcon != null) {
            if (actualIcon.equals(obstacleIcon)) {
                casilla.setName("Obst");
            } else if (actualIcon.equals(sampleIcon)) {
                casilla.setName("Muestra");
            } else if (actualIcon.equals(motherIcon)) {
                casilla.setName("Nave");
            }
            casilla.setIcon(actualIcon);
        }
    }

}
