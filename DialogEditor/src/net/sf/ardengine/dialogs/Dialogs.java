package net.sf.ardengine.dialogs;

import java.io.File;
import java.util.Optional;
import net.sf.ardengine.dialogs.cache.DocumentCache;
import net.sf.ardengine.dialogs.cache.LoadedDocument;
import net.sf.ardengine.dialogs.variables.VariableLoader;
import org.jdom2.Element;

/**
 * This is main class of the library. It is used to load and 
 * obtain info about dialogs.
 * 
 * Example:
 * Dialogs testedDialogs = new Dialogs();
 * 
 * testedDialogs.loadFolder("example_project");
 * testedDialogs.loadDialog("simple_test:easy-test");
 * 
 * System.out.println(testedDialogs.getActiveDialog().getEvent().getText());
 * for(Response r : responses) {
 *    System.out.println("R:  "+r.getText());
 * }
 */
public class Dialogs {
    public static final String PATH_DELIMITER = ":";
    
    /**Format of dialog files*/
    public static final String DIALOG_FORMAT = ".xml"; //todo config
    
    /**Stored JDOM documents*/
    private final DocumentCache xmlCache= new DocumentCache();
    /**Stored JSON variable documents*/
    private final VariableLoader variables= new VariableLoader();
    /**Actual JDOM document*/
    private LoadedDocument actualDocument; 

    
    
    
    
    /**Actual dialog*/
    private Dialog activeDialog;
    /**Path to current folder with dialog files*/
    private String currentProjectPath;

    
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
        //clean last project
        xmlCache.clear();
        variables.clear();
        
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
            
            if(path.contains(PATH_DELIMITER)){ //Otherwise loading from actual file
                String[] pathSplit = path.split(PATH_DELIMITER);
                String filePath = currentProjectPath+File.separator+pathSplit[0]+DIALOG_FORMAT;
                dialogID = pathSplit[1];
                if(actualDocument==null || !actualDocument.source.getPath().equals(filePath)){
                    actualDocument = xmlCache.getFile(filePath);
                }
            }else{
                dialogID = path;
            }
            
            Optional<Element> dialogElement = actualDocument.loadedXML.getRootElement()
                .getChildren().stream().filter((Element t) -> {
                    return t.getName().equals(Dialog.TAG_DIALOG) && 
                           t.getAttributeValue(Dialog.ATTR_DIALOG_ID).equals(dialogID);
                }).findFirst();
            
            if(dialogElement.isPresent()){
                activeDialog = new Dialog(dialogElement.get());
                return activeDialog;
            }else{
                throw new DialogEditorException("Dialog with id "+dialogID+" "
                        + "does not exists in file "+actualDocument.source.getPath());
            }
        }else{
            throw new DialogEditorException("Project directory has not been selected!");
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
