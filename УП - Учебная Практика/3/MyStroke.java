import java.awt.BasicStroke;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.GeneralPath;
import java.awt.geom.PathIterator;

public class MyStroke implements Stroke {

    BasicStroke stroke;

    public MyStroke(float width) {
        stroke = new BasicStroke(width);
    }

    public Shape createStrokedShape (Shape shape) {
        GeneralPath path = new GeneralPath();
        float[] coord = new float[2];
        float[] prev = new float[2];
        double got = 0;
        for (PathIterator iter = shape.getPathIterator(null); !iter.isDone(); iter.next()) {
            int type = iter.currentSegment(coord);
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    path.moveTo(coord[0], coord[1]);
                    break;
                case PathIterator.SEG_LINETO:
                    double x1 = prev[0];
                    double y1 = prev[1];
                    double dx = coord[0] - x1;
                    double dy = coord[1] - y1;
                    double length = Math.sqrt( dx*dx + dy*dy );
                    double cos = dx / length;
                    double sin = dy / length;
                    x1 += cos * got;
                    y1 += sin * got;
                    length -= got;
                    got = 0;
                    double step = 4;
                    boolean isStraight = true;
                    while (got < length)
                    {
                        x1 += cos * step;
                        y1 += sin * step;
                        got += step;
                        if ( !isStraight )
                        {
                            path.lineTo(x1 + sin * step, y1 - cos * step);
                        }

                        path.lineTo(x1, y1);
                        isStraight = !isStraight;
                    }
                    got -= length;
                    break;
            }

            prev[0] = coord[0];
            prev[1] = coord[1];
        }
        return stroke.createStrokedShape(path);
    }
}
