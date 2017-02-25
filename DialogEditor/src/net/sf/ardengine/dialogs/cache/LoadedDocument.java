package net.sf.ardengine.dialogs.cache;

import java.io.File;
import java.util.Optional;
import net.sf.ardengine.dialogs.Dialog;
import org.jdom2.Document;
import org.jdom2.Element;

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
    
    /**
     * @param dialogID ID of requested dialog (for example "Cat1")
     * @return Dialog or null, if such id does not exists within dialog elements.
     */
    public Dialog getDialog(String dialogID){
        Optional<Element> dialogElement = getDialogElement(dialogID);
        
        if(dialogElement.isPresent()){
            return new Dialog(dialogElement.get());
        }
        
        return null;
    }    

    /**
     * Inserts new dialog element into current XML document.
     * If document already contains dialog with such id, it will be replaced.
     * @param newDialog Complete dialog to save
     */
    public void addDialog(Dialog newDialog){
        Optional<Element> dialogElement = getDialogElement(newDialog.getDialogID());
        
        if(dialogElement.isPresent()){
            loadedXML.getRootElement().getChildren().remove(dialogElement.get());
        }
        
        loadedXML.getRootElement().getChildren().add(newDialog.createElement());
    }
    
    /**
     * Removes dialog with given id from document.
     * @param dialogID id property of dialog element
     */
    public void removeDialog(String dialogID){
        Optional<Element> dialogElement = getDialogElement(dialogID);
        
        if(dialogElement.isPresent()){
            loadedXML.getRootElement().getChildren().remove(dialogElement.get());
        }
    }
    
    private Optional<Element> getDialogElement(String dialogID){
        return loadedXML.getRootElement()
                .getChildren().stream().filter((Element t) -> {
                    return t.getName().equals(Dialog.TAG_DIALOG) && 
                           t.getAttributeValue(Dialog.ATTR_DIALOG_ID).equals(dialogID);
                }).findFirst();
    }
}
