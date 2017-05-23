package csm.pde;

import csm.pde.workspace.PDEWorkspace;
import djf.AppTemplate;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_PDE;

/**
 * @author Thomas Povinelli
 *         Created 4/9/17
 *         In Homework4
 */
public class PDETab {
    public final Tab self;
    protected PDEWorkspace workspace;

    public PDETab(AppTemplate app, TabPane pane) {
        this.workspace = new PDEWorkspace(app);
        self = new Tab();
        self.setText(TAB_NAME_PDE);
        self.setContent(workspace.getContentPane());
        pane.getTabs().add(self);
    }

    public PDEWorkspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(PDEWorkspace workspace) {
        this.workspace = workspace;
    }
}
