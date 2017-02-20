package net.sf.ardengine.dialogs.cache;

import java.io.File;
import org.jdom2.Document;

/**
 * Represents loaded and built XML document. 
 * Instances are stored within DocumentCache.
 */
public class LoadedDocument {
    /**The size of original file(kB)*/
    public final long size;
    /**Original file*/
    public final File source;
    /**Loaded XML DOM*/
    public final Document loadedXML;

    /**How many times has been document requested for loading since last cache cleanup*/
    private int usage = 0;
    
    /**
     * Represents loaded and built XML document. 
     * @param source Original file
     * @param loadedXML Loaded XML DOM
     */
    public LoadedDocument(File source, Document loadedXML) {
        this.source = source;
        this.loadedXML = loadedXML;
        this.size = getFileSize(source);
    }
    
    /**
     * Increase to prevent file to by dropped by cache when cleaning
     */
    public void increaseUsage(){
        usage++;
    }
    
    /**
     * Decrease to increase chance that this document will be deleted to free space
     */
    public void decreaseUsage(){
        usage--;
    }

    /**
     * @return How many times has been document requested 
     * for loading since last cache cleanup
     */
    public int getUsage() {
        return usage;
    }
    
    /**
     * @param source existing file
     * @return Size of given file
     */
    public static final long getFileSize(File source){
        return (source.getTotalSpace() - source.getFreeSpace()) / 1024;
    }
}
