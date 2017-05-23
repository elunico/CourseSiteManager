package csm.sde;

import csm.sde.workspace.SDEWorkspace;
import csm.ui.CourseSiteManagerTab;
import djf.AppTemplate;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_SDE;

/**
 * @author Thomas Povinelli
 *         Created 4/6/17
 *         In Homework4
 */
public class SDETab extends CourseSiteManagerTab {

    public SDETab(AppTemplate app, TabPane pane) {
        super(new SDEWorkspace(app), pane);
        workspace.getWorkspace()
                 .prefWidthProperty()
                 .bind(app.getGUI().getWindow().widthProperty().subtract(10));
        self.setText(TAB_NAME_SDE);

    }

    public SDEWorkspace getWorkspace() {
        return (SDEWorkspace) workspace;
    }

}
