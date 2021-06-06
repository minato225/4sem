/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_10;

/**
 *
 * @author Рома
 */
import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicInteger;

public class MyComboBox extends JPanel {
    private Button bAdd,bDel, bRAll;
    
    private String[] items = {"One", "Two", "Three", "Four", "Fiv"};
    private String newItemText = "Enter Text...";
    private String buttonAddText = "Add new";
    private String buttonDeleteText = "Delete";
    private String buttonRemoveAllText = "Remove all";
    
    public void setItems(String[] items){ this.items = items; }
    public String[] getItems(){ return this.items; }
    
    public void setNewItemText(String str){ this.newItemText = str; }
    public String getNewItemText(){ return this.newItemText; }
    
    public void setButtonAddText(String str){
        this.bAdd.setLabel(str);
        this.buttonAddText = str; 
    }
    public String getButtonAddText(){ return this.buttonAddText; }
    
    public void setButtonDeleteText(String str){
        this.bDel.setLabel(str);
        this.buttonDeleteText = str; 
    }
    public String getButtonDeleteText(){ return this.buttonDeleteText; }
    
    public void setButtonRemoveAllText(String str){ 
        this.bRAll.setLabel(str);
        this.buttonRemoveAllText = str; 
    }
    public String getButtonRemoveAllText(){ return this.buttonRemoveAllText; }
    
    public MyComboBox() {
        this("ADD","DEL","REMOVE");
    }

    public MyComboBox(String addText, String DelText, String RemoveAllText){
        initialize(addText, DelText, RemoveAllText);
    }
    
    public void initialize(String addText, String DelText, String RemoveAllText) {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setSize(500, 500);
        var indexItemToDelete = new AtomicInteger();
        var labelDeleteIndex = new JLabel("Current Index to delete: 0");

        final var comboBox = new JComboBox<>(items);
        comboBox.addActionListener(e -> {
            indexItemToDelete.set(comboBox.getSelectedIndex());
            labelDeleteIndex.setText("Current Index to delete: " + indexItemToDelete.incrementAndGet());
        });
        add(comboBox);

        this.bAdd = new Button(addText);
        this.bAdd.addActionListener(e -> {
            JPanel panel = new JPanel(new GridLayout(0, 1));
            panel.add(new Label("Enter New Item Text"));
            var text = new JTextField(this.newItemText);
            panel.add(text);
            var result = JOptionPane.showConfirmDialog(null, panel, "Добавить",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION){
                this.newItemText = text.getText();
                comboBox.insertItemAt(this.newItemText, 0);
            }
        });
        add(this.bAdd);

        this.bDel = new Button(DelText);
        this.bDel.addActionListener(e -> {
            if (indexItemToDelete.get() < 1) {
                error("Invalid Index to delete (index < 1)!!!");
            } else if (JOptionPane.showConfirmDialog(null,
                    "Delete \" " + comboBox.getItemAt(indexItemToDelete.get()-1) + " \" ?",
                    "", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                comboBox.removeItemAt(indexItemToDelete.get() - 1);
                indexItemToDelete.set(-1);
            }
        });
        add(this.bDel);

        this.bRAll = new Button(RemoveAllText);
        this.bRAll.addActionListener(e -> comboBox.removeAllItems());
        add(this.bRAll);

        add(labelDeleteIndex);
        setVisible(true);
        
        this.setButtonAddText(addText);
        this.setButtonDeleteText(DelText);
        this.setButtonRemoveAllText(RemoveAllText);
    }

    private void error(String e) {
        JOptionPane.showMessageDialog(new JPanel(), e, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
