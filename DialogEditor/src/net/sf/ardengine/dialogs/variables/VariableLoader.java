package net.sf.ardengine.dialogs.variables;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ardengine.dialogs.DialogEditorException;
import net.sf.ardengine.dialogs.Dialogs;

/**
 * Provides methods for interaction with files containing variables.
 * 
 * Variables are stored within json files. These files are loaded when 
 * their inner variable is request into instance of LoadedVariables class.
 */
public class VariableLoader {  
    /**Format of dialog files*/
    public static final String VARIABLE_FORMAT = ".json"; //todo config
    
    /**Already loaded documents identified by path to source file*/
    private final HashMap<String, LoadedVariables> loadedDocuments = new HashMap<>();
    
    /**JSON Parser*/
    private final JsonParser gsonParser = new JsonParser();
    
    /**JSON builder*/
    private final Gson gsonBuilder = new GsonBuilder().create();
    
    /**
     * Removes all loaded JSON variables from HashMap, exposing them to GC.
     */
    public void clear(){
        loadedDocuments.clear();
    }

    /**
     * Obtains requested file from HashMap or loads it, if not present.
     * @param filePath Path to JSON file
     * @return loaded variables
     */
    public LoadedVariables getFile(String filePath){
        LoadedVariables loadedDocument;
        
        String realPath = filePath+VARIABLE_FORMAT;
        
        if(loadedDocuments.containsKey(realPath)){
            loadedDocument = loadedDocuments.get(realPath);
        }else{
            try{
                loadedDocument = loadFile(realPath);
            }catch(Exception e){
                throw new DialogEditorException("File at "+realPath+" could not be loaded!", e);
            }
        }

        return loadedDocument;
    }
        
    /**
     * Loads given JSON file to memory and builds it.
     * @param filePath Path to JSON file
     */
    private LoadedVariables loadFile(String filePath){
        File jsonFile = new File(filePath);
        JsonObject loadedJson;
        LoadedVariables variables;
        
        if(jsonFile.exists()){
            try{
                loadedJson = gsonParser.parse(new FileReader(jsonFile)).getAsJsonObject();
                variables = new LoadedVariables(jsonFile, loadedJson);
                loadedDocuments.put(filePath,variables);
                
                return variables;
            }catch(JsonIOException | JsonSyntaxException e){
                //File does not corresponds with XML format, inform user trough logger
                Logger.getLogger(Dialogs.class.getName()).log(Level.SEVERE, 
                        "Loaded file contains corrupted XML: {0}", e);
                
                throw new DialogEditorException("Error while loading file "+filePath, e);
            }catch(FileNotFoundException ex){ //Should not ever happen.
                throw new DialogEditorException("Error while loading file "+filePath, ex);
            }
        }else{
            throw new DialogEditorException("Loaded path "+filePath+" does not exist!");
        }
    }
    
    /**
     * Saves loaded JSON file identified by path back to its original location.
     * @param filePath Path to JSON file
     */
    public void saveFile(String filePath){
        File targetFile = new File(filePath);
        
        LoadedVariables variables = loadedDocuments.get(filePath);
        
        if(variables == null){
            throw new DialogEditorException(filePath+" is not loaded!");
        }
        
        if(variables.isModified() || !targetFile.exists()){
           variables.save(gsonBuilder, targetFile);
        }
    }
    
    /**
     * Saves all modified JSON files.
     */
    public void saveAll(){
        for(LoadedVariables variables : loadedDocuments.values()){
            if(variables.isModified()){
                variables.save(gsonBuilder, variables.jsonFile);
            }
        }
    }

    /**
     * To obtain concrete data class, please note JsonElement.getAs().
     * Example: Path/to/fileName:object.varName
     * @param filePath path to file within loaded folder (Path/to/fileName)
     * @param variablePath Path to variable within file (object.varName)
     * @return Variable stored in json element.
     * or null, if it does not exists.
     */
    public  JsonPrimitive getVariable(String filePath, String variablePath){
        try{
            LoadedVariables variables;
            
            if(filePath != null){
                variables = getFile(filePath);

                return variables.getVariable(variablePath);
            }else{
                //todo GLOBALS
            }
        }catch(Exception e){
            Logger.getLogger(VariableLoader.class.getName())
                        .log(Level.WARNING, "Failed when obtaining variable {0} because {1}",
                                new Object[]{variablePath, e});
        }
        return null;
    }
    
    /**
     * If some objects or array specified by variablePath are missing, they will be created.
     * Does not check if value is already present!
     * Example: Path/to/fileName:object.varName
     * @param filePath path to file within loaded folder (Path/to/fileName)
     * @param variablePath Path to variable within file (object.varName)
     * @param newValue Value of JsonPrimitive identified by given path
     */
    public void setVariable(String filePath, String variablePath, JsonPrimitive newValue){
        try{
            if(filePath != null){
                LoadedVariables variables = getFile(filePath);
            
                variables.setVariable(variablePath, newValue);
            }else{
                //todo GLOBALS
            }
        }catch(Exception e){
            Logger.getLogger(VariableLoader.class.getName())
                .log(Level.WARNING, "Failed when creating variable {0} because {1}",
                    new Object[]{variablePath, e});
        }
    }
}
