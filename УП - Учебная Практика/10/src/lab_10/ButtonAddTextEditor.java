/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_10;

import java.beans.*;

/**
 *
 * @author Рома
 */
public class ButtonAddTextEditor extends PropertyEditorSupport{
    protected String value; 
    
    @Override
    public String[] getTags() {	return new String[] { "ADD", "add", "add new", "Add new Record" }; }
    
    @Override
    public void setAsText(String s) { 
        this.value = s;
        super.setAsText(s);
    }
    
    @Override
    public String getJavaInitializationString() { return "\"" + value + "\""; }
}
