package net.sf.ardengine.dialogs.cache;

import java.util.HashMap;

/**
 * XML document loader. Loaded documents are stored in Hashmap to prevent 
 * constant reloading when changing files.
 */
public class DocumentCache {
    /**Change to increase space for loaded files (2048 - 2MB default)*/
    public static long CACHE_SIZE = 2048; //todo config
    
    /**Already loaded documents*/
    private final HashMap<String, LoadedDocument> loadedDocuments = new HashMap<>();

    public DocumentCache() {
    }
    
    
}
