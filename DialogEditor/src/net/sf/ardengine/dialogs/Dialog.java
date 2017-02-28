package net.sf.ardengine.dialogs;

import net.sf.ardengine.dialogs.variables.VariableTranslator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;
import org.jdom2.Element;

/**
 * Represents Dialog - Event and collection of possible Responses.
 * 
 * Tag: dialog
 *  Compulsory “id”     - ID of this dialog, must be unique in its file
 * 
 * Inner tag: event     -   contains unavoidable part of dialog 
 *                      - starting text and commands
 * Inner tag: responses  -   contains available responses in form of response elements
 */
public class Dialog {
    /**Name of tag symbolizing dialog*/
    public static final String TAG_DIALOG="dialog";
    /**Name of tag containing responses*/
    public static final String TAG_RESPONSES="responses";
    
    /**Attribute name of dialog id*/
    public static final String ATTR_DIALOG_ID="id";
    
    /**Unique dialog ID in file*/
    private String dialogID;
    /**Entry text and commands*/
    private Event event;
    /**Available responses*/
    private final List<Response> responses = new LinkedList<>();
    
    /**
     * @param dialogID Unique dialog ID in file
     * @param event Entry text and commands
     * @param responses Available responses
     */
    public Dialog(String dialogID, Event event, List<Response> responses) {
        this.dialogID=dialogID;
        this.event = event;
        this.responses.addAll(responses);
    }
    
    /**
     * Loads dialog from XML element.
     * @param dialogElement element with TAG_DIALOG name
     */
    public Dialog(Element dialogElement) {
        this.dialogID = dialogElement.getAttributeValue(ATTR_DIALOG_ID);
        this.event = new Event(dialogElement.getChild(Event.TAG_EVENT));
        //Load all available response contained in responses element
        for(Element response : 
                dialogElement.getChild(TAG_RESPONSES).getChildren(Response.TAG_RESPONSE) ){
            responses.add(new Response(response));
        }
    }
    
    /**
     * Creates normal dialog text from raw text by replacing 
     * variable names with their value.
     * @param translator object responsible for translation
     */
    public void translateVariables(VariableTranslator translator){
        event.setTranslatedText(translator.process(event.getRawText()));
        
        getAvailableResponses().forEach((Response t) -> {
            t.setTranslatedText(translator.process(t.getRawText()));
        });
    }

    /**
     * @return XML element representing this dialog
     */
    public Element createElement(){
        Element eventTag = event.createElement();
        Element responsesTag = new Element(TAG_RESPONSES);
        for(Response response: responses){
            responsesTag.getChildren().add(response.createElement());
        }
            
        Element dialog = new Element(TAG_DIALOG);
            dialog.getChildren().add(eventTag);
            dialog.getChildren().add(responsesTag);
            dialog.setAttribute(ATTR_DIALOG_ID, dialogID);
            
        return dialog;
    }

    /**
     * @return  Entry text and commands
     */
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @return Unique dialog ID in file
     */
    public String getDialogID() {
        return dialogID;
    }

    public void setDialogID(String dialogID) {
        this.dialogID = dialogID;
    }
    
    /**
     * Removes this response from available responses for this dialog.
     * @param deletedResponse Response to delete
     */
    public void removeResponse(Response deletedResponse){
        responses.remove(deletedResponse);
    }
    
    /**
     * Adds this response into available responses for this dialog.
     * @param newResponse Response to add
     */
    public void addResponse(Response newResponse){
        responses.add(newResponse);
    }

    /**
     * If there are no available response, 
     * NO_RESPONSE will be added to returned stream.
     * @return Available responses for this dialog
     */
    public Stream<Response> getAvailableResponses() {
        if(responses.size() > 0){
            return responses.stream();
        }
        
        return Stream.of(Response.NO_RESPONSE);
    }
    
    /**
     * If there are no available response, 
     * NO_RESPONSE will be added to returned array.
     * @return Array of available responses for this dialog
     */
    public Response[] getAvailableResponsesArray() {
        if(responses.size() > 0){
            return responses.toArray(new Response[responses.size()]);
        }
        
        return new Response[]{Response.NO_RESPONSE};
    }
    
    /**
     * @return Array of all responses defined for this dialog.
     */
    public Response[] getAllResponsesArray() {
            return responses.toArray(new Response[responses.size()]);
    }

    @Override
    public String toString() {
        return event.getRawText(); 
    }
       
}
