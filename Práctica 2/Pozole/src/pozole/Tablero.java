package pozole;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Tablero extends JFrame {
    private final int[] size;
    private final JButton[][] jBoard;
    private final LinkedHashMap<Integer, BufferedImage> puzzle = new LinkedHashMap<>();
    private final int[] initialState;
    private final int[] finalState;
    private final JMenuItem solveBroadth = new JMenuItem("Solve BFS");
    private final JMenuItem solveDeep = new JMenuItem("Solve DFS");
    private BufferedImage empty;
    private boolean deep = false;

    public Tablero(int[] initialState, int[] finalState, int[] size){
        this.initialState = initialState;
        this.finalState = finalState;
        this.size = size;
        jBoard = new JButton[this.size[0]][this.size[1]];
        initComponents();
    }

    private void initComponents() {
        this.setTitle("8-Puzzle");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        int width = pantalla.width;
        int height = pantalla.height;
        this.setBounds((width - 516) / 2, (height - 563) / 2, 516, 563);

        JMenuBar mainMenu = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem exit = new JMenuItem("Exit");

        mainMenu.add(file);
        file.add(solveBroadth);
        file.add(solveDeep);
        file.add(exit);
        this.setJMenuBar(mainMenu);

        this.setLayout(null);
        this.imagePieces("img/minako.jpg", size);
        paintPieces(initialState, size[0], size[1]);
        exit.addActionListener(evt -> gestionarExit());
        solveBroadth.addActionListener(this::whichMethod);
        solveDeep.addActionListener(this::whichMethod);

        // Handle the X-Button 
        class MyWindowAdapter extends WindowAdapter {
            @Override
            public void windowClosing(WindowEvent eventObject) {
                goodBye();
            }
        }
        addWindowListener(new MyWindowAdapter());
    }

    private void goodBye() {
        int respuesta = JOptionPane.showConfirmDialog(rootPane, "Are you sure?", "Exit", JOptionPane.YES_NO_OPTION);
        if (respuesta == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    private void gestionarExit() {
        goodBye();
    }


    // Parte la imagen en piezas
    private void imagePieces(String pathName, int[] gridSize) {
        try {
            BufferedImage fullImage = ImageIO.read(new File(pathName));
            int numRows = gridSize[0];
            int numCols = gridSize[1];
            int subImageWidth = fullImage.getWidth() / numCols;
            int subImageHeight = fullImage.getHeight() / numRows;

            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    int subImageX = j * subImageWidth;
                    int subImageY = i * subImageHeight;
                    BufferedImage subImage = fullImage.getSubimage(subImageX, subImageY, subImageWidth, subImageHeight);
                    puzzle.put(i * numCols + j, subImage);
                }
            }
        } catch (IOException e) {
            e.printStackTrace(System.out);
        }
    }


    public void paintPieces(int[] puzzleArray, int rows, int cols) {
        int n = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int k = puzzleArray[n];
                BufferedImage subImage = puzzle.get(k);
                int width = subImage.getWidth();
                int height = subImage.getHeight();
                jBoard[i][j] = new JButton();
                jBoard[i][j].setBounds(j * width + 1, i * height + 1, width, height); // Calcula la posición del botón i,j
                this.add(jBoard[i][j]);
                if (k != 0) jBoard[i][j].setIcon(new ImageIcon(subImage));
                else empty = subImage;
                n++;
            }
        }
    }

    // Este es el método que realmente busca mediante la técnica en anchura
    private void whichMethod(ActionEvent e) {
        if (e.getSource() == solveDeep) {
            deep = true;  // En caso de que se trate de búsqueda en profundidad
        }
        solve();
    }

    private void solve()
    {
        boolean success = false;
        int deadEnds = 0;
        int totalNodes = 0;
        State startState = new State(start);
        State goalState   = new State(goal);
        ArrayDeque queue = new ArrayDeque();
        ArrayList<State> first = new ArrayList();
        ArrayList<State> path=null;
        solveB.setEnabled(false);
        solveD.setEnabled(false);

        first.add(startState);
        queue.add(first);

        // Search loop

        int m=0;
        long startTime = System.currentTimeMillis();
        while(!queue.isEmpty() && !success && m < maxDeep )
        {
            int validStates = 0;
            m++;
            //System.out.println("Ciclo " + m);
            ArrayList<State> l = (ArrayList<State>) queue.getFirst();
            //System.out.println("Analizando Ruta de :" + l.size());
            // muestraEstados(l);
            State last = (State) l.get(l.size()-1);
            //last.show();
            ArrayList<State> next = last.nextStates();
            //System.out.println("Se encontraron " + next.size()+ " estados sucesores posibles");
            totalNodes+=next.size();

            queue.removeFirst(); // Se elimina el primer camino de la estrutura

            for(State ns: next)
            {
                if(!repetido(l,ns)) // Se escribió un método propio para verificar repetidos
                {
                    validStates++;
                    ArrayList<State> nl = (ArrayList<State>) l.clone();
                    if(ns.goalFunction(goalState))
                    {
                        success = true;
                        path = nl;
                    }
                    nl.add(ns);
                    //muestraEstados(nl);
                    if(deep)
                        queue.addFirst(nl); // Si es en profundidad agrega al principio la nueva ruta
                    else
                        queue.addLast(nl); // Si es en anchura agrega al final
                    //System.out.println("Agregé un nuevo camino con "+nl.size()+ " nodos");
                }
                //else System.out.println("Un nodo repetido descartado");
            }
            if(validStates==0) deadEnds++;  // Un callejón sin salida
        }

        if(success) // Si hubo éxito
        {
            long elapsed = System.currentTimeMillis()-startTime;
            if(deep) this.setTitle("8-Puzzle (Deep-First Search)");
            else this.setTitle("8-Puzzle (Breadth-First Search)");
            JOptionPane.showMessageDialog(rootPane, "Success!! \nPath with "+path.size()+" nodes"+"\nGenerated nodes: "+totalNodes+"\nDead ends: "+ deadEnds+"\nLoops: "+m+"\nElapsed time: " + elapsed + " milliseconds",
                    "Good News!!!", JOptionPane.INFORMATION_MESSAGE);
            System.out.println("Success!");
            String thePath="";
            int n=0;
            int i=startState.getI();
            int j=startState.getJ();
            for(State st: path)
            {
                st.show();
                if(n>0)
                    thePath = thePath+st.getMovement();
                n++;
            }
            Executor exec = new Executor(jBoard,i,j,thePath, empty);
            exec.start();
        }
        else
        {
            JOptionPane.showMessageDialog(rootPane, "Path not found", "Sorry!!!", JOptionPane.WARNING_MESSAGE);
            System.out.println("Path not found");
        }
    }

    private boolean repetido(ArrayList<State> l, State ns) {
        for (State s : l) {
            if (s.equals(ns)) {
                deadEnds++;
                return true;
            }
        }
        return false;
    }

    private String getPath(ArrayList<State> path, State startState) {
        StringBuilder thePath = new StringBuilder();
        int n = 0;
        int i = startState.getI();
        int j = startState.getJ();
        for (State st : path) {
            st.show();
            if (n > 0) {
                thePath.append(st.getMovement());
            }
            n++;
        }
        return thePath.toString();
    }

    private void executeSolution(String path, State startState, JButton[][] jBoard, JButton empty) {
        int i = startState.getI();
        int j = startState.getJ();
        Executor exec = new Executor(jBoard, i, j, path, empty);
        exec.start();
    }

    private void executeSolution(String path, State startState, JButton[][] jBoard, JButton empty) {
        Executor exec = new Executor(jBoard, startState.getI(), startState.getJ(), path, empty);
        exec.start();
    }


    private void muestraEstados(ArrayList<State> ruta) {
        System.out.println("======");
        for (State s : ruta){
            s.show();
        }
        System.out.println("======");
    }


}
