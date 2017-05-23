package csm.rde.data;

import csm.CourseSiteManagerApp;
import djf.components.AppDataComponent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Thomas Povinelli
 *         Created 4/20/17
 *         In Homework5
 */
public class RDEData implements AppDataComponent {

    private ObservableList<Recitation> recitations = FXCollections.observableArrayList();

    public RDEData(CourseSiteManagerApp courseSiteManagerApp) {
    }

    public ObservableList<Recitation> getRecitations() {
        return recitations;
    }

    public void setRecitations(ObservableList<Recitation> recitations)
    {
        this.recitations = recitations;
    }

    @Override
    public void resetData() {
        recitations.clear();
    }
}
