package net.sf.ardengine.dialogs;

import net.sf.ardengine.dialogs.variables.VariableTranslator;
import com.google.gson.JsonPrimitive;
import java.io.File;
import net.sf.ardengine.dialogs.cache.DocumentCache;
import net.sf.ardengine.dialogs.functions.FunctionsTranslator;
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
    
    /**Stored JDOM documents*/
    private final DocumentCache xmlCache = new DocumentCache();
    /**Stored JSON variable documents*/
    private final VariableLoader variables = new VariableLoader();
    /**Class responsible for variable translating*/
    private final VariableTranslator variableTranslator = new VariableTranslator(variables);
    /**Class responsible for execute and condition*/
    private final FunctionsTranslator functionTranslator = new FunctionsTranslator(variables, variableTranslator);

    /**Actual dialog*/
    private Dialog activeDialog;
        
    /**
     * Clears resources and discards unsaved changes from previous project and loads new.
     * 
     * @param path Path to directory with dialog project
     * @return true, if project has been successfully loaded.
     */
    public boolean loadFolder(String path){
        return loadFolder(new File(path));
    }
    
    /**
     * Clears resources and discards unsaved changes from previous project and loads new.
     * @param dialogFolder Path to directory with dialog project
     * @return true, if project has been successfully loaded.
     */
    public boolean loadFolder(File dialogFolder){
        if(dialogFolder.exists() && dialogFolder.isDirectory()){
            String folderPath = dialogFolder.getPath();
            
            xmlCache.setProjectPath(folderPath);
            variables.setProjectPath(folderPath);
            
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
        Dialog requestedDialog = xmlCache.loadDialog(path);
        
        if(requestedDialog != null){
            activeDialog = requestedDialog;
            
            activeDialog.translateVariables(variableTranslator);
            functionTranslator.process(activeDialog);
            
            return activeDialog;
        }
        
        return null;            
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
     * Example: Path/to/fileName:object.varName
     * @param completePath complete variable path (Path/to/fileName:object.varName)
     * @return Variable stored in json element.
     * or null, if it does not exists.
     */
    public JsonPrimitive getVariable(String completePath){
        return variables.getVariable(completePath);
    }
    
    /**
     * If some objects or array specified by variablePath are missing, they will be created.
     * Does not check if value is already present!
     * Example: Path/to/fileName:object.varName
     * @param completePath complete variable path (Path/to/fileName:object.varName)
     * @param newValue Value of JsonPrimitive identified by given path
     */
    public void setVariable(String completePath, JsonPrimitive newValue){
        variables.setVariable(completePath, newValue);
    }
    
    /**
     * Saves variable changes at selected file.
     * @param filePath path to variable file without suffix (Path/to/fileName:object.varName)
     */
    public void saveVariableChanges(String filePath){
        variables.saveFile(filePath);
    }

}
