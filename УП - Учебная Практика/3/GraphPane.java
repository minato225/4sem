import javax.swing.*;
import java.awt.*;

public class GraphPane extends JComponent {
    RoseManager toDraw;
    Dimension size;

    public GraphPane(RoseManager toDraw) {
        this.toDraw = toDraw;
        size = new Dimension(500, 500);
        setMaximumSize(size);
    }

    public void paintComponent(Graphics g) {
        toDraw.draw((Graphics2D) g);
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public Dimension getMinimumSize() {
        return size;
    }
}