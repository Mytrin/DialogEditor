package net.sf.ardengine.dialogs.functions;

import com.google.gson.JsonPrimitive;
import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Writes given value to target variable
 *
 * Name: "VARIABLE_SAVE"
 * Compulsory:
 *  value - any value
 *  taget-var - variablePath - saves answer to given variable
 */
public class VariableSaveFunction implements IFunction<String>{
    
    /**Function unique name*/
    public static final String NAME = "VARIABLE_SAVE";
    
    private static final String ATTR_VALUE = "value";

    private static final String[] COMPULSORY_ARGS = new String[]{IFunction.ATTR_TARGET, ATTR_VALUE};
    private static final String[] OPTIONAL_ARGS = new String[]{};
    
    private String answer;
    
    @Override
    public String getFunctionName() {
        return NAME;
    }

    @Override
    public void execute(VariableLoader loader, VariableTranslator translator, FunctionAttributes attributes) {
         String target = FunctionUtil.translateAttributeAsString(translator, attributes, IFunction.ATTR_TARGET);
         answer = FunctionUtil.translateAttributeAsString(translator, attributes, ATTR_VALUE);

         loader.setVariable(target, new JsonPrimitive(answer));
    }

    @Override
    public String getAnswer() {
        return answer;
    }

    @Override
    public String[] getCompulsoryArgsNames() {
        return COMPULSORY_ARGS;
    }

    @Override
    public String[] getOptionalArgsNames() {
        return OPTIONAL_ARGS;
    }
    
    
    
}
