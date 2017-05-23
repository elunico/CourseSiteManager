package csm.tam.data;

import csm.CourseSiteManagerApp;
import csm.tam.workspace.TAMWorkspace;
import csm.ui.CourseSiteManagerWorkspace;
import djf.components.AppDataComponent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import jtps.jTPS_Transaction;
import properties_manager.PropertiesManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static csm.CourseSiteManagerProp.DAYS_OF_WEEK;
import static csm.CourseSiteManagerProp.OFFICE_HOURS_TABLE_HEADERS;

/**
 * This is the data component for TAManagerApp. It has all the data needed
 * to be set by the user via the User Interface and file I/O can set and get
 * all the data from this object
 *
 * @author Richard McKenna
 * @author Thomas Povinelli
 */
public class TAData implements AppDataComponent {

    // DEFAULT VALUES FOR START AND END HOURS IN MILITARY HOURS
    public static final int MIN_START_HOUR = 9;
    public static final int MAX_END_HOUR = 20;
    public final ArrayList<String> timeHeaders;
    // WE'LL NEED ACCESS TO THE APP TO NOTIFY THE GUI WHEN DATA CHANGES
    CourseSiteManagerApp app;

    // NOTE THAT THIS DATA STRUCTURE WILL DIRECTLY STORE THE
    // DATA IN THE ROWS OF THE TABLE VIEW
    ObservableList<TeachingAssistant> teachingAssistants;
    // THIS WILL STORE ALL THE OFFICE HOURS GRID DATA, WHICH YOU
    // SHOULD NOTE ARE StringProperty OBJECTS THAT ARE CONNECTED
    // TO UI LABELS, WHICH MEANS IF WE CHANGE VALUES IN THESE
    // PROPERTIES IT CHANGES WHAT APPEARS IN THOSE LABELS
    HashMap<String, StringProperty> officeHours;
    // THESE ARE THE LANGUAGE-DEPENDENT VALUES FOR
    // THE OFFICE HOURS GRID HEADERS. NOTE THAT WE
    // LOAD THESE ONCE AND THEN HANG ON TO THEM TO
    // INITIALIZE OUR OFFICE HOURS GRID
    ArrayList<String> gridHeaders;
    // THESE ARE THE TIME BOUNDS FOR THE OFFICE HOURS GRID. NOTE
    // THAT THESE VALUES CAN BE DIFFERENT FOR DIFFERENT FILES, BUT
    // THAT OUR APPLICATION USES THE DEFAULT TIME VALUES AND PROVIDES
    // NO MEANS FOR CHANGING THESE VALUES
    SimpleIntegerProperty startHour;
    SimpleIntegerProperty endHour;

    /**
     * This constructor will setup the required data structures for
     * use, but will have to wait on the office hours grid, since
     * it receives the StringProperty objects from the Workspace.
     *
     * @param initApp The application this data manager belongs to.
     */
    public TAData(CourseSiteManagerApp initApp) {
        // KEEP THIS FOR LATER
        app = initApp;

        // CONSTRUCT THE LIST OF TAs FOR THE TABLE
        teachingAssistants = FXCollections.observableArrayList();

        // THESE ARE THE DEFAULT OFFICE HOURS
        startHour = new SimpleIntegerProperty(MIN_START_HOUR);
        endHour = new SimpleIntegerProperty(MAX_END_HOUR);

        //THIS WILL STORE OUR OFFICE HOURS
        officeHours = new HashMap();

        // THESE ARE THE LANGUAGE-DEPENDENT OFFICE HOURS GRID HEADERS
        PropertiesManager props = PropertiesManager.getPropertiesManager();
        timeHeaders = OFFICE_HOURS_TABLE_HEADERS;
        ArrayList<String> dowHeaders = DAYS_OF_WEEK;
        gridHeaders = new ArrayList();
        gridHeaders.addAll(timeHeaders);
        gridHeaders.addAll(dowHeaders);
    }

    /**
     * Called each time new work is created or loaded, it resets all data
     * and data structures such that they can be used for new values.
     */
    @Override
    public void resetData() {
        startHour.setValue(MIN_START_HOUR);
        endHour.set(MAX_END_HOUR);
        teachingAssistants.clear();
        officeHours.clear();
    }

    public int getStartHour() {
        return startHour.get();
    }

