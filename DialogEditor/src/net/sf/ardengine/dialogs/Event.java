package net.sf.ardengine.dialogs;

import org.jdom2.Element;

/**
 * Represents unavoidable part of dialog - entry text and commands.
 * 
 * Tag: event
 *  Compulsory “source” - author of event(speaker)
 * 
 * Inner tag: text  -   contains text representing this response
 */
public class Event {
    /**Name of tag symbolizing event*/
    public static final String TAG_EVENT="event";
    /**Name of tag containing text of response/event*/
    public static final String TAG_TEXT="text";
    
    /**Attribute name of target*/
    private static final String ATTR_SOURCE="source";
    
    
    /**Text rendered as this event*/
    private String text;
    /**Speaker of the text*/
    private String sourceID;
    
    /**
     * @param text Text rendered as this event
     * @param sourceID Speaker of the text
     */
    public Event(String text, String sourceID) {
        this.text = text;
        this.sourceID = sourceID;
    }
    
    /**
     * Loads event from XML element.
     * @param eventElement element with TAG_EVENT name
     */
    public Event(Element eventElement) {
        this.text = eventElement.getChild(TAG_TEXT).getText();
        this.sourceID = eventElement.getAttributeValue(ATTR_SOURCE);
    }

    /**
     * @return XML element representing this event
     */
    public Element createElement(){
        Element textTag = new Element(TAG_TEXT);
            textTag.setText(text);
            
        Element response = new Element(TAG_EVENT);
            response.getChildren().add(response);
            response.setAttribute(ATTR_SOURCE, sourceID);
            
        return response;
    }
    
    /**
     * @return text rendered as this event
     */
    public String getText() {
        return text;
    }
    
    public void setText(String text) {
        this.text = text;
    }

    /**
     * @return ID of speaker of the text
     */
    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }
}
