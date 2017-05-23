package csm.tam.data;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * This class represents a Teaching Assistant for the table of TAs.
 *
 * @author Richard McKenna
 */
public class TeachingAssistant<E extends Comparable<E>>
  implements Comparable<E>
{
    // THE TABLE WILL STORE TA NAMES AND EMAILS
    private final StringProperty name;
    private final StringProperty email; // not used in compareTo
    private final BooleanProperty undergraduate;
    private final BooleanProperty inRecitation = new SimpleBooleanProperty(false);

    public boolean isInRecitation() {
        return inRecitation.get();
    }

    public BooleanProperty inRecitationProperty() {
        return inRecitation;
    }

    public void setInRecitation(boolean inRecitation) {
        this.inRecitation.set(inRecitation);
    }

    public TeachingAssistant(String initName, String initEmail) {
        this(initName, initEmail, true);
    }

    /**
     * Constructor initializes the TA name
     */
    public TeachingAssistant(String initName, String initEmail,
      boolean undergrad)
    {
        name = new SimpleStringProperty(initName);
        email = new SimpleStringProperty(initEmail);
        undergraduate = new SimpleBooleanProperty(undergrad);

        inRecitation.addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {

            }
        });
    }

    // ACCESSORS AND MUTATORS FOR THE PROPERTIES

    @Override
    public int compareTo(E otherTA) {
        return getName().compareTo(((TeachingAssistant) otherTA).getName());
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
