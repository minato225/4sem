import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;

public class MainFrame extends JFrame {
    public static String str;

    public static void main(String[] args) {
        str = "./src/CustomShape.java";
        Frame f = new MainFrame();
        f.setVisible(true);
        f.pack();
    }

    public MainFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new MyPanel(), "Center");

        Panel p = new Panel();
        p.setFont(new Font("Arial", Font.BOLD, 25));
        add(p, "South");

        Button print = new Button("Print");
        print.addActionListener(x -> {
            try {
                printDemoPage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        p.add(print);
    }

    public void printDemoPage() throws IOException{
        FileReader fr = new FileReader(str);
        HardcopyWriter hw = new HardcopyWriter(this, "Roman",12, .20, .20, .20, .60);
        PrintWriter writer = new PrintWriter(hw);

        BufferedImage img = new BufferedImage(500, 500, Image.SCALE_DEFAULT);

        Graphics2D ig = (Graphics2D) img.getGraphics();
        ig.fillRect(0, 0, 500, 500);
        ig.setColor(Color.BLACK);
        ig.translate(200, 240);
        ig.setStroke(new MyStroke(1f));
        ig.draw(new CustomShape(200));

        hw.newpage();
        hw.drawImage(img);
        writer.println("\n\nЧетырёхлепестковая роза");
        ig.translate(0, 0);
        HardcopyWriter.printFile(fr, hw);
    }
}
