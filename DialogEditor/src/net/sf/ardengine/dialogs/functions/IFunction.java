package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Interface for new functions, object referenced by execute elements 
 * and condition attributes.
 * @param <T> function return value
 */
public interface IFunction<T> {
    /**Value for arguments, which value is true*/
    public static final String VALUE_TRUE = "true";
    /**Value for arguments, which value is false*/
    public static final String VALUE_FALSE = "false";
    
    /**Optional argument names*/
    public static final String ATTR_TARGET = "taget-var";
    
    /**
     * @return Name of this function, must be unique, otherwise it covers original
     */
    public String getFunctionName();
    
    /**
     * @return Names of compulsory arguments required by this function 
     */
    public String[] getCompulsoryArgsNames();
    
    /**
     * @return Names of optional arguments required by this function 
     */
    public String[] getOptionalArgsNames();
    
    /**
     * Executes this function. If it has any return value, it will be stored within getAnswer();
     * @param loader Enables storing and editing variables
     * @param translator Enables reading variables
     * @param attributes attributes of execute or response element, used to get arguments
     */
    public void execute(VariableLoader loader, VariableTranslator translator, FunctionAttributes attributes);
    
    /**
     * Call after execute()!
     * @return Operation return value
     */
    public T getAnswer();
    
}
