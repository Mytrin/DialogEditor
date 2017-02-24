package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Arrays;
import net.sf.ardengine.dialogs.DialogEditorException;

/**
 * This class contains variable access and creation methods.
 */
public class PathBuilder {
    
    /**Singleton*/
    private PathBuilder(){};
    
    private static final String CATEGORY_DELIMITER = "\\.";
    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String ARRAY_START_REGEX = "\\[";
    
    /**
     * @param json Source json object
     * @param variablePath Path to variable within file (Path/to/fileName:object.varName)
     * @return Variable stored in json in String format 
     * or null, if it does not exists.
     */
    public static JsonElement getVariable(JsonObject json, String variablePath){
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
                
                int[] indexes = parseArrayIndexes(part, indexStart);
                                
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
       
        return currentElement;
    }
    
    //arrayName[2][4][5] -> new int[]{2, 4, 5};
    private static int[] parseArrayIndexes(String arrayPath, int indexStart){
        String[] pathSplit = arrayPath.substring(indexStart, arrayPath.length() - 1)
                        .replaceAll(ARRAY_START_REGEX, "").split(ARRAY_END);

        int[] indexes = new int[pathSplit.length];
        
        for(int i=0; i <pathSplit.length; i++){
            indexes[i] = Integer.parseInt(pathSplit[i]);
        }
        
        return indexes;
    }
    
}
