package csm.sde.workspace;

import csm.CourseSiteManagerData;
import csm.DateHybrid;
import csm.sde.data.SDEData;
import csm.sde.data.ScheduleItem;
import csm.ui.CourseSiteManagerWorkspace;
import djf.AppTemplate;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import javafx.event.ActionEvent;
import jtps.jTPS_Transaction;

import java.time.LocalDate;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 5/2/17
 *         In Homework6
 */
public class SDEController {
    private AppTemplate app;

    public SDEController(AppTemplate app) {
        this.app = app;
    }

    public void handleScheduleItemsTableClicked(ScheduleItem item) {
        if (item == null) {
            return;
        }

        String type = item.getType();
        String time = item.getTime();
        String title = item.getTitle();
        String topic = item.getTopic();
        String link = item.getLink();
        String criteria = item.getCriteria();

        SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getScheduleDataTab()
          .getWorkspace();

        workspace.typeChoiceBox.getSelectionModel().select(type);
        workspace.datePicker.setValue(item.getDate().asLocalDate());
        workspace.timeField.setText(time);
        workspace.topicField.setText(topic);
        workspace.titleField.setText(title);
        workspace.linkField.setText(link);
        workspace.criteriaField.setText(criteria);


    }

    public void handleAddUpdateAction(ScheduleItem selectedItem) {
        if (selectedItem == null) {
            handleAddAction();
        } else {
            handleChangeAction(selectedItem);
        }

    }

