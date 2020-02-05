package com.squirrels.snow.dialogs;

import org.junit.Test;
import static org.junit.Assert.*;

public class DialogsTest {
    
    Dialogs testedDialogs = new Dialogs();

    public DialogsTest() {
        testedDialogs.loadFolder("example_project");
    }

    @Test
    public void testBasicLoading() {
        Dialog loadedDialog = testedDialogs.loadDialog("simple_test:easy-test");
        
        TestUtils.printDialog(loadedDialog, 1);
        
        Response[] responses = testedDialogs.getActiveDialog().getAvailableResponsesArray();
        assertEquals(responses.length, 2);
        
        testedDialogs.selectResponse(responses[1]);
        assertNull(testedDialogs.getActiveDialog());
    }
    
        @Test
    public void testNextStepLoading() {
        Dialog loadedDialog = testedDialogs.loadDialog("step_test:step-test");
        
        TestUtils.printDialog(loadedDialog, 0);
        
        Response[] availableResponses = loadedDialog.getAvailableResponsesArray();
        loadedDialog = testedDialogs.selectResponse(availableResponses[0]);
        
        TestUtils.printDialog(loadedDialog, 0);
        
        assertNotNull(loadedDialog);
        
        availableResponses = loadedDialog.getAvailableResponsesArray();
        loadedDialog = testedDialogs.selectResponse(availableResponses[0]);
        
        assertNull(loadedDialog);
    }
    
    @Test
    public void testBasicVariableLoading() {
        Dialog loadedDialog = testedDialogs.loadDialog("simple_test:easy-variable-test");
        
        TestUtils.printDialog(loadedDialog, 0);
        
        String expectedDialogEventText = testedDialogs.getVariable("Test1").getAsString();
        String dialogEventText = testedDialogs.getActiveDialog().getEvent().getText();

        assertEquals(expectedDialogEventText, dialogEventText);
    }
        
}
