package net.sf.ardengine.dialogs;

import org.junit.Test;
import static org.junit.Assert.*;

public class DialogsTest {
    
    Dialogs testedDialogs = new Dialogs();

    public DialogsTest() {
        testedDialogs.loadFolder("example_project");
    }

    @Test
    public void testBasicLoading() {
        testedDialogs.loadDialog("simple_test:easy-test");
        
        System.out.println(testedDialogs.getActiveDialog().getEvent().getText());
        
        Response[] responses = testedDialogs.getActiveDialog().getAvailableResponsesArray();
        
        for(Response r : responses) {
            System.out.println("R:  "+r.getText());
        }

        assertEquals(responses.length, 2);

        System.out.println("-> "+responses[1].getText());
        
        testedDialogs.selectResponse(responses[1]);
        
        assertNull(testedDialogs.getActiveDialog());
        
        System.out.println("----------------------------");
    }
    
    @Test
    public void testBasicVariableLoading() {
        testedDialogs.loadDialog("simple_test:easy-variable-test");
        String expectedDialogEventText = testedDialogs.getVariable("Test1").getAsString();
        String dialogEventText = testedDialogs.getActiveDialog().getEvent().getText();
        
        System.out.println(dialogEventText);

        Response[] responses = testedDialogs.getActiveDialog().getAvailableResponsesArray();
        
        for(Response r : responses) {
            System.out.println("R:  "+r.getText());
        }

        System.out.println("-> "+responses[0].getText());
        
        testedDialogs.selectResponse(responses[0]);
        
        assertEquals(expectedDialogEventText, dialogEventText);
        
        System.out.println("----------------------------");
    }
    
}
