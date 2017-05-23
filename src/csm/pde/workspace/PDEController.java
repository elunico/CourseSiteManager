package csm.pde.workspace;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.pde.data.PDEData;
import csm.pde.data.Student;
import csm.pde.data.Team;
import csm.ui.CourseSiteManagerWorkspace;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import jtps.jTPS_Transaction;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 4/9/17
 *         In Homework4
 */
public class PDEController {
    CourseSiteManagerApp app;

    public PDEController(CourseSiteManagerApp app) {
        this.app = app;
    }

    public void handleAddUpdateTeamAction(Team team) {
        if (team == null) {
            handleAddTeamAction();
        } else {
            handleUpdateTeamAction(team);
        }
    }

    private void handleUpdateTeamAction(Team outer_team) {
        jTPS_Transaction action = new jTPS_Transaction() {

            public Team team;
            String new_link;
            String new_textColor;
            String new_color;
            String new_name;
            String old_name;
            String old_color;
            String old_textColor;
            String old_link;

            @Override
            public void undoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                team.setName(old_name);
                team.setLink(old_link);
                team.setColor(old_color);
                team.setTextColor(old_textColor);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
            @Override
            public void doTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

                int teamIndex = data.getTeams().indexOf(outer_team);

                team = data.getTeams().get(teamIndex);

                old_name = team.getName();
                old_color = team.getColor();
                old_textColor = team.getTextColor();
                old_link = team.getLink();

                new_name = workspace.getProjectNameField().getText();
                new_color = workspace.getColorPicker().getValue().toString();
                new_textColor = workspace.getTextColorPicker()
                                         .getValue()
                                         .toString();
                new_link = workspace.getLinkTextField().getText();
                data.verifyLink(new_link);

                if (new_name.isEmpty() || new_link.isEmpty()) {
                    AppMessageDialogSingleton.getSingleton()
                                             .show(PDE_EMPTY_TEAM_DATA_TITLE, PDE_EMPTY_TEAM_DATA_MSG);
                    return;
                }

                team.setName(new_name);
                team.setColor(new_color);
                team.setTextColor(new_textColor);
                team.setLink(new_link);

                workspace.getStudentTableView().refresh();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }



            @Override
            public void redoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                team.setName(new_name);
                team.setLink(new_link);
                team.setColor(new_color);
                team.setTextColor(new_textColor);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }
        };

        app.getGUI().getTps().addTransaction(action);
    }

    private void handleAddTeamAction() {
        jTPS_Transaction action = new jTPS_Transaction() {

            Team team;

            @Override
            public void doTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

                String name = workspace.getProjectNameField().getText();
                Color color = workspace.getColorPicker().getValue();
                Color textColor = workspace.getTextColorPicker().getValue();
                String link = workspace.getLinkTextField().getText();
                data.verifyLink(link);

                if (name.isEmpty() || link.isEmpty()) {
                    AppMessageDialogSingleton.getSingleton()
                                             .show(PDE_EMPTY_TEAM_DATA_TITLE, PDE_EMPTY_TEAM_DATA_MSG);
                    return;
                }

                team = new Team(name, color.toString(), textColor.toString(), link);

                data.getTeams().add(team);

                workspace.getProjectNameField().clear();
                workspace.getLinkTextField().clear();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();
                data.getTeams().remove(team);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();
                data.getTeams().add(team);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);
    }

    public void handleClearProjectAction(TableView<Team> projectTableView) {
        PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getProjectDataTab()
          .getWorkspace();

        projectTableView.getSelectionModel().clearSelection();
        workspace.getProjectNameField().clear();
        workspace.getLinkTextField().clear();
    }

    public void handleTeamTableClick() {
        PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getProjectDataTab()
          .getWorkspace();

        Team team = workspace.getTeamTableView()
                             .getSelectionModel()
                             .getSelectedItem();

        workspace.getProjectNameField().setText(team.getName());
        try {
            workspace.getColorPicker()
                     .setValue(Color.web(team.getColor().substring(2,
                       team.getColor().length() - 2)));
            workspace.getTextColorPicker()
                     .setValue(Color.web(team.getTextColor().substring(2,
                       team.getTextColor().length() - 2)));
        } catch (IllegalArgumentException | StringIndexOutOfBoundsException e) {
            workspace.getColorPicker().setValue(Color.BLACK);
            workspace.getTextColorPicker().setValue(Color.BLACK);
        }
        workspace.getLinkTextField().setText(team.getLink());
    }

    public void handleStudentTableClicked(MouseEvent e) {
        PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getProjectDataTab()
          .getWorkspace();

        Student student = workspace.getStudentTableView()
                                   .getSelectionModel()
                                   .getSelectedItem();

        if (student == null) {
            return;
        }

        workspace.getFirstNameField().setText(student.getFirstName());
        workspace.getLastNameField().setText(student.getLastName());

        workspace.getTeamChoiceBox()
                 .getSelectionModel()
                 .select(student.getTeam());

        workspace.getRoleField().setText(student.getRole());
    }

    public void handleAddUpdateStudentAction(Student selectedItem) {
        if (selectedItem == null) {
            handleAddStudentAction();
        } else {
            handleChangeStudentAction(selectedItem);
        }

    }

    private void handleChangeStudentAction(Student selectedItem) {
        jTPS_Transaction action = new jTPS_Transaction() {

            String old_FirstName, old_LastName, old_Role, new_FirstName, new_LastName, new_Role;
            Team old_Team, new_Team;

            Student item;

            @Override
            public void doTransaction() {
                item = selectedItem;

                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();

                old_FirstName = item.getFirstName();
                old_LastName = item.getLastName();
                old_Team = item.getTeam();
                old_Role = item.getRole();

                new_FirstName = workspace.getFirstNameField().getText();
                new_LastName = workspace.getLastNameField().getText();
                new_Team = workspace.getTeamChoiceBox()
                                    .getSelectionModel()
                                    .getSelectedItem();
                new_Role = workspace.getRoleField().getText();

                item.setFirstName(new_FirstName);
                item.setLastName(new_LastName);
                item.setTeam(new_Team);
                item.setRole(new_Role);

                workspace.getFirstNameField().clear();
                workspace.getLastNameField().clear();

                workspace.getTeamChoiceBox()
                         .getSelectionModel()
                         .clearSelection();

                workspace.getRoleField().clear();

                workspace.getStudentTableView()
                         .getSelectionModel()
                         .clearSelection();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();

                item.setFirstName(old_FirstName);
                item.setLastName(old_LastName);
                item.setTeam(old_Team);
                item.setRole(old_Role);

                handleClearStudentAction(workspace.getStudentTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();

                item.setFirstName(new_FirstName);
                item.setLastName(new_LastName);
                item.setTeam(new_Team);
                item.setRole(new_Role);

                handleClearStudentAction(workspace.getStudentTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }
        };

        app.getGUI().getTps().addTransaction(action);

    }

    private void handleAddStudentAction() {
        jTPS_Transaction action = new jTPS_Transaction() {

            Student s;

            @Override
            public void doTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

                s = new Student();

                s.setFirstName(workspace.getFirstNameField().getText());
                s.setLastName(workspace.getLastNameField().getText());

                s.setTeam(workspace.getTeamChoiceBox()
                                   .getSelectionModel()
                                   .getSelectedItem());

                s.setRole(workspace.getRoleField().getText());

                data.getStudents().add(s);

                workspace.getFirstNameField().clear();
                workspace.getLastNameField().clear();

                workspace.getTeamChoiceBox()
                         .getSelectionModel()
                         .clearSelection();

                workspace.getRoleField().clear();

                workspace.getStudentTableView()
                         .getSelectionModel()
                         .clearSelection();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();

                data.getStudents().remove(s);
                handleClearStudentAction(workspace.getStudentTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }

            @Override
            public void redoTransaction() {
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();

                data.getStudents().add(s);
                handleClearStudentAction(workspace.getStudentTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

        };

        app.getGUI().getTps().addTransaction(action);

    }

    public void handleClearStudentAction(TableView<Student> studentTableView) {
        PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getProjectDataTab()
          .getWorkspace();


        workspace.getFirstNameField().clear();
        workspace.getLastNameField().clear();

        workspace.getTeamChoiceBox().getSelectionModel().clearSelection();

        workspace.getRoleField().clear();

        workspace.getStudentTableView().getSelectionModel().clearSelection();
    }


    public void handleRemoveStudentAction() {

        AppYesNoCancelDialogSingleton.getSingleton()
                                     .show(APP_DELETE_PROMPT_TITLE, APP_DELETE_PROMPT_MSG);
        if (!AppYesNoCancelDialogSingleton.getSingleton()
                                          .getSelection()
                                          .equals(AppYesNoCancelDialogSingleton.YES))
        {
            return;
        }

        jTPS_Transaction action = new jTPS_Transaction() {

            Student student;

            @Override
            public void doTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();


                TableView<Student> studentTableView = workspace.getStudentTableView();


                student = studentTableView.getSelectionModel()
                                          .getSelectedItem();
                if (student == null) {
                    return;
                }

                ((CourseSiteManagerData) app.getDataComponent()).getPdeData()
                                                                .getStudents()
                                                                .remove(student);
                workspace.getFirstNameField().clear();
                workspace.getLastNameField().clear();
                workspace.getTeamChoiceBox()
                         .getSelectionModel()
                         .clearSelection();
                workspace.getRoleField().clear();
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

                data.getStudents().add(student);
                handleClearStudentAction(workspace.getStudentTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void redoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

                data.getStudents().remove(student);
                handleClearStudentAction(workspace.getStudentTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }
        };

        app.getGUI().getTps().addTransaction(action);

    }

    public void handleRemoveTeamAction() {

        AppYesNoCancelDialogSingleton.getSingleton()
                                     .show(APP_DELETE_PROMPT_TITLE, APP_DELETE_PROMPT_MSG);
        if (!AppYesNoCancelDialogSingleton.getSingleton()
                                          .getSelection()
                                          .equals(AppYesNoCancelDialogSingleton.YES))
        {
            return;
        }

        jTPS_Transaction action = new jTPS_Transaction() {

            Team team;

            @Override
            public void doTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();

                TableView<Team> teamTableView = workspace.getTeamTableView();


                team = teamTableView.getSelectionModel().getSelectedItem();
                if (team == null) {
                    return;
                }

                ((CourseSiteManagerData) app.getDataComponent()).getPdeData()
                                                                .getTeams()
                                                                .remove(team);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());

            }

            @Override
            public void undoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

                data.getTeams().add(team);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());


            }

            @Override
            public void redoTransaction() {
                PDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
                  .getProjectDataTab()
                  .getWorkspace();
                PDEData data = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();
                data.getTeams().remove(team);

                handleClearProjectAction(workspace.getTeamTableView());
                app.getGUI().getFileController().markAsEdited(app.getGUI());
            }
        };

        app.getGUI().getTps().addTransaction(action);

    }
}
