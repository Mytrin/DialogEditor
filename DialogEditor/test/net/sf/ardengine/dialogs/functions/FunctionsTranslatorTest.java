/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.ardengine.dialogs.functions;

import com.google.gson.JsonPrimitive;
import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.Dialogs;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mytrin
 */
public class FunctionsTranslatorTest {
    
    Dialogs dialogs = new Dialogs();
    
    public FunctionsTranslatorTest() {
        dialogs.loadFolder("example_project");
    }

    @Test
    public void testBasicConditions() {
        Dialog dialog = dialogs.loadDialog("function_test:function-test");
        
        System.out.println(dialog.getEvent().getText());
        
        int testCount = dialogs.getVariable("function_test:CurrentTest").getAsInt() + 1;
        dialogs.setVariable("function_test:CurrentTest", new JsonPrimitive(testCount));
        dialogs.saveVariableChanges("function_test");
    }
    
}
