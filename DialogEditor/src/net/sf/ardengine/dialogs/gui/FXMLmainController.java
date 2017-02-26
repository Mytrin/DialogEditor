package net.sf.ardengine.dialogs.gui;

import javafx.scene.control.TextArea;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.Event;
import net.sf.ardengine.dialogs.Response;
import net.sf.ardengine.dialogs.cache.LoadedDocument;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

/**
 * FXML Controller class
 *
 * @author Kuba
 */
public class FXMLmainController implements Initializable {

    @FXML
    TextArea taQuestion, taAnswer;
    @FXML
    AnchorPane ap;
    @FXML
    TextField tfTokenRemember, tfCondition;
    @FXML
    CheckBox cbTokenRemember;
    @FXML
    ChoiceBox chbCondition;
    @FXML
    MenuButton mbCondition, mbTarget;
    @FXML
    ListView<Dialog> lvDialogs;
    @FXML
    ListView<Response> lvAnswers;
    @FXML
    TabPane tp2, tp3;
    @FXML
    Label lFile;
    @FXML
    Tab tabChosenDialog;
    @FXML
    Pane pAnswer;
    LoadedDocument document;
    Dialog actualDialog;
    Response actualResponse;
    String fileName, target, projectFolder, filePath;
    ObservableList<Dialog> dialogs;
    ObservableList<Response> answers;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<String> anoNe = FXCollections.observableArrayList();
        anoNe.add("Ano");
        anoNe.add("Ne");
        chbCondition.setItems(anoNe);
        chbCondition.getSelectionModel().selectFirst();
    }

    private void refreshDialogs() {
        dialogs = FXCollections.observableArrayList();
        document.getAllDialogs().forEach((dialog) -> {
            dialogs.add(dialog);
        });
        lvDialogs.setItems(dialogs);
    }

    private void refreshAnswers() {
        if (actualDialog != null) {
            answers = FXCollections.observableArrayList();
            answers.addAll(actualDialog.getAllResponsesArray());
            lvAnswers.setItems(answers);
            taQuestion.setText(actualDialog.getEvent().getRawText());
            fillTokens();
        }
    }

    private void refreshAns() {

        if (actualResponse == null) {
            pAnswer.setDisable(true);
        } else {
            pAnswer.setDisable(false);
            taAnswer.setText(actualResponse.getRawText());
            //condition = ans.getIfToken();
            /* if (condition.startsWith("#")) {
                chbCondition.setDisable(false);
                for (AnswerToken answerstoken : answerstokens) {
                    if (answerstoken.getContent().equals(condition)) {
                        mbCondition.setText(answerstoken.getName());
                        break;
                    }
                }
                if (condition.startsWith("#!")) {
                    chbCondition.getSelectionModel().selectLast();
                } else {
                    chbCondition.getSelectionModel().selectFirst();
                }
            } else {
                chbCondition.setDisable(true);
            }*/
            target = actualResponse.getTarget();
            mbTarget.setText(target);
        }

    }

    private String dialogWindow(String dotaz) {
        TextInputDialog dialog = new TextInputDialog("");
        dialog.setHeaderText(null);
        dialog.setContentText(dotaz);
        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            return result.get();
        }
        return "0";
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otevřít dialogový soubor.");
        File directory = new File("\\");                     //TODO
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File file = fileChooser.showOpenDialog((Stage) (ap.getScene().getWindow()));
        Document jdomDocument;
        try {
            SAXBuilder jdomBuilder = new SAXBuilder();
            jdomDocument = jdomBuilder.build(file);
            document = new LoadedDocument(file, jdomDocument);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(FXMLmainController.class.getName()).log(Level.SEVERE, null, ex);
            //throw some cool exception
        }
        refreshDialogs();
        refreshAnswers();
        refreshAns();
        filePath = file.getAbsolutePath();
        fileName = file.getAbsolutePath().replace(directory.getAbsolutePath() + "\\", "");
        lFile.setText(fileName);
        fillTargets();
    }

    public void newFile() {
        fileName = dialogWindow("Zadejte název souboru: "); //TODO cesta
        lFile.setText(fileName);
        document = new LoadedDocument(new File(fileName), (new Element("root")).getDocument());
        filePath = document.source.getAbsolutePath();
        fillTargets();
        refreshDialogs();
        refreshAnswers();
        refreshAns();
    }

    public void saveFile() {
        document.save(new XMLOutputter(Format.getPrettyFormat()), document.source);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Soubor " + fileName + " uložen.");
        alert.showAndWait();
    }

    public void newDialog() {
        String id = dialogWindow("Zadejte id dialogu:");
        //TODO unikátnost
        addDialogToMenu(id);
        actualResponse = null;
        actualDialog = new Dialog(id, new Event("", "Undefined"), new ArrayList<>());
        tabChosenDialog.setDisable(false);
        tp2.getSelectionModel().select(2);
        refreshDialogs();
        refreshAnswers();
        refreshAns();
    }

    public void editDialog() {
        actualDialog = lvDialogs.getSelectionModel().getSelectedItem();
        actualResponse = null;
        refreshAnswers();
        refreshAns();
        tabChosenDialog.setDisable(false);
        tp2.getSelectionModel().select(2);
    }

    public void deleteDialog() {
        String id = lvDialogs.getSelectionModel().getSelectedItem().getDialogID();
        removeDialogFromMenu(id);
        document.removeDialog(id);
        refreshDialogs();
        refreshAnswers();
        refreshAns();
    }

    public void saveQuestion() { //saveEvent
        //TODO source
        actualDialog.getEvent().setRawText(taQuestion.getText());
        tp3.getSelectionModel().select(2);
        document.addOrRefreshDialog(actualDialog);
    }

    public void saveAnswer() { //saveResponse
        actualResponse.setRawText(taAnswer.getText());

        if (cbTokenRemember.isSelected()) {
            //TODO exeCute pro zapamatování
        }
        actualResponse.setTarget(target);
         document.addOrRefreshDialog(actualDialog);
        refreshAnswers();
        refreshAns();
    }

    public void newAnswer() { //newResponse

        actualResponse = new Response("...");
        mbCondition.setText("žádná");
        mbTarget.setText("Konec");
        //condition = "";
        target = "exit()";
        cbTokenRemember.setSelected(false);
        tfTokenRemember.setDisable(true);
        actualDialog.addResponse(actualResponse);
        refreshAnswers();
        refreshAns();
    }

    public void checkBox() {
        boolean bool = cbTokenRemember.isSelected();
        tfTokenRemember.setDisable(!bool);
    }

    public void editAnswer() {
        actualResponse = lvAnswers.getSelectionModel().getSelectedItem();
        refreshAnswers();
        refreshAns();
    }

    public void deleteAnswer() {
        actualDialog.removeResponse(lvAnswers.getSelectionModel().getSelectedItem());
        refreshAnswers();
        refreshAns();
    }

    public void fillTokens() { //bude překopáno s příchodem podmínek
        mbCondition.getItems().get(0).setOnAction((ActionEvent t) -> {
            mbCondition.setText("žádná");
            // condition = "";
            chbCondition.setDisable(true);
        });
        /*   Menu m = (Menu) mbCondition.getItems().get(1);
        m.getItems().clear();
        if (answerstokens == null) {
            answerstokens = IO.getAnswerTokens(new File("answers"));
        }
        answerstokens.stream().forEach((AnswerToken answerToken) -> {
            MenuItem item = new MenuItem(answerToken.getName());
            item.setOnAction((ActionEvent t) -> {
                mbCondition.setText(answerToken.getName());
                condition = answerToken.getContent();
                chbCondition.setDisable(false);
            });
            m.getItems().add(item);
        });
         */

    }

    public void fillTargets() {
        mbTarget.getItems().get(0).setOnAction((ActionEvent t) -> {
            mbTarget.setText("Konec");
            target = "exit()";
        });

        Menu subMenu = (Menu) mbTarget.getItems().get(1);
        boolean exists = false;
        MenuItem[] menuArray = subMenu.getItems().toArray(new MenuItem[0]);
        for (MenuItem menu : menuArray) {
            if (menu.getText().equals(filePath)) {
                exists = true;
            }
        }
        if (!exists) {
            Menu newFileMenu = new Menu(filePath);
            document.getAllDialogsIDs().forEach((String id) -> {
                MenuItem item = new MenuItem(id);
                item.setOnAction((ActionEvent t) -> {
                    mbTarget.setText(id);
                    target = filePath.replace(projectFolder + "\\", "");
                });
                newFileMenu.getItems().add(item);
            });
            subMenu.getItems().add(newFileMenu);
        }
    }

    /**
     * Add dialog to menu of avilible targets under the correct file.
     *
     * @param id - Dialog ID added to avilible targets
     */
    public void addDialogToMenu(String id) {
        MenuItem item = new MenuItem(id);
        item.setOnAction((ActionEvent t) -> {
            mbTarget.setText(id);
            target = filePath.replace(projectFolder + "\\", "");
        });
        ((Menu) mbTarget.getItems().get(1)).getItems().forEach((MenuItem subMenu) -> {
            if (subMenu.getText().equals(filePath)) {
                ((Menu) subMenu).getItems().add(item);
            }
        });
    }

    /**
     * Remove dialog from menu of avilible targets.
     *
     * @param id - Dialog ID removed from avilible targets
     */
    public void removeDialogFromMenu(String id) {
        ((Menu) mbTarget.getItems().get(1)).getItems().forEach((MenuItem subMenu) -> {
            if (subMenu.getText().equals(filePath)) {
                ((Menu) subMenu).getItems().forEach((MenuItem item) -> {
                    if (item.getText().equals(id)) {
                        ((Menu) subMenu).getItems().remove(item);
                    }
                });
            }
        });
    }
}
