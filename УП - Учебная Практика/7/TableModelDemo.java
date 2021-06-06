import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

public class TableModelDemo extends JPanel {
    public ArrayList<ArrayList<String>> data = new ArrayList<>();

    public TableModelDemo() throws ClassNotFoundException{
        initializePanel();
    }

    private void initializePanel() throws ClassNotFoundException {
        data = DB_SqLite_Helper.GetData("video_lib");
        PremiereLeagueTableModel tableModel = new PremiereLeagueTableModel();

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        JScrollPane scrollPane = new JScrollPane(table);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createAddButton());
        menuBar.add(createCreateButton());

        this.setPreferredSize(new Dimension(500, 200));
        this.setLayout(new BorderLayout());
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(menuBar, BorderLayout.BEFORE_FIRST_LINE);
    }

    private JMenu createCreateButton() {
        JMenu createHandler = new JMenu();
        createHandler.setIcon(new ImageIcon(getClass().getResource("create.png")));
        JMenuItem add = new JMenuItem("создать+ ");
        add.addActionListener(event -> {
            try {
                showFrame();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
        createHandler.add(add);
        return createHandler;
    }

    private JMenu createAddButton() {
        JMenu addHandler = new JMenu();
        addHandler.setIcon(new ImageIcon(getClass().getResource("add.png")));
        JMenuItem add = new JMenuItem("Добавить+ ");
        add.addActionListener(event -> addDataPlanes());
        addHandler.add(add);
        return addHandler;
    }

    public static void showFrame() throws ClassNotFoundException {
        JPanel panel = new TableModelDemo();
        panel.setOpaque(true);

        JFrame frame = new JFrame("Cinema Data Base.");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setVisible(true);
    }

    class PremiereLeagueTableModel extends AbstractTableModel {
        private final String[] columnNames = { "Жанр", "Имя", "Цена", "Год"};

        public int getRowCount() {
            return data.size();
        }
        public int getColumnCount() {
            return columnNames.length;
        }
        public String getColumnName(int column) {
            return columnNames[column];
        }
        public Class<?> getColumnClass(int columnIndex) {
            return getValueAt(0, columnIndex).getClass();
        }
        public Object getValueAt(int rowIndex, int columnIndex) {
            return data.get(rowIndex).get(columnIndex);
        }
    }

    private void addDataPlanes() {
        JTextField[] textField = new JTextField[]{
                new JTextField(""), new JTextField(""), new JTextField(""), new JTextField("")};

        String[] dataString = new String[]{"Жанр", "Имя", "Цена", "Год",};
        JPanel panel = new JPanel(new GridLayout(0, 1));

        int i = 0;
        for (String x : dataString) {
            panel.add(new Label(x));
            panel.add(textField[i++]);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Добавить",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String genre = textField[0].getText();
            String name = textField[1].getText();
            String price = textField[2].getText();
            String year = textField[3].getText();

            boolean isValid = !genre.equals("") && !name.equals("") && !price.equals("") && !year.equals("");
            try {
                if (!isValid)
                    this.error("Пустая строка!");

                this.data.add(new ArrayList<>(Arrays.asList(genre,name,price,year)));
                DB_SqLite_Helper.addNewFilm(new FilmInfo(genre, name, price, year), "video_lib");
            } catch (Exception e) {
                this.error(e.getMessage());
            }
        }
    }

    private void error(String e) {
        JOptionPane.showMessageDialog(new JPanel(), e, "Error", JOptionPane.ERROR_MESSAGE);
    }
}