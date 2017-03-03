package net.sf.ardengine.dialogs.functions;

import com.google.gson.JsonPrimitive;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ardengine.dialogs.variables.VariableLoader;
import net.sf.ardengine.dialogs.variables.VariableTranslator;

/**
 * Writes given value to target variable
 * SUPPORTS NUMBER ONLY!
 *
 * Value syntax examples:   "+5" //Adds 5 to current value
 *                          "-$MwH" //Removes number value of global variable MwH
 *                          "*1" //Multiplies by 1
 *                          "/0.5" //Divides by 0.5
 * 
 * Name: "NUMBER_EDIT"
 * Compulsory:
 *  value - number value or variable name with +-/*
 *  taget-var - variablePath - saves answer to given variable
 */
public class NumberEditFunction implements IFunction<Number>{
    
    /**Function unique name*/
    public static final String NAME = "NUMBER_EDIT";
    
    private static final String ATTR_VALUE = "value";

    private static final String[] COMPULSORY_ARGS = new String[]{IFunction.ATTR_TARGET, ATTR_VALUE};
    private static final String[] OPTIONAL_ARGS = new String[]{};
    
    private static final char OPERATION_ADD = '+';
    private static final char OPERATION_REMOVE = '-';
    private static final char OPERATION_MULTIPLY = '*';
    private static final char OPERATION_DIVIDE = '/';
    
    private static final double TOLERANCE = 0.00001;
    
    private Number answer;
    
    @Override
    public String getFunctionName() {
        return NAME;
    }

    @Override
    public void execute(VariableLoader loader, VariableTranslator translator, FunctionAttributes attributes) {
        String target = FunctionUtil.translateAttributeAsString(translator, attributes, IFunction.ATTR_TARGET);
        Number currentTargetValue = loader.getVariable(target).getAsNumber();
        String value = FunctionUtil.translateAttributeAsString(translator, attributes, ATTR_VALUE);
        
         try{
            Number numberValue = FunctionUtil.convertStringToNumber(value.substring(1));
            
            double calculated = calculate(currentTargetValue.doubleValue(), numberValue.doubleValue(), OPERATION_ADD);
            
            if(isInteger(calculated)){
                answer = (int)calculated;
            }else{
                answer = calculated;
            }
            
            loader.setVariable(target, new JsonPrimitive(answer));
         }catch(Exception e){
             Logger.getLogger(NumberEditFunction.class.getName()).log(Level.WARNING, 
                     "Failed to use {0} as number: {1}", new Object[]{value, e});
         }
    }
    
    private boolean isInteger(double number){
       return  Math.abs( number - Math.floor(number) ) < TOLERANCE;
    }
    
    private Double calculate(Double a, Double b, char operation){
        switch(operation){
                case OPERATION_ADD:         return a + b;
                case OPERATION_REMOVE:      return a - b;
                case OPERATION_MULTIPLY:    return a * b;
                case OPERATION_DIVIDE:      return a / b;
        }
        
        return null;
    }
    
    @Override
    public Number getAnswer() {
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
