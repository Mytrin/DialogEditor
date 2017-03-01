package net.sf.ardengine.dialogs.functions;

import com.google.gson.JsonPrimitive;
import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.Dialogs;
import net.sf.ardengine.dialogs.Response;
import net.sf.ardengine.dialogs.TestUtils;
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
    
}
