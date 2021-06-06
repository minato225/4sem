import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.*;
public class CustomShape implements Shape {
    double radius;
    public CustomShape(double radius) { this.radius = radius; }
    public class ListIterator implements PathIterator {
        AffineTransform transform;
        boolean done = false;
        double angle = 0;

        public ListIterator(AffineTransform transform) { this.transform = transform; }
        public int getWindingRule() { return WIND_NON_ZERO; }
        public boolean isDone() { return done; }
        public int currentSegment(float[] coords) {
            double[] doubleCoords = new double[2];
            int result = currentSegment(doubleCoords);
            coords[0] = (float) doubleCoords[0];
            coords[1] = (float) doubleCoords[1];
            return result;
        }
        public int currentSegment(double[] coords) {
            double radian = Math.PI / 180.0;
            coords[0] = radius * Math.cos(angle * 2 * radian) * Math.sin(angle * radian);
            coords[1] = radius * Math.cos(angle * 2 * radian) * Math.cos(angle * radian);
            if (angle > 360) done = true;
            if (transform != null) transform.transform(coords, 0, coords, 0, 1);
            if (angle < 1e-5) return SEG_MOVETO;
            return SEG_LINETO;
        }

        public void next() {
            if (done) return;
            angle += 1;
        }
    }
    public Rectangle getBounds() { return new Rectangle(); }
    public Rectangle2D getBounds2D() { return new Rectangle(); }
    public boolean contains(double x, double y) { return false; }
    public boolean contains(Point2D p) { return false; }
    public boolean contains(Rectangle2D r) { return false; }
    public boolean contains(double x, double y, double w, double h) { return false;}
    public boolean intersects(double x, double y, double w, double h) {return false; }
    public boolean intersects(Rectangle2D r) { return intersects(r.getX(), r.getY(), r.getWidth(), r.getHeight()); }
    public PathIterator getPathIterator(AffineTransform arg0) { return new ListIterator(arg0); }
    public PathIterator getPathIterator(AffineTransform arg0, double arg1) { return new ListIterator(arg0); }
}