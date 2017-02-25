package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.function.Supplier;
import javafx.util.Pair;
import net.sf.ardengine.dialogs.DialogEditorException;

/**
 * This class contains variable access and creation methods.
 */
public class PathBuilder {
    
    /**Singleton*/
    private PathBuilder(){};
    
    private static final String CATEGORY_DELIMITER = ".";
    private static final String CATEGORY_DELIMITER_REGEX = "\\.";
    private static final String ARRAY_START = "[";
    private static final String ARRAY_END = "]";
    private static final String ARRAY_START_REGEX = "\\[";
    
    private static final Supplier<JsonElement> JSON_ARRAY_FILLER = () -> new JsonArray();
    private static final Supplier<JsonElement> JSON_OBJECT_FILLER = () -> new JsonObject();
    
    /**
     * @param json Source json object
     * @param variablePath Path to variable within file (object.varName.arrayName[0][1])
     * @return Variable stored in json in String format 
     * or null, if it does not exists.
     */
    public static JsonPrimitive getVariable(JsonObject json, String variablePath){
        String[] path = variablePath.split(CATEGORY_DELIMITER_REGEX);
        
        JsonElement currentElement = json;
        
        for (String part : path) {
            if (isArrayPart(part)) {
                //parse number in "varName[XX][YY]"
                Pair<String, int[]> arrayInfo = parseArrayInfo(part);
                String categoryName = arrayInfo.getKey();
                int[] indexes = arrayInfo.getValue();
                
                if(!currentElement.getAsJsonObject().has(categoryName)){
                    throw new DialogEditorException("Accesing non existing array variable: "+categoryName);
                }
                
                currentElement = currentElement.getAsJsonObject().get(categoryName);
                                                
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
       
        return currentElement.getAsJsonPrimitive();
    }
    
    /**
     * @param json Source json object
     * @param variablePath Path to variable within file (object.varName.arrayName[0][1])
     * @param newValue New value of primitive identified by the given path
     */
    public static void setVariable(JsonObject json, String variablePath, JsonPrimitive newValue){
        JsonElement target = getOrCreatePath(json, variablePath);
        
        if(target.isJsonArray()){
            String lastIndexStr = variablePath.substring(
                    variablePath.lastIndexOf(ARRAY_START)+1, variablePath.length()-1);
            int lastIndex = Integer.parseInt(lastIndexStr);
            
            target.getAsJsonArray().set(lastIndex, newValue);
        }else if(target.isJsonObject()){
            String lastName = variablePath.substring(variablePath.lastIndexOf(CATEGORY_DELIMITER)+1);
            target.getAsJsonObject().add(lastName, newValue);
        }else{
            throw new DialogEditorException("Path "+variablePath+" led to "
                    + "JsonObject Primitive instead of JsonObject or JsonArray!");
        }
    }
    
    /**
     * objectName.ArrayName[2][4][5].anotherObject.ArrayName2[0][1] returns element of ArrayName2[0]
     * objectName.ArrayName[2][4][5].anotherObject.varName return element of anotherObject
     * 
     * @param json Source json object
     * @param variablePath Path to variable within file (object.varName.arrayName[0][1])
     * @return element of last array in variablePath or of last but one object 
     */
    private static JsonElement getOrCreatePath(JsonObject json, String variablePath){
        String[] path = variablePath.split(CATEGORY_DELIMITER_REGEX);
        JsonElement currentElement = json;
        
        for(int i=0; i<path.length; i++){
            String pathPart = path[i];
        
            if (isArrayPart(pathPart)) {
                Pair<String, int[]> arrayInfo = parseArrayInfo(pathPart);
                String arrayName = arrayInfo.getKey();
                int[] indexes = arrayInfo.getValue();
            
                JsonArray array = getOrCreateLastArray(currentElement.getAsJsonObject(), arrayName, indexes);

                fillArray(array, indexes[indexes.length-1], JSON_OBJECT_FILLER);
                
                if(i+1 < path.length){ //path continues
                    currentElement = array.get(indexes[indexes.length-1]);
                }else{ //path ends, let the calling function handle what to insert
                    currentElement = array;
                }
            }else{
                if(i+1 < path.length){ //path continues
                    currentElement = getOrCreateElement(currentElement.getAsJsonObject(), 
                            pathPart, JSON_OBJECT_FILLER);
                }
            }
        }
        
        return currentElement;
    }

    private static boolean isArrayPart(String partName){
        return partName.endsWith(ARRAY_END);
    }
    
    /**
     * @param json source element
     * @param arrayName property with array
     * @param indexes path to element, missing data in array on path to index will be filled with empty data.
     *  Last index is ignored!
     * @return Last array in multidimensional array specified by path
     */
    private static JsonArray getOrCreateLastArray(JsonObject json, String arrayName, int[] indexes){
        JsonArray array;
        
        array = getOrCreateElement(json, arrayName, JSON_ARRAY_FILLER).getAsJsonArray();
        
        for(int i=0; i<indexes.length-1; i++){
            fillArray(array, indexes[i], JSON_ARRAY_FILLER);
            array = array.get(indexes[i]).getAsJsonArray();
        }

        return array;
    }
    
    //get specified element or create and append it if missing
    private static JsonElement getOrCreateElement(JsonObject json, String elementName, Supplier<JsonElement> fillGenerator){
        JsonElement returnedElement;
        
        if(!json.has(elementName)){
            returnedElement = fillGenerator.get();
            json.add(elementName, returnedElement);
        }else{
            returnedElement = json.get(elementName);
        }
        
        return returnedElement;
    }
    
    //fill missing elements so lastIndex is valid
    private static void fillArray(JsonArray array, int lastIndex, Supplier<JsonElement> fillGenerator){
        if(array.size() <= lastIndex){
            for(int j=array.size(); j <= lastIndex; j++){
                array.add(fillGenerator.get());
            }
        }
    }
    
    private static Pair<String, int[]> parseArrayInfo(String part){
        int indexStart = part.indexOf(ARRAY_START);
        String categoryName = part.substring(0, indexStart);
        int[] indexes = parseArrayIndexes(part, indexStart);

        return new Pair<>(categoryName, indexes);
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
