package net.sf.ardengine.dialogs.functions;

import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ardengine.dialogs.variables.VariableTranslator;
import org.jdom2.Element;

/**
 * Methods common for all functions.
 */
public class FunctionUtil {
    /**Used when deciding whether to parse Integer or Double*/
    private static final String NUMBER_PATTERN = ".";
    
    private FunctionUtil(){} //singleton

    /**
     * @param translator Available variables
     * @param element Element calling the function
     * @param attrName the function attribute
     * @return Value of text after replacing variable names with their value
     */
    public static String translateAttributeAsString(VariableTranslator translator, Element element, String attrName){
        return translator.process(element.getAttributeValue(attrName));
    }
    
    
    /**
     * @param translator Available variables
     * @param element Element calling the function
     * @param attrName the function attribute
     * @return Number value of text after replacing variable names with their value
     * or null if parsing fails.
     */
    public static Number translateAttributeAsNumber(VariableTranslator translator, Element element, String attrName){
        String attrValue =  translator.process(element.getAttributeValue(attrName));
        Number parsedNumber = null;
        
        try{
            if(attrValue.contains(NUMBER_PATTERN)){
                parsedNumber = Double.parseDouble(attrValue);
            }else{
                parsedNumber = Integer.parseInt(attrValue);
            }
        }catch(Exception e){
            Logger.getLogger(FunctionUtil.class.getName()).log(Level.WARNING, 
                    "Failed totranslate {0} as number!", attrValue);
        }
        
        return parsedNumber;
    }
    
    /**
     * If value cannot be parsed, returns false!
     * @param translator Available variables
     * @param element Element calling the function
     * @param attrName the function attribute
     * @return Boolean value of text after replacing variable names with their value
     */
    public static Boolean translateAttributeAsBoolean(VariableTranslator translator, Element element, String attrName){
         String attrValue = translator.process(element.getAttributeValue(attrName));
         
         return (attrValue != null && attrValue.trim().toLowerCase().equals(IFunction.VALUE_TRUE));
    }

}
