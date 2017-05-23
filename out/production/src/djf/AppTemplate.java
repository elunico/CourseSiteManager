package djf;

import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.components.AppStyleComponent;
import djf.components.AppWorkspaceComponent;
import javafx.application.Application;
import javafx.scene.control.ChoiceDialog;
import javafx.stage.Stage;
import properties_manager.InvalidXMLFileFormatException;
import properties_manager.PropertiesManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static djf.settings.AppPropertyType.*;
import static djf.settings.AppStartupConstants.*;
import djf.ui.AppGUI;
import djf.ui.AppMessageDialogSingleton;
import djf.ui.AppYesNoCancelDialogSingleton;

/**
 * This is the framework's JavaFX application. It provides the start method that
 * begins the program initialization, which delegates component initialization
 * to the application-specific child class' hook function.
 *
 * @author Richard McKenna
 * @version 1.0
 */
public abstract class AppTemplate extends Application {

    // THIS IS THE APP'S FULL JavaFX GUI. NOTE THAT ALL APPS WOULD
    // SHARE A COMMON UI EXCEPT FOR THE CUSTOM WORKSPACE
    protected AppGUI gui;

    // THIS CLASS USES A COMPONENT ARCHITECTURE DESIGN PATTERN, MEANING IT
    // HAS OBJECTS THAT CAN BE SWAPPED OUT FOR OTHER COMPONENTS
    // THIS APP HAS 4 COMPONENTS
    // THE COMPONENT FOR MANAGING CUSTOM APP DATA
    protected AppDataComponent dataComponent;

    // THE COMPONENT FOR MANAGING CUSTOM FILE I/O
    protected AppFileComponent fileComponent;

    // THE COMPONENT FOR THE GUI WORKSPACE
    protected AppWorkspaceComponent workspaceComponent;

    // THE COMPONENT FOR MANAGING UI STYLE
    protected AppStyleComponent styleComponent;
    private boolean testing = false;

    // THIS METHOD MUST BE OVERRIDDEN WHERE THE CUSTOM BUILDER OBJECT
    // WILL PROVIDE THE CUSTOM APP COMPONENTS

    /**
     * Accessor for the data component.
     */
    public AppDataComponent getDataComponent() {
        return dataComponent;
    }

    // COMPONENT ACCESSOR METHODS

    /**
     * Accessor for the file component.
     */
    public AppFileComponent getFileComponent() {
        return fileComponent;
    }

    /**
     * Accessor for the workspace component.
     */
    public AppWorkspaceComponent getWorkspaceComponent() {
        return workspaceComponent;
    }

    /**
     * Accessor for the style component.
     */
    public AppStyleComponent getStyleComponent() {
        return styleComponent;
    }

    /**
     * Accessor for the gui. Note that the GUI would contain the workspace.
     */
    public AppGUI getGUI() {
        return gui;
    }

    /**
     * This is where our Application begins its initialization, it will load the
     * custom app properties, build the components, and fully initialize
     * everything to get the app rolling.
     *
     * @param primaryStage This application's window.
     */
    @Override
    public void start(Stage primaryStage) {
        // LET'S START BY INITIALIZING OUR DIALOGS
        AppMessageDialogSingleton messageDialog = AppMessageDialogSingleton.getSingleton();
        messageDialog.init(primaryStage);
        AppYesNoCancelDialogSingleton yesNoDialog = AppYesNoCancelDialogSingleton
          .getSingleton();
        yesNoDialog.init(primaryStage);
        PropertiesManager props = PropertiesManager.getPropertiesManager();

        List<String> choices = new ArrayList<>();
        choices.add("English");
        choices.add("Español");

        ChoiceDialog<String> choiceDialog = new ChoiceDialog<>("English", choices);
        choiceDialog.setTitle("Choose A Language");
        choiceDialog.setHeaderText("Choose a Language/Elija un idioma");
        //choiceDialog.setContentText("Choose your letter:");

        String filename = "";
// Traditional way to get the response value.
        Optional<String> result;
        if (!isTesting()) {
            result = choiceDialog.showAndWait();
        } else {
            result = Optional.of("English");
        }
        if (result.isPresent()) {
            if (result.get().equals("English")) {
                filename = APP_PROPERTIES_FILE_NAME_ENGLISH;
            } else {
                filename = APP_PROPERTIES_FILE_NAME_SPANISH;
            }
        }

        try {
            // LOAD APP PROPERTIES, BOTH THE BASIC UI STUFF FOR THE FRAMEWORK
            // AND THE CUSTOM UI STUFF FOR THE WORKSPACE
            boolean success = loadProperties(filename);
            AppYesNoCancelDialogSingleton.getSingleton().initConstants();

            if (success) {
                // GET THE TITLE FROM THE XML FILE
                String appTitle = props.getProperty(APP_TITLE);

                // BUILD THE APP GUI OBJECT FIRST, BUT DON'T
                gui = new AppGUI(primaryStage, appTitle, this);

                // THIS BUILDS ALL OF THE COMPONENTS, NOTE THAT
                // IT WOULD BE DEFINED IN AN APPLICATION-SPECIFIC
                // CHILD CLASS
                buildAppComponentsHook();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
            //AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            //dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
        }
    }

    /**
     * This function must be overridden, it should initialize all of the
     * components used by the app in the proper order according to the
     * particular app's dependencies.
     */
    public abstract void buildAppComponentsHook();

    public boolean isTesting() {
        return testing;
    }

    /**
     * Loads this application's properties file, which has a number of settings
     * for initializing the user interface.
     *
     * @param propertiesFileName The XML file containing properties to be loaded
     *                           in order to initialize the UI.
     * @return true if the properties file was loaded successfully, false
     * otherwise.
     */
    public boolean loadProperties(String propertiesFileName) {
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        try {
            // LOAD THE SETTINGS FOR STARTING THE APP
            props.addProperty(PropertiesManager.DATA_PATH_PROPERTY, PATH_DATA);
            props.loadProperties(propertiesFileName, PROPERTIES_SCHEMA_FILE_NAME);
            return true;
        } catch (InvalidXMLFileFormatException ixmlffe) {
            // SOMETHING WENT WRONG INITIALIZING THE XML FILE
            AppMessageDialogSingleton dialog = AppMessageDialogSingleton.getSingleton();
            dialog.show(props.getProperty(PROPERTIES_LOAD_ERROR_TITLE), props.getProperty(PROPERTIES_LOAD_ERROR_MESSAGE));
            return false;
        }
    }

    public abstract void handleAboutRequest();
}
