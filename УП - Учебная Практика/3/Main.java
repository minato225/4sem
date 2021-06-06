import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main(RoseManager toDraw) {
        Container pane = getContentPane();
        pane.setLayout(new BorderLayout());
        pane.add(new GraphPane(toDraw));
    }

    public static void main(String[] args) {
        Main frame = new Main(new RoseManager(250, 250, 200));
        frame.pack();
        frame.setVisible(true);
        frame.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}
