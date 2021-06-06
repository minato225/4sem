import java.awt.geom.*;

public class Rose extends Path2D.Double {
    double startX, startY, radius;

    public Rose(double startX, double startY, double radius) {
        super();
        this.startX = startX;
        this.startY = startY;
        this.radius = radius;
        createrose();
    }

    private void createrose() {
        this.moveTo(450, 250);
        for (double angle = 0; angle < 2 * Math.PI; angle += Math.toRadians(1))
            this.lineTo(translateX(xByAngle(angle)), translateY(yByAngle(angle)));
        closePath();
    }

    private double translateX(double x) {return x + startX; }
    private double translateY(double y) {return y + startY; }
    private double yByAngle(double angle) {return radius * Math.cos(angle * 2)*Math.sin(angle);}
    private double xByAngle(double angle) {return radius * Math.cos(angle * 2)*Math.cos(angle);}
}
