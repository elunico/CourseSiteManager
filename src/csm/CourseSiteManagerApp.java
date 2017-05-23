package csm;

import csm.ui.CourseSiteManagerWorkspace;
import djf.AppTemplate;
import djf.ui.AppMessageDialogSingleton;

import java.util.Locale;

import static csm.CourseSiteManagerProp.ABOUT_MESSAGE;
import static csm.CourseSiteManagerProp.ABOUT_TITLE;

/**
 * This class serves as the application class for our TA Manager App program.
 * Note that much of its behavior is inherited from AppTemplate, as defined in
 * the Desktop Java Framework. This app starts by loading all the UI-specific
 * settings like icon files and tooltips and other things, then the full
 * User Interface is loaded using those settings. Note that this is a
 * JavaFX application.
 *
 * @author Richard McKenna
 * @author Thomas Povinelli
 * @version 1.0
 */
public class CourseSiteManagerApp extends AppTemplate {
    public boolean testing = false;

    /**
     * This is where program execution begins. Since this is a JavaFX app it
     * will simply call launch, which gets JavaFX rolling, resulting in sending
     * the properly initialized Stage (i.e. window) to the start method inherited
     * from AppTemplate, defined in the Desktop Java Framework.
     */
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        launch(args);
    }

    /**
     * This hook method must initialize all four components in the
     * proper order ensuring proper dependencies are respected, meaning
     * all proper objects are already constructed when they are needed
     * for use, since some may need others for initialization.
     */
    @Override
    public void buildAppComponentsHook() {
        // CONSTRUCT ALL FOUR COMPONENTS. NOTE THAT FOR THIS APP
        // THE WORKSPACE NEEDS THE DATA COMPONENT TO EXIST ALREADY
        // WHEN IT IS CONSTRUCTED, SO BE CAREFUL OF THE ORDER


        dataComponent = new CourseSiteManagerData(this);
        fileComponent = new CourseSiteManagerFiles(this);
        workspaceComponent = new CourseSiteManagerWorkspace(this);
        //if (!testing) {
        styleComponent = new CourseSiteManagerStyle(this);
        //}

        getGUI().getFileController().handleNewRequest(false);
    }

    @Override
    public boolean isTesting() {
        return false;
    }

    public void setTesting(boolean testing) {
        this.testing = testing;
    }

    public void handleAboutRequest() {
        AppMessageDialogSingleton singleton = AppMessageDialogSingleton.getSingleton();
        singleton.show(ABOUT_TITLE, ABOUT_MESSAGE);

    }
}
