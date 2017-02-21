package net.sf.ardengine.dialogs.variables;

import org.junit.Test;
import static org.junit.Assert.*;

public class VariableLoaderTest {
    
    VariableLoader loader = new VariableLoader();

    public VariableLoaderTest() {
        loader.getFile("example_project/var_test");
    }

    @Test
    public void testBasicLoading() {
        assertEquals("lorem ipsum", loader.getVariable("example_project/var_test:test.Text"));
    }
    
    @Test
    public void testArrayLoading() {
        assertEquals("2.7", loader.getVariable("example_project/var_test:test.Array[1]"));
    }
    
    @Test
    public void testMultidimensionalArrayLoading() {
        assertEquals("4", loader.getVariable("example_project/var_test:test.2DArray[1][1]"));
    }
    
}
