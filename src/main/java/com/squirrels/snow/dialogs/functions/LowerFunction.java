package com.squirrels.snow.dialogs.functions;


import com.squirrels.snow.dialogs.variables.VariableLoader;
import com.squirrels.snow.dialogs.variables.VariableTranslator;

/**
 * Compares if first argument is lower than second.
 *     
 * Name: "LOWER_THAN"
 * Compulsory:
 *  value1 - any value
 *  value2 - any value
 * Optional:
 *  negate - "true"/"false"
 *  taget-var - variablePath - saves answer to given variable
 */
public class LowerFunction extends ACompareFunction{
    
    /**Function unique name*/
    public static final String NAME = "LOWER_THAN";
    
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
            setAnswer(loader, translator, attributes, arg1.doubleValue()<arg2.doubleValue());
        }
    }
    
}
