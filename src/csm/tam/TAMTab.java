package csm.tam;

import csm.CourseSiteManagerApp;
import csm.tam.workspace.TAMWorkspace;
import csm.ui.CourseSiteManagerTab;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_TAM;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class TAMTab extends CourseSiteManagerTab{

    public TAMTab(CourseSiteManagerApp app, TabPane pane) {
        super(new TAMWorkspace(app), pane);
        self.setText(TAB_NAME_TAM);
    }

    public TAMWorkspace getWorkspace() {
        return (TAMWorkspace) workspace;
    }

}
