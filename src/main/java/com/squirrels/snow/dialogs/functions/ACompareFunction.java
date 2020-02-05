package com.squirrels.snow.dialogs.functions;

import com.google.gson.JsonPrimitive;
import com.squirrels.snow.dialogs.DialogEditorException;
import com.squirrels.snow.dialogs.variables.VariableLoader;
import com.squirrels.snow.dialogs.variables.VariableTranslator;

/**
 * Base for compare functions, contains arguments names and negate attribute logic.
 */
public abstract class ACompareFunction implements IFunction<Boolean>{
   
    /**Compulsory argument names*/
    protected static final String ATTR_ARG1 = "value1";
    protected static final String ATTR_ARG2 = "value2";
    
    /**Optional argument names*/
    private static final String ATTR_NEGATE = "negate";
    
    private static final String NUMBER_PATTERN = "^\\d*\\.?\\d*$";
    
    /**True, execute() has been called*/
    private boolean executed = false;
    /**Return value of this function*/   
    private boolean answer;
        
    /**
     * Updates answer property according to negate and target attributes
     * @param loader Object responsible for saving answer to variable, if required
     * @param translator Object responsible for replacing variable names with their value
     * @param attributes attributes of element calling this function
     * @param answer child operation answer
     */
    protected void setAnswer(VariableLoader loader, VariableTranslator translator, FunctionAttributes attributes, boolean answer){
        executed = true;
        
        if(FunctionUtil.translateAttributeAsBoolean(translator, attributes, ATTR_NEGATE)){
            this.answer = !answer;
        }else{
            this.answer = answer; 
        }
        
        String targetVar = FunctionUtil.translateAttributeAsString(translator, attributes, IFunction.ATTR_TARGET);
        if(targetVar != null){
            loader.setVariable(targetVar, new JsonPrimitive(answer));
        }
    }
    
    /**
     * @param number supposed number
     * @return true, if given String can be parsed as number
     */
    protected boolean isNumber(String number){
        return number.matches(NUMBER_PATTERN);
    }
    
    @Override
    public String[] getCompulsoryArgsNames() {
        return new String[]{ATTR_ARG1, ATTR_ARG2};
    }

    @Override
    public String[] getOptionalArgsNames() {
        return new String[]{IFunction.ATTR_TARGET, ATTR_NEGATE};
    }

    @Override
    public Boolean getAnswer() {
        if(!executed) throw new DialogEditorException("Getting answer before calling execute!");
        
        return answer;
    }   

}
