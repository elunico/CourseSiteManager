package csm.tam.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 *
 * @author Richard McKenna
 */
public class TeachingAssistant implements Comparable<TeachingAssistant> {
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email; // not used in compareTo
    private final BooleanProperty undergraduate;
    private final BooleanProperty inRecitation;

    private TeachingAssistant(String name, String email, boolean undergrad, boolean inRecitation) {
        this(new SimpleStringProperty(name), new SimpleStringProperty(email), new SimpleBooleanProperty(undergrad), new SimpleBooleanProperty(inRecitation));

    }

    private TeachingAssistant(StringProperty nameProperty, StringProperty emailProperty, BooleanProperty undergradProperty, BooleanProperty inRecitationProperty) {
        name = nameProperty;
        email = emailProperty;
        undergraduate = undergradProperty;
        inRecitation = inRecitationProperty;
        inRecitation.addListener((observable, oldValue, newValue) -> {

        });
    }

    public TeachingAssistant(String initName, String initEmail) {
        this(initName, initEmail, true);
    }

    /**
     * Constructor initializes the TA name
     */
    public TeachingAssistant(String initName, String initEmail, boolean undergrad)
    {
        this(new SimpleStringProperty(initName), new SimpleStringProperty(initEmail), new SimpleBooleanProperty(undergrad), new SimpleBooleanProperty(false));
    }

    public BooleanProperty inRecitationProperty() {
        return inRecitation;
    }

    public TeachingAssistant deepCopy() {
        return new TeachingAssistant(getName(), getEmail(), isUndergraduate(), isInRecitation());
    }

    public boolean isInRecitation() {
        return inRecitation.get();
    }

    public void setInRecitation(boolean inRecitation) {
        this.inRecitation.set(inRecitation);
    }

    public TeachingAssistant copy() {
        return new TeachingAssistant(name, email, undergraduate, inRecitation);
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    @Override
    public int compareTo(TeachingAssistant otherTA) {
        return getName().compareTo(otherTA.getName());
    }

    public String getName() {
        return name.get();
    }

    public void setName(String initName) {
        name.set(initName);
    }

    @Override
    public String toString() {
        return name.getValue();
    }

    public String getEmail() {
        return email.get();
    }

    public void setEmail(String email) {
        this.email.setValue(email);
    }

    public boolean isUndergraduate() {
        return undergraduate.get();
    }

    public void setUndergraduate(boolean undergraduate) {
        this.undergraduate.set(undergraduate);
    }

    public BooleanProperty undergraduateProperty() {
        return undergraduate;
    }


}
