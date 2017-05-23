package csm.tam.workspace;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.rde.data.RDEData;
import csm.tam.data.TAData;
import csm.tam.data.TeachingAssistant;
import csm.ui.CourseSiteManagerWorkspace;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

import java.util.*;
import java.util.function.Predicate;

import static csm.CourseSiteManagerProp.*;
import static csm.tam.style.TAMStyle.*;
import static java.lang.Integer.parseInt;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class TAMController {
    private CourseSiteManagerApp app;
    private TAMWorkspace workspace;

    public TAMController(CourseSiteManagerApp app, TAMWorkspace workspace)
    {
        this.app = app;
        this.workspace = workspace;
    }

    /**
     * This function provides a response for when the user clicks
     * on the office hours grid to add or remove a TA to a time slot.
     *
     * @param pane The pane that was toggled.
     */
    public void handleCellToggle(Pane pane) {
        // GET THE TABLE
        TableView<TeachingAssistant> taTable = workspace.getTaTable();

        // IS A TA SELECTED IN THE TABLE?
        Object selectedItem = taTable.getSelectionModel().getSelectedItem();

        // GET THE TA
        TeachingAssistant ta = (TeachingAssistant) selectedItem;
        if (ta == null) {
            return;
        }
        String taName = ta.getName();
        TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();
        String cellKey = pane.getId();

        // AND TOGGLE THE OFFICE HOURS IN THE CLICKED CELL
        data.toggleTAOfficeHours(cellKey, taName);
        app.getGUI().getFileController().markAsEdited(app.getGUI());


    }

    public void handleHoverGridPane(int col, int row, HBox cellPane,
      GridPane officeHoursGridPane)
    {
        ObservableList<Node> children = officeHoursGridPane.getChildren();
        if (col == 0 || col == 1 || row == 0) {
            return;
        }
        for (Node child : children) {
            /* All children get the 'border pane' style except the cellPane
               itself. it gets a different style so we have to skip it otherwise
               there are conflicts */
            int currentCol = GridPane.getColumnIndex(child);
            int currentRow = GridPane.getRowIndex(child);

            if ((currentCol == 0 || currentCol == 1) && currentRow == row) {
                child.getStyleClass().add(CLASS_HOVER_ON_TIME_PANE);
            } else if (currentCol == col && child != cellPane &&
                       currentRow < row)
            {
                child.getStyleClass()
                     .add(CLASS_HOVER_ON_VERTICAL_BORDER_PANE);
            } else if (currentRow == row && child != cellPane &&
                       currentCol < col)
            {
                child.getStyleClass()
                     .add(CLASS_HOVER_ON_HORIZONTAL_BORDER_PANE);
            }
        }
        cellPane.getStyleClass().add(CLASS_HOVER_ON_PANE);
    }

    public void handleHoverOffGridPane(int col, int row, HBox cellPane,
      GridPane officeHoursGridPane)
    {
        if (col == 0 || row == 0 || col == 1) {
            // if the cell is not a valid, changeable cell then don't
            // highlight it
            return;
        }
        ObservableList<Node> children = officeHoursGridPane.getChildren();
        for (Node child : children) {
            if (GridPane.getColumnIndex(child) == col) {
                child.getStyleClass()
                     .remove(CLASS_HOVER_ON_VERTICAL_BORDER_PANE);
                child.getStyleClass()
                     .remove(CLASS_HOVER_ON_TIME_PANE);
            } else if (GridPane.getRowIndex(child) == row) {
                child.getStyleClass()
                     .remove(CLASS_HOVER_ON_TIME_PANE);
                child.getStyleClass()
                     .remove(CLASS_HOVER_ON_HORIZONTAL_BORDER_PANE);
            }
        }
        cellPane.getStyleClass().remove(CLASS_HOVER_ON_PANE);
    }

    public void handleChooseEndTime() {
        PropertiesManager pm = PropertiesManager.getPropertiesManager();
        TAMWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getTaManagerTab()
          .getWorkspace();
        TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();

        jTPS_Transaction chooseEndTimeAction = new jTPS_Transaction() {

            int oldStartHour, oldEndHour, newEndHour;
            HashMap<String, String> previousData = new HashMap<>();
            HashMap<String, String> redoData;

            @Override
            public void doTransaction() {
                ComboBox<String> end = workspace.endTimesBox;
                String rawEndString = end.getSelectionModel().getSelectedItem();
                if (rawEndString == null || rawEndString.isEmpty()) {
                    return;
                }

                String endTime = rawEndString.substring(0, 2);
                endTime = rawEndString.contains("PM") ?
                          String.valueOf(parseInt(endTime) + 12) :
                          endTime;
                newEndHour = parseInt(endTime);

                if (newEndHour == 24) {
                    endTime = "12";
                    newEndHour = 12;
                }

                if (newEndHour <= data.getStartHour()) {
                    AppMessageDialogSingleton s = AppMessageDialogSingleton.getSingleton();
                    s.show(pm.getProperty(
                      TA_GRID_INVALID_START_END_TIME_TITLE.toString()),
                      pm.getProperty(
                        TA_GRID_INVALID_START_END_TIME_MESSAGE.toString()));
                    return;
                }

                oldStartHour = data.getStartHour();
                oldEndHour = data.getEndHour();

                data.getOfficeHours().entrySet().forEach(item ->
                  previousData.put(item.getKey(), item.getValue().getValue()));

                HashMap<String, String> gridMapping = getNewGripMapping(0,
                  (newEndHour - oldStartHour) * 2);

                redoData = gridMapping;

                if (gridMapping == null) {
                    workspace.getEndTimesBox()
                             .getSelectionModel()
                             .select(oldEndHour);
                    return;
                }

                data.setEndHour(newEndHour);

                workspace.reloadWorkspace(app.getDataComponent());

                repopulateOfficeHoursGrid(gridMapping);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                replaceWithHours((oldEndHour - oldStartHour) * 2, oldEndHour,
                  previousData);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                replaceWithHours((newEndHour - oldStartHour) * 2, newEndHour,
                  redoData);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            private void replaceWithHours(int newRowCount, int newHour,
              HashMap<String, String> gridMapping)
            {

                if (gridMapping == null) {
                    EventHandler<ActionEvent> actionE = workspace.getEndTimesBox()
                                                                 .getOnAction();
                    workspace.getEndTimesBox().setOnAction(e -> {
                    });
                    workspace.getEndTimesBox()
                             .getSelectionModel()
                             .select(newHour);
                    workspace.getEndTimesBox().setOnAction(actionE);
                    return;
                }

                data.setEndHour(newHour);

                workspace.reloadWorkspace(app.getDataComponent());

                repopulateOfficeHoursGrid(gridMapping);

                EventHandler<ActionEvent> actionE = workspace.getEndTimesBox()
                                                             .getOnAction();
                workspace.getEndTimesBox().setOnAction(e -> {
                });
                workspace.getEndTimesBox()
                         .getSelectionModel()
                         .select(newHour);
                workspace.getEndTimesBox().setOnAction(actionE);


            }
        };

        if (!app.testing) {
            app.getGUI().getTps()
               .addTransaction(
                 chooseEndTimeAction);
        } else {
            chooseEndTimeAction.doTransaction();
        }


    }

    private void repopulateOfficeHoursGrid(HashMap<String, String> gridMapping)
    {
        TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();
        final int[] biggestRow = {0};
        gridMapping.entrySet().forEach(e -> {
            int row = parseInt(
              e.getKey().substring(e.getKey().indexOf("_") + 1));
            if (row > biggestRow[0]) {
                biggestRow[0] = row;
            }
        });
        Iterator<Map.Entry<String, StringProperty>> it = data.getOfficeHours()
                                                             .entrySet()
                                                             .iterator();
        while (it.hasNext()) {
            Map.Entry<String, StringProperty> next = it.next();
            int row = parseInt(
              next.getKey().substring(next.getKey().indexOf("_") + 1));
            if (row > biggestRow[0]) {
                it.remove();
            }
        }
        for (Map.Entry<String, String> entry : gridMapping.entrySet()) {
            int currentRow = parseInt(
              entry.getKey().substring(entry.getKey().indexOf("_") + 1));
            if (currentRow == 0 || (entry.getKey().charAt(0) == '0') ||
                entry.getKey().charAt(0) == '1')
            {
                continue;
            }
            String[] tas = entry.getValue().split("\n");
            for (String i : tas) {
                if (!i.isEmpty()) {
                    // we do not want each of these to be separately undo-able
                    // therefore we pass false to not have these actions register
                    // the jTPS class
                    data.toggleTAOfficeHours(entry.getKey(), i, false);
                }
            }
        }
    }

    private HashMap<String, String> getNewGripMapping(int rowShiftAmount,
      int newRowCount)
    {
        TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();
        HashMap<String, String> gridMapping = new HashMap<>();

        boolean sure = false;
        for (Map.Entry<String, StringProperty> entry : data.getOfficeHours()
                                                           .entrySet()) {
            int currentRow = parseInt(
              entry.getKey().substring(entry.getKey().indexOf("_") + 1));
            if (currentRow == 0 || (entry.getKey().charAt(0) == '0') ||
                entry.getKey().charAt(0) == '1')
            {
                continue;
            }
            String oldID = entry.getKey();
            // we are using the ID of the entry to get the row
            int oldRow = parseInt(oldID.substring(oldID.indexOf("_") + 1));
            // then we will use the change in start time to determine the new row
            int newRow = oldRow + (rowShiftAmount * 2);
            if (newRow > newRowCount) {
                // BASICALLY IF THIS HAPPENS I DO NOT WANT THE PROMPT
                // BUT I ALSO DO NOT WANT TO STORE THE VALUE OR ELSE
                // WHEN REBUILDING THE GRID IT WILL BE PLACED EVEN THO IT
                // DOES NOT BELONG. THIS IS HANDLING A DIFFERENT ISSUE THAN
                // THE OUTER IF
                continue;
            }
            if (newRow >= 0 &&
                !(rowShiftAmount == 0 && (newRow > newRowCount)))
            {
                String newID = oldID.charAt(0) + "_" + newRow;
                gridMapping.put(newID, entry.getValue().getValue());
            } else {
                if ((!entry.getValue().getValue().isEmpty() ||
                     containsOnlyWhitespace(entry.getValue().getValue())) &&
                    !sure)
                {
                    AppYesNoCancelDialogSingleton s = AppYesNoCancelDialogSingleton
                      .getSingleton();
                    s.show(TA_CUT_TA, TA_CUT_TA_MSG);
                    if (!AppYesNoCancelDialogSingleton.YES.equals(
                      s.getSelection()))
                    {
                        return null;
                    } else {
                        sure = true;
                    }
                }
            }
        }
        return gridMapping;
    }

    private boolean containsOnlyWhitespace(String value) {
        return value.chars().anyMatch(i -> !Character.isWhitespace(i));
    }

    public void handleRemoveTaAction(TeachingAssistant ta) {

        AppYesNoCancelDialogSingleton.getSingleton().show(APP_DELETE_PROMPT_TITLE, APP_DELETE_PROMPT_MSG);
        if (!AppYesNoCancelDialogSingleton.getSingleton().getSelection().equals(AppYesNoCancelDialogSingleton.YES)) {
            return ;
        }

        if (ta == null) {
            return;
        }


        ObservableList<TeachingAssistant> tableData = workspace.getTaTable()
                                                               .getItems();


        jTPS_Transaction action = new jTPS_Transaction() {
            String taName, taEmail;
            HashMap<String, String> oldEntries = new HashMap<>();
            TeachingAssistant lta = ta;

            String[] containingRecitations = new String[5];
            int[] taValue = new int[5];
            int currentR = 0;

            @Override
            public void doTransaction() {
                TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();
                RDEData rdeData = ((CourseSiteManagerData) app.getDataComponent())
                  .getRdeData();

                rdeData.getRecitations().forEach(item -> {
                    if (item.getTa1() != null &&
                        item.getTa1().getName().equalsIgnoreCase(ta.getName()))
                    {
                        item.setTa1(null);
                        containingRecitations[currentR] = item.getSection();
                        taValue[currentR] = 1;
                    } else if (item.getTa2() != null
                               && item.getTa2()
                                      .getName()
                                      .equalsIgnoreCase(ta.getName()))
                    {
                        item.setTa2(null);
                        containingRecitations[currentR] = item.getSection();
                        taValue[currentR] = 2;
                    }
                });

                HashMap<String, StringProperty> oh = data.getOfficeHours();
                taName = ta.getName();
                taEmail = ta.getEmail();
                for (Map.Entry<String, StringProperty> entry : oh.entrySet()) {
                    oldEntries.put(entry.getKey(), entry.getValue().getValue());
                }
                tableData.remove(ta);
                for (StringProperty value : oh.values()) {
                    String[] tasInCell = value.getValue().split("\n");
                    // Predicate used to check every TA Name in the cell for a match to remove
                    // This was necessary because equals was too narrow and contains was too broad
                    // equals failed to match due to new lines and contains would delete
                    // Billie Mays Hays when Billie Mays was selected.
                    Predicate<String> p = (String t) -> t.equals(ta.getName());
                    if (Arrays.stream(tasInCell).anyMatch(p)) {
                        data.removeTAFromCell(value, ta.getName());
                    }
                }

                workspace.getTaTable().getSelectionModel().clearSelection();
                clearAddTAFields();
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }

            @Override
            public void undoTransaction() {

                RDEData rdeData = ((CourseSiteManagerData) app.getDataComponent())
                  .getRdeData();

                for (int i = 0; i < containingRecitations.length; i++) {
                    if (containingRecitations[i] != null &&
                        !containingRecitations[i].isEmpty())
                    {
                        int finalI = i;
                        rdeData.getRecitations().forEach(item -> {
                            if (item.getSection()
                                    .equalsIgnoreCase(containingRecitations[finalI]))
                            {
                                switch (taValue[finalI]) {
                                    case 1:
                                        item.setTa1(lta);
                                        break;
                                    case 2:
                                        item.setTa2(lta);
                                }
                            }
                        });
                    }
                }


                TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();
                HashMap<String, StringProperty> oh = data.getOfficeHours();
                tableData.add(lta);
                Collections.sort(tableData);
                for (Map.Entry<String, String> entry : oldEntries.entrySet()) {
                    oh.get(entry.getKey()).setValue(entry.getValue());
                }
                workspace.getTaTable().getSelectionModel().clearSelection();
                clearAddTAFields();
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }

            @Override
            public void redoTransaction() {

                RDEData rdeData = ((CourseSiteManagerData) app.getDataComponent())
                  .getRdeData();

                rdeData.getRecitations().forEach(item -> {
                    if (item.getTa1() != null &&
                        item.getTa1().getName().equalsIgnoreCase(lta.getName()))
                    {
                        item.setTa1(null);
                        containingRecitations[currentR] = item.getSection();
                        taValue[currentR] = 1;
                    } else if (item.getTa2() != null
                               && item.getTa2()
                                      .getName()
                                      .equalsIgnoreCase(lta.getName()))
                    {
                        item.setTa2(null);
                        containingRecitations[currentR] = item.getSection();
                        taValue[currentR] = 2;
                    }
                });

                TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();
                HashMap<String, StringProperty> oh = data.getOfficeHours();
                for (Map.Entry<String, StringProperty> entry : oh.entrySet()) {
                    oldEntries.put(entry.getKey(), entry.getValue().getValue());
                }
                tableData.remove(lta);
                for (StringProperty value : oh.values()) {
                    String[] tasInCell = value.getValue().split("\n");
                    // Predicate used to check every TA Name in the cell for a match to remove
                    // This was necessary because equals was too narrow and contains was too broad
                    // equals failed to match due to new lines and contains would delete
                    // Billie Mays Hays when Billie Mays was selected.
                    Predicate<String> p = (String t) -> t.equals(taName);
                    if (Arrays.stream(tasInCell).anyMatch(p)) {
                        data.removeTAFromCell(value, taName);
                    }
                }

                workspace.getTaTable().getSelectionModel().clearSelection();
                clearAddTAFields();
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }
        };
        app.getGUI().getTps()
           .addTransaction(
             action);
        app.getGUI().getFileController().markAsEdited(app.getGUI());

    }

    public void handleChooseStartTime() {

        // THIS IS THE PART THAT CONVERTS THE STRING IN THE COMBOBOX TO A
        // USABLE HOUR THIS CODE IS WORKING
        PropertiesManager pm = PropertiesManager.getPropertiesManager();
        TAMWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getTaManagerTab()
          .getWorkspace();
        TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();

        jTPS_Transaction chooseStartTimeAction = new jTPS_Transaction() {

            int oldStartHour, oldEndHour, newStartHour;
            HashMap<String, String> previousData = new HashMap<>();
            HashMap<String, String> redoData;

            @Override
            public void doTransaction() {
                ComboBox<String> start = workspace.startTimesBox;
                String rawStartString = start.getSelectionModel()
                                             .getSelectedItem();
                if (rawStartString == null || rawStartString.isEmpty()) {
                    return;
                }

                String startTime = rawStartString.substring(0, 2);
                startTime = rawStartString.contains("PM") ?
                            String.valueOf(parseInt(startTime) + 12) :
                            startTime;
                newStartHour = parseInt(startTime);

                if (newStartHour == 24) {
                    startTime = "12";
                    newStartHour = 12;
                }

                if (newStartHour >= data.getEndHour()) {
                    AppMessageDialogSingleton s = AppMessageDialogSingleton.getSingleton();
                    s.show(pm.getProperty(
                      TA_GRID_INVALID_START_END_TIME_TITLE.toString()),
                      pm.getProperty(
                        TA_GRID_INVALID_START_END_TIME_MESSAGE.toString()));
                    return;
                }

                data.getOfficeHours().entrySet().forEach(c -> {
                    previousData.put(c.getKey(), c.getValue().getValue());
                });

                oldStartHour = data.getStartHour();
                oldEndHour = data.getEndHour();

                HashMap<String, String> gridMapping = getNewGripMapping(
                  oldStartHour - newStartHour,
                  (oldEndHour - newStartHour) * 2);


                redoData = gridMapping;

                if (gridMapping == null) {
                    workspace.getStartTimesBox()
                             .getSelectionModel()
                             .select(oldStartHour);
                    return;
                }

                data.setStartHour(newStartHour);

                workspace.reloadWorkspace(app.getDataComponent());

                repopulateOfficeHoursGrid(gridMapping);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                replaceWithHours(oldStartHour, previousData);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                replaceWithHours(newStartHour, redoData);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            private void replaceWithHours(int newHour,
              HashMap<String, String> gridMapping)
            {

                if (gridMapping == null) {
                    // LETS HOPE THIS WORKS :(
                    EventHandler<ActionEvent> actionE = workspace.getStartTimesBox()
                                                                 .getOnAction();
                    workspace.getStartTimesBox().setOnAction(e -> {
                    });

                    workspace.getStartTimesBox()
                             .getSelectionModel()
                             .select(newHour);

                    workspace.getStartTimesBox().setOnAction(actionE);
                    return;
                }

                data.setStartHour(newHour);

                workspace.reloadWorkspace(app.getDataComponent());

                repopulateOfficeHoursGrid(gridMapping);

                EventHandler<ActionEvent> actionE = workspace.getStartTimesBox()
                                                             .getOnAction();
                workspace.getStartTimesBox().setOnAction(e -> {
                });

                workspace.getStartTimesBox()
                         .getSelectionModel()
                         .select(newHour);

                workspace.getStartTimesBox().setOnAction(actionE);
            }
        };

        if (!app.testing) {
            app.getGUI().getTps()
               .addTransaction(
                 chooseStartTimeAction);
        } else {
            chooseStartTimeAction.doTransaction();
        }
    }

    public void clearAddTAFields() {
        workspace.clearButton.setDisable(
          true);
        workspace.getNameTextField().clear();
        workspace.getEmailTextField().clear();
        workspace.getNameTextField()
                 .requestFocus();
        workspace.getTaTable()
                 .getSelectionModel()
                 .clearSelection();
        workspace.getAddBox()
                 .getChildren()
                 .remove(workspace.changeButton);
        if (!workspace.getAddBox().getChildren().contains(workspace.addButton)) {
            workspace.getAddBox()
                 .getChildren()
                 .add(2,workspace.addButton);
        }
    }

    public void handleAddTA() {
        // WE'LL NEED THE WORKSPACE TO RETRIEVE THE USER INPUT VALUES
        TextField nameTextField = workspace.getNameTextField();
        TextField emailTextField = workspace.getEmailTextField();
        String name = nameTextField.getText();
        String email = emailTextField.getText();

        // WE'LL NEED TO ASK THE DATA SOME QUESTIONS TOO
        TAData data = ((CourseSiteManagerData) app.getDataComponent()).getTaData();


        // DID THE USER NEGLECT TO PROVIDE A TA NAME?
        if (name.isEmpty()) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(MISSING_TA_NAME_TITLE, MISSING_TA_NAME_MESSAGE);
        }
        // DOES A TA ALREADY HAVE THE SAME NAME OR EMAIL?
        else if (data.containsTA(name)) {
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(
              TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE,
              TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE);
        }
        // Require an email for the TA
        else if (email.isEmpty()) {
            AppMessageDialogSingleton singleton = AppMessageDialogSingleton.getSingleton();
            singleton.show(MISSING_TA_EMAIL_TITLE,
              MISSING_TA_EMAIL_MESSAGE);
        }
        // VALIDATE EMAIL ADDRESS USING REGEX
        else if (!isValidEmail(email)) {
            AppMessageDialogSingleton singleton = AppMessageDialogSingleton.getSingleton();
            singleton.show("Invalid Email",
              "The email address you entered is not valid. \nPlease use a valid email address");
        }
        // EVERYTHING IS FINE, ADD A NEW TA
        else {
            // ADD THE NEW TA TO THE DATA
            data.addTA(name, email);

            // CLEAR THE TEXT FIELDS
            nameTextField.setText("");
            emailTextField.setText("");

            // AND SEND THE CARET BACK TO THE NAME TEXT FIELD FOR EASY DATA ENTRY
            nameTextField.requestFocus();

        }
        app.getGUI().getFileController().markAsEdited(app.getGUI());


    }

    private boolean isValidEmail(String email) {
        return email.matches(
          "[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})");
    }
}
