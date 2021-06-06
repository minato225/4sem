import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;

public class Sector{
    private final float width;
    private final Color fillColor;
    private final Color contourColor;
    private final Arc2D.Float toDraw;
    private Float angle = 0f;

    public Sector(float width, Color fillColor, Color contourColor) {
        this.toDraw = new Arc2D.Float(
                new Rectangle(-100, -100, 200, 200),
                0,
                60f,
                Arc2D.PIE
        );
        this.width = width;
        this.fillColor = fillColor;
        this.contourColor = contourColor;
    }

    public void draw(Graphics2D g) {
        g.setStroke(new BasicStroke(width, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_BEVEL));
        g.translate(250, 250);
        g.transform(AffineTransform.getRotateInstance(angle));
        angle += (float) Math.PI / 24f;
        g.setColor(fillColor);
        g.fill(toDraw);
        g.setColor(contourColor);
        g.draw(toDraw);
    }
}