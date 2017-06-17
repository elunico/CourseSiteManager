package csm.pde.data;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * @author Thomas Povinelli
 *         Created 4/9/17
 *         In Homework4
 */
public class Student {
    final SimpleStringProperty firstName, lastName, role;
    final SimpleObjectProperty<Team> team;

    public Student() {
        this("", "", "");
    }

    public Student(String firstName, String lastName, String role) {
        this(firstName, lastName, role, new Team());
    }

    public Student(String firstName, String lastName, String role, Team team) {
        this.firstName = new SimpleStringProperty();
        this.lastName = new SimpleStringProperty();
        this.role = new SimpleStringProperty();
        this.team = new SimpleObjectProperty<>();

        this.firstName.set(firstName);
        this.lastName.set(lastName);
        this.role.set(role);
        this.team.set(team);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }

    public String getLastName() {
        return lastName.get();
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public SimpleStringProperty lastNameProperty() {
        return lastName;
    }

    public String getRole() {
        return role.get();
    }

    public void setRole(String role) {
        this.role.set(role);
    }

    public SimpleStringProperty roleProperty() {
        return role;
    }

    public Team getTeam() {
        return team.get();
    }

    public void setTeam(Team team) {
        this.team.set(team);
    }

    @Override
    public String toString() {
        return "Student{" +
               "firstName=" + firstName +
               ", lastName=" + lastName +
               ", role=" + role +
               ", team=" + team +
               '}';
    }

    public SimpleObjectProperty<Team> teamProperty() {
        return team;
    }
}
