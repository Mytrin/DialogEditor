package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Compares if both arguments are equal.
 *
 * Name: "EQUALS"
 * Compulsory:
 *  value1 - any value
 *  value2 - any value
 * Optional:
 *  negate - "true"/"false"
 *  target - variablePath - saves answer to given variable
 */
public class EqualsFunction extends ACompareFunction{
    
    /**Function unique name*/
    public static final String NAME = "EQUALS";
    
    @Override
    public String getFunctionName() {
        return NAME;
    }

    @Override
    public void execute(VariableLoader loader, VariableTranslator translator, FunctionAttributes attributes) {
        String arg1 = attributes.getAttributeValue(ACompareFunction.ATTR_ARG1);
        String arg2 = attributes.getAttributeValue(ACompareFunction.ATTR_ARG2);
        
        boolean answer = (arg1 == null ? arg2 == null : arg1.equals(arg2));
        setAnswer(loader, translator, attributes, answer);
    }

}