    private void handleAddAction() {
        jTPS_Transaction a = new jTPS_Transaction() {

            ScheduleItem item;

            @Override
            public void doTransaction() {
                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();

                String type = workspace.typeChoiceBox.getValue();
                String title = workspace.titleField.getText();
                LocalDate date = workspace.datePicker.getValue();
                DateHybrid hdate =
                  date != null ? new DateHybrid(date) : DateHybrid.WEEKEND;
                String time = workspace.timeField.getText();
                String link = workspace.linkField.getText();
                String criteria = workspace.criteriaField.getText();
                String topic = workspace.topicField.getText();

                if (!validate(hdate, time, link)) {
                    return;
                }

                item = new ScheduleItem(type, title, topic,
                  hdate, time, link, criteria);

                ((CourseSiteManagerData) app.getDataComponent()).getSdeData()
                                                                .getScheduleItems()
                                                                .add(item);

                workspace.titleField.clear();
                workspace.topicField.clear();
                workspace.typeChoiceBox.getSelectionModel().clearSelection();
                workspace.linkField.clear();
                workspace.timeField.clear();
                workspace.criteriaField.clear();

                workspace.scheduleItemsTable.getSelectionModel()
                                            .clearSelection();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                ((CourseSiteManagerData) app.getDataComponent()).getSdeData()
                                                                .getScheduleItems()
                                                                .remove(item);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                ((CourseSiteManagerData) app.getDataComponent()).getSdeData()
                                                                .getScheduleItems()
                                                                .add(item);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(a);
    }

    private boolean isTimeValid(String time) {
        return
          time.matches("\\w{7,10}, \\d\\d?:\\d\\d[ap]m-\\d\\d?:\\d\\d[ap]m") ||
          time.matches("\\d\\d?:\\d\\d[ap]m-\\d\\d?:\\d\\d[ap]m");
    }

    private boolean isDateValid(DateHybrid date) {
        return !date.isWeekend();
    }

    private boolean isLinkValid(String link) {
        // TODO: implement this
        return true;
    }

    private void handleChangeAction(ScheduleItem selectedItem) {

        jTPS_Transaction action = new jTPS_Transaction() {


            String old_type;
            String old_title;
            LocalDate old_date;
            DateHybrid old_hdate;
            String old_time;
            String old_link;
            String old_criteria;
            String old_topic;

            String new_type;
            String new_title;
            LocalDate new_date;
            DateHybrid new_hdate;
            String new_time;
            String new_link;
            String new_criteria;
            String new_topic;

            ScheduleItem theItem;

            @Override
            public void doTransaction() {


                theItem = selectedItem;

                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();

                old_type = theItem.getType();
                old_title = theItem.getTitle();
                old_date = theItem.getDate().asLocalDate();
                old_hdate = theItem.getDate();
                old_time = theItem.getTime();
                old_link = theItem.getLink();
                old_criteria =theItem.getCriteria();
                old_topic = theItem.getTopic();

                new_type = workspace.typeChoiceBox.getValue();
                new_title = workspace.titleField.getText();
                new_date = workspace.datePicker.getValue();
                new_hdate = new DateHybrid(workspace.datePicker.getValue());
                new_time = workspace.timeField.getText();
                new_link = workspace.linkField.getText();
                new_criteria = workspace.criteriaField.getText();
                new_topic = workspace.topicField.getText();

                if (!validate(old_hdate, old_time, old_link)) {
                    return;
                }

                selectedItem.setDate(new_hdate);
                selectedItem.setTitle(new_title);
                selectedItem.setType(new_type);
                selectedItem.setTime(new_time);
                selectedItem.setLink(new_link);
                selectedItem.setCriteria(new_criteria);
                selectedItem.setTopic(new_topic);

                workspace.titleField.clear();
                workspace.topicField.clear();
                workspace.typeChoiceBox.getSelectionModel().clearSelection();
                workspace.linkField.clear();
                workspace.timeField.clear();
                workspace.criteriaField.clear();

                workspace.scheduleItemsTable.getSelectionModel()
                                            .clearSelection();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();

                new_type = theItem.getType();
                new_title = theItem.getTitle();
                new_date = theItem.getDate().asLocalDate();
                new_hdate = theItem.getDate();
                new_time = theItem.getTime();
                new_link = theItem.getLink();
                new_criteria = theItem.getCriteria();
                new_topic = theItem.getTopic();

                theItem.setDate(old_hdate);
                theItem.setTitle(old_title);
                theItem.setType(old_type);
                theItem.setTime(old_time);
                theItem.setLink(old_link);
                theItem.setCriteria(old_criteria);
                theItem.setTopic(old_topic);
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }

            @Override
            public void redoTransaction() {
                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();

                theItem.setDate(new_hdate);
                theItem.setTitle(new_title);
                theItem.setType(new_type);
                theItem.setTime(new_time);
                theItem.setLink(new_link);
                theItem.setCriteria(new_criteria);
                theItem.setTopic(new_topic);
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);

    }

    private boolean validate(DateHybrid date, String time, String link) {
        if (!isDateValid(date)) {
            AppMessageDialogSingleton.getSingleton()
                                     .show(SDE_INVALID_DATE_TITLE, SDE_INVALID_DATE_MSG);
            return false;
        } else if (!isTimeValid(time)) {
            AppMessageDialogSingleton.getSingleton()
                                     .show(SDE_INVALID_TIME_TITLE, SDE_INVALID_TIME_MSG);
            return false;
        } else if (!isLinkValid(link)) {
            AppMessageDialogSingleton.getSingleton()
                                     .show(SDE_INVALID_LINK_TITLE, SDE_INVALID_LINK_MSG);
            return false;
        }
        return true;
    }

    public void handleClearAction() {
        SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getScheduleDataTab()
          .getWorkspace();

        workspace.titleField.clear();
        workspace.topicField.clear();
        workspace.typeChoiceBox.getSelectionModel().clearSelection();
        workspace.linkField.clear();
        workspace.timeField.clear();
        workspace.criteriaField.clear();

        workspace.scheduleItemsTable.getSelectionModel().clearSelection();

    }

    public void handleDeleteAction(ActionEvent e) {

        AppYesNoCancelDialogSingleton.getSingleton().show(APP_DELETE_PROMPT_TITLE, APP_DELETE_PROMPT_MSG);
        if (!AppYesNoCancelDialogSingleton.getSingleton().getSelection().equals(AppYesNoCancelDialogSingleton.YES)) {
            return ;
        }

        jTPS_Transaction action = new jTPS_Transaction() {

            ScheduleItem selectedItem;

            @Override
            public void doTransaction() {
                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();

                SDEData data = ((CourseSiteManagerData) app.getDataComponent()).getSdeData();

                selectedItem = workspace.getScheduleItemsTable()
                                        .getSelectionModel()
                                        .getSelectedItem();

                data.getScheduleItems().remove(selectedItem);

                workspace.titleField.clear();
                workspace.topicField.clear();
                workspace.typeChoiceBox.getSelectionModel().clearSelection();
                workspace.linkField.clear();
                workspace.timeField.clear();
                workspace.criteriaField.clear();

                workspace.scheduleItemsTable.getSelectionModel()
                                            .clearSelection();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();

                SDEData data = ((CourseSiteManagerData) app.getDataComponent()).getSdeData();

                data.getScheduleItems().add(selectedItem);

                workspace.getScheduleItemsTable()
                         .getSelectionModel()
                         .select(selectedItem);

                workspace.typeChoiceBox.getSelectionModel()
                                       .select(selectedItem.getType());
                workspace.datePicker.setValue(selectedItem.getDate()
                                                          .asLocalDate());
                workspace.timeField.setText(selectedItem.getTime());
                workspace.topicField.setText(selectedItem.getTopic());
                workspace.titleField.setText(selectedItem.getTitle());
                workspace.linkField.setText(selectedItem.getLink());
                workspace.criteriaField.setText(selectedItem.getCriteria());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getScheduleDataTab()
                  .getWorkspace();


                SDEData data = ((CourseSiteManagerData) app.getDataComponent()).getSdeData();

                data.getScheduleItems().remove(selectedItem);

                workspace.titleField.clear();
                workspace.topicField.clear();
                workspace.typeChoiceBox.getSelectionModel().clearSelection();
                workspace.linkField.clear();
                workspace.timeField.clear();
                workspace.criteriaField.clear();

                workspace.scheduleItemsTable.getSelectionModel()
                                            .clearSelection();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);
    }
}
