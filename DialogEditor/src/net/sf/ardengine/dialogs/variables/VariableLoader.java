package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
    JsonParser gsonParser = new JsonParser();
    
    /**
     * Removes all loaded JSON variables from hashmap, exposing them to GC.
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
     * @param variablePath Path to variable (Path/to/fileName:object.varName)
     * @return Variable stored in json in String format 
     * or null, if it does not exists.
     */
    public String getVariable(String variablePath){
        if(variablePath.contains(Dialogs.PATH_DELIMITER)){
            
            String[] pathSplit = variablePath.split(Dialogs.PATH_DELIMITER);
            
            LoadedVariables variables = getFile(pathSplit[0]);
            String varValue = null;
            
            try{
                varValue = variables.getVariable(pathSplit[1]);
            }catch(Exception e){
                Logger.getLogger(VariableLoader.class.getName())
                        .log(Level.WARNING, "Failed when obtaining variable {0} because {1}",
                                new Object[]{variablePath, e});
            }
            
            return varValue;
        }else{
            //todo GLOBALS
            return null;
        }
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
}
