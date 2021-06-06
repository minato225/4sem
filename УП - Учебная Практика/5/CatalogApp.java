import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

public class CatalogApp extends JFrame {
    public ArrayList<MovieNode> nodes;
    public CatalogTableModel tableModel;
    public CatalogTreeModel treeModel;
    public JTable infoPanel;
    public JTree tree;

    public CatalogApp() {
        super("Системное меню");
        setLocation(200, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(700, 500);

        initTree();
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                new JScrollPane(tree), new JScrollPane(infoPanel));
        splitPane.setDividerLocation(250);
        getContentPane().add(splitPane);

        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createAddButton());
        menuBar.add(createRemoveButton());
        menuBar.add(createSaveButton());
        menuBar.add(crateImportButton());
        setJMenuBar(menuBar);
        setVisible(true);
    }

    private void initTree() {
        tableModel = new CatalogTableModel();
        infoPanel = new JTable(tableModel);
        treeModel = new CatalogTreeModel(new MovieTreeNode("Video library"));
        tree = new JTree(treeModel);

        tree.addTreeSelectionListener(e -> {
            MovieTreeNode node = (MovieTreeNode) tree.getLastSelectedPathComponent();
            if (node == null) return;
            List<MovieNode> movieNodes = node.getAllLeaves();
            tableModel = new CatalogTableModel(movieNodes);
            infoPanel.setModel(tableModel);
        });

        nodes = new ArrayList<>();
        nodes.add(new MovieNode("Animation", "Moana", 2016, 200));
        nodes.add(new MovieNode("Horror", "Silent Hill", 2001, 109));
        nodes.add(new MovieNode("Action", "Terminator", 1982, 200));
        nodes.add(new MovieNode("Action", "Batman vs Superman", 2017, 100));
        nodes.add(new MovieNode("Animation", "Shrek", 2009, 180));

        nodes.forEach(this::addNewItem);
    }

    private JMenu createAddButton() {
        JMenu addHandler = new JMenu();
        addHandler.setIcon(new ImageIcon(getClass().getResource("add.png")));
        JMenuItem add = new JMenuItem("Добавить+ ");
        add.addActionListener(event -> addDataPlanes());
        addHandler.add(add);
        return addHandler;
    }

    private JMenu createRemoveButton() {
        JMenu removeHandler = new JMenu();
        removeHandler.setIcon(new ImageIcon(getClass().getResource("remove.png")));
        JMenuItem add = new JMenuItem("Удалить+ ");

        add.addActionListener(event -> {
            TreePath currentSelection = tree.getSelectionPath();
            if (currentSelection != null) {
                MovieTreeNode currentNode = (MovieTreeNode) (currentSelection.getLastPathComponent());
                MovieTreeNode parent = (MovieTreeNode) (currentNode.getParent());
                if (parent != null) {
                    treeModel.removeNodeFromParent(currentNode);
                    parent.deleteNode(currentNode);
                    List<MovieNode> movieNodes = parent.getAllLeaves();
                    tableModel = new CatalogTableModel(movieNodes);
                    infoPanel.setModel(tableModel);
                }
            }
        });
        removeHandler.add(add);
        return removeHandler;
    }

    private JMenu createSaveButton() {
        JMenu saveHandler = new JMenu();
        saveHandler.setIcon(new ImageIcon(getClass().getResource("save.png")));
        JMenuItem add = new JMenuItem("Сохранить+ ");

        add.addActionListener(event -> {
            try {
                SaveInFile(treeModel.getRoot());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        saveHandler.add(add);
        return saveHandler;
    }

    private JMenu crateImportButton() {
        JMenu importHandler = new JMenu();
        importHandler.setIcon(new ImageIcon(getClass().getResource("import.png")));
        JMenuItem add = new JMenuItem("Импорт+ ");

        add.addActionListener(event -> {
            try {
                importOutFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        importHandler.add(add);
        return importHandler;
    }

    private void SaveInFile(MovieTreeNode root) throws IOException {
        FileWriter fileWriter = new FileWriter("movie_library.txt", false);

        for (Enumeration e = root.depthFirstEnumeration(); e.hasMoreElements(); ) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (node.isLeaf()) {
                MovieTreeNode c = (MovieTreeNode) node;
                fileWriter.write(c.getNodeValue().toString());
                fileWriter.write('\n');
            }
        }

        JOptionPane.showMessageDialog(new JPanel(), "Файл сохранен.", "", JOptionPane.WARNING_MESSAGE);
        fileWriter.close();
    }

    public void importOutFile() throws IOException {
        JTextField textField = new JTextField("");
        String label = "Введите путь к файлу.";
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new Label(label));
        panel.add(textField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Файл импорта.",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String path = textField.getText();
            if (path.isEmpty()) path = "movie_library.txt";
            try {
                Scanner in = new Scanner(new FileReader(path));
                while (in.hasNext()) {
                    MovieNode node = new MovieNode(in.nextLine());
                    nodes.add(node);
                    this.addNewItem(node);
                }
            } catch (Exception ex) {
                this.error("Wrong format of file");
            }
        }
    }

    public MovieTreeNode findNode(MovieTreeNode root, String str) {
        Enumeration e = root.depthFirstEnumeration();
        while (e.hasMoreElements()) {
            MovieTreeNode node = (MovieTreeNode) e.nextElement();
            if (node.toString().equalsIgnoreCase(str))
                return node;
        }
        return null;
    }

    private void addDataPlanes() {
        JTextField[] textField = new JTextField[]{
                new JTextField(""), new JTextField(""),
                new JTextField(""), new JTextField("")};

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

                addNewItem(new MovieNode(genre, name, Integer.parseInt(price), Float.parseFloat(year)));
            } catch (Exception e) {
                this.error(e.getMessage());
            }
        }
    }

    private void addNewItem(MovieNode addResult) {
        MovieTreeNode where, insert, root = treeModel.getRoot();
        assert (addResult != null);
        try {
            insert = new MovieTreeNode(addResult.name, addResult);
            if ((where = findNode(root, addResult.genre)) == null) {
                treeModel.insertNodeInto(new MovieTreeNode(addResult.genre), root, root.getChildCount(), false);
                where = findNode(root, addResult.genre);
                assert where != null;
            }
            treeModel.insertNodeInto(insert, where, where.getChildCount(), false);
        } catch (Exception e) {
            this.error(e.getMessage());
        }
    }

    private void error(String e) {
        JOptionPane.showMessageDialog(new JPanel(), e, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
