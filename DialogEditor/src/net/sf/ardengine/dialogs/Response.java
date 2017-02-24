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
    
    
    /**Text rendered as this response*/
    private String text;
    /**Path and ID of target dialog*/
    private String target;

    /**
     * 
     * @param text text rendered as this response
     * @param target Path and ID of target dialog or exit()
     */
    public Response(String text, String target) {
        this(text);
        this.target = target;
    }

    /**
     * @param text text rendered as this response
     */
    public Response(String text) {
        this.text = text;
        this.target = EXIT_RESPONSE;
    }
    
    /**
     * Loads response from XML element.
     * @param responseElement element with TAG_RESPONSE name
     */
    public Response(Element responseElement) {
        this.text = responseElement.getChild(TAG_TEXT).getText();
        this.target = responseElement.getAttributeValue(ATTR_TARGET);
    }

    /**
     * @return XML element representing this reponse
     */
    public Element createElement(){
        Element textTag = new Element(TAG_TEXT);
            textTag.setText(text);
            
        Element response = new Element(TAG_RESPONSE);
            response.getChildren().add(response);
            response.setAttribute(ATTR_TARGET, target);
            
        return response;
    }
    
    /**
     * @return text rendered as this response
     */
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
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

}