    public void setStartHour(int startHour) {
        this.startHour.set(startHour);
    }

    public SimpleIntegerProperty startHourProperty() {
        return startHour;
    }

    public int getEndHour() {
        return endHour.get();
    }

    // ACCESSOR METHODS

    public void setEndHour(int endHour) {
        this.endHour.set(endHour);
    }

    public SimpleIntegerProperty endHourProperty() {
        return endHour;
    }

    public ArrayList<String> getGridHeaders() {
        return gridHeaders;
    }

    public ObservableList<TeachingAssistant> getTeachingAssistants() {
        return teachingAssistants;
    }

    public void setTeachingAssistants(
      ObservableList<TeachingAssistant> teachingAssistants)
    {
        this.teachingAssistants = teachingAssistants;
    }

    public StringProperty getCellTextProperty(int col, int row) {
        String cellKey = getCellKey(col, row);
        return officeHours.get(cellKey);
    }

    public String getCellKey(int col, int row) {
        return col + "_" + row;
    }

    public HashMap<String, StringProperty> getOfficeHours() {
        return officeHours;
    }

    public int getNumRows() {
        return ((endHour.subtract(startHour).intValue()) * 2) + 1;
    }

    public String getTimeString(int militaryHour, boolean onHour) {
        String minutesText = "00";
        if (!onHour) {
            minutesText = "30";
        }

        // FIRST THE START AND END CELLS
        int hour = militaryHour;
        if (hour > 12) {
            hour -= 12;
        }
        String cellText = "" + hour + ":" + minutesText;
        if (militaryHour < 12) {
            cellText += "am";
        } else {
            cellText += "pm";
        }
        return cellText;
    }

