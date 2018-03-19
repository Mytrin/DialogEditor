package net.sf.ardengine.dialogs.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import net.sf.ardengine.dialogs.gui.utils.BrowserStarter;

public class FXMLMainController implements Initializable {

    @FXML private AnchorPane windowPane;
    
    @FXML private MenuBar menuBar;
    @FXML private Menu programMenu;
    @FXML private Menu projectMenu;
    @FXML private Menu configMenu;
    @FXML private Menu helpMenu;
    
    @FXML private SplitPane mainPane;
    
    @FXML private AnchorPane projectPane;
    @FXML private TabPane workTabsPane;
    
    @FXML private ScrollPane canvasPane;
    @FXML private Group canvasGroup;
    
    @FXML private HBox statusBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void close(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void showWiki(ActionEvent event) {
        BrowserStarter.openWebPage("https://github.com/Mytrin/DialogEditor");
    }

    @FXML
    private void displayAbout(ActionEvent event) {
    }
    
}
