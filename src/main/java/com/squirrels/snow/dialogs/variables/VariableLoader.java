package com.squirrels.snow.dialogs.variables;

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

import com.squirrels.snow.dialogs.AResourceLoader;
import com.squirrels.snow.dialogs.DialogEditorException;
import com.squirrels.snow.dialogs.Dialogs;
import javafx.util.Pair;

/**
 * Provides methods for interaction with files containing variables.
 * 
 * Variables are stored within json files. These files are loaded when 
 * their inner variable is request into instance of LoadedVariables class.
 */
public class VariableLoader extends AResourceLoader<LoadedVariables> {
    /**Format of dialog files*/
    public static final String VARIABLE_FORMAT = ".json"; //todo config
    /**Path to globals file for Variables*/
    private static final String GLOBALS_LOCATION = "globals"; //todo config
    
    /**Already loaded documents identified by path to source file*/
    private final HashMap<String, LoadedVariables> loadedDocuments = new HashMap<>();
    
    /**JSON Parser*/
    private final JsonParser gsonParser = new JsonParser();
    
    /**JSON builder with reasonable format*/
    private final Gson gsonBuilder = new GsonBuilder().setPrettyPrinting().create();
    
    /**File with variable with unspecified file path*/
    private LoadedVariables globals;
        
    @Override
    public void clear(){
        super.clear();
        loadedDocuments.clear();
        globals = null;
    }

    
    @Override
    public void setProjectPath(String folderPath) {
        super.setProjectPath(folderPath);
        globals = getFile(GLOBALS_LOCATION);
    }    
    

    @Override
    public LoadedVariables getFile(String filePath){
        LoadedVariables loadedDocument;

        if(loadedDocuments.containsKey(filePath)){
            loadedDocument = loadedDocuments.get(filePath);
        }else{
            try{
                loadedDocument = loadFile(filePath);
            }catch(Exception e){
                throw new DialogEditorException("Resource "+filePath+" could not be loaded!", e);
            }
        }

        return loadedDocument;
    }
        
    @Override
    protected LoadedVariables loadFile(String filePath){
        File jsonFile = new File(currentProjectPath+filePath+VARIABLE_FORMAT);
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
                        "Loaded file contains corrupted JSON: {0}", e);
                
                throw new DialogEditorException("Error while loading file "+filePath, e);
            }catch(FileNotFoundException ex){ //Should not ever happen.
                throw new DialogEditorException("Error while loading file "+filePath, ex);
            }
        }else{
            throw new DialogEditorException("Loaded path "+filePath+" does not exist!");
        }
    }
    
    @Override
    public void saveFile(String filePath){       
        LoadedVariables variables = loadedDocuments.get(filePath);
        File targetFile = variables.jsonFile;
        
        if(variables == null){
            throw new DialogEditorException(filePath+" is not loaded!");
        }
        
        if(variables.isModified() || !targetFile.exists()){
           variables.save(gsonBuilder, targetFile);
        }
    }
    
    @Override
    public void saveAll(){
        for(LoadedVariables variables : loadedDocuments.values()){
            if(variables.isModified()){
                variables.save(gsonBuilder, variables.jsonFile);
            }
        }
        globals.save(gsonBuilder, globals.jsonFile);
    }

    /**
     * To obtain concrete data class, please note JsonElement.getAs().
     * Example: Path/to/fileName:object.varName
     * @param completePath complete variable path (Path/to/fileName:object.varName)
     * @return Variable stored in json element.
     * or null, if it does not exists.
     */
    public  JsonPrimitive getVariable(String completePath){
        Pair<String, String> pathInfo = parsePath(completePath);
        String filePath = pathInfo.getKey();
        String variablePath = pathInfo.getValue();
        
        try{
            LoadedVariables variables;
            
            if(filePath != null){
                variables = getFile(filePath);

                return variables.getVariable(variablePath);
            }else{
                if(globals != null){
                    return globals.getVariable(variablePath);
                }else{
                    throw new DialogEditorException("Global variables file is not set!");
                }
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
     * @param completePath complete variable path (Path/to/fileName:object.varName)
     * @param newValue Value of JsonPrimitive identified by given path
     */
    public void setVariable(String completePath, JsonPrimitive newValue){
        Pair<String, String> pathInfo = parsePath(completePath);
        String filePath = pathInfo.getKey();
        String variablePath = pathInfo.getValue();
        
        try{
            if(filePath != null){
                LoadedVariables variables = getFile(filePath);
            
                variables.setVariable(variablePath, newValue);
            }else{
                if(globals != null){
                    globals.setVariable(variablePath, newValue);
                }else{
                    throw new DialogEditorException("Global variables file is not set!");
                }
            }
        }catch(Exception e){
            Logger.getLogger(VariableLoader.class.getName())
                .log(Level.WARNING, "Failed when creating variable {0} because {1}",
                    new Object[]{variablePath, e});
        }
    }
    
}
