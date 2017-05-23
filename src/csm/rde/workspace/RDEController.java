package csm.rde.workspace;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.rde.data.RDEData;
import csm.rde.data.Recitation;
import csm.tam.data.TeachingAssistant;
import csm.ui.CourseSiteManagerWorkspace;
import djf.ui.AppYesNoCancelDialogSingleton;
import javafx.event.ActionEvent;
import jtps.jTPS_Transaction;

import static csm.CourseSiteManagerProp.APP_DELETE_PROMPT_MSG;
import static csm.CourseSiteManagerProp.APP_DELETE_PROMPT_TITLE;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class RDEController {

    private CourseSiteManagerApp app;

    public RDEController(CourseSiteManagerApp app) {
        this.app = app;
    }

    public void handleAddUpdateRecitationAction(Recitation selection) {
        if (selection == null) {
            handleAddRecitationAction();
        } else {
            handleChangeRecitationAction(selection);
        }

    }

    private void handleChangeRecitationAction(Recitation selection) {

        jTPS_Transaction action = new jTPS_Transaction() {

            String new_section;
            String new_instructor;
            String new_dayTime;
            String new_location;
            TeachingAssistant new_sta1;
            TeachingAssistant new_sta2;

            String old_section;
            String old_instructor;
            String old_dayTime;
            String old_location;
            TeachingAssistant old_sta1;
            TeachingAssistant old_sta2;

            Recitation item;

            @Override
            public void doTransaction() {
                RDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getRecitationDataTab()
                  .getWorkspace();

                item = selection;

                old_section = item.getSection();
                old_instructor = item.getInstructor();
                old_dayTime = item.getDayTime();
                old_location = item.getLocation();
                old_sta1 = item.getTa1();
                old_sta2 = item.getTa2();


                new_section = workspace.sectionTextField.getText();
                new_instructor = workspace.instructorTextField.getText();
                new_dayTime = workspace.dayTimeTextField.getText();
                new_location = workspace.locationTextField.getText();
                new_sta1 = workspace.supervisingTATextFieldTop.getSelectionModel()
                                                              .getSelectedItem();
                new_sta2 = workspace.supervisingTATextFieldBottom.getSelectionModel()
                                                                 .getSelectedItem();

                selection.setSection(new_section);
                selection.setInstructor(new_instructor);
                selection.setDayTime(new_dayTime);
                selection.setTa1(new_sta1);
                selection.setTa2(new_sta2);
                selection.setLocation(new_location);

                handleClearRecitationDataAction(null);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                item.setSection(new_section);
                item.setInstructor(new_instructor);
                item.setDayTime(new_dayTime);
                item.setTa1(new_sta1);
                item.setTa2(new_sta2);
                item.setLocation(new_location);

                handleClearRecitationDataAction(null);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                item.setSection(old_section);
                item.setInstructor(old_instructor);
                item.setDayTime(old_dayTime);
                item.setTa1(old_sta1);
                item.setTa2(old_sta2);
                item.setLocation(old_location);

                handleClearRecitationDataAction(null);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);

    }

    private void handleAddRecitationAction() {
        jTPS_Transaction action = new jTPS_Transaction() {

            Recitation item;

            @Override
            public void doTransaction() {
                RDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getRecitationDataTab()
                  .getWorkspace();
                RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();


                String section = workspace.sectionTextField.getText();
                String instructor = workspace.instructorTextField.getText();
                String dayTime = workspace.dayTimeTextField.getText();
                String location = workspace.locationTextField.getText();
                TeachingAssistant sta1 = workspace.supervisingTATextFieldTop.getSelectionModel()
                                                                            .getSelectedItem();
                TeachingAssistant sta2 = workspace.supervisingTATextFieldBottom.getSelectionModel()
                                                                               .getSelectedItem();

                item = new Recitation(section, instructor, location, sta1, sta2, dayTime);

                handleClearRecitationDataAction(null);

                data.getRecitations().add(item);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();

                data.getRecitations().remove(item);

                handleClearRecitationDataAction(null);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();

                data.getRecitations().add(item);

                handleClearRecitationDataAction(null);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);

    }

    public void handleClearRecitationDataAction(ActionEvent actionEvent) {
        RDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getRecitationDataTab()
          .getWorkspace();

        workspace.sectionTextField.clear();
        workspace.instructorTextField.clear();
        workspace.dayTimeTextField.clear();
        workspace.locationTextField.clear();
        workspace.supervisingTATextFieldTop.getSelectionModel()
                                           .clearSelection();
        workspace.supervisingTATextFieldBottom.getSelectionModel()
                                              .clearSelection();

        workspace.recitationTable.getSelectionModel().clearSelection();

    }

    public void handleDeleteRecitationAction(ActionEvent actionEvent) {
        AppYesNoCancelDialogSingleton.getSingleton().show(APP_DELETE_PROMPT_TITLE, APP_DELETE_PROMPT_MSG);
        if (!AppYesNoCancelDialogSingleton.getSingleton().getSelection().equals(AppYesNoCancelDialogSingleton.YES)) {
            return ;
        }
        jTPS_Transaction action = new jTPS_Transaction() {

            Recitation item;

            @Override
            public void doTransaction() {
                RDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getRecitationDataTab()
                  .getWorkspace();
                RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();

                item = workspace.recitationTable.getSelectionModel()
                                                .getSelectedItem();

                data.getRecitations().remove(item);

                handleClearRecitationDataAction(null);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();

                data.getRecitations().add(item);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();

                data.getRecitations().remove(item);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);
    }

    public void handleTableClickedAction() {
        RDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getRecitationDataTab()
          .getWorkspace();
        RDEData data = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();

        Recitation r = workspace.recitationTable.getSelectionModel()
                                                .getSelectedItem();
        if (r == null) {
            return;
        }
        workspace.sectionTextField.setText(r.getSection());
        workspace.instructorTextField.setText(r.getInstructor());
        workspace.dayTimeTextField.setText(r.getDayTime());
        workspace.locationTextField.setText(r.getLocation());
        workspace.supervisingTATextFieldTop.getSelectionModel()
                                           .select(r.getTa1());
        workspace.supervisingTATextFieldBottom.getSelectionModel()
                                              .select(r.getTa2());

    }
}
