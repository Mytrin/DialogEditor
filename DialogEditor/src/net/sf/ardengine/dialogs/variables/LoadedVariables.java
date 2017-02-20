package net.sf.ardengine.dialogs.variables;

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
     * @param variablePath Path to variable within file
     * @return Variable stored in json in String format 
     * or null, if it does not exists.
     */
    public String getVariable(String variablePath){
        String[] path = variablePath.split(CATEGORY_DELIMITER);
        
        JsonElement currentElement = json;
        
        for (String part : path) {
            if (part.endsWith(ARRAY_END)) {
                //parse number in "varName[XX]"
                int indexStart = part.indexOf(ARRAY_START);
                String categoryName = part.substring(0, indexStart);
                int index = Integer.parseInt(part.substring(indexStart+1, part.length() - 1));
                if(!currentElement.getAsJsonObject().has(categoryName)){
                    throw new DialogEditorException("Accesing non existing array variable: "+categoryName);
                }
                
                currentElement = currentElement.getAsJsonObject().get(categoryName);
                
                //array check
                if(!currentElement.isJsonArray()){
                    throw new DialogEditorException("Accesing non array variable as array!");
                }
                
                currentElement = currentElement.getAsJsonArray().get(index);
            } else {
                if (!currentElement.getAsJsonObject().has(part)) {
                    throw new DialogEditorException("Accesing non existing variable: " + part);
                }
                currentElement = currentElement.getAsJsonObject().get(part);
            }
        }
                    
        return currentElement.getAsString();
    }
    
}
