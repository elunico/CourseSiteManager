package csm.cdm;

import csm.cdm.workspace.CDMWorkspace;
import djf.AppTemplate;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_CDM;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CDMTab {
    public final Tab self;
    protected CDMWorkspace workspace;

    public CDMTab(AppTemplate app, TabPane pane) {
        this.workspace = new CDMWorkspace(app);
        self = new Tab();
        self.setText(TAB_NAME_CDM);
        self.setContent(workspace.getContent());
        pane.getTabs().add(self);
        pane.setMaxHeight(Double.NEGATIVE_INFINITY);

    }

    public Tab getSelf() {
        return self;
    }

    public CDMWorkspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(CDMWorkspace workspace) {
        this.workspace = workspace;
    }
}
