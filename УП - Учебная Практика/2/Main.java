import javax.swing.*;
import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

public class Main extends JFrame {
    public Main(Sign toDraw) {
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(400, 100);
        setResizable(false);
        add(new GraphSamplePane(toDraw));
        pack();
    }

    public static void main(String[] args) {
        new Main(new Sign());
    }
}

class GraphSamplePane extends JComponent {
    Sign example;
    Dimension size;

    public GraphSamplePane(Sign example) {
        this.example = example;
        size = new Dimension(500, 500);
        setMaximumSize(size);
    }

    public void paintComponent(Graphics g) {
        g.setColor(Color.white);
        g.fillRect(0, 0, size.width, size.height);
        g.setColor(Color.black);
        example.draw((Graphics2D) g, this);
    }

    public Dimension getPreferredSize() {
        return size;
    }

    public Dimension getMinimumSize() {
        return size;
    }
}

class Sign {
    static final int WIDTH = 500, HEIGHT = 500;

    public void draw(Graphics2D ig, Component c) {

        BufferedImage bimage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bimage.createGraphics();

        //градиент серый
        g.setPaint(new GradientPaint(0, 0, Color.WHITE, WIDTH, 0, Color.GRAY));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        //красный
        g.setColor(Color.RED);

        g.setStroke(new BasicStroke(40));
        g.drawOval(50, 50, WIDTH - 100, HEIGHT - 100);

        g.setColor(Color.white);
        g.fillOval(70, 70, WIDTH - 140, HEIGHT - 140);

        Font font = new Font("Serif", Font.BOLD, 17);
        Font bigfont = font.deriveFont(AffineTransform.getScaleInstance(13.0, 13.0));
        GlyphVector gv = bigfont.createGlyphVector(g.getFontRenderContext(), "80");

        Shape shapeEight = gv.getGlyphOutline(0);
        Shape shapeZero = gv.getGlyphOutline(1);

        g.setStroke(new BasicStroke(5.0f));

        g.translate(140, 320);
        g.setPaint(Color.RED);
        g.fill(shapeEight);
        g.fill(shapeZero);

        ig.drawImage(new RescaleOp(1.25f, 0, null).filter(bimage, null), 0,0, c);

        ig.drawString("Brighten", 80, 13);
    }
}