package agentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.stream.IntStream;

public class Scene extends JFrame {

    private static final int dim = 12;
    private final JMenu settings = new JMenu("Settings");
    private final JLabel[][] board = new JLabel[dim][dim];
    private final Agent wallE;
    private final Agent eva;
    private ImageIcon obstacleIcon = new ImageIcon("AgentesFinal/img/bomb.png");
    private ImageIcon sampleIcon = new ImageIcon("AgentesFinal/img/sample.png");
    private ImageIcon spacecraftIcon = new ImageIcon("AgentesFinal/img/spacecraft.png");
    private ImageIcon selectedIcon;

    public Scene() {
        BackgroundPanel fondo = new BackgroundPanel(new ImageIcon("AgentesFinal/img/surface.jpg"));
        this.setContentPane(fondo);
        this.setTitle("Agentes");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setBounds(50, 50, dim * 50 + 35, dim * 50 + 85);
        ButtonGroup settingsOptions = new ButtonGroup();

        JRadioButtonMenuItem sampleOption = new JRadioButtonMenuItem("Sample");
        settingsOptions.add(sampleOption);
        JRadioButtonMenuItem obstacleOption = new JRadioButtonMenuItem("Obstacle");
        settingsOptions.add(obstacleOption);
        JRadioButtonMenuItem spacecraftOption = new JRadioButtonMenuItem("Spacecraft");
        settingsOptions.add(spacecraftOption);

        JMenuBar barraMenus = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem run = new JMenuItem("Run");
        JMenuItem exit = new JMenuItem("Exit");

        this.setJMenuBar(barraMenus);
        barraMenus.add(file);
        barraMenus.add(settings);
        file.add(run);
        file.add(exit);
        settings.add(spacecraftOption);
        settings.add(obstacleOption);
        settings.add(sampleOption);

        ImageIcon walleIcon = new ImageIcon("AgentesFinal/img/wall-e.png");
        walleIcon = new ImageIcon(walleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

        ImageIcon evaIcon = new ImageIcon("AgentesFinal/img/eva.png");
        evaIcon = new ImageIcon(evaIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));


        this.setLayout(null);
        int[][] matrix = new int[dim][dim];
        createBoard(dim);
        exit.addActionListener(this::exitInterface);
        run.addActionListener(this::runInterface);

        IconSelector iconSelector = new IconSelector();
        obstacleOption.addItemListener(iconSelector);
        sampleOption.addItemListener(iconSelector);
        spacecraftOption.addItemListener(iconSelector);

        class MyWindowAdapter extends WindowAdapter {
            public void windowClosing(WindowEvent eventObject) {
                System.exit(0);
            }
        }
        addWindowListener(new MyWindowAdapter());

        // Crea 2 agentes
        wallE = new Agent("Wall-E", walleIcon, matrix, board);
        eva = new Agent("Eva", evaIcon, matrix, board);

    }


    private void exitInterface(ActionEvent eventObject) {
        System.exit(0);
    }


    private void createBoard(int dim) {
        IntStream.range(0, dim).forEach(i -> IntStream.range(0, dim).forEach(j -> {
                            board[i][j] = new JLabel();
                            board[i][j].setBounds(j * 50 + 12, i * 50 + 12, 50, 50);
                            board[i][j].setBorder(BorderFactory.createDashedBorder(Color.white));
                            board[i][j].setOpaque(false);
                            this.add(board[i][j]);
                            board[i][j].addMouseListener(
                                    new MouseAdapter() {
                                        @Override
                                        public void mousePressed(MouseEvent e) {
                                            insertInterface(e);
                                        }

                                        @Override
                                        public void mouseReleased(MouseEvent e) {
                                            insertInterface(e);
                                        }
                                    }
                            );
                        }
                )
        );
    }


    public void insertInterface(MouseEvent e) {
        JLabel casilla = (JLabel) e.getSource();
        if (selectedIcon != null) {
            if (selectedIcon.equals(obstacleIcon)) {
                casilla.setName("Obstacle");
            } else if (selectedIcon.equals(sampleIcon)) {
                casilla.setName("Sample");
            } else if (selectedIcon.equals(spacecraftIcon)) {
                casilla.setName("Spacecraft");
            }
            casilla.setIcon(selectedIcon);
        }
    }

    private void runInterface(ActionEvent eventObject) {
        if (!wallE.isAlive()) {
            wallE.start();
        }
        if (!eva.isAlive()) {
            eva.start();
        }
        settings.setEnabled(false);
    }

    private class IconSelector implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            obstacleIcon = new ImageIcon(obstacleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
            sampleIcon = new ImageIcon(sampleIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));
            spacecraftIcon = new ImageIcon(spacecraftIcon.getImage().getScaledInstance(50, 50, java.awt.Image.SCALE_SMOOTH));

            JRadioButtonMenuItem insertOption = (JRadioButtonMenuItem) e.getSource();
            if (insertOption.isSelected()) {
                switch (insertOption.getText()) {
                    case "Obstacle" -> selectedIcon = obstacleIcon;
                    case "Sample" -> selectedIcon = sampleIcon;
                    case "Spacecraft" -> selectedIcon = spacecraftIcon;
                }
            } else {
                selectedIcon = null;
            }
        }
    }

}
