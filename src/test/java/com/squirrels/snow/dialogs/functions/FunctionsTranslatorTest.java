package com.squirrels.snow.dialogs.functions;

import com.google.gson.JsonPrimitive;
import com.squirrels.snow.dialogs.Dialog;
import com.squirrels.snow.dialogs.Dialogs;
import com.squirrels.snow.dialogs.Response;
import com.squirrels.snow.dialogs.TestUtils;
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
        TestUtils.printDialog(dialog, 1);
        
        Response[] responses = dialog.getAllResponsesArray();
        assertEquals(responses[0].isAvailable(), false);
        assertEquals(responses[1].isAvailable(), true);
        
        int testCount = dialogs.getVariable("function_test:CurrentTest").getAsInt();
        assertEquals(responses[2].isAvailable(), testCount > 20);
        
        testCount++;
        dialogs.setVariable("function_test:CurrentTest", new JsonPrimitive(testCount));
        dialogs.saveVariableChanges("function_test");
    }
    
    @Test
    public void testExecutes() {
        Dialog dialog = dialogs.loadDialog("function_test:execute-test");
        TestUtils.printDialog(dialog, 1);

        int nextTestCount = dialogs.getVariable("function_test:CurrentTest").getAsInt();
        int savedTestCount = dialogs.getVariable("ExecuteTest").getAsInt();
        
        assertEquals(nextTestCount, savedTestCount);
        dialogs.saveAll();
    }
    
    @Test
    public void testVariableEditFunction() {
        //Every test at this dialog is value of $CurrentTest added to itself
        Dialog dialog = dialogs.loadDialog("function_test:variable-edit-test");
        TestUtils.printDialog(dialog, 1);

        int executeTestCount = dialogs.getVariable("ExecuteTest").getAsInt();

        assertTrue(executeTestCount%2 == 0);
        dialogs.saveAll();
    }
    
}
