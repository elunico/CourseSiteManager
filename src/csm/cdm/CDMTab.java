package csm.cdm;

import csm.cdm.workspace.CDMWorkspace;
import csm.ui.CourseSiteManagerTab;
import djf.AppTemplate;
import djf.components.AppWorkspaceComponent;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_CDM;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CDMTab extends CourseSiteManagerTab {

    public CDMTab(AppTemplate app, TabPane pane) {
        super(new CDMWorkspace(app), pane);
        self.setText(TAB_NAME_CDM);

    }

    public CDMWorkspace getWorkspace() {
        return (CDMWorkspace) workspace;
    }
}
