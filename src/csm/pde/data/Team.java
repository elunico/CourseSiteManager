package csm.pde.data;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Thomas Povinelli
 *         Created 4/9/17
 *         In Homework4
 */
public class Team {
    private final SimpleStringProperty name, color, textColor, link;
    private ObservableList<Student> members;

    public Team(String name, String color, String textColor, String link, Student... members) {
        this(name, color, textColor, link, FXCollections.observableArrayList(members));
    }

    public Team(String name, String color, String textColor, String link, ObservableList<Student> students) {
        this();
        this.name.setValue(name);
        this.color.setValue(color);
        this.textColor.setValue(textColor);
        this.link.setValue(link);
        this.members = students;
    }

    public Team()
    {
        this.name = new SimpleStringProperty("new default value");
        this.color = new SimpleStringProperty();
        this.textColor = new SimpleStringProperty();
        this.link = new SimpleStringProperty();
        this.members = FXCollections.observableArrayList();
    }

    public ObservableList<Student> getMembers() {
        return members;
    }

    public void setMembers(ObservableList<Student> members)
    {
        this.members = members;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getColor() {
        return color.get();
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public SimpleStringProperty colorProperty() {
        return color;
    }

    public String getTextColor() {
        return textColor.get();
    }

    public void setTextColor(String textColor) {
        this.textColor.set(textColor);
    }

    public SimpleStringProperty textColorProperty() {
        return textColor;
    }

    public String getLink() {
        return link.get();
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public SimpleStringProperty linkProperty() {
        return link;
    }

    public String toString() {
        return this.name == null ? "" : this.name.get();
    }
}
