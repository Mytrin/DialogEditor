package net.sf.ardengine.dialogs.functions;

import java.util.HashMap;
import java.util.List;

/**
 * Contains a HashMap of loaded function instances.
 */
public class FunctionClassifier {
    
    /** Loaded functions, the key is their name */
    private final HashMap<String, IFunction> loadedFunctions = new HashMap<>();

    /**
     * Loads default functions
     */
    protected FunctionClassifier() {
        //Variable comparation
        loadedFunctions.put(EqualsFunction.NAME, new EqualsFunction());
        loadedFunctions.put(GreaterFunction.NAME, new GreaterFunction());
        loadedFunctions.put(LowerFunction.NAME, new LowerFunction());
        //Variable manipulation
        
    }

    /**
     * @param functionName Name of wanted function
     * @return IFunction with such name, or null, if it does not exists
     */
    protected IFunction getFunction(String functionName){
        return loadedFunctions.get(functionName);
    }
    
    /**
     * Inserts function to HashMap.
     * Please note that functions, that has the same name as some already available
     * will overwrite the original.
     * @param function user's function to be added
     */
    protected void addFunction(IFunction function){
        loadedFunctions.put(function.getFunctionName(), function);
    }
    
    /**
     * Inserts functions to HashMap.
     * Please note that functions, that has the same name as some already available
     * will overwrite the original.
     * @param functions user's functions to be added
     */
    protected void addAllFunctions(List<IFunction> functions){
        for(IFunction function: functions){
            addFunction(function);
        }
    }
}
