package net.sf.ardengine.dialogs;

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
 *   Optional “condition”  - type of condition
 *   Optional “convalueX”  - Xth value for condition evaluation
 *   Optional “negate”     - should the condition result be negated? [true/false]
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
    
    
    /**Text rendered as this event without translated variables*/
    private String rawText;
    /**Text rendered as this event*/
    private String translatedText;
    /**Path and ID of target dialog*/
    private String target;

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
    }
    
    /**
     * Loads response from XML element.
     * @param responseElement element with TAG_RESPONSE name
     */
    public Response(Element responseElement) {
        this.rawText = responseElement.getChild(TAG_TEXT).getText();
        this.target = responseElement.getAttributeValue(ATTR_TARGET);
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

}
