package net.sf.ardengine.dialogs;

/**
 * Simple runtime exception for DialogEditor. 
 * Usually caused by inconsistency in loaded dialog.
 */
public class DialogEditorException extends RuntimeException {
    
    public DialogEditorException() {
        super();
    }

    /**
     * @param message cause of exception
     */
    public DialogEditorException(String message) {
        super(message);
    }

    /**
     * @param message additional info
     * @param cause catched exception
     */
    public DialogEditorException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
