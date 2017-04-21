package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonPrimitive;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Creates normal text from raw text by replacing 
 * variable names with their value.
 */
public class VariableTranslator {
    
    /**Stored JSON variable documents*/
    private final VariableLoader loader;
    /**Looks for $variableName*/
    private static final Pattern VAR_PATTERN = Pattern.compile("(\\$)([A-Za-z0-9]+[A-Za-z:._\\-0-9]+)");
    
    private static final String VAR_START_REGEX = "\\$";

    /**
     * @param loader Stored JSON variable documents
     */
    public VariableTranslator(VariableLoader loader) {
        this.loader = loader;
    }
    
    /**
     * @param rawText Text with variable names
     * @return text with variable names replaced by their values
     */
    public String process(String rawText){
        Matcher matcher = VAR_PATTERN.matcher(rawText);
        String translatedText = rawText;
        
        while(matcher.find()){
            String varPath = matcher.group(2);
            JsonPrimitive varValue = loader.getVariable(varPath);
            String varValueStr = (varValue!=null)?varValue.getAsString():"null";
            
            translatedText = translatedText.replaceAll(VAR_START_REGEX+varPath, varValueStr);
            //better X times rescanning the same than browsing json again
            matcher.reset(translatedText);
        }
        
        return translatedText;
    }
    
}
