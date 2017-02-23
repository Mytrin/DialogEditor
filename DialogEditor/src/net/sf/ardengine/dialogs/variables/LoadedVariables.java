package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.File;
import net.sf.ardengine.dialogs.DialogEditorException;

/**
 * Represents loaded and built JSON document. 
 * Instances are stored within Variables.
 */
public class LoadedVariables {
    public static final String CATEGORY_DELIMITER = "\\.";
    public static final String ARRAY_START = "[";
    public static final String ARRAY_END = "]";
    public static final String ARRAY_START_REGEX = "\\[";
    
    /**Original file*/
    public final File jsonFile;
    /**Loaded JSON containing dialogs*/
    private final JsonObject json;

    /**
     * Represents loaded and built JSON document. 
     * @param jsonFile Original file
     * @param json Loaded JSON containing dialogs
     */
    public LoadedVariables(File jsonFile, JsonObject json) {
        this.jsonFile = jsonFile;
        this.json = json;
    }
    
    /**
     * @param variablePath Path to variable within file (Path/to/fileName:object.varName)
     * @return Variable stored in json in String format 
     * or null, if it does not exists.
     */
    public String getVariable(String variablePath){
        String[] path = variablePath.split(CATEGORY_DELIMITER);
        
        JsonElement currentElement = json;
        
        for (String part : path) {
            if (part.endsWith(ARRAY_END)) {
                //parse number in "varName[XX][YY]"
                int indexStart = part.indexOf(ARRAY_START);
                String categoryName = part.substring(0, indexStart);
                
                if(!currentElement.getAsJsonObject().has(categoryName)){
                    throw new DialogEditorException("Accesing non existing array variable: "+categoryName);
                }
                
                currentElement = currentElement.getAsJsonObject().get(categoryName);
                
                int[] indexes = parseArrayIndexes(categoryName, indexStart);
                                
                for(int index : indexes){                   
                    //array check
                    if(!currentElement.isJsonArray()){
                        throw new DialogEditorException("Accesing non array variable as array!");
                    }
                    
                    currentElement = currentElement.getAsJsonArray().get(index);
                }
            } else {
                if (!currentElement.getAsJsonObject().has(part)) {
                    throw new DialogEditorException("Accesing non existing variable: " + part);
                }
                currentElement = currentElement.getAsJsonObject().get(part);
            }
        }
        //todo            
       
        return currentElement.getAsString();
    }
    
    /**
     * @param variablePath Path to variable within file. 
     * Missing objects will be created
     * @param value New value, Unknown objects will be converted to String.
     */
    
    
    private JsonElement createVariablePath(String variablePath, Object value){
        String[] path = variablePath.split(CATEGORY_DELIMITER);
        
        JsonElement currentElement = json;
        //Test/Pole[1][2]/Text
        for (int i=0; i < path.length -1; i++) {
            if (path[i].contains(ARRAY_START)) {
                //element is an array
                currentElement = walkThroughArray(currentElement.getAsJsonObject(), 
                        path[i]);
            } else {
                if(currentElement.getAsJsonObject().has(path[i])){
                    currentElement = currentElement.getAsJsonObject().get(path[i]);
                }else{
                    JsonElement newElement = new JsonObject();
                    currentElement.getAsJsonObject().add(path[i], newElement);
                    currentElement = newElement;
                }
            }
        }
        
        //creating final element
        if(path[path.length -1].contains(ARRAY_START)){
                currentElement = walkThroughArray(currentElement.getAsJsonObject(), 
                        path[path.length -1]);
                int indexStart = path[path.length -1].indexOf(ARRAY_START);
                String arrayName = path[path.length -1].substring(0, indexStart);
                
                
                
                return currentElement;
        }else{
           return currentElement;
        }
    }
    
    private int[] parseArrayIndexes(String arrayPath, int indexStart){
        String[] pathSplit =arrayPath.substring(indexStart, arrayPath.length() - 1)
                        .replaceAll(ARRAY_START_REGEX, "").split(ARRAY_END);
        
        int[] indexes = new int[pathSplit.length];
        
        for(int i=0; i <pathSplit.length; i++){
            indexes[i] = Integer.parseInt(pathSplit[i]);
        }
        
        return indexes;
    }
    
    private JsonElement walkThroughArray(JsonObject ancestor, String path){
        int indexStart = path.indexOf(ARRAY_START);
        String arrayName = path.substring(0, indexStart);
        int[] arrayPath = parseArrayIndexes(path, indexStart);
        
        JsonArray array;
        if(!ancestor.has(arrayName)){ //create array, if missing
            ancestor.add(arrayName, new JsonArray());
        }
        array = ancestor.getAsJsonArray(arrayName);
        
        for(int i=0; i<arrayPath.length-1; i++){ //Last element might not be array
            //create missing dimensions
            for(int missingI= array.size(); missingI< arrayPath[i]+1; missingI++){
                array.set(missingI, new JsonArray());
            }

            array = array.get(arrayPath[i]).getAsJsonArray();
        }

        //walkThroughArray() is not used for last element in setVariable() path, 
        //so we can safely presume that last dimension is full of JsonObjects
        for(int missingI= array.size(); missingI< arrayPath[arrayPath.length-1]+1; missingI++){
            array.set(missingI, new JsonObject());
        }
        
        return array.get(arrayPath[arrayPath.length-1]);
    }
    
}
