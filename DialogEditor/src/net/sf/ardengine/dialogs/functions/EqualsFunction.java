package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;
import org.jdom2.Element;

/**
 * Compares if both arguments are equal.
 *
 * Name: "EQUALS"
 * Compulsory:
 *  value1 - any value
 *  value2 - any value
 * Optional:
 *  negate - "true"/"false"
 */
public class EqualsFunction extends ACompareFunction{
    
    /**Function unique name*/
    public static final String NAME = "EQUALS";
    
    @Override
    public String getFunctionName() {
        return NAME;
    }

    @Override
    public void execute(VariableTranslator translator, Element element) {
        executed = true;
        
        String arg1 = element.getAttributeValue(ACompareFunction.ARG1);
        String arg2 = element.getAttributeValue(ACompareFunction.ARG2);
        
        answer = (arg1 == null ? arg2 == null : arg1.equals(arg2));
    }

}
