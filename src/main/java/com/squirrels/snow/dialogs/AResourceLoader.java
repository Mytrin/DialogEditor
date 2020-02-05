package com.squirrels.snow.dialogs;

import java.io.File;
import javafx.util.Pair;

/**
 * Common methods for VariableLoader and DocumentCache. 
 * @param <T> Type of loaded resource
 */
public abstract class AResourceLoader<T> {
    
    /**The char between file path and ID*/
    public static final String PATH_DELIMITER = ":";
    
    /**Path to current folder with dialog files*/
    protected String currentProjectPath;
        
    /**
     * Removes all loaded files from hashmap, exposing them to GC.
     */
    public void clear(){
        currentProjectPath = null;
    }
    
    /**
     * Clears resources and discards unsaved changes from previous project and loads new.
     * @param folderPath 
     */
    public void setProjectPath(String folderPath){
        clear();
        currentProjectPath = folderPath+File.separator;
    }
    
    /**
     * Obtains requested file from HashMap or loads it, if not present.
     * @param filePath Path to file (without suffix)
     * @return Instance of loaded file 
     */
    public abstract T getFile(String filePath);
    
    /**
     * Loads given file to memory and builds it.
     * @param filePath Path to file
     * @return loaded file or null, if fails
     */
    protected abstract T loadFile(String filePath);
    
    /**
     * Saves loaded file identified by path back to its original location.
     * @param filePath Path to file
     */
    public abstract void saveFile(String filePath);
    
    /**
     * Saves all modified files.
     */
    public abstract void saveAll();
    
    //File, ID
    protected Pair<String, String> parsePath(String path){
        String filePath = null;
        String dialogID = path;
        
        if(path.contains(PATH_DELIMITER)){
            String[] pathSplit = path.split(PATH_DELIMITER);
            filePath = pathSplit[0];
            dialogID = pathSplit[1];
        }
        
        return new Pair<>(filePath, dialogID);
    }
}
