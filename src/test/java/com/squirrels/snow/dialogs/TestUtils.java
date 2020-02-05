package com.squirrels.snow.dialogs;

/**
 * Collection of useful methods for dialog testing
 * @author mytrin
 */
public class TestUtils {
    
    /**
     * Prints Dialog to System.out
     * @param loadedDialog - dialog obtained from Dialogs instance
     * @param selectedResponse - used response index of -1
     */
    public static void printDialog(Dialog loadedDialog, int selectedResponse){
        if(loadedDialog == null){
            System.out.println("NULL DIALOG!");
            return;
        }
        
        System.out.println(loadedDialog.getEvent().getText());
        
        Response[] responses = loadedDialog.getAllResponsesArray();
        
        for(Response r : responses){
            System.out.println("R["+(r.isAvailable()?"A":"N")+"]: "+r.getText());
        }
        
        if(selectedResponse < responses.length){
            System.out.println("-> "+responses[selectedResponse].getText());
        }
        
        System.out.println("----------------------------");
    }
    
}
