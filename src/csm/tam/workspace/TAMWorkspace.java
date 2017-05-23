package csm.tam.workspace;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.CourseSiteManagerStyle;
import csm.tam.data.TAData;
import csm.tam.data.TeachingAssistant;
import csm.tam.style.TAMStyle;
import csm.ui.CourseSiteManagerWorkspace;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import javafx.application.Platform;
import javafx.beans.property.StringProperty;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import jtps.jTPS_Transaction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Predicate;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class TAMWorkspace extends AppWorkspaceComponent {

    public final ComboBox<String> startTimesBox = new ComboBox<>();
    public final ComboBox<String> endTimesBox = new ComboBox<>();
    public final GridPane officeHoursGridPane;
    public final Button clearButton;
    public final Button addButton;
    public final Button changeButton;
    public final Label tasHeaderLabel;
    public final Button minusButton;
    final CourseSiteManagerApp app;
    final VBox contentPane = new VBox();
    private final HBox tasHeaderBox;
    private final TableView<TeachingAssistant> taTable;
    private final TableColumn nameColumn;
    private final TableColumn emailColumn;
    private final HBox addBox;
    private final TextField nameTextField;
    private final TextField emailTextField;
    private final HBox officeHoursHeaderBox;
    private final Label officeHoursHeaderLabel;
    private final Label startLabel;
    private final Label endLabel;
    private final TableColumn undergradColumn;
    TAMController controller;
    HashMap<String, Pane> officeHoursGridTimeHeaderPanes;
    HashMap<String, Label> officeHoursGridTimeHeaderLabels;
    HashMap<String, Pane> officeHoursGridDayHeaderPanes;
    HashMap<String, Label> officeHoursGridDayHeaderLabels;
    HashMap<String, Pane> officeHoursGridTimeCellPanes;
    HashMap<String, Label> officeHoursGridTimeCellLabels;
    HashMap<String, Pane> officeHoursGridTACellPanes;
    HashMap<String, Label> officeHoursGridTACellLabels;
    private EventHandler<MouseEvent> oldHandler;

    public TAMWorkspace(CourseSiteManagerApp app) {
        this.app = app;
        controller = new TAMController(app, this);
        tasHeaderBox = new HBox();
        tasHeaderLabel = new Label(TAS_HEADER_TEXT);
        minusButton = CourseSiteManagerWorkspace.makeMinusButton();

        tasHeaderBox.getChildren().addAll(tasHeaderLabel, minusButton);

        // MAKE THE TABLE AND SETUP THE DATA MODEL
        taTable = new TableView();
        taTable.setEditable(true);
        taTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        String nameColumnText = NAME_COLUMN_TEXT;
        String emailColumnText = EMAIL_COLUMN_TEXT;
        String undergradTAColumnText = UNDERGRAD_COLUMN_TEXT;
        nameColumn = new TableColumn(nameColumnText);
        nameColumn.setCellValueFactory(
          new PropertyValueFactory<TeachingAssistant, String>("name")
        );
        emailColumn = new TableColumn(emailColumnText);
        emailColumn.setCellValueFactory(
          new PropertyValueFactory<TeachingAssistant, String>("email")
        );
        undergradColumn = new TableColumn(undergradTAColumnText);
        undergradColumn.setEditable(true);
        undergradColumn.setCellFactory(
          c -> new CheckBoxTableCell<TeachingAssistant, Boolean>());
        undergradColumn.setCellValueFactory(
          new PropertyValueFactory<TeachingAssistant, Boolean>("undergraduate")
        );

        nameColumn.prefWidthProperty()
                  .bind(taTable.widthProperty().multiply(0.4));
        emailColumn.prefWidthProperty()
                   .bind(taTable.widthProperty().multiply(0.4));
        undergradColumn.prefWidthProperty()
                       .bind(taTable.widthProperty().multiply(0.2));

        taTable.getColumns().add(nameColumn);
        taTable.getColumns().add(emailColumn);
        taTable.getColumns().add(0, undergradColumn);

        taTable.setItems(
          ((CourseSiteManagerData) app.getDataComponent()).getTaData()
                                                          .getTeachingAssistants());

        // ADD BOX FOR ADDING A TA
        String namePromptText = NAME_PROMPT_TEXT;
        String emailPromptText = EMAIL_PROMPT_TEXT;
        String addButtonText = ADD_BUTTON_TEXT;
        nameTextField = new TextField();
        nameTextField.setPromptText(namePromptText);
        emailTextField = new TextField();
        emailTextField.setPromptText(emailPromptText);
        addButton = new Button(addButtonText);
        changeButton = new Button(TAM_CHANGE_BUTTON);
        clearButton = new Button(RDE_CLEAR_BUTTON);
        clearButton.setDisable(true);
        addBox = new HBox();
        nameTextField.prefWidthProperty()
                     .bind(addBox.widthProperty().multiply(.4));
        addButton.prefWidthProperty().bind(addBox.widthProperty().multiply(.2));
        clearButton.prefWidthProperty()
                   .bind(addBox.widthProperty().multiply(0.2));
        addBox.getChildren().add(nameTextField);
        addBox.getChildren().add(emailTextField);
        addBox.getChildren().add(addButton);
        addBox.getChildren().add(clearButton);

        // INIT THE HEADER ON THE RIGHT
        officeHoursHeaderBox = new HBox();
        String officeHoursGridText = OFFICE_HOURS_SUBHEADER;
        officeHoursHeaderLabel = new Label(officeHoursGridText);
        officeHoursHeaderBox.getChildren().add(officeHoursHeaderLabel);


        // THESE WILL STORE PANES AND LABELS FOR OUR OFFICE HOURS GRID
        officeHoursGridPane = new GridPane();
        officeHoursGridTimeHeaderPanes = new HashMap();
        officeHoursGridTimeHeaderLabels = new HashMap();
        officeHoursGridDayHeaderPanes = new HashMap();
        officeHoursGridDayHeaderLabels = new HashMap();
        officeHoursGridTimeCellPanes = new HashMap();
        officeHoursGridTimeCellLabels = new HashMap();
        officeHoursGridTACellPanes = new HashMap();
        officeHoursGridTACellLabels = new HashMap();

        // ORGANIZE THE LEFT AND RIGHT PANES
        VBox leftPane = new VBox();
        leftPane.getChildren().add(tasHeaderBox);
        leftPane.getChildren().add(taTable);
        leftPane.getChildren().add(addBox);
        VBox rightPane = new VBox();
        rightPane.getChildren().add(officeHoursHeaderBox);
        rightPane.getChildren().add(officeHoursGridPane);

        // BOTH PANES WILL NOW GO IN A SPLIT PANE
        SplitPane sPane = new SplitPane(leftPane, new ScrollPane(rightPane));
        //workspace = new BorderPane();


        // COMBO BOXES FOR HOURS SET UP
        Platform.runLater(() -> initComboBox(startTimesBox));
        Platform.runLater(() -> initComboBox(endTimesBox));

        startTimesBox.setOnAction(e -> controller.handleChooseStartTime());
        endTimesBox.setOnAction(e -> controller.handleChooseEndTime());

        minusButton.setOnAction(e -> {
            controller.handleRemoveTaAction(getTaTable().getSelectionModel()
                                                        .getSelectedItem());
        });

        // AND PUT EVERYTHING IN THE WORKSPACE
        //((BorderPane) workspace).setCenter(sPane);

        // PLACE THE GRID HOUR COMBO BOX THINGS
        // SPACING THEM OUT TO LOOK GOOD
        Region spacer = new Region();
        startLabel = new Label(START_LABEL_TEXT);
        endLabel = new Label(END_LABEL_TEXT);
        spacer.setMinWidth(45);
        spacer.setMaxWidth(60);
        officeHoursHeaderBox.getChildren()
                            .addAll(spacer, startLabel, startTimesBox,
                              endLabel, endTimesBox);


        contentPane.getChildren().add(sPane);

        // CONTROLS

        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
        }

        nameTextField.setOnAction(e -> controller.handleAddTA());
        addButton.setOnAction(e -> controller.handleAddTA());
        clearButton.setOnAction(e -> controller.clearAddTAFields());

        // TODO: Back to delete
        taTable.setOnKeyPressed((e) -> {
            if (e.getCode() == KeyCode.DELETE) {
                controller.handleRemoveTaAction(taTable.getSelectionModel()
                                                       .getSelectedItem());
            }
        });

        nameTextField.addEventHandler(KeyEvent.KEY_PRESSED,
          event -> {
              if (event.getCode() == KeyCode.ENTER) {
                  controller.handleAddTA();
              }
          });

        emailTextField.addEventHandler(KeyEvent.KEY_PRESSED,
          event -> {
              if (event.getCode() == KeyCode.ENTER) {
                  controller.handleAddTA();
              }
          });

        app.getGUI().getWindow().addEventHandler(KeyEvent.KEY_PRESSED, e -> {
            if (e.getCode() == KeyCode.Z && e.isControlDown()) {
                app.getGUI().getTps().undoTransaction();
            } else if (e.getCode() == KeyCode.Y && e.isControlDown()) {
                app.getGUI().getTps().redoTransaction();
            }

        });

        taTable.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            TeachingAssistant ta = taTable.getSelectionModel()
                                          .getSelectedItem();
            if (ta != null) {
                clearButton.setDisable(false);
                nameTextField.setText(ta.getName());
                emailTextField.setText(ta.getEmail());

                addBox.getChildren().remove(addButton);
                if (oldHandler != null) {
                    changeButton.removeEventHandler(MouseEvent.MOUSE_CLICKED,
                      oldHandler);
                }

                oldHandler = event -> {
                    jTPS_Transaction action = new jTPS_Transaction() {

                        String oldName = ta.getName();
                        String oldEmail = ta.getEmail();

                        String redoName, redoEmail;

                        @Override
                        public void undoTransaction() {
                            updateTADataInGrid(ta, ta.getName(), oldName);
                            app.getGUI()
                               .getFileController()
                               .markAsEdited(app.getGUI());
                            ta.setName(oldName);
                            ta.setEmail(oldEmail);
                            addBox.getChildren().remove(addButton);
                            if (!addBox.getChildren().contains(changeButton)) {
                                addBox.getChildren().add(2, changeButton);
                            }
                            nameTextField.setText(oldName);
                            emailTextField.setText(oldEmail);
                            nameTextField.requestFocus();
                            taTable.getSelectionModel().select(ta);
                            clearButton.setDisable(false);
                            Collections.sort(taTable.getItems());
                            taTable.refresh();
                        }

                        @Override
                        public void redoTransaction() {
                            app.getGUI()
                               .getFileController()
                               .markAsEdited(app.getGUI());
                            updateTADataInGrid(ta, ta.getName(), redoName);
                            ta.setName(redoName);
                            ta.setEmail(redoEmail);
                            addBox.getChildren().remove(changeButton);
                            if (!addBox.getChildren().contains(addButton)) {
                                addBox.getChildren().add(2, addButton);
                            }
                            nameTextField.clear();
                            emailTextField.clear();
                            taTable.getSelectionModel().clearSelection();
                            clearButton.setDisable(true);
                            nameTextField.requestFocus();
                            Collections.sort(taTable.getItems());
                            taTable.refresh();
                        }

                        @Override
                        public void doTransaction() {
                            app.getGUI()
                               .getFileController()
                               .markAsEdited(app.getGUI());
                            updateTADataInGrid(ta, ta.getName(),
                              nameTextField.getText());
                            redoName = nameTextField.getText();
                            redoEmail = emailTextField.getText();
                            ta.setName(redoName);
                            ta.setEmail(redoEmail);
                            addBox.getChildren().remove(changeButton);
                            if (!addBox.getChildren().contains(addButton)) {
                                addBox.getChildren().add(2, addButton);
                            }
                            nameTextField.clear();
                            emailTextField.clear();
                            nameTextField.requestFocus();
                            taTable.getSelectionModel().clearSelection();
                            clearButton.setDisable(true);
                            Collections.sort(taTable.getItems());
                            taTable.refresh();
                        }
                    };
                    app.getGUI().getTps()
                       .addTransaction(
                         action);
                };

                changeButton.addEventHandler(MouseEvent.MOUSE_CLICKED,
                  oldHandler);

                if (!addBox.getChildren().contains(changeButton)) {
                    addBox.getChildren().add(2, changeButton);
                }

            }
        });
    }

    private void updateTADataInGrid(TeachingAssistant ta, String oldName, String newName)
    {
        HashMap<String, StringProperty> oh = ((CourseSiteManagerData) app.getDataComponent())
          .getTaData()
          .getOfficeHours();
        for (StringProperty value : oh.values()) {
            String[] tasInCell = value.getValue().split("\n");
            // Predicate used to check every TA Name in the cell for a match to remove
            // This was necessary because equals was too narrow and contains was too broad
            // equals failed to match due to new lines and contains would delete
            // Billie Mays Hays when Billie Mays was selected.
            Predicate<String> p = (String t) -> t.equals(ta.getName());
            if (Arrays.stream(tasInCell).anyMatch(p)) {
                value.setValue(value.getValue().replace(oldName, newName));
            }
        }
    }

    private void initComboBox(ComboBox<String> box) {

        box.getItems().add("12:00 AM");

        for (int i = 1; i < 12; i++) {
            box.getItems().add(String.format("%02d:00 AM", i));
        }

        box.getItems().add("12:00 PM");

        for (int i = 1; i < 12; i++) {
            box.getItems().add(String.format("%02d:00 PM", i));
        }

        int start = ((CourseSiteManagerData) app.getDataComponent()).getTaData()
                                                                    .getStartHour();
        int end = ((CourseSiteManagerData) app.getDataComponent()).getTaData()
                                                                  .getEndHour();


        // SICNE WE ARE USING HOURS FROM 0 TO 23 AND THE ITEMS ARE INDEXED
        // ON 0 TO 23 WE CAN SIMPLY SELECT THE INDEX CORRESPONDING TO THE HOUR
        // STORED IN TADATA
        startTimesBox.getSelectionModel().select(start);
        ((CourseSiteManagerData) app.getDataComponent()).getTaData()
                                                        .startHourProperty()
                                                        .addListener(
                                                          (observable, oldValue, newValue) -> startTimesBox
                                                            .getSelectionModel()
                                                            .select(newValue.intValue()));
        endTimesBox.getSelectionModel().select(end);
        ((CourseSiteManagerData) app.getDataComponent()).getTaData()
                                                        .endHourProperty()
                                                        .addListener(
                                                          (observable, oldValue, newValue) -> endTimesBox
                                                            .getSelectionModel()
                                                            .select(newValue.intValue()));
    }

    public TableView<TeachingAssistant> getTaTable() {
        return taTable;
    }

    public VBox getContentPane() {
        return contentPane;
    }

    @Override
    public void resetWorkspace() {
        // CLEAR OUT THE GRID PANE
        officeHoursGridPane.getChildren().clear();

        // AND THEN ALL THE GRID PANES AND LABELS
        officeHoursGridTimeHeaderPanes.clear();
        officeHoursGridTimeHeaderLabels.clear();
        officeHoursGridDayHeaderPanes.clear();
        officeHoursGridDayHeaderLabels.clear();
        officeHoursGridTimeCellPanes.clear();
        officeHoursGridTimeCellLabels.clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();

        if (!clearButton.isDisabled()) {
            clearButton.fire();
        }
    }

    @Override
    public void reloadWorkspace(AppDataComponent dataComponent) {
        TAData taData = ((CourseSiteManagerData) dataComponent).getTaData();

        //Platform.runLater(() -> reloadOfficeHoursGrid(taData));
        reloadOfficeHoursGrid(taData);
    }

    public void reloadOfficeHoursGrid(TAData dataComponent) {
        ArrayList<String> gridHeaders = dataComponent.getGridHeaders();


        // CLEAR THE OLD VALUES SINCE WE ARE *RE*LOADING

        officeHoursGridPane.getChildren().clear();
        officeHoursGridTACellPanes.clear();
        officeHoursGridTACellLabels.clear();

        // ADD THE TIME HEADERS
        for (int i = 0; i < 2; i++) {
            addCellToGrid(dataComponent, officeHoursGridTimeHeaderPanes,
              officeHoursGridTimeHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }

        // THEN THE DAY OF WEEK HEADERS
        for (int i = 2; i < 7; i++) {
            addCellToGrid(dataComponent, officeHoursGridDayHeaderPanes,
              officeHoursGridDayHeaderLabels, i, 0);
            dataComponent.getCellTextProperty(i, 0).set(gridHeaders.get(i));
        }

        // THEN THE TIME AND TA CELLS
        int row = 1;
        for (int i = dataComponent.getStartHour();
          i < dataComponent.getEndHour(); i++) {
            // START TIME COLUMN
            int col = 0;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes,
              officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row)
                         .set(buildCellText(i, "00"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes,
              officeHoursGridTimeCellLabels, col, row + 1);
            dataComponent.getCellTextProperty(col, row + 1)
                         .set(buildCellText(i, "30"));

            // END TIME COLUMN
            col++;
            int endHour = i;
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes,
              officeHoursGridTimeCellLabels, col, row);
            dataComponent.getCellTextProperty(col, row)
                         .set(buildCellText(endHour, "30"));
            addCellToGrid(dataComponent, officeHoursGridTimeCellPanes,
              officeHoursGridTimeCellLabels, col, row + 1);
            dataComponent.getCellTextProperty(col, row + 1)
                         .set(buildCellText(endHour + 1, "00"));
            col++;

            // AND NOW ALL THE TA TOGGLE CELLS
            while (col < 7) {
                addCellToGrid(dataComponent, officeHoursGridTACellPanes,
                  officeHoursGridTACellLabels, col, row);
                addCellToGrid(dataComponent, officeHoursGridTACellPanes,
                  officeHoursGridTACellLabels, col, row + 1);
                col++;
            }
            row += 2;
        }

        // CONTROLS FOR TOGGLING TA OFFICE HOURS
        for (Pane p : officeHoursGridTACellPanes.values()) {
            p.setOnMouseClicked(e -> {
                controller.handleCellToggle((Pane) e.getSource());
            });
        }

        TAMStyle style = ((CourseSiteManagerStyle) app.getStyleComponent()).taStyle;
        style.initOfficeHoursGridStyle();

    }

    public void addCellToGrid(TAData dataComponent, HashMap<String, Pane> panes,
      HashMap<String, Label> labels, int col, int row)
    {
        // MAKE THE LABEL IN A PANE
        StringProperty cell = dataComponent.getOfficeHours()
                                           .get(dataComponent.getCellKey(col, row));
        String currentValue;
        if (cell == null) {
            currentValue = "";
        } else {
            currentValue = cell.getValue();
        }
        Label cellLabel = new Label(currentValue);
        HBox cellPane = new HBox();
        cellPane.setAlignment(Pos.CENTER);
        cellPane.getChildren().add(cellLabel);

        // BUILD A KEY TO EASILY UNIQUELY IDENTIFY THE CELL
        String cellKey = dataComponent.getCellKey(col, row);
        cellPane.setId(cellKey);
        cellLabel.setId(cellKey);

        // EVENT HANDLER TO HIGHLIGHT BOX WHEN HOVERING
        cellPane.setOnMouseEntered(e -> {
            controller.handleHoverGridPane(col, row, cellPane,
              officeHoursGridPane);
        });

        cellPane.setOnMouseExited(e -> {
            controller.handleHoverOffGridPane(col, row, cellPane,
              officeHoursGridPane);
        });

        // NOW PUT THE CELL IN THE WORKSPACE GRID
        officeHoursGridPane.add(cellPane, col, row);

        // AND ALSO KEEP IN IN CASE WE NEED TO STYLIZE IT
        panes.put(cellKey, cellPane);
        labels.put(cellKey, cellLabel);

        // AND FINALLY, GIVE THE TEXT PROPERTY TO THE DATA MANAGER
        // SO IT CAN MANAGE ALL CHANGES
        dataComponent.setCellProperty(col, row, cellLabel.textProperty());

    }

    public String buildCellText(int militaryHour, String minutes) {
        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutes;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    public ComboBox<String> getStartTimesBox() {
        return startTimesBox;
    }

    public ComboBox<String> getEndTimesBox() {
        return endTimesBox;
    }

    public Button getChangeButton() {
        return changeButton;
    }

    public TAMController getController() {
        return controller;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeHeaderPanes() {
        return officeHoursGridTimeHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeHeaderLabels() {
        return officeHoursGridTimeHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridDayHeaderPanes() {
        return officeHoursGridDayHeaderPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridDayHeaderLabels() {
        return officeHoursGridDayHeaderLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTimeCellPanes() {
        return officeHoursGridTimeCellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTimeCellLabels() {
        return officeHoursGridTimeCellLabels;
    }

    public HashMap<String, Pane> getOfficeHoursGridTACellPanes() {
        return officeHoursGridTACellPanes;
    }

    public HashMap<String, Label> getOfficeHoursGridTACellLabels() {
        return officeHoursGridTACellLabels;
    }

    public GridPane getOfficeHoursGridPane() {
        return officeHoursGridPane;
    }

    public HBox getTasHeaderBox() {
        return tasHeaderBox;
    }

    public TableColumn getNameColumn() {
        return nameColumn;
    }

    public TableColumn getEmailColumn() {
        return emailColumn;
    }

    public HBox getAddBox() {
        return addBox;
    }

    public TextField getNameTextField() {
        return nameTextField;
    }

    public TextField getEmailTextField() {
        return emailTextField;
    }

    public Button getClearButton() {
        return clearButton;
    }

    public Button getAddButton() {
        return addButton;
    }

    public HBox getOfficeHoursHeaderBox() {
        return officeHoursHeaderBox;
    }

    public Label getOfficeHoursHeaderLabel() {
        return officeHoursHeaderLabel;
    }

    public Label getStartLabel() {
        return startLabel;
    }

    public Label getEndLabel() {
        return endLabel;
    }

    public AppTemplate getApp() {
        return app;
    }
}
