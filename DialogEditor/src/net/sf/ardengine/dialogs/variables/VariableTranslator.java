package net.sf.ardengine.dialogs.variables;

import com.google.gson.JsonPrimitive;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.sf.ardengine.dialogs.Dialogs;

/**
 * Creates normal text from raw text by replacing 
 * variable names with their value.
 */
public class VariableTranslator {
    
    /**Available resources*/
    public final Dialogs dialogs;
    /**Looks for $variableName*/
    private static final Pattern VAR_PATTERN = Pattern.compile("(\\$)([A-Za-z._\\-0-9]*)");
    
    private static final String VAR_START_REGEX = "\\$";

    /**
     * @param dialogs Makes available Variable loader 
     */
    public VariableTranslator(Dialogs dialogs) {
        this.dialogs = dialogs;
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
            JsonPrimitive varValue = dialogs.getVariable(varPath);
            translatedText = translatedText.replaceAll(VAR_START_REGEX+varPath, varValue.getAsString());
            //better X times rescanning the same than browsing json again
            matcher.reset(translatedText);
        }
        
        return translatedText;
    }
    
}
