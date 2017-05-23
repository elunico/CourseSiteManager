package csm.rde;

import csm.CourseSiteManagerApp;
import csm.rde.workspace.RDEWorkspace;
import csm.ui.CourseSiteManagerTab;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_RDE;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class RDETab extends CourseSiteManagerTab {

    public RDETab(CourseSiteManagerApp app, TabPane pane) {
        super(new RDEWorkspace(app), pane);
        self.setText(TAB_NAME_RDE);
    }

    public RDEWorkspace getWorkspace() {
        return (RDEWorkspace) workspace;
    }

}
