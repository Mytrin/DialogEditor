package com.squirrels.snow.dialogs;

import com.squirrels.snow.dialogs.functions.FunctionAttributes;
import org.jdom2.Element;

/**
 * Represents function that should be executed as a part of dialog event.
 * 
 * Attributes:
 *  Compulsory “function”  - name of function
 *  
 *  Other depend of type of function called
 */
public class Execute {
    /**Name of tag symbolizing Execute*/
    public static final String TAG_EXECUTE="execute";
    /**Attribute containing name of function*/
    public static final String ATTR_FUNCTION_NAME="function";

    /**Contains info about possibly called function*/
    private final FunctionAttributes functionArgs;
    
    /**
     * @param functionName name of called function
     */
    public Execute(String functionName) {
        functionArgs = new FunctionAttributes(ATTR_FUNCTION_NAME);
        functionArgs.setFunction(functionName);
    }
    
    /**
     * Loads response from XML element.
     * @param responseElement element with TAG_RESPONSE name
     */
    public Execute(Element responseElement) {
        this.functionArgs = new FunctionAttributes(responseElement, ATTR_FUNCTION_NAME);
    }
    
    /**
     * @return XML element representing this execute
     */
    public Element createElement(){
        Element executeElement = new Element(TAG_EXECUTE);
            functionArgs.save(executeElement);
            
        return executeElement;
    }

    /**
     * @return Object containing access to condition settings
     */
    public FunctionAttributes getFunctionAttributes() {
        return functionArgs;
    }

    @Override
    public String toString() {
        return functionArgs.getFunctionName() + "("+functionArgs.getAllAttributesWithoutName()+")";
    }
    
    

}
