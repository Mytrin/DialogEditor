package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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
     * To obtain concrete data class, please note JsonElement.getAs().
     * @param variablePath Path to variable within file (Path/to/fileName:object.varName)
     * @return Variable stored in json element.
     * or null, if it does not exists.
     */
    protected JsonElement getVariable(String variablePath){
        JsonElement target =  PathBuilder.getVariable(json, variablePath);
        return target;
    }
        
}
