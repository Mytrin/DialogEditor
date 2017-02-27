package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;
import org.jdom2.Element;

/**
 * Compares if first argument is lower than second.
 *     
 * Name: "GREATER_THAN"
 * Compulsory:
 *  value1 - any value
 *  value2 - any value
 * Optional:
 *  negate - "true"/"false"
 *  target - variablePath - saves answer to given variable
 */
public class LowerFunction extends ACompareFunction{
    
    /**Function unique name*/
    public static final String NAME = "LOWER_THAN";
    
    @Override
    public String getFunctionName() {
        return NAME;
    }
    
    @Override
    public void execute(VariableLoader loader, VariableTranslator translator, Element element) {        
        Number arg1 = FunctionUtil.translateAttributeAsNumber(translator, element, ACompareFunction.ATTR_ARG1);
        Number arg2 = FunctionUtil.translateAttributeAsNumber(translator, element, ACompareFunction.ATTR_ARG2);

        if(arg1 == null || arg2 ==null){
            setAnswer(loader, translator, element, false);
        }else{
            setAnswer(loader, translator, element, arg1.doubleValue()<arg2.doubleValue());
        }
    }
    
}
