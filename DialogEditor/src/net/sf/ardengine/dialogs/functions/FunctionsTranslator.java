package net.sf.ardengine.dialogs.functions;

import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.Dialogs;

/**
 * Executes execute elements and disables responses, 
 * should they not fulfill conditions, inside dialogs.
 */
public class FunctionsTranslator {

    /**Available resources*/
    public final Dialogs dialogs;
    
    /**
     * @param dialogs 
     */
    public FunctionsTranslator(Dialogs dialogs) {
        this.dialogs = dialogs;
    }
    
    /**
     * Executes execute elements and disables responses, 
     * should they not fulfill conditions, inside dialogs.
     * @param dialog Dialog without executed execute elements and disabled responses
     */
    public void process(Dialog dialog){

    }
    
}
