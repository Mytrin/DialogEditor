package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;
import org.jdom2.Element;

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
     * @param translator Available variables
     * @param element execute or response element, used to get arguments
     */
    public void execute(VariableTranslator translator, Element element);
    
    /**
     * Call after execute()!
     * @return Operation return value
     */
    public T getAnswer();
    
}
