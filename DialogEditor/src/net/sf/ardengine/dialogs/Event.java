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
    
    
    /**Text rendered as this event without translated variables*/
    private String rawText;
    /**Text rendered as this event*/
    private String translatedText;
    /**Speaker of the text*/
    private String sourceID;
    
    /**
     * @param rawText Text rendered as this event
     * @param sourceID Speaker of the text
     */
    public Event(String rawText, String sourceID) {
        this.rawText = rawText;
        this.sourceID = sourceID;
    }
    
    /**
     * Loads event from XML element.
     * @param eventElement element with TAG_EVENT name
     */
    public Event(Element eventElement) {
        this.rawText = eventElement.getChild(TAG_TEXT).getText();
        this.sourceID = eventElement.getAttributeValue(ATTR_SOURCE);
    }

    /**
     * @return XML element representing this event
     */
    public Element createElement(){
        Element textTag = new Element(TAG_TEXT);
            textTag.setText(rawText);
            
        Element event = new Element(TAG_EVENT);
            event.getChildren().add(textTag);
            event.setAttribute(ATTR_SOURCE, sourceID);
            
        return event;
    }
    
    /**
     * @return text rendered as this event without loaded variables
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
     * @return ID of speaker of the text
     */
    public String getSourceID() {
        return sourceID;
    }

    public void setSourceID(String sourceID) {
        this.sourceID = sourceID;
    }
}
