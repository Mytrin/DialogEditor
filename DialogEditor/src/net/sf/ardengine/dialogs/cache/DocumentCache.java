package net.sf.ardengine.dialogs.cache;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ardengine.dialogs.DialogEditorException;
import net.sf.ardengine.dialogs.Dialogs;
import org.jdom2.Document;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 * XML document loader. Loaded documents are stored in Hashmap to prevent 
 * constant reloading when changing files.
 */
public class DocumentCache {
    /**
     * Change to increase space for loaded files (2048 - 2MB default)
     * When loading large files, CACHE_SIZE might be ignored!
     */
    public static long CACHE_SIZE = 2048; //todo config
    
    /**Already loaded documents identified by path to source file*/
    private final HashMap<String, LoadedDocument> loadedDocuments = new HashMap<>();

    /**JDOM Parser*/
    private final SAXBuilder builder = new SAXBuilder();
    
    public DocumentCache() {
    }
    
    /**
     * Removes all loaded XML DOMS from hashmap, exposing them to GC.
     */
    public void clear(){
        loadedDocuments.clear();
    }
    
    /**
     * Called when loading new file, checks if there is enough space in cache
     * and removes rarely used ones, if needed.
     * @param newFileSize 
     */
    private void cleanUp(long newFileSize){
        
        long currentCacheSize = newFileSize;
        
        for(String key : loadedDocuments.keySet()){
            currentCacheSize += loadedDocuments.get(key).size;
        }
        do {
            currentCacheSize = deleteLowUsage(currentCacheSize);
        } while(currentCacheSize > CACHE_SIZE && newFileSize < CACHE_SIZE);
                
    }
    
    private long deleteLowUsage(long currentCacheSize){
        long newCacheSize = currentCacheSize;
        
        //file is larger than cache free space, time to clean up
        for(String key : loadedDocuments.keySet()){
            LoadedDocument info = loadedDocuments.get(key);
            if(info.getUsage() >= 0){
                newCacheSize-= info.size;
                loadedDocuments.remove(key);
            }else{
                info.decreaseUsage();
            }
        }
            
        return newCacheSize;
    }
    
    /**
     * Obtains requested file from HashMap increasing its usage 
     * or loads it, if not present.
     * @param filePath Path to XML file
     * @return XML DOM of file at given path
     */
    public LoadedDocument getFile(String filePath){
        LoadedDocument loadedDocument;
        
        if(loadedDocuments.containsKey(filePath)){
            loadedDocument = loadedDocuments.get(filePath);
        }else{
            try{
                loadedDocument = loadFile(filePath);
            }catch(Exception e){
                throw new DialogEditorException("File at "+filePath+" could not be loaded!", e);
            }
        }

        loadedDocument.increaseUsage();
        return loadedDocument;
    }
    
    
    /**
     * Loads given XML file to memory and builds its DOM
     * @param filePath Path to XML file
     */
    private LoadedDocument loadFile(String filePath){
        File dialogFile = new File(filePath);
        Document loadedDOM;
        LoadedDocument loadedInfo;
        
        if(dialogFile.exists()){
            try{
                cleanUp(LoadedDocument.getFileSize(dialogFile));
                loadedDOM = builder.build(dialogFile);
                loadedInfo = new LoadedDocument(dialogFile, loadedDOM);
                loadedDocuments.put(filePath,loadedInfo);
                
                return loadedInfo;
            }catch(JDOMException e){
                //File does not corresponds with XML format, inform user trough logger
                Logger.getLogger(Dialogs.class.getName()).log(Level.SEVERE, 
                        "Loaded file contains corrupted XML: {0}", e);
                
                throw new DialogEditorException("Error while loading file "+filePath, e);
            }catch(IOException ex){
                throw new DialogEditorException("Error while loading file "+filePath, ex);
            }
        }else{
            throw new DialogEditorException("Loaded path "+filePath+" does not exist!");
        }
    }
    
}
