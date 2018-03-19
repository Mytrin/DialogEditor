package net.sf.ardengine.dialogs.gui.utils;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Singleton building console command 
 * because Java Desktop class is unreliable on UNIX systems.
 */
public class BrowserStarter {

    private static final String[] LINUX_BROWSERS = {"epiphany", "firefox", "mozilla", "konqueror",
	       			             "chrome","opera", "vivaldi", "links","lynx", 
                                             "explorer", "edge"};
    
    private BrowserStarter() {};
    
    /**
     * Opens available system web browser and displays given url 
     * or at least displays error dialog.
     * 
     * @param url -example https://github.com/Mytrin/DialogEditor
     */
    public static final void openWebPage(String url) {
        OSType type = getOSType();
        if(type != null) {
            type.displayUrl.accept(url);
        } else {
            //todo display dialog
        }
    }
    
    private static void displayPageOnWindows(String url) {
        try {
            Runtime rt = Runtime.getRuntime();
             // this doesn't support showing urls in the form of "page.html#nameLink" 
            rt.exec( "rundll32 url.dll,FileProtocolHandler " + url);
        } catch(Exception e) {
            Logger.getLogger(BrowserStarter.class.getName()).log(Level.WARNING, "Could not display page on Windows.");
            //todo display dialog
        }
    }
    
    private static void displayPageOnMac(String url) {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec( "open " + url);
        } catch(Exception e) {
            Logger.getLogger(BrowserStarter.class.getName()).log(Level.WARNING, "Could not display page on Mac.");
            //todo display dialog
        }
    }
    
    private static void displayPageOnLinux(String url) {
        try {
            Runtime rt = Runtime.getRuntime();
            // Do a best guess on unix until we get a platform independent way
	    // Build a list of browsers to try, in this order.  
            // Build a command string which looks like "browser1 "url" || browser2 "url" ||..."
	    StringBuilder cmd = new StringBuilder();
	    for (int i=0; i<LINUX_BROWSERS.length; i++)
	        cmd.append(i==0  ? "" : " || ").append(LINUX_BROWSERS[i]).append(" \"").append(url).append("\" ");
	        String[] commandAndParams = new String[] { "sh", "-c", cmd.toString() };
	        rt.exec(commandAndParams);
        } catch(Exception e) {
            Logger.getLogger(BrowserStarter.class.getName()).log(Level.WARNING, "Could not display page on linux.");
            //todo display dialog
        }
    }
    
    
    private static OSType getOSType() {
        String os = System.getProperty("os.name").toLowerCase();
        
        for(OSType type : OSType.values()) {
            if(type.isThisOS.test(os)) {
                return type;
            }
        }
        
        Logger.getLogger(BrowserStarter.class.getName()).log(Level.WARNING, "Could not determine OS: {0}", os);
        return null;
    }
    
        
    private enum OSType {
        WINDOWS((String os) -> os.contains("win"), BrowserStarter::displayPageOnWindows), 
        MAC((String os) -> os.contains("mac"), BrowserStarter::displayPageOnMac), 
        LINUX((String os) -> os.contains( "nix") || os.contains( "nux"), BrowserStarter::displayPageOnLinux);
        
        public final Predicate<String> isThisOS;
        public final Consumer<String> displayUrl;

        private OSType(Predicate<String> isThisOS, Consumer<String> displayUrl) {
            this.isThisOS = isThisOS;
            this.displayUrl = displayUrl;
        }
    }
}
