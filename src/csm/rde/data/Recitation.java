package csm.rde.data;

import csm.tam.data.TeachingAssistant;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class Recitation implements Comparable<Recitation> {

    private final SimpleStringProperty section;
    private final SimpleStringProperty instructor;
    private final SimpleStringProperty location;
    private final SimpleStringProperty dayTime;
    private final SimpleObjectProperty<TeachingAssistant> ta1, ta2;

    public Recitation(String section, String instructor, String location, TeachingAssistant ta1, TeachingAssistant ta2, String dayTime) {
        this.section = new SimpleStringProperty(section);
        this.instructor = new SimpleStringProperty(instructor);
        this.location = new SimpleStringProperty(location);
        this.ta1 = new SimpleObjectProperty<>(ta1);
        this.ta2 = new SimpleObjectProperty<>(ta2);
        this.dayTime = new SimpleStringProperty(dayTime);
    }

    public TeachingAssistant getTa1() {
        return ta1.get();
    }

    public void setTa1(TeachingAssistant ta1) {
        this.ta1.set(ta1);
    }

    public SimpleObjectProperty<TeachingAssistant> ta1Property() {
        return ta1;
    }

    public TeachingAssistant getTa2() {
        return ta2.get();
    }

    public void setTa2(TeachingAssistant ta2) {
        this.ta2.set(ta2);
    }

    public SimpleObjectProperty<TeachingAssistant> ta2Property() {
        return ta2;
    }

    public String getSection() {
        return section.get();
    }

    public void setSection(String section) {
        this.section.set(section);
    }

    public SimpleStringProperty sectionProperty() {
        return section;
    }

    public String getInstructor() {
        return instructor.get();
    }

    public void setInstructor(String instructor) {
        this.instructor.set(instructor);
    }

    public SimpleStringProperty instructorProperty() {
        return instructor;
    }

    public String getLocation() {
        return location.get();
    }

    public void setLocation(String location) {
        this.location.set(location);
    }

    public SimpleStringProperty locationProperty() {
        return location;
    }

    public String getDayTime() {
        return dayTime.get();
    }

    public void setDayTime(String dayTime) {
        this.dayTime.set(dayTime);
    }

    public SimpleStringProperty dayTimeProperty() {
        return dayTime;
    }

    @Override
    public int compareTo(Recitation o) {
        throw new NotImplementedException();
    }
}
