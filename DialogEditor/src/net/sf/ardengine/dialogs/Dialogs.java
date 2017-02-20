package net.sf.ardengine.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;


public class Dialogs {
    /**Format of dialog files*/
    public static final String DIALOG_FORMAT = ".xml"; //todo config
    
    /**JDOM Parser*/
    private final SAXBuilder builder = new SAXBuilder();
    /**Actual JDOM document*/
    private Document actualDocument; 
    /**Actual dialog*/
    private Dialog activeDialog;
    /**Path to current folder with dialog files*/
    private String currentProjectPath;
    /**Actual file*/
    private File currentFile;
    
    /**
     * @param path Path to directory with dialog project
     * 
     * @return true, if project has been successfully loaded.
     */
    public boolean loadFolder(String path){
        return loadFolder(new File(path));
    }
    
    /**
     * @param dialogFolder Path to directory with dialog project
     * 
     * @return true, if project has been successfully loaded.
     */
    public boolean loadFolder(File dialogFolder){
        if(dialogFolder.exists() && dialogFolder.isDirectory()){
            currentProjectPath = dialogFolder.getPath();
            //todo variables loading
            return true;
        }else{
            throw new DialogEditorException("Given directory does not exist!");
        }
    }
    
    /**
     * Checks given path and in case of need loads corresponding file. 
     * @param path Path do dialog in this format: Path/To/File:DialogID
     * @return Dialog with this path, otherwise throws DialogEditorException
     */
    public Dialog loadDialog(String path){
        if(currentProjectPath != null && !currentProjectPath.isEmpty()){
            
            String dialogID;
            
            if(path.contains(":")){ //Otherwise loading from actual file
                String[] pathSplit = path.split(":");
                String filePath = currentProjectPath+File.separator+pathSplit[0]+DIALOG_FORMAT;
                dialogID = pathSplit[1];
                if(currentFile==null || !currentFile.getPath().equals(filePath)){
                    loadFile(filePath);
                }
            }else{
                dialogID = path;
            }
            
            Optional<Element> dialogElement = actualDocument.getRootElement()
                .getChildren().stream().filter((Element t) -> {
                    return t.getName().equals(Dialog.TAG_DIALOG) && 
                           t.getAttributeValue(Dialog.ATTR_DIALOG_ID).equals(dialogID);
                }).findFirst();
            
            if(dialogElement.isPresent()){
                activeDialog = new Dialog(dialogElement.get());
                return activeDialog;
            }else{
                throw new DialogEditorException("Dialog with id "+dialogID+" "
                        + "does not exists in file "+currentFile.getPath());
            }
        }else{
            throw new DialogEditorException("Project directory has not been selected!");
        }
    }
    
    /**
     * Loads given XML file to memory and builds its DOM
     * @param filePath Path to XML file
     */
    private void loadFile(String filePath){
        File dialogFile = new File(filePath);
        if(dialogFile.exists()){
            try{
                actualDocument = builder.build(dialogFile); //todo caching
                currentFile = dialogFile;
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
    
    /**
     * Changes active dialog according to response.
     * @param dialogResponse Selected response
     * @return Dialog triggered by response or null, if response was exit()
     */
    public Dialog selectResponse(Response dialogResponse){
        String dialogPath = dialogResponse.getTarget();
        
        if(!dialogPath.equals(Response.EXIT_RESPONSE)){
            activeDialog = loadDialog(dialogPath);
        }else{
            activeDialog = null;
        }

        return activeDialog;
    }

    /**
     * @return Currently processed dialog or null
     */
    public Dialog getActiveDialog() {
        return activeDialog;
    }

}
