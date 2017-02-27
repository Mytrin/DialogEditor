package net.sf.ardengine.dialogs;

import net.sf.ardengine.dialogs.functions.FunctionAttributes;
import org.jdom2.Element;

/**
 *  Represent possible response to current dialog event.
 * 
 *  Tag: response
 *   Compulsory “target”   - ID of next dialog with path, 
 *                              if dialog is saved in another file, 
 *                              or exit() to signal end of conversation.
 *                              ["dialogID";"/Path/To/File/dialogID/";"exit()"]
 * 
 *   Optional “condition”  - name of function
 *  
 *  Depends on condition:
 *   Depends of type of function called
 * 
 * Inner tag: text  -   contains text representing this response
 *   
 */
public class Response {    
    /** Used as response target, signals that there is no following dialog*/
    public static final String EXIT_RESPONSE_TEXT="EXIT";
    
    /** Used as response target, signals that there is no following dialog*/
    public static final String EXIT_RESPONSE="exit()";
    
    /** Response automatically returned by dialog, if no other response found*/   
    public static final Response NO_RESPONSE= new Response(EXIT_RESPONSE_TEXT, EXIT_RESPONSE);
    
    /**Name of tag symbolizing Response*/
    public static final String TAG_RESPONSE="response";
    /**Name of tag containing text of response/event*/
    public static final String TAG_TEXT="text";
    
    /**Attribute name of target*/
    private static final String ATTR_TARGET="target";
    /**Attribute name of function*/
    private static final String ATTR_CONDITION="condition";
    
    
    /**Text rendered as this event without translated variables*/
    private String rawText;
    /**Text rendered as this event*/
    private String translatedText;
    /**Path and ID of target dialog*/
    private String target;
    /**True if condition has been fulfilled*/
    private boolean isAvailable = true;
    /**Contains info about possibly called function*/
    private final FunctionAttributes functionArgs;
    
    /**
     * 
     * @param rawText text rendered as this response without translated variables
     * @param target Path and ID of target dialog or exit()
     */
    public Response(String rawText, String target) {
        this(rawText);
        this.target = target;
    }

    /**
     * @param rawText text rendered as this response without translated variables
     */
    public Response(String rawText) {
        this.rawText = rawText;
        this.target = EXIT_RESPONSE;
        this.functionArgs = new FunctionAttributes(ATTR_CONDITION);
    }
    
    /**
     * Loads response from XML element.
     * @param responseElement element with TAG_RESPONSE name
     */
    public Response(Element responseElement) {
        this.rawText = responseElement.getChild(TAG_TEXT).getText();
        this.target = responseElement.getAttributeValue(ATTR_TARGET);
        this.functionArgs = new FunctionAttributes(responseElement, ATTR_CONDITION);
    }

    /**
     * @return XML element representing this reponse
     */
    public Element createElement(){
        Element textTag = new Element(TAG_TEXT);
            textTag.setText(rawText);
            
        Element response = new Element(TAG_RESPONSE);
            response.getChildren().add(textTag);
            response.setAttribute(ATTR_TARGET, target);
            functionArgs.save(response);
            
        return response;
    }
    
    /**
     * @return text rendered as this response without loaded variables
     */
    public String getRawText() {
        return rawText;
    }
    
    public void setRawText(String text) {
        this.rawText = text;
    }

    /**
     * @return text rendered as this event
     */
    public String getText() {
        return translatedText;
    }

    /**
     * @param translatedText text rendered as this event WITH translated variables
     */
    protected void setTranslatedText(String translatedText) {
        this.translatedText = translatedText;
    }
    
    /**
     * @return Path and ID of target dialog or exit()
     */
    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return rawText;
    }

    /**
     * @return Object containing access to condition settings
     */
    public FunctionAttributes getFunctionAttributes() {
        return functionArgs;
    }

    /**
     * @return True if condition has been fulfilled and Game can show them to player
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Used by FunctionsTranslator.
     * @param isAvailable  True if condition has been fulfilled and Game can show them to player
     */
    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
