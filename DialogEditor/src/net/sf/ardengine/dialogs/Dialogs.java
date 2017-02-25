package net.sf.ardengine.dialogs;

import com.google.gson.JsonPrimitive;
import java.io.File;
import javafx.util.Pair;
import net.sf.ardengine.dialogs.cache.DocumentCache;
import net.sf.ardengine.dialogs.cache.LoadedDocument;
import net.sf.ardengine.dialogs.variables.VariableLoader;

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
    /**The char between file path and ID*/
    public static final String PATH_DELIMITER = ":";
    
    /**Stored JDOM documents*/
    public final DocumentCache xmlCache= new DocumentCache();
    /**Stored JSON variable documents*/
    public final VariableLoader variables= new VariableLoader();

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
            
            Pair<String, String> pathInfo = parsePath(path);
            String filePath = pathInfo.getKey();
            String dialogID = pathInfo.getValue();
            
            LoadedDocument dialogFile = xmlCache.getFile(filePath);
            
            if(dialogFile != null){
                Dialog requestedDialog = dialogFile.getDialog(dialogID);
            
                if(requestedDialog != null){
                    activeDialog = requestedDialog;
                    return activeDialog;
                }else{
                    throw new DialogEditorException("Dialog with id "+dialogID+" "
                        + "does not exists in file "+filePath);
                }
            }else{
                throw new DialogEditorException("Could not load file "+filePath);
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
    
    /**
     * To obtain concrete data class, please note JsonElement.getAs().
     * @param variablePath Path to variable within file (Path/to/fileName:object.varName)
     * @return Variable stored in json element.
     * or null, if it does not exists.
     */
    public JsonPrimitive getVariable(String variablePath){
        Pair<String, String> pathInfo = parsePath(variablePath);
        
        return variables.getVariable(pathInfo.getKey(), pathInfo.getValue());
    }
    
   /**
    * If some objects or array specified by variablePath are missing, they will be created.
    * Does not check if value is already present!
    * @param variablePath Path to variable within file (Path/to/fileName:object.varName)
    * @param newValue Value of JsonPrimitive identified by given path
    */
    public void setVariable(String variablePath, JsonPrimitive newValue){
        Pair<String, String> pathInfo = parsePath(variablePath);
        
        variables.setVariable(pathInfo.getKey(), pathInfo.getValue(), newValue);
    }
        
    //File, ID
    private Pair<String, String> parsePath(String path){
        String filePath = null;
        String dialogID = path;
        
        if(path.contains(PATH_DELIMITER)){
            String[] pathSplit = path.split(PATH_DELIMITER);
            filePath = currentProjectPath+File.separator+pathSplit[0];
            dialogID = pathSplit[1];
        }
        
        return new Pair<>(filePath, dialogID);
    }

}