    public TeachingAssistant getTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return ta;
            }
        }
        return null;
    }

    /**
     * This method is for giving this data manager the string property
     * for a given cell.
     */
    public void setCellProperty(int col, int row, StringProperty prop) {
        String cellKey = getCellKey(col, row);
        officeHours.put(cellKey, prop);
    }

    /**
     * This method is for setting the string property for a given cell.
     */
    public void setGridProperty(ArrayList<ArrayList<StringProperty>> grid,
      int column, int row, StringProperty prop)
    {
        grid.get(row).set(column, prop);
    }

    public void initHours(String startHourText, String endHourText) {
        TAMWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getTaManagerTab()
          .getWorkspace();
        int initStartHour = Integer.parseInt(startHourText);
        int initEndHour = Integer.parseInt(endHourText);
        if ((initStartHour >= MIN_START_HOUR)
            && (initEndHour <= MAX_END_HOUR)
            && (initStartHour <= initEndHour))
        {
            // THESE ARE VALID HOURS SO KEEP THEM
            workspace.getStartTimesBox()
                     .getSelectionModel()
                     .select(initStartHour);
            workspace.getEndTimesBox().getSelectionModel().select(initEndHour);
            initOfficeHours(initStartHour, initEndHour);
        }
    }

    private void initOfficeHours(int initStartHour, int initEndHour) {
        TAMWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getTaManagerTab()
          .getWorkspace();

        // NOTE THAT THESE VALUES MUST BE PRE-VERIFIED
        startHour.set(initStartHour);
        endHour.set(initEndHour);

        // EMPTY THE CURRENT OFFICE HOURS VALUES
        //officeHours.clear();

        // WE'LL BUILD THE USER INTERFACE COMPONENT FOR THE
        // OFFICE HOURS GRID AND FEED THEM TO OUR DATA
        // STRUCTURE AS WE GO
        workspace.reloadOfficeHoursGrid(this);
    }

    public void addTA(String initName, String initEmail) {
        addTA(initName, initEmail, true);
    }

    public void addTA(String initName, String initEmail, boolean isUndergrad) {
        jTPS_Transaction addTAAction = new jTPS_Transaction() {

            TeachingAssistant ta;

            @Override
            public void doTransaction() {
                // MAKE THE TA
                ta = new TeachingAssistant(initName, initEmail, isUndergrad);

                // ADD THE TA
                if (!containsTA(initName)) {
                    teachingAssistants.add(ta);
                }

                // SORT THE TAS
                Collections.sort(teachingAssistants);
                app.getGUI().getFileController().markAsEdited(app.getGUI());
            }

            @Override
            public void undoTransaction() {
                teachingAssistants.remove(ta);
                String name = ta.getName();
                for (StringProperty property : officeHours.values()) {
                    if (property.getValue().contains(name)) {
                        removeTAFromCell(property, name);
                    }
                }
            }
        };

        if (!app.testing) {
            app.getGUI().getTps().addTransaction(addTAAction);
        } else {
            addTAAction.doTransaction();
        }


    }

    public boolean containsTA(String testName) {
        for (TeachingAssistant ta : teachingAssistants) {
            if (ta.getName().equals(testName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method removes taName from the office grid cell
     * represented by cellProp.
     */
    public void removeTAFromCell(StringProperty cellProp, String taName) {
        // GET THE CELL TEXT
        String cellText = cellProp.getValue();
        // IS IT THE ONLY TA IN THE CELL?
        if (cellText.equals(taName)) {
            cellProp.setValue("");
        }
        // IS IT THE FIRST TA IN A CELL WITH MULTIPLE TA'S?
        else if (cellText.indexOf(taName) == 0) {
            int startIndex = cellText.indexOf("\n") + 1;
            cellText = cellText.substring(startIndex);
            cellProp.setValue(cellText);
        }
        // IT MUST BE ANOTHER TA IN THE CELL
        else {
            int startIndex = cellText.indexOf("\n" + taName);
            int length = startIndex + taName.length() + 1;
            String newCellText = cellText.substring(0, startIndex);
            newCellText = newCellText + cellText.substring(length);
            cellProp.setValue(newCellText);
        }
    }

    public void addOfficeHoursReservation(String day, String time,
      String taName)
    {
        String cellKey = getCellKey(day, time);
        toggleTAOfficeHours(cellKey, taName);
    }

    public String getCellKey(String day, String time) {
        int col = gridHeaders.indexOf(day);
        int row = 1;
        int hour = Integer.parseInt(time.substring(0, time.indexOf("_")));
        int milHour = hour;
        if (hour < startHour.intValue()) {
            milHour += 12;
        }
        row += (milHour - startHour.intValue()) * 2;
        if (time.contains("_30")) {
            row += 1;
        }
        return getCellKey(col, row);
    }

    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName) {
        toggleTAOfficeHours(cellKey, taName, true);
    }

    /**
     * This function toggles the taName in the cell represented
     * by cellKey. Toggle means if it's there it removes it, if
     * it's not there it adds it.
     */
    public void toggleTAOfficeHours(String cellKey, String taName,
      boolean withUndo)
    {
        if (officeHours.get(cellKey) == null) {
            // sometimes when resizing the grid we get extra empty cells
            // that need to be ignored
            // note we do this here to not create extra transactions
            return;
        }
        jTPS_Transaction toggleOHAction = new jTPS_Transaction() {

            private String oldValue;

            @Override
            public void doTransaction() {
                StringProperty cellProp = officeHours.get(cellKey);
                String cellText = "";
                cellText = cellProp.getValue();
                oldValue = cellText;
                String[] presentTAs = cellText.split("\n");
                if (Arrays.stream(presentTAs).anyMatch((String s) -> {
                    return s.equals(taName);
                }))
                {
                    String[] names = cellText.split("\n");
                    StringBuilder sb = new StringBuilder();
                    for (String name : names) {
                        if (!name.equals(taName)) {
                            sb.append(name).append("\n");
                        }
                    }
                    if (sb.indexOf("\n") >= 0) {
                        sb.deleteCharAt(sb.lastIndexOf("\n"));
                    }
                    cellProp.setValue(sb.toString());
                } else {
                    cellProp.setValue(cellText + "\n" + taName);
                }
                app.getGUI().getFileController().markAsEdited(app.getGUI());
            }

            @Override
            public void undoTransaction() {
                StringProperty cellProp = officeHours.get(cellKey);
                cellProp.setValue(oldValue);
            }
        };
        if (withUndo) {
            app.getGUI().getTps().addTransaction(toggleOHAction);
        } else {
            toggleOHAction.doTransaction();
        }
    }

    @Override
    public String toString() {
        return "TAData{" +
               "startHour=" + startHour +
               ", endHour=" + endHour +
               ", \nteachingAssistants=" + teachingAssistants +
               ", officeHours=" + officeHours +
               ", gridHeaders=" + gridHeaders +
               ", timeHeaders=" + timeHeaders +

               '}';
    }
}
