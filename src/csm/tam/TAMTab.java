package csm.tam;

import csm.CourseSiteManagerApp;
import csm.tam.workspace.TAMWorkspace;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_TAM;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class TAMTab {
    public final Tab self;
    protected TAMWorkspace workspace;

    public TAMTab(CourseSiteManagerApp app, TabPane pane) {
        workspace = new TAMWorkspace(app);
        self = new Tab();
        self.setContent(workspace.getContentPane());
        self.setText(TAB_NAME_TAM);
        pane.getTabs().add(self);
    }

    public Tab getTab() {
        return self;
    }

    public TAMWorkspace getWorkspace() {
        return workspace;
    }
}
