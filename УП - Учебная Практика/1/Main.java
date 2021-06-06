import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    public Main(Sector toDraw){
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(400, 100);
        setResizable(false);
        this.add(new GraphPane(toDraw));
        pack();
    }

    public static void main(String[] args) {
        new Main(new Sector(5, Color.GREEN, Color.BLUE));
    }
}