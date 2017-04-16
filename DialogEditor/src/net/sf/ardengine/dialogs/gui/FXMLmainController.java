package net.sf.ardengine.dialogs.gui;

import javafx.scene.control.TextArea;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.ardengine.dialogs.Dialog;
import net.sf.ardengine.dialogs.Event;
import net.sf.ardengine.dialogs.Execute;
import net.sf.ardengine.dialogs.Response;
import net.sf.ardengine.dialogs.cache.LoadedDocument;
import net.sf.ardengine.dialogs.functions.FunctionAttributes;
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
    ScrollPane spAttributes;
    @FXML
    TextField tfConditionL, tfConditionR;
    @FXML
    ChoiceBox chbCondition;
    @FXML
    ComboBox cbFunctions, cbSource;
    @FXML
    MenuButton mbTarget;
    @FXML
    ListView<Dialog> lvDialogs;
    @FXML
    ListView<Response> lvAnswers;
    @FXML
    ListView<Execute> lvExecute;
    @FXML
    TabPane tp1, tp2, tp3;
    @FXML
    Label lFile, lDialog;
    @FXML
    Tab tabChosenDialog, tabDialogs, tabFile;
    @FXML
    Pane pAnswer, pAttr;
    LoadedDocument document;
    Document configXML;
    Dialog actualDialog;
    Response actualResponse;
    String fileName, target, projectFolder, filePath;
    ObservableList<Dialog> dialogs;
    ObservableList<Response> answers;
    ObservableList<Condition> functions, conditions;
    SAXBuilder jdomBuilder;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conditions = FXCollections.observableArrayList();
        functions = FXCollections.observableArrayList();
        conditions.add(new Condition("", "Žádná", false));
        conditions.add(new Condition("EQUALS", "==", false));
        conditions.add(new Condition("EQUALS", "!=", true));
        conditions.add(new Condition("GREATER_THAN", ">", false));
        conditions.add(new Condition("LOWER_THAN", "<", false));
        conditions.add(new Condition("LOWER_THAN", ">=", true));
        conditions.add(new Condition("GREATER_THAN", "<=", true));
        chbCondition.setItems(conditions);
        chbCondition.getSelectionModel().selectFirst();
        jdomBuilder = new SAXBuilder();
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
            FunctionAttributes attrs = actualResponse.getFunctionAttributes();
            tfConditionL.setText(attrs.getAttributeValue("value1"));
            tfConditionR.setText(attrs.getAttributeValue("value2"));
            if (attrs.getAttributeValue(Response.ATTR_CONDITION) != null) {
                switch (attrs.getAttributeValue(Response.ATTR_CONDITION)) {
                    case "EQUALS":
                        if (attrs.getAttributeValue("negate").equals("true")) {
                            chbCondition.getSelectionModel().select(2);
                        } else {
                            chbCondition.getSelectionModel().select(1);
                        }
                        break;
                    case "GREATER_THAN":
                        if (attrs.getAttributeValue("negate").equals("true")) {
                            chbCondition.getSelectionModel().select(6);
                        } else {
                            chbCondition.getSelectionModel().select(3);
                        }
                        break;
                    case "LOWER_THAN":
                        if (attrs.getAttributeValue("negate").equals("true")) {
                            chbCondition.getSelectionModel().select(5);
                        } else {
                            chbCondition.getSelectionModel().select(4);
                        }
                        break;
                    default:
                        chbCondition.getSelectionModel().select(0);
                        tfConditionL.setText("");
                        tfConditionR.setText("");
                        break;
                }
            } else {
                chbCondition.getSelectionModel().select(0);
                tfConditionL.setText("");
                tfConditionR.setText("");
            }
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

    private void dialogWarning(String text) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Warning Dialog");
        alert.setHeaderText("Varování!");
        alert.setContentText(text);
        alert.showAndWait();
    }

    public void openProject() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setTitle("JavaFX Projects");
        File defaultDirectory = new File(System.getProperty("user.home"));
        chooser.setInitialDirectory(defaultDirectory);
        
        projectFolder = (chooser.showDialog((Stage) (ap.getScene().getWindow()))).getAbsolutePath();
        tabDialogs.setDisable(false);
        tabFile.setDisable(false);
        System.out.println(projectFolder);
        File configFile = new File(projectFolder + File.separator+".configuration"+File.separator+"functions.xml");
        File sourcesFile = new File(projectFolder + File.separator +".configuration"+File.separator+"sources.xml");
        Document sourcesXML;
        try {
            configXML = jdomBuilder.build(configFile);
            sourcesXML = jdomBuilder.build(sourcesFile);
            ObservableList<String> sources = FXCollections.observableArrayList();
            sourcesXML.getRootElement().getChildren().forEach((s) -> {
                sources.add(s.getAttributeValue("name"));
            });
            cbSource.setItems(sources);
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(FXMLmainController.class.getName()).log(Level.SEVERE, null, ex);
            //throw some cool exception
        }

        functions.clear();
        configXML.getRootElement().getChildren().forEach((func) -> {
            Condition con = new Condition(func.getAttributeValue("name"), func.getChildText("description"), false);
            func.getChild("compulsory").getChildren().forEach((arg) -> {
                con.addArgument(new Argument(arg.getAttributeValue("name"), arg.getChildText("description"), false));
            });
            func.getChild("optional").getChildren().forEach((arg) -> {
                con.addArgument(new Argument(arg.getAttributeValue("name"), arg.getChildText("description"), true));
            });
            functions.add(con);
        });
        /*  for (int i = 3; i < functions.size(); i++) {
            conditions.add(functions.get(i));
        }*/
        cbFunctions.setItems(functions);
        cbFunctions.getSelectionModel().select(0);
        tp1.getSelectionModel().select(1);
    }

    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Otevřít dialogový soubor.");
        File directory = new File(projectFolder);
        fileChooser.setInitialDirectory(directory);
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML", "*.xml"));
        File file = fileChooser.showOpenDialog((Stage) (ap.getScene().getWindow()));
        try {
            document = new LoadedDocument(file, jdomBuilder.build(file));
        } catch (JDOMException | IOException ex) {
            Logger.getLogger(FXMLmainController.class.getName()).log(Level.SEVERE, null, ex);
            //throw some cool exception
        }
        refreshDialogs();
        refreshAnswers();
        refreshAns();
        filePath = file.getAbsolutePath();
        fileName = file.getAbsolutePath().replace(directory.getAbsolutePath() + File.separator, "");
        lFile.setText(fileName);
        fillTargets();
        tp1.getSelectionModel().select(2);
    }

    public void newFile() {
        fileName = dialogWindow("Zadejte název souboru: "); //TODO cesta
        lFile.setText(fileName + ".xml");
        document = new LoadedDocument(new File(projectFolder + File.separator + fileName + ".xml"), new Document(new Element("root")));
        filePath = document.source.getAbsolutePath();
        filePath = filePath.substring(0, filePath.length() - 4);
        fillTargets();
        refreshDialogs();
        refreshAnswers();
        refreshAns();
        tp1.getSelectionModel().select(2);
    }

    public void saveFile() {
        if (actualDialog!=null) {
            document.addOrRefreshDialog(actualDialog);
        }
        document.save(new XMLOutputter(Format.getPrettyFormat()), document.source);
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText(null);
        alert.setContentText("Soubor " + fileName + " uložen.");
        alert.showAndWait();
    }

    public void newDialog() {
        String id = dialogWindow("Zadejte id dialogu:");
        lDialog.setText(id);
        addDialogToMenu(id);
        actualResponse = null;
        actualDialog = new Dialog(id, new Event("", "Undefined"), new ArrayList<>());
        tabChosenDialog.setDisable(false);
        document.addOrRefreshDialog(actualDialog);
        //   tp2.getSelectionModel().select(2);
        refreshDialogs();
        refreshAnswers();
        refreshAns();
        refreshExecutes();
    }

    public void editDialog() {
        actualDialog = lvDialogs.getSelectionModel().getSelectedItem();
        lDialog.setText(actualDialog.getDialogID());
        cbSource.getSelectionModel().select(actualDialog.getEvent().getSourceID());
        actualResponse = null;
        refreshExecutes();
        refreshAnswers();
        refreshAns();
        tabChosenDialog.setDisable(false);
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
        actualDialog.getEvent().setSourceID((String) cbSource.getSelectionModel().getSelectedItem());
        actualDialog.getEvent().setRawText(taQuestion.getText());
        tp3.getSelectionModel().select(2);
        document.addOrRefreshDialog(actualDialog);
    }

    public void saveAnswer() { //saveResponse
        actualResponse.setRawText(taAnswer.getText());

        //not gonna work anymore..
        /* if (cbTokenRemember.isSelected()) {
        //TODO exeCute pro zapamatování
        //Execute execute = new Execute(VariableSaveFunction.NAME);
        //execute.getFunctionAttributes().setAttribute(IFunction.ATTR_TARGET, filePath.replace(projectFolder + "//", "responses:").replace("/", "."));
        }*/
        FunctionAttributes attrs = actualResponse.getFunctionAttributes();
        if (!chbCondition.getSelectionModel().isSelected(0)) {
            Condition selected = (Condition) chbCondition.getSelectionModel().getSelectedItem();
            attrs.setAttribute(Response.ATTR_CONDITION, selected.functionName);
            attrs.setAttribute("value1", tfConditionL.getText());
            attrs.setAttribute("value2", tfConditionR.getText());
            attrs.setAttribute("negate", selected.negate ? "true" : "false");
        } else if (attrs.getAttributeValue(Response.ATTR_CONDITION) != null) {
            attrs.setAttribute(Response.ATTR_CONDITION, "");
        }

        actualResponse.setTarget(target);
        document.addOrRefreshDialog(actualDialog);
        refreshAnswers();
        refreshAns();
    }

    public void newAnswer() { //newResponse

        actualResponse = new Response("...");
        //   mbCondition.setText("žádná");
        mbTarget.setText("Konec");
        chbCondition.getSelectionModel().selectFirst();
        tfConditionL.setText("");
        tfConditionR.setText("");
        target = "exit()";
        //cbTokenRemember.setSelected(false);
        actualDialog.addResponse(actualResponse);
        refreshAnswers();
        refreshAns();
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
                    target = filePath.replace(projectFolder + File.separator, "");
                    target = target.replace(".xml", ":" + id);
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
            target = filePath.replace(projectFolder + File.separator, "");
            target = target.replace(".xml", ":" + id);
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

    public void addExecute() {
        if (actualDialog != null) {
            Condition con = (Condition) cbFunctions.getSelectionModel().getSelectedItem();
            pAttr.getChildren().clear();

            List<Argument> arguments = con.getArgumentList();
            for(int i=0; i < arguments.size(); i++){
                createArgumentComponents(arguments.get(i), i);
            }
        } else {
            dialogWarning("Není zvolen dialog. Upravte existující nebo vytvořte nový.");
        }
    }
    
    private void createArgumentComponents(Argument arg, int offset){
        Label label = new Label(arg.name + (arg.optional ? "" : "*"));
        label.setLayoutY(offset * 25);
        label.setId("");
        pAttr.getChildren().add(label);
        
        TextField text = new TextField();
        text.setId(arg.name);
        text.promptTextProperty().setValue(arg.desc);
        text.setLayoutX(80);
        text.setPrefWidth(320);
        text.setLayoutY(offset * 25);
        offset++;
        pAttr.getChildren().add(text);
    }

    public void saveExecute() {
        if (actualDialog != null) {
            Execute exe = new Execute(((Condition) cbFunctions.getSelectionModel().getSelectedItem()).functionName);
            pAttr.getChildren().forEach((node) -> {
                if (!node.idProperty().getValue().isEmpty()) {
                    TextField text = (TextField) node;
                    if (!text.getText().isEmpty()) {
                        exe.getFunctionAttributes().setAttribute(text.getId(), text.getText());
                    }
                }
            });
            actualDialog.getEvent().addExecute(exe);
            document.addOrRefreshDialog(actualDialog);
            refreshExecutes();
        } else {
            dialogWarning("Není zvolen dialog. Upravte existující nebo vytvořte nový.");
        }
    }

    public void deleteExecute() {
        if (lvExecute.getSelectionModel().getSelectedItem() != null) {
            actualDialog.getEvent().removeExecute(lvExecute.getSelectionModel().getSelectedItem());
            lvExecute.getItems().remove(lvExecute.getSelectionModel().getSelectedItem());
        } else {
            dialogWarning("Není vybrán žádný execute.");
        }

    }

    private void refreshExecutes() {
        pAttr.getChildren().clear();
        ObservableList<Execute> executes = FXCollections.observableArrayList();
        actualDialog.getEvent().getAllExecutes().forEach((t) -> {
            executes.add(t);
        });
        lvExecute.setItems(executes);
    }

    private class Condition {

        String desc, functionName;
        List<Argument> list;
        boolean negate;

        Condition(String name, String desc, boolean negate) {
            this.functionName = name;
            this.desc = desc;
            this.negate = negate;
            list = new ArrayList<>();
        }

        public void addArgument(Argument a) {
            list.add(a);
        }

        public List<Argument> getArgumentList() {
            return list;
        }

        public boolean isBasicCondition() {
            return list.isEmpty();
        }

        @Override
        public String toString() {
            if (isBasicCondition()) {
                return desc;
            }else{
                return functionName;
            }
        }

    }

    private class Argument {

        String name, desc;
        boolean optional;

        Argument(String name, String desc, boolean optional) {
            this.name = name;
            this.desc = desc;
            this.optional = optional;
        }

        @Override
        public String toString() {
            return desc;
        }

    }
}
