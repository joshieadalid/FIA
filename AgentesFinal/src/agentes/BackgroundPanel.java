package agentes;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class BackgroundPanel extends JPanel {
    private final ImageIcon fondo;

    public BackgroundPanel(ImageIcon fondo) {
        super();
        this.fondo = fondo;
        setOpaque(false);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repaint();
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension dim = getSize();
        int ancho = (int) dim.getWidth();
        int alto = (int) dim.getHeight();
        g.drawImage(fondo.getImage(), 0, 0, ancho, alto, null);
    }
}
