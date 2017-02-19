package net.sf.ardengine.dialogs;

import org.junit.Test;
import static org.junit.Assert.*;

public class DialogsTest {
    
    Dialogs testedDialogs = new Dialogs();
    
    @Test
    public void testBasicLoading() {
        testedDialogs.loadFolder("example_project");
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
        
    }
    
}
