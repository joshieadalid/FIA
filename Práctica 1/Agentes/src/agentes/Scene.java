package agentes;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Random;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;

public class Scene extends JFrame {

    private final int dim = 15;
    private final JMenu settings = new JMenu("Settings");
    private final JRadioButtonMenuItem obstacle = new JRadioButtonMenuItem("Obstacle");
    private final JRadioButtonMenuItem sample = new JRadioButtonMenuItem("Sample");
    private final JRadioButtonMenuItem motherShip = new JRadioButtonMenuItem("MotherShip");
    private JLabel[][] tablero;
    private int[][] matrix;
    private ImageIcon obstacleIcon;
    private ImageIcon sampleIcon;
    private ImageIcon actualIcon;
    private ImageIcon motherIcon;
    private Agente wallE;
    private Agente eva;

    public Scene() {
        Background fondo = new Background(new ImageIcon("img/surface.jpg"));
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
        JMenu file = new JMenu("File");
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

        ImageIcon robot1 = new ImageIcon("img/wall-e.png");
        robot1 = new ImageIcon(robot1.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        ImageIcon robot2 = new ImageIcon("img/eva.png");
        robot2 = new ImageIcon(robot2.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        obstacleIcon = new ImageIcon("img/bomb.png");
        obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        sampleIcon = new ImageIcon("img/sample.png");
        sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        motherIcon = new ImageIcon("img/mothership.png");
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
                goodbye();
            }
        }
        addWindowListener(new MyWindowAdapter());

        // Crea 2 agentes
        Random random = new Random(System.currentTimeMillis());
        wallE = new Agente("Wall-E", robot1, matrix, tablero,new Position(random.nextInt(matrix.length),random.nextInt(matrix.length)));
        wallE = new Agente("Wall-E", robot1,matrix,tablero,new Position(random.nextInt(matrix.length), random.nextInt(matrix.length)));
        eva = new Agente("Eva", robot2, matrix, tablero, new Position(random.nextInt(matrix.length),random.nextInt(matrix.length)));
    }

    private void gestionaSalir(ActionEvent eventObject) {
        goodbye();
    }

    private void goodbye() {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Desea salir?", "Aviso", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void formaPlano() {
        tablero = new JLabel[dim][dim];
        matrix = new int[dim][dim];

        int cellWidth = this.getWidth() / dim;
        int cellHeight = this.getHeight() / dim;

        for (int i = 0; i < dim; ++i) {
            for (int j = 0; j < dim; ++j) {
                matrix[i][j] = 0;
                tablero[i][j] = new JLabel();
                tablero[i][j].setBounds(j * cellWidth, i * cellHeight, cellWidth, cellHeight);
                tablero[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                tablero[i][j].setOpaque(false);
                this.add(tablero[i][j]);

                // Este listener nos ayuda a agregar poner objetos en la rejilla
                tablero[i][j].addMouseListener(new MouseAdapter() {
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
            casilla.setIcon(actualIcon);
        }
    }
}