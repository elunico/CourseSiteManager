package csm.pde;

import csm.pde.workspace.PDEWorkspace;
import csm.ui.CourseSiteManagerTab;
import djf.AppTemplate;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_PDE;

/**
 * @author Thomas Povinelli
 *         Created 4/9/17
 *         In Homework4
 */
public class PDETab extends CourseSiteManagerTab {

    public PDETab(AppTemplate app, TabPane pane) {
        super(new PDEWorkspace(app), pane);
        self.setText(TAB_NAME_PDE);

    }

    public PDEWorkspace getWorkspace() {
        return (PDEWorkspace) workspace;
    }

}
