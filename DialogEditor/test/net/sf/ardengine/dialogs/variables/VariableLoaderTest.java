package net.sf.ardengine.dialogs.variables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import org.junit.Test;
import static org.junit.Assert.*;

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
        loader.getFile("example_project/var_test");
    }

    @Test
    public void testBasicLoading() {
        assertEquals("lorem ipsum", loader.getVariable("example_project/var_test", "test.Text").getAsString());
    }
    
    @Test
    public void testArrayLoading() {
        assertEquals("2.7", loader.getVariable("example_project/var_test", "test.Array[1]").getAsString());
    }
    
    @Test
    public void testMultidimensionalArrayLoading() {
        assertEquals("4", loader.getVariable("example_project/var_test", "test.2DArray[1][1]").getAsString());
    }
    
    @Test
    public void testBasicSaving() {
        System.out.println("\n---------------------");
           
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        System.out.print(gson.toJson(json));
           
        variables.setVariable("cat1.arr1[5]", new JsonPrimitive(Boolean.TRUE));
        
        System.out.print(" -> "); 
        System.out.println(gson.toJson(json));
           
        assertTrue(variables.getVariable("cat1.arr1[5]").getAsBoolean());
    }
    
    @Test
    public void test3DArraySave() {
        System.out.println("\n---------------------");
        
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        System.out.print(gson.toJson(json));
           
        variables.setVariable("cat1.arr1[5][2][0]", new JsonPrimitive(Boolean.TRUE));
        
        System.out.print(" -> ");
        System.out.println(gson.toJson(json));
           
        assertTrue(variables.getVariable("cat1.arr1[5][2][0]").getAsBoolean());
    }
    
    @Test
    public void testNewArraySave() {
        System.out.println("\n---------------------");
        
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        System.out.print(gson.toJson(json));
           
        variables.setVariable("cat1.arr2[2]", new JsonPrimitive(Boolean.TRUE));
        
        System.out.print(" -> "); 
        System.out.println(gson.toJson(json));
           
        assertTrue(variables.getVariable("cat1.arr2[2]").getAsBoolean());
    }
    
    @Test
    public void testNewObjectSave() {
        System.out.println("\n---------------------");
        
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        System.out.print(gson.toJson(json));
           
        variables.setVariable("cat1.subcat1", new JsonPrimitive(Boolean.TRUE));
        
        System.out.print(" -> ");    
        System.out.println(gson.toJson(json));
           
        assertTrue(variables.getVariable("cat1.subcat1").getAsBoolean());
    }    
    
    @Test
    public void testNewObjectInArray() {
        System.out.println("\n---------------------");
        
        JsonObject json = new JsonParser().parse(basicJSON).getAsJsonObject();
           
        LoadedVariables variables = new LoadedVariables(null, json);
           
        System.out.print(gson.toJson(json));
           
        variables.setVariable("cat1.arr1[2].arrcat.subcat1", new JsonPrimitive(Boolean.TRUE));
        
        System.out.print(" -> ");    
        System.out.println(gson.toJson(json));
           
        assertTrue(variables.getVariable("cat1.arr1[2].arrcat.subcat1").getAsBoolean());
    }    
}
