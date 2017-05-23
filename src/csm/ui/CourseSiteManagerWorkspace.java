package csm.ui;

import csm.CourseSiteManagerApp;
import csm.cdm.CDMTab;
import csm.pde.PDETab;
import csm.rde.RDETab;
import csm.sde.SDETab;
import csm.tam.TAMTab;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import jtps.jTPS;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CourseSiteManagerWorkspace extends AppWorkspaceComponent {
    public final FlowPane pane;
    public final CDMTab courseDataTab;
    public final RDETab recitationDataTab;
    final CourseSiteManagerApp app;
    final TabPane tabPane;
    private final SDETab scheduleDataTab;
    private final PDETab projectDataTab;
    private final TAMTab taManagerTab;
    private final jTPS history = new jTPS();

    public CourseSiteManagerWorkspace(CourseSiteManagerApp app) {
        this.app = app;
        // REQUIRED TO GET THE BUTTONS TO BE MANAGED BY THE JTPS

        if (!app.isTesting()) {
            app.getGUI().registerTPS(history);
            app.getGUI().registerButtons();
        }


        tabPane = new TabPane();
        tabPane.setPadding(new Insets(3));
        tabPane.setBackground(new Background(new BackgroundFill(
          Color.color(0.71568628f, 0.6529412f, 1f), CornerRadii.EMPTY,
          Insets.EMPTY)));

        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        pane = new FlowPane(tabPane);

        courseDataTab = new CDMTab(app, tabPane);
        taManagerTab = new TAMTab(app, tabPane);
        recitationDataTab = new RDETab(app, tabPane);
        scheduleDataTab = new SDETab(app, tabPane);
        projectDataTab = new PDETab(app, tabPane);

        // KEEP THE WORKSPACE FLOWPANE
        workspace = pane;

        // Actually puts the tabs in the window under the toolbar
        app.getGUI()
           .getAppPane()
           .setBackground(new Background(
             new BackgroundFill(Color.NAVY, CornerRadii.EMPTY,
               Insets.EMPTY)));
        app.getGUI().getAppPane().setCenter(workspace);
    }


    public static Button makeMinusButton() {
        Button b = new Button("-");
        b.setFont(
          Font.font(Font.getDefault().getFamily(), FontWeight.BOLD, 18));
        return b;
    }

    public CDMTab getCourseDataTab() {
        return courseDataTab;
    }

    public RDETab getRecitationDataTab() {
        return recitationDataTab;
    }

    public SDETab getScheduleDataTab() {
        return scheduleDataTab;
    }

    public PDETab getProjectDataTab() {
        return projectDataTab;
    }

    public TAMTab getTaManagerTab() {
        return taManagerTab;
    }

    public jTPS getHistory() {
        return history;
    }

    @Override
    public void resetWorkspace() {
        taManagerTab.getWorkspace().resetWorkspace();
        courseDataTab.getWorkspace().resetWorkspace();
        scheduleDataTab.getWorkspace().resetWorkspace();
        recitationDataTab.getWorkspace().resetWorkspace();
        projectDataTab.getWorkspace().resetWorkspace();
    }

    @Override
    public void reloadWorkspace(AppDataComponent appDataComponent) {
        taManagerTab.getWorkspace().reloadWorkspace(appDataComponent);
        courseDataTab.getWorkspace().reloadWorkspace(appDataComponent);
        scheduleDataTab.getWorkspace().reloadWorkspace(appDataComponent);
        recitationDataTab.getWorkspace().reloadWorkspace(appDataComponent);
        projectDataTab.getWorkspace().reloadWorkspace(appDataComponent);
    }
}
