package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.io.File;

/**
 * Represents loaded and built JSON document. 
 * Instances are stored within Variables.
 */
public class LoadedVariables {   
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
     * @return Variable stored in json element.
     * or null, if it does not exists.
     */
    public JsonPrimitive getVariable(String variablePath){
        JsonPrimitive target =  PathBuilder.getVariable(json, variablePath);
        return target;
    }
    
    /**
     * If some objects or array specified by variablePath are missing, they will be created.
     * Does not check if value is already present!
     * 
     * @param variablePath Path to variable within file (Path/to/fileName:object.varName)
     * @param newValue Value of JsonPrimitive identified by given path
     */
    public void setVariable(String variablePath, JsonPrimitive newValue){
          PathBuilder.setVariable(json, variablePath, newValue);
    }
        
}
