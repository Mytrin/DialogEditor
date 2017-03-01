package net.sf.ardengine.dialogs.variables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Once I killed an hour trying to figure, why is every test loader HashMap empty...
 * Turns out JUnit runs constructor before every test.
 * 
 * @author mytrin
 */
public class VariableLoaderTest {
    
    Gson gson = new GsonBuilder().create();
    
    VariableLoader loader = new VariableLoader();
    
    String basicJSON = "{"
                        +"\"cat1\": {"
                                    +"\"arr1\": ["
                                                   +"\"String1\", "
                                                   +"42"
                                              +"]"
                                    +"}"
                       +"}";


    public VariableLoaderTest() {
        //Please not that these test are not using proper way trough Dialogs.get*
        loader.setProjectPath("example_project");
        loader.getFile("var_test");
    }

    @Test
    public void testBasicLoading() {
        assertEquals("lorem ipsum", loader.getVariable("var_test:test.Text").getAsString());
    }
    
    @Test
    public void testArrayLoading() {
        assertEquals("2.7", loader.getVariable("var_test:test.Array[1]").getAsString());
    }
    
    @Test
    public void testMultidimensionalArrayLoading() {
        assertEquals("4", loader.getVariable("var_test:test.2DArray[1][1]").getAsString());
    }
    
    @Test
    public void testBasicSaving() {
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject(); 
        LoadedVariables variables = new LoadedVariables(null, json);
           
        String oldJson = gson.toJson(json);
           
        variables.setVariable("cat1.arr1[5]", new JsonPrimitive(Boolean.TRUE));
        
        String newJson = gson.toJson(json);
           
        assertTrue(variables.getVariable("cat1.arr1[5]").getAsBoolean());
        
        printJsonChange(oldJson, newJson);
    }
    
    @Test
    public void test3DArraySave() {
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        String oldJson = gson.toJson(json);
           
        variables.setVariable("cat1.arr1[5][2][0]", new JsonPrimitive(Boolean.TRUE));
        
        String newJson = gson.toJson(json);
           
        assertTrue(variables.getVariable("cat1.arr1[5][2][0]").getAsBoolean());
        
        printJsonChange(oldJson, newJson);
    }
    
    @Test
    public void testNewArraySave() {
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        String oldJson = gson.toJson(json);
           
        variables.setVariable("cat1.arr2[2]", new JsonPrimitive(Boolean.TRUE));
        
        String newJson = gson.toJson(json);
           
        assertTrue(variables.getVariable("cat1.arr2[2]").getAsBoolean());
        
        printJsonChange(oldJson, newJson);
    }
    
    @Test
    public void testNewObjectSave() {
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
        LoadedVariables variables = new LoadedVariables(null, json);
           
        String oldJson = gson.toJson(json);
           
        variables.setVariable("cat1.subcat1", new JsonPrimitive(Boolean.TRUE));
        
        String newJson = gson.toJson(json);
           
        assertTrue(variables.getVariable("cat1.subcat1").getAsBoolean());
        
        printJsonChange(oldJson, newJson);
    }    
    
    @Test
    public void testNewObjectInArray() {
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
        LoadedVariables variables = new LoadedVariables(null, json);
           
        String oldJson = gson.toJson(json);
           
        variables.setVariable("cat1.arr1[2].arrcat.subcat1", new JsonPrimitive(Boolean.TRUE));
        
        String newJson = gson.toJson(json);
        
        assertTrue(variables.getVariable("cat1.arr1[2].arrcat.subcat1").getAsBoolean());
        
        printJsonChange(oldJson, newJson);
    }    
    
    private void printJsonChange(String oldJson, String newJson){
        System.out.println("\n---------------------");
        
        System.out.println(oldJson+" -> "+newJson);
    }
}
