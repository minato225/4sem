/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_10;

import java.beans.SimpleBeanInfo;
import java.beans.*;

/**
 *
 * @author Рома
 */
public class MyComboBoxBeanInfo extends SimpleBeanInfo {
    
    @Override
    public BeanDescriptor getBeanDescriptor() {
	return new BeanDescriptor(MyComboBox.class);
    }
    
    /** This is a convenience method for creating PropertyDescriptor objects */
    static PropertyDescriptor prop(String name, String description) {
	try {
	    PropertyDescriptor p = new PropertyDescriptor(name, MyComboBox.class);
	    p.setShortDescription(description);
	    return p;
	}
	catch(IntrospectionException e) { return null; } 
    }
    
    static PropertyDescriptor[] props = {
	prop("ButtonAddText", "add new record"),
	prop("ButtonDeleteText", "delete by ID"),
	prop("ButtonRemoveAllText", "Remove all records"),
    };
    
    static {
	props[0].setPropertyEditorClass(ButtonAddTextEditor.class);
        props[1].setPropertyEditorClass(ButtonDeleteTextEditor.class);
        props[2].setPropertyEditorClass(ButtonRemoveAllTextEditor.class);
    }
    
    @Override
    public PropertyDescriptor[] getPropertyDescriptors() { return props; }
    
    @Override
    public int getDefaultPropertyIndex() { return 0; }
}
