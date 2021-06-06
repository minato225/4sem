import javax.swing.*;
import java.awt.*;

public class MyPanel extends JPanel {

    public MyPanel() {
        super();
        setSize(300, 300);
        repaint();
    }

    public void paint(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.translate(250, 230);
        g2.setStroke(new MyStroke(2f));
        g2.draw(new CustomShape(200));
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(500, 500);
    }
}