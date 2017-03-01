package net.sf.ardengine.dialogs.variables;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.stream.JsonWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import net.sf.ardengine.dialogs.DialogEditorException;

/**
 * Represents loaded and built JSON document. 
 * Instances are stored within Variables.
 */
public class LoadedVariables {   
    /**Original file*/
    public final File jsonFile;
    /**Loaded JSON containing dialogs*/
    private final JsonObject json;
    /**True if there has been changes in json*/
    private boolean isModified = false;
    
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
        isModified = true;
    }

    /**
     * @return True if there has been changes in json
     */
    public boolean isModified() {
        return isModified;
    }
    
    /**
    * Saves json of this instance to target file.
    * @param builder Builder to use
    * @param target File to create or overwrite
    */    
    public void save(Gson builder, File target){
        try(FileWriter writer = new FileWriter(target)) {
            builder.toJson(json, new JsonWriter(writer));
        }catch(IOException | JsonIOException e){
            throw new DialogEditorException("Failed to save variable file: ", e);
        }
    }
    
}
