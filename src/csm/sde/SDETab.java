package csm.sde;

import csm.sde.workspace.SDEWorkspace;
import djf.AppTemplate;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import static csm.CourseSiteManagerProp.TAB_NAME_SDE;

/**
 * @author Thomas Povinelli
 *         Created 4/6/17
 *         In Homework4
 */
public class SDETab {
    public final Tab self;
    protected SDEWorkspace workspace;

    public SDETab(AppTemplate app, TabPane pane) {
        this.workspace = new SDEWorkspace(app);
        workspace.getContentPane()
                 .prefWidthProperty()
                 .bind(app.getGUI().getWindow().widthProperty().subtract(10));
        self = new Tab();
        self.setText(TAB_NAME_SDE);
        self.setContent(workspace.getContentPane());
        pane.getTabs().add(self);
    }

    public SDEWorkspace getWorkspace() {
        return workspace;
    }

    public void setWorkspace(SDEWorkspace workspace) {
        this.workspace = workspace;
    }
}
