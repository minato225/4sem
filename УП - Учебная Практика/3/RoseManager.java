import java.awt.*;

public class RoseManager {
    Shape toDraw;

    public void draw(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.draw(new MyStroke(1).createStrokedShape(toDraw));
    }

    public RoseManager(int startX, int startY, double radius) {
        this.toDraw = new Rose(startX, startY, radius);
    }
}
