/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_9;

/**
 *
 * @author Рома
 */
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main extends JFrame {
    public Main() {
        super("My Combo Box");
        initialize();
    }

    public  void initialize() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(400, 300);
        setResizable(false);

        String[] items = {"One", "Two", "Three", "Four", "Five"};
        var indexItemToDelete = new AtomicInteger();
        var labelDeleteIndex = new JLabel("Current Index to delete: 0");

        final var comboBox = new JComboBox<>(items);
        comboBox.addActionListener(e -> {
            indexItemToDelete.set(comboBox.getSelectedIndex());
            labelDeleteIndex.setText("Current Index to delete: " + indexItemToDelete.incrementAndGet());
        });
        add(comboBox);

        var bAdd = new JButton("Add new");
        bAdd.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new Label("Enter New Item Text"));
            var text = new JTextField("Enter Text...");
            panel.add(text);
            var result = JOptionPane.showConfirmDialog(null, panel, "Добавить",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION)
                comboBox.insertItemAt(text.getText(), 0);
        });
        add(bAdd);

        var bDeleteBuIndex = new JButton("Delete");
        bDeleteBuIndex.addActionListener(e -> {
            if (indexItemToDelete.get() < 1) {
                error("Invalid Index to delete (index < 1)!!!");
            } else if (JOptionPane.showConfirmDialog(null,
                    "Delete \" " + comboBox.getItemAt(indexItemToDelete.get()-1) + " \" ?",
                    "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                comboBox.removeItemAt(indexItemToDelete.get() - 1);
                indexItemToDelete.set(-1);
            }
        });
        add(bDeleteBuIndex);

        var bRemoveAll = new JButton("Remove All");
        bRemoveAll.addActionListener(e -> comboBox.removeAllItems());
        add(bRemoveAll);

        add(labelDeleteIndex);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void error(String e) {
        JOptionPane.showMessageDialog(new JPanel(), e, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
