package agentes;

import javax.swing.*;
import java.awt.*;

public class BackgroundPanel extends JPanel {
    ImageIcon fondo;

    public BackgroundPanel(ImageIcon fondo) {
        super();
        this.fondo = fondo;
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        Dimension dim = this.getSize();
        int ancho = (int) dim.getWidth();
        int alto = (int) dim.getHeight();
        g.drawImage(fondo.getImage(), 0, 0, ancho, alto, null);
        setOpaque(false);
        super.paintComponent(g);
    }
}
