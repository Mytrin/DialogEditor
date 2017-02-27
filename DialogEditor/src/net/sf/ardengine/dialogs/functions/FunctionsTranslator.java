package net.sf.ardengine.dialogs.functions;

import java.util.List;
import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Executes execute elements and disables responses, 
 * should they not fulfill conditions, inside dialogs.
 */
public class FunctionsTranslator {

    /**Stored JSON variable documents*/
    private final VariableTranslator translator;
    /**Stored available functions*/
    private final FunctionClassifier functions = new FunctionClassifier();
    
    /**
     * @param varTranslator Object responsible fro changing variable names to their value
     */
    public FunctionsTranslator(VariableTranslator varTranslator) {
        this.translator = varTranslator;
    }
    
    /**
     * Executes execute elements and disables responses, 
     * should they not fulfill conditions, inside dialogs.
     * @param dialog Dialog without executed execute elements and disabled responses
     */
    public void process(Dialog dialog){
        
    }
    
    /**
     * Inserts function to HashMap.
     * Please note that functions, that has the same name as some already available
     * will overwrite the original.
     * @param function user's function to be added
     */
    public void addFunction(IFunction function){
        functions.addFunction(function);
    }
    
    /**
     * Inserts functions to HashMap.
     * Please note that functions, that has the same name as some already available
     * will overwrite the original.
     * @param functions user's functions to be added
     */
    public void addAllFunctions(List<IFunction> functions){
        functions.addAll(functions);
    }
    
}
