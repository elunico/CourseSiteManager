package csm.ui;

import csm.cdm.workspace.CDMWorkspace;
import djf.AppTemplate;
import djf.components.AppWorkspaceComponent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_CDM;

/**
 * @author Thomas Povinelli
 *         Created 5/23/17
 *         In CourseSiteManager
 */
public abstract class CourseSiteManagerTab {

    protected AppWorkspaceComponent workspace;
    protected Tab self;

    protected CourseSiteManagerTab(AppWorkspaceComponent workspace, TabPane pane) {
        self = new Tab();
        self.setContent(workspace.getWorkspace());
        this.workspace = workspace;
        pane.getTabs().add(self);
        pane.setMaxHeight(Double.NEGATIVE_INFINITY);
    }

    public Tab getSelf() {
        return self;
    }

    public AppWorkspaceComponent getSuperClassWorkspace() {

        return workspace;
    }

    public void setWorkspace(AppWorkspaceComponent workspace) {
        this.workspace = workspace;
    }
}
