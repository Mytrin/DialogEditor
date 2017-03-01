package net.sf.ardengine.dialogs.functions;

import java.util.List;
import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.Execute;
import net.sf.ardengine.dialogs.Response;
import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Executes execute elements and disables responses, 
 * should they not fulfill conditions, inside dialogs.
 */
public class FunctionsTranslator {

    /**Stored for saving and manipulating with variables*/
    private final VariableLoader loader;
    /**Stored fro variable translation*/
    private final VariableTranslator translator;
    /**Stored available functions*/
    private final FunctionMapper functions = new FunctionMapper();
    
    /**
     * @param loader Object responsible for saving of variables
     * @param varTranslator Object responsible for changing variable names to their value
     */
    public FunctionsTranslator(VariableLoader loader, VariableTranslator varTranslator) {
        this.loader = loader;
        this.translator = varTranslator;
    }
    
    /**
     * Executes execute elements and disables responses, 
     * should they not fulfill conditions, inside dialogs.
     * @param dialog Dialog without executed execute elements and disabled responses
     */
    public void process(Dialog dialog){
        dialog.getEvent().getAllExecutes().forEach((Execute t) -> {
            executeFunction(t.getFunctionAttributes());
        });
        
        dialog.getAvailableResponses().forEach((Response t) -> {
            Object answer = executeFunction(t.getFunctionAttributes());
           
            if(answer instanceof Boolean){
                t.setAvailable((Boolean)answer);
            }
        });
    }
    
    private Object executeFunction(FunctionAttributes functionAttributes){
        String functionName = functionAttributes.getFunctionName();
        
        if(functionName != null){
            IFunction function = functions.getFunction(functionName);
                function.execute(loader, translator, functionAttributes);
                
                return function.getAnswer();
        }else{
            return null;
        }
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
