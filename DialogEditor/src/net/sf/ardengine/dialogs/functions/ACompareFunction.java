package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.DialogEditorException;
import org.jdom2.Element;

/**
 * Base for compare functions, contains arguments names and negate attribute logic.
 */
public abstract class ACompareFunction implements IFunction<Boolean>{
   
    /**Compulsory argument names*/
    protected static final String ARG1 = "value1";
    protected static final String ARG2 = "value2";
    
    /**Optional argument names*/
    private static final String NEGATE = "negate";
    
    private static final String NUMBER_PATTERN = "^\\d*\\.?\\d*$";
    
    /**True, execute() has been called*/
    protected boolean executed = false;
    /**Return value of this function*/   
    protected boolean answer;
    
    /**
     * @param element element calling this function
     * @return true, if negate arguments is true
     */
    protected boolean haveToNegate(Element element){
        String negateValue = element.getAttributeValue(NEGATE);
        
        return (negateValue!= null && negateValue.toLowerCase().equals(VALUE_TRUE));
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
        return new String[]{ARG1, ARG2};
    }

    @Override
    public String[] getOptionalArgsNames() {
        return new String[]{NEGATE};
    }

    @Override
    public Boolean getAnswer() {
        if(!executed) throw new DialogEditorException("Getting answer before calling execute!");
        
        return answer;
    }   

}
