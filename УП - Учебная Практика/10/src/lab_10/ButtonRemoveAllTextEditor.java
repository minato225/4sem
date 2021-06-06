/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lab_10;

import java.beans.PropertyEditorSupport;

/**
 *
 * @author Рома
 */
public class ButtonRemoveAllTextEditor extends PropertyEditorSupport{
    protected String value; 
    
    @Override
    public String[] getTags() {	return new String[] { "REMOVE", "Remove all", "Purge", "Delte All!" }; }
    
    @Override
    public void setAsText(String s) {
        this.value = s;
        super.setAsText(s); 
    }
    
    @Override
    public String getJavaInitializationString() { return "\"" + value + "\""; }
}
