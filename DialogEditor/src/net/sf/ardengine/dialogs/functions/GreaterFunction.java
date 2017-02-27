package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Compares if first argument is greater than second.
 *     
 * Name: "GREATER_THAN"
 * Compulsory:
 *  value1 - any value
 *  value2 - any value
 * Optional:
 *  negate - "true"/"false"
 *  target - variablePath - saves answer to given variable
 */
public class GreaterFunction extends ACompareFunction{
    
    /**Function unique name*/
    public static final String NAME = "GREATER_THAN";
    
    @Override
    public String getFunctionName() {
        return NAME;
    }
    
    @Override
    public void execute(VariableLoader loader, VariableTranslator translator, FunctionAttributes attributes) {        
        Number arg1 = FunctionUtil.translateAttributeAsNumber(translator, attributes, ACompareFunction.ATTR_ARG1);
        Number arg2 = FunctionUtil.translateAttributeAsNumber(translator, attributes, ACompareFunction.ATTR_ARG2);

        if(arg1 == null || arg2 ==null){
            setAnswer(loader, translator, attributes, false);
        }else{
            setAnswer(loader, translator, attributes, arg1.doubleValue() > arg2.doubleValue());
        }
    }
    
}
