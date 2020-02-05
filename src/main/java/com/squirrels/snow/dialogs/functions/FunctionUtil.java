package com.squirrels.snow.dialogs.functions;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.squirrels.snow.dialogs.variables.VariableTranslator;

/**
 * Methods common for all functions.
 */
public class FunctionUtil {
    /**Used when deciding whether to parse Integer or Double*/
    private static final String NUMBER_PATTERN = ".";
    
    private FunctionUtil(){} //singleton

    /**
     * @param translator Available variables
     *  @param attributes attributes of element calling the function
     * @param attrName the function attribute
     * @return Value of text after replacing variable names with their value
     */
    public static String translateAttributeAsString(VariableTranslator translator, FunctionAttributes attributes, String attrName){
        String attrValue = attributes.getAttributeValue(attrName);
        if(attrValue == null) return null;
        return translator.process(attrValue);
    }
    
    
    /**
     * @param translator Available variables
     * @param attributes attributes of element calling the function
     * @param attrName the function attribute
     * @return Number value of text after replacing variable names with their value
     * or null if parsing fails.
     */
    public static Number translateAttributeAsNumber(VariableTranslator translator, FunctionAttributes attributes, String attrName){
        String attrValue =  translateAttributeAsString(translator, attributes, attrName);

        if(attrValue == null) return null;
        
        Number parsedNumber = null;
        try{
            parsedNumber = convertStringToNumber(attrValue);
        }catch(Exception e){
            Logger.getLogger(FunctionUtil.class.getName()).log(Level.WARNING, 
                    "Failed to translate {0} as number!", attrName);
        }
        
        return parsedNumber;
    }

    /**
     * If value cannot be parsed, returns false!
     * @param translator Available variables
     * @param attributes attributes of element calling the function
     * @param attrName the function attribute
     * @return Boolean value of text after replacing variable names with their value
     */
    public static Boolean translateAttributeAsBoolean(VariableTranslator translator, FunctionAttributes attributes, String attrName){
         String attrValue =  translateAttributeAsString(translator, attributes, attrName);
         
         return (attrValue != null && attrValue.trim().toLowerCase().equals(IFunction.VALUE_TRUE));
    }
    
    /**
     * 
     * @param number String to be converted to number
     * @return Number value or null if parsing fails.
     * @throws java.lang.Exception When given String is null or not number
     */
    public static Number convertStringToNumber(String number) throws Exception{
        if(number.contains(NUMBER_PATTERN)){
                return Double.parseDouble(number);
        }else{
                return Integer.parseInt(number);
        }
    }

}
