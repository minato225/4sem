import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GraphPane extends JComponent implements ActionListener {
    private final int HEIGHT_PANE = 500;
    private final int WIDTH_PANE = 500;
    private final Dimension size = new Dimension(WIDTH_PANE, HEIGHT_PANE);
    private final Timer timer = new Timer(50, this);
    private final Sector toDraw;

    public GraphPane(Sector toDraw) {
        this.toDraw = toDraw;
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, WIDTH_PANE, HEIGHT_PANE);
        toDraw.draw((Graphics2D) g);
    }

    @Override
    public Dimension getPreferredSize() {
        return size;
    }

    @Override
    public Dimension getMinimumSize() {
        return size;
    }
}