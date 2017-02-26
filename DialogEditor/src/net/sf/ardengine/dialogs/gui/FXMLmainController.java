package net.sf.ardengine.dialogs.gui;

import com.sun.javafx.scene.control.skin.VirtualFlow;
import javafx.scene.control.TextArea;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
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
    String  fileName,target;
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

/*    private void refreshDialogs() {
        dialogs = FXCollections.observableArrayList();
        koren.getDialogs().stream().forEach((dialog) -> {
            dialogs.add(dialog);
        });
        lvDialogs.setItems(dialogs);
    }*/

 /*   private void refreshAnswers() {
        if (dial != null) {
            answers = FXCollections.observableArrayList();
            dial.getAnswers().getAnswers().stream().forEach((anss) -> {
                answers.add(anss);
            });
            lvAnswers.setItems(answers);
            taQuestion.setText(dial.getFQuestion().getText());
            fillTokens();
        }
    }*/

   /* private void refreshAns() {

        if (ans == null) {
            pAnswer.setDisable(true);
        } else {
            pAnswer.setDisable(false);
            taAnswer.setText(ans.getText());
            condition = ans.getIfToken();
            if (condition.startsWith("#")) {
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
            }
            function = ans.getEffect();
            if (function.startsWith("editor.goto")) {
                String buff = function.substring(12, function.length() - 1);
                mbFunction.setText(koren.getDialog(Integer.parseInt(buff)).toString());
            }
            if (ans.getCreateToken().isEmpty()) {
                cbTokenRemember.setSelected(false);
                tfTokenRemember.setText("");
                tfTokenRemember.setDisable(true);
            } else {
                cbTokenRemember.setSelected(true);
                for (AnswerToken answerstoken : answerstokens) {
                    if (answerstoken.getContent().equals(ans.getCreateToken())) {
                        tfTokenRemember.setText(answerstoken.getName());
                        break;
                    }
                }
                tfTokenRemember.setDisable(false);
            }
        }

    }*/

    private void refresh() {
        refreshDialogs();
        refreshAnswers();
        refreshAns();
     
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
        File directory = new File("/");                     //TODO
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
        refresh();
        fileName = file.getAbsolutePath().replace(directory.getAbsolutePath() + "\\", "");
        lFile.setText(fileName);
    }

    public void newFile() {
        Element element = new Element("root");
        fileName = dialogWindow("Zadejte název souboru: ");
        lFile.setText(fileName);
        document = new LoadedDocument(new File(fileName), element.getDocument());
        refresh();
    }

    public void saveFile() {
        document.save(new XMLOutputter(), document.source);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Soubor " + fileName + " uložen.");
        alert.showAndWait();
    }

    public void newDialog() {
        String id = dialogWindow("Zadejte id dialogu:");
      //  ans = null;
        actualDialog = new Dialog(id, new Event("Undefined", ""), new ArrayList<>());
        tabChosenDialog.setDisable(false);
        tp2.getSelectionModel().select(2);
        refresh();
    }

    public void editDialog() {
        actualDialog = lvDialogs.getSelectionModel().getSelectedItem();
      //  ans = null;
        refresh();
        tabChosenDialog.setDisable(false);
        tp2.getSelectionModel().select(2);
    }

    public void deleteDialog() {
        document.removeDialog(lvDialogs.getSelectionModel().getSelectedItem().getDialogID());
        refresh();
    }

    public void saveQuestion() { //saveEvent
       //TODO source
       actualDialog.getEvent().setRawText(taQuestion.getText());
        tp3.getSelectionModel().select(2);
    }

    public void saveAnswer() { //saveResponse
        actualResponse = new Response(taAnswer.getText());
        
        if (cbTokenRemember.isSelected()) {
           //TODO exeCute pro zapamatování
        } 
        actualResponse.setTarget(target);
        
        
        refresh();
    }

    public void newAnswer() {
        int id;
        if (answers.size() == 0) {
            id = 0;
        } else {
            id = answers.get(answers.size() - 1).getId() + 1;
        }
        ans = new AnsShard(id, "", "", "", "Odpověď");
        mbCondition.setText("žádná");
        mbFunction.setText("Konec");
        condition = "";
        function = "";
        cbTokenRemember.setSelected(false);
        tfTokenRemember.setDisable(true);
        dial.getAnswers().addAnswer(ans);
        refresh();
    }

    public void checkBox() {
        boolean bool = cbTokenRemember.isSelected();
        tfTokenRemember.setDisable(!bool);
    }

    public void editAnswer() {
        ans = dial.getAnswers().getAnswers().get(lvAnswers.getSelectionModel().getSelectedIndex());
        refresh();
    }

    public void deleteAnswer() {
        if (lvAnswers.getSelectionModel().getSelectedIndex() >= 0) {
            AnsShard buff = dial.getAnswers().getAnswers().get(lvAnswers.getSelectionModel().getSelectedIndex());
            if (ans != null) {
                if (buff.getId() == ans.getId()) {
                    ans = null;
                }
            }
            buff.getElement().detach();
            refresh();
        }
    }

    public void fillTokens() {
        mbCondition.getItems().get(0).setOnAction((ActionEvent t) -> {
            mbCondition.setText("žádná");
            condition = "";
            chbCondition.setDisable(true);
        });
        Menu m = (Menu) mbCondition.getItems().get(1);
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

        mbTarget.getItems().get(0).setOnAction((ActionEvent t) -> {
            mbTarget.setText("Konec");
            target = "exit()";
        });
        Menu m2 = (Menu) mbTarget.getItems().get(1);
        m2.getItems().clear();
        dialogs.stream().forEach((Dialog d) -> {
            MenuItem item = new MenuItem(d.toString());
            item.setOnAction((ActionEvent t) -> {
                mbTarget.setText(d.getDialogID());
                target = d.getDialogID();
            });
            m2.getItems().add(item);
        });
    }
}
