package csm.pde.data;

import csm.CourseSiteManagerApp;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Thomas Povinelli
 *         Created 4/19/17
 *         In Homework5
 */
public class PDEData implements AppDataComponent {

    private ObservableList<Team> teams = FXCollections.observableArrayList();
    private ObservableList<Student> students = FXCollections.observableArrayList();

    private CourseSiteManagerApp app;

    public PDEData(CourseSiteManagerApp app) {
        this.app = app;
    }

    public ObservableList<Team> getTeams() {
        return teams;
    }

    public void setTeams(ObservableList<Team> teams) {
        this.teams = teams;
    }

    public ObservableList<Student> getStudents() {
        return students;
    }

    public void setStudents(ObservableList<Student> students) {
        this.students = students;
    }

    public CourseSiteManagerApp getApp() {
        return app;
    }

    public void setApp(CourseSiteManagerApp app) {
        this.app = app;
    }

    @Override
    public void resetData() {
        teams.clear();
        students.clear();
    }

    @Override
    public String toString() {
        return "PDEData{" +
               "teams=" + teams +
               ", students=" + students +
               ", app=" + app +
               '}';
    }

    public boolean verifyLink(String link) {
        // TODO: Implement this
        return true;
    }
}
