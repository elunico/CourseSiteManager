package csm.rde;

import csm.CourseSiteManagerApp;
import csm.rde.workspace.RDEWorkspace;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_RDE;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class RDETab {
    public final Tab self;
    protected RDEWorkspace workspace;

    public RDETab(CourseSiteManagerApp app, TabPane pane) {
        workspace = new RDEWorkspace(app);
        self = new Tab();
        self.setContent(workspace.getContentPane());
        self.setText(TAB_NAME_RDE);
        pane.getTabs().add(self);
    }

    public RDEWorkspace getWorkspace() {
        return workspace;
    }
}
