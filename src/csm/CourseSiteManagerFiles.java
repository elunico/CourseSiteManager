package csm;

import csm.cdm.data.CDMData;
import csm.cdm.data.TemplateDirectoryFile;
import csm.pde.data.PDEData;
import csm.pde.data.Student;
import csm.pde.data.Team;
import csm.rde.data.RDEData;
import csm.rde.data.Recitation;
import csm.sde.data.SDEData;
import csm.sde.data.ScheduleItem;
import csm.tam.data.TAData;
import csm.tam.data.TeachingAssistant;
import csm.tam.data.TimeSlot;
import djf.components.AppDataComponent;
import djf.components.AppFileComponent;
import djf.ui.AppMessageDialogSingleton;
import javafx.application.Platform;
import javafx.collections.ObservableList;

import javax.json.*;
import javax.json.stream.JsonGenerator;
import java.io.*;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CourseSiteManagerFiles implements AppFileComponent {


    public static final String TA_NAME = "name";
    public static final String TA_EMAIL = "email";
    public static final String TA_UNDERGRAD = "undergrad";
    public static final String TAS = "TAS";
    public static final String TAM = "TAM";
    public static final String RDE = "RDE";
    public static final String RECITATIONS = "recitations";
    public static final String CDM = "CDM";
    public static final String SECTION = "section";
    public static final String INSTRUCTOR = "instructor";
    public static final String DAY_TIME = "day_time";
    public static final String LOCATION = "location";
    public static final String TA_1 = "ta_1";
    public static final String TA_2 = "ta_2";
    public static final String SUBJECT = "subject";
    public static final String NUMBER = "number";
    public static final String SEMESTER = "semester";
    public static final String YEAR = "year";
    public static final String INSTRUCTOR_NAME = "instructor_name";
    public static final String INSTRUCTOR_HOME = "instructor_home";
    public static final String EXPORT_DIR = "export_dir";
    public static final String SITE_TEMPLATE_DIR = "site_template_dir";
    public static final String BANNER_IMAGE = "banner_image";
    public static final String LEFT_IMAGE = "left_image";
    public static final String RIGHT_IMAGE = "right_image";
    public static final String STYLESHEET = "stylesheet";
    public static final String END_HOUR = "endHour";
    public static final String START_HOUR = "startHour";
    public static final String SDE = "SDE";
    public static final String SCHEDULE_ITEMS = "schedule_items";
    public static final String TOPIC = "topic";
    public static final String TYPE = "type";
    public static final String DATE = "date";
    public static final String TITLE = "title";
    public static final String END_FRIDAY = "end_friday";
    public static final String START_MONDAY = "start_monday";
    public static final String ROLE = "role";
    public static final String TEAM = "team";
    public static final String LAST_NAME = "last_name";
    public static final String FIRST_NAME = "first_name";
    public static final String LINK = "link";
    public static final String TEXT_COLOR = "text_color";
    public static final String COLOR = "color";
    public static final String NAME = "name";
    public static final String PDE = "PDE";
    public static final String STUDENTS = "students";
    public static final String TEAMS = "teams";
    public static final String START_MONTH = "start_month";
    public static final String START_DAY = "start_day";
    public static final String START_YEAR = "start_year";
    public static final String END_MONTH = "end_month";
    public static final String END_DAY = "end_day";
    public static final String END_YEAR = "end_year";
    public static final String UNDERGRAD_TAS = "undergrad_tas";
    public static final String TIME = "time";
    public static final String DAY = "day";
    public static final String OFFICE_HOURS = "officeHours";
    public static final String GRAD_TAS = "grad_tas";
    public static final String CRITERIA = "criteria";
    public static final String HOLIDAYS = "holidays";
    public static final String LECTURES = "lectures";
    private final CourseSiteManagerApp app;

    public CourseSiteManagerFiles(CourseSiteManagerApp courseSiteManagerApp) {
        this.app = courseSiteManagerApp;
    }

    private static void copyFile(File source, File dest) throws IOException {
        FileChannel sourceChannel = null;
        FileChannel destinationChannel = null;
        source = new File(source.toString().replace("file:", ""));
        dest = new File(dest.toString().replace("file:", ""));
        try {
            sourceChannel = new FileInputStream(source).getChannel();
            destinationChannel = new FileOutputStream(dest).getChannel();
            destinationChannel.transferFrom(sourceChannel, 0,
              sourceChannel.size());
        } finally {
            sourceChannel.close();
            destinationChannel.close();
        }
    }

    @Override
    public void saveData(AppDataComponent appDataComponent, String s)
      throws IOException
    {
        CourseSiteManagerData data = (CourseSiteManagerData) appDataComponent;

        CDMData cdmData = data.getCdmData();
        TAData taData = data.getTaData();
        RDEData rdeData = data.getRdeData();
        SDEData sdeData = data.getSdeData();
        PDEData pdeData = data.getPdeData();

        // PDE Data
        // TODO: Save office hours grid

        JsonArrayBuilder teamJsonArray = Json.createArrayBuilder();
        for (Team team : pdeData.getTeams()) {
            teamJsonArray.add(Json.createObjectBuilder()
                                  .add(NAME, team.getName())
                                  .add(COLOR, team.getColor())
                                  .add(TEXT_COLOR, team.getTextColor())
                                  .add(LINK, team.getLink())
                                  .build());
        }

        JsonArrayBuilder studentJsonArray = Json.createArrayBuilder();
        for (Student student : pdeData.getStudents()) {
            studentJsonArray.add(Json.createObjectBuilder()
                                     .add(FIRST_NAME, student.getFirstName())
                                     .add(LAST_NAME, student.getLastName())
                                     .add(TEAM, student.getTeam().getName())
                                     .add(ROLE, student.getRole())
                                     .build());
        }

        JsonObject pdeJsonObject = Json.createObjectBuilder()
                                       .add(TEAMS, teamJsonArray.build())
                                       .add(STUDENTS, studentJsonArray.build())
                                       .build();


        // CDM Data

        JsonObjectBuilder cdmBuilder = Json.createObjectBuilder();

        // TOP PANEL OF DATA

        cdmBuilder.add(SUBJECT, cdmData.getSubject());
        cdmBuilder.add(TITLE, cdmData.getTitle());
        cdmBuilder.add(NUMBER, cdmData.getNumber());
        cdmBuilder.add(SEMESTER, cdmData.getSemester());
        cdmBuilder.add(YEAR, cdmData.getYear());
        cdmBuilder.add(INSTRUCTOR_NAME, cdmData.getInstructorName());
        cdmBuilder.add(INSTRUCTOR_HOME, cdmData.getInstructorHome());
        cdmBuilder.add(EXPORT_DIR, cdmData.getExportDir() != null ?
                                   cdmData.getExportDir() :
                                   "");

        cdmBuilder.add(SITE_TEMPLATE_DIR, cdmData.getTemplateDir() != null ?
                                          cdmData.getTemplateDir().toString() :
                                          "");

        // Table of pages is loaded dynamically from the saved directory location
        // no need to save them here

        // THIRD PANEL

        cdmBuilder.add(BANNER_IMAGE, cdmData.getBannerImagePath());
        cdmBuilder.add(LEFT_IMAGE, cdmData.getLeftImagePath());
        cdmBuilder.add(RIGHT_IMAGE, cdmData.getRightImagePath());
        cdmBuilder.add(STYLESHEET, cdmData.getStylesheet() != null ?
                                   cdmData.getStylesheet().toString() :
                                   "");

        JsonObject cdmJsonObject = cdmBuilder.build();

        // RECITATION DATA

        JsonArrayBuilder recitationArrayBuilder = Json.createArrayBuilder();
        for (Recitation recitation : rdeData.getRecitations()) {
            recitationArrayBuilder.add(Json.createObjectBuilder()
                                           .add(SECTION,
                                             recitation.getSection())
                                           .add(INSTRUCTOR,
                                             recitation.getInstructor())
                                           .add(DAY_TIME,
                                             recitation.getDayTime())
                                           .add(LOCATION,
                                             recitation.getLocation())
                                           .add(TA_1,
                                             recitation.getTa1() != null ?
                                             recitation.getTa1().getName() :
                                             "")
                                           .add(TA_2,
                                             recitation.getTa2() != null ?
                                             recitation.getTa2().getName() :
                                             "")
                                           .build());
        }

        JsonObject rdeJsonObject = Json.createObjectBuilder()
                                       .add(RECITATIONS,
                                         recitationArrayBuilder.build())
                                       .build();

        // SCHEDULE DATA
        JsonObjectBuilder sdeBuilder = Json.createObjectBuilder();
        sdeBuilder.add(START_MONTH, sdeData.getStartMonth());
        sdeBuilder.add(START_DAY, sdeData.getStartDay());
        sdeBuilder.add(START_YEAR, sdeData.getStartYear());
        sdeBuilder.add(END_MONTH, sdeData.getEndMonth());
        sdeBuilder.add(END_DAY, sdeData.getEndDay());
        sdeBuilder.add(END_YEAR, sdeData.getEndYear());

        JsonArrayBuilder sdeItemsArrayBuilder = Json.createArrayBuilder();
        for (ScheduleItem item : sdeData.getScheduleItems()) {
            sdeItemsArrayBuilder.add(Json.createObjectBuilder()
                                         .add(TYPE, item.getType())
                                         .add(DATE, item.getDate().toString())
                                         .add(TITLE, item.getTitle())
                                         .add(TOPIC, item.getTopic())
                                         .add(LINK, item.getTopic())
                                         .add(CRITERIA, item.getCriteria())
                                         .add(TIME, item.getTime())
                                         .build());
        }
        sdeBuilder.add(SCHEDULE_ITEMS, sdeItemsArrayBuilder.build());

        JsonObject sdeJsonObject = sdeBuilder.build();

        // TA DATA

        JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
        for (TeachingAssistant ta : taData.getTeachingAssistants()) {
            taArrayBuilder.add(Json.createObjectBuilder()
                                   .add(TA_NAME, ta.getName())
                                   .add(TA_EMAIL, ta.getEmail())
                                   .add(TA_UNDERGRAD, ta.isUndergraduate())
                                   .build());
        }
        JsonArray tasArray = taArrayBuilder.build();

        JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
        ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(taData);
        for (TimeSlot ts : officeHours) {
            JsonObject tsJson = Json.createObjectBuilder()
                                    .add(DAY, ts.getDay())
                                    .add(TIME, ts.getTime())
                                    .add(NAME, ts.getName()).build();
            timeSlotArrayBuilder.add(tsJson);
        }
        JsonArray timeSlotsArray = timeSlotArrayBuilder.build();

        // OUTER MOST TA OBJECT 1 of 5 Main objects to put into JSON
        JsonObject taJsonObject = Json.createObjectBuilder()
                                      .add(START_HOUR, taData.getStartHour())
                                      .add(END_HOUR, taData.getEndHour())
                                      .add(TAS, tasArray)
                                      .add(OFFICE_HOURS, timeSlotsArray)
                                      .build();


        // WHOLE JSON OBJECT with 5 components 1 per tab

        JsonObject entireJson = Json.createObjectBuilder()
                                    .add(TAM, taJsonObject)
                                    .add(RDE, rdeJsonObject)
                                    .add(CDM, cdmJsonObject)
                                    .add(SDE, sdeJsonObject)
                                    .add(PDE, pdeJsonObject)
                                    .build();

        writeJsonObject(entireJson, s);

    }

    private void writeJsonObject(JsonObject object, String fileName)
      throws IOException
    {
        // AND NOW OUTPUT IT TO A JSON FILE WITH PRETTY PRINTING
        Map<String, Object> properties = new HashMap<>(1);
        properties.put(JsonGenerator.PRETTY_PRINTING, true);
        JsonWriterFactory writerFactory = Json.createWriterFactory(properties);
        StringWriter sw = new StringWriter();
        JsonWriter jsonWriter = writerFactory.createWriter(sw);
        jsonWriter.writeObject(object);
        jsonWriter.close();


        // INIT THE WRITER
        OutputStream os = new FileOutputStream(fileName);
        JsonWriter jsonFileWriter = Json.createWriter(os);
        jsonFileWriter.writeObject(object);
        String prettyPrinted = sw.toString();
        PrintWriter pw = new PrintWriter(fileName);
        pw.write(prettyPrinted);
        pw.close();
    }

    @Override
    public void loadData(AppDataComponent appDataComponent, String s)
      throws IOException
    {


        // TODO: Erase data before loading because it doesn't seem like that's happening
        CourseSiteManagerData data = (CourseSiteManagerData) appDataComponent;

        CDMData cdmData = data.getCdmData();
        TAData taData = data.getTaData();
        RDEData rdeData = data.getRdeData();
        SDEData sdeData = data.getSdeData();
        PDEData pdeData = data.getPdeData();

        JsonObject in = loadJSONFile(s);

        JsonObject taJsonObject = in.getJsonObject(TAM);
        JsonObject cdmJsonObject = in.getJsonObject(CDM);
        JsonObject sdeJsonObject = in.getJsonObject(SDE);
        JsonObject pdeJsonObject = in.getJsonObject(PDE);
        JsonObject rdeJsonObject = in.getJsonObject(RDE);

        // LOAD TA first
        taData.initHours(String.valueOf(taJsonObject.getInt(START_HOUR)),
          String.valueOf(taJsonObject.getInt(END_HOUR)));
        //app.getWorkspaceComponent().reloadWorkspace(data);


        Platform.runLater(
          () -> taData.setStartHour(taJsonObject.getInt(START_HOUR)));
        Platform.runLater(
          () -> taData.setEndHour(taJsonObject.getInt(END_HOUR)));
        JsonArray jsonTAArray = taJsonObject.getJsonArray(TAS);
        for (int i = 0; i < jsonTAArray.size(); i++) {
            JsonObject jsonTA = jsonTAArray.getJsonObject(i);
            String name = jsonTA.getString(TA_NAME);
            String email = "";
            try {
                email = jsonTA.getString(TA_EMAIL);
            } catch (NullPointerException e) {
                AppMessageDialogSingleton singleton = AppMessageDialogSingleton.getSingleton();
                singleton.show("No emails in File",
                  "The JSON file does not contain emails and is improperly formatted");
                return;
            }
            boolean undergrad = jsonTA.getBoolean(TA_UNDERGRAD);
            taData.addTA(name, email, undergrad);
        }


        // AND THEN ALL THE OFFICE HOURS
        JsonArray jsonOfficeHoursArray = taJsonObject.getJsonArray(
          OFFICE_HOURS);
        for (int i = 0; i < jsonOfficeHoursArray.size(); i++) {
            JsonObject jsonOfficeHours = jsonOfficeHoursArray.getJsonObject(i);
            String day = jsonOfficeHours.getString(DAY);
            String time = jsonOfficeHours.getString(TIME);
            String name = jsonOfficeHours.getString(NAME);
            taData.addOfficeHoursReservation(day, time, name);
        }

        // LOAD CDM next

        cdmData.setStylesheet(cdmJsonObject.getString(STYLESHEET));
        cdmData.setTitle(cdmJsonObject.getString(TITLE));
        // images are really annoying and cannot have empty urls .-. -.-
        cdmData.setBannerImage(cdmJsonObject.getString(BANNER_IMAGE).isEmpty() ?
                               "file:" :
                               cdmJsonObject.getString(BANNER_IMAGE));
        cdmData.setLeftImage(cdmJsonObject.getString(LEFT_IMAGE).isEmpty() ?
                             "file:" :
                             cdmJsonObject.getString(LEFT_IMAGE));
        cdmData.setRightImage(cdmJsonObject.getString(RIGHT_IMAGE).isEmpty() ?
                              "file:" :
                              cdmJsonObject.getString(RIGHT_IMAGE));
        cdmData.setInstructorName(cdmJsonObject.getString(INSTRUCTOR_NAME));
        cdmData.setInstructorHome(cdmJsonObject.getString(INSTRUCTOR_HOME));
        cdmData.setYear(cdmJsonObject.getString(YEAR));
        cdmData.setSemester(cdmJsonObject.getString(SEMESTER));
        cdmData.setNumber(cdmJsonObject.getInt(NUMBER));
        cdmData.setSubject(cdmJsonObject.getString(SUBJECT));
        cdmData.setTemplateDir(cdmJsonObject.getString(SITE_TEMPLATE_DIR));
        cdmData.loadTemplateDir();
        cdmData.setExportDir(cdmJsonObject.getString(EXPORT_DIR));


        // Load PDE Next

        JsonArray jsonTeamsArray = pdeJsonObject.getJsonArray(TEAMS);
        for (int i = 0; i < jsonTeamsArray.size(); i++) {
            JsonObject jsonTeam = jsonTeamsArray.getJsonObject(i);
            String name = jsonTeam.getString(NAME);
            String color = jsonTeam.getString(COLOR);
            String textColor = jsonTeam.getString(TEXT_COLOR);
            String link = jsonTeam.getString(LINK);
            pdeData.getTeams().add(new Team(name, color, textColor, link));
        }

        JsonArray jsonStudentsArray = pdeJsonObject.getJsonArray(STUDENTS);
        for (int i = 0; i < jsonStudentsArray.size(); i++) {
            JsonObject jsonStudent = jsonStudentsArray.getJsonObject(i);
            String firstName = jsonStudent.getString(FIRST_NAME);
            String lastName = jsonStudent.getString(LAST_NAME);
            String teamName = jsonStudent.getString(TEAM);
            Object[] teams = pdeData.getTeams()
                                    .stream()
                                    .filter(t -> t.getName()
                                                  .equalsIgnoreCase(teamName))
                                    .toArray();
            Team team = (Team) teams[0];
            String role = jsonStudent.getString(ROLE);
            Student tempStudent = new Student(firstName, lastName, role, team);
            team.getMembers().add(tempStudent);
            pdeData.getStudents()
                   .add(tempStudent);
        }

        // LOAD RDE Next
        JsonArray jsonRecitations = rdeJsonObject.getJsonArray(RECITATIONS);
        for (int i = 0; i < jsonRecitations.size(); i++) {
            JsonObject recitation = jsonRecitations.getJsonObject(i);
            String section = recitation.getString(SECTION);
            String instructor = recitation.getString(INSTRUCTOR);
            String dayTime = recitation.getString(DAY_TIME);
            String location = recitation.getString(LOCATION);
            String ta1 = recitation.getString(TA_1);
            String ta2 = recitation.getString(TA_2);
            TeachingAssistant teachingAssistant1 = null, teachingAssistant2 = null;

            for (TeachingAssistant t : taData.getTeachingAssistants()) {
                if (t.getName().equalsIgnoreCase(ta1)) {
                    teachingAssistant1 = t;
                }
                if (t.getName().equalsIgnoreCase(ta2)) {
                    teachingAssistant2 = t;
                }
            }

            rdeData.getRecitations()
                   .add(new Recitation(section, instructor, location, teachingAssistant1, teachingAssistant2,
                     dayTime));
        }

        // LOAD SDE Last

        sdeData.setEndDay(sdeJsonObject.getInt(END_DAY));
        sdeData.setEndMonth(sdeJsonObject.getInt(END_MONTH));
        sdeData.setEndYear(sdeJsonObject.getInt(END_YEAR));
        sdeData.setStartDay(sdeJsonObject.getInt(START_DAY));
        sdeData.setStartMonth(sdeJsonObject.getInt(START_MONTH));
        sdeData.setStartYear(sdeJsonObject.getInt(START_YEAR));

        sdeData.updateDatePickers();

        JsonArray jsonScheduleItems = sdeJsonObject.getJsonArray(
          SCHEDULE_ITEMS);
        for (int i = 0; i < jsonScheduleItems.size(); i++) {
            JsonObject scheduleItem = jsonScheduleItems.getJsonObject(i);
            String type = scheduleItem.getString(TYPE);
            String title = scheduleItem.getString(TITLE);
            String topic = scheduleItem.getString(TOPIC);
            DateHybrid date = DateHybrid.parse(scheduleItem.getString(DATE));
            String criteria = scheduleItem.getString(CRITERIA);
            String link = scheduleItem.getString(LINK);
            String time = scheduleItem.getString(TIME);
            sdeData.getScheduleItems()
                   .add(new ScheduleItem(type, title, topic, date, time, link,
                     criteria));
        }

        app.getGUI().getFileController().markAsUnEdited(app.getGUI());
    }

    @Override
    public void exportData(AppDataComponent appDataComponent)
      throws IOException
    {

        // formatter: off
        if (((CourseSiteManagerData) appDataComponent).getCdmData()
                                                      .getTemplateDir() == null
            || ((CourseSiteManagerData) appDataComponent).getCdmData()
                                                         .getExportDir() ==
               null)
        {
            AppMessageDialogSingleton.getSingleton()
                                     .show(CourseSiteManagerProp.SITE_TEMPLATE_MISSING_ALERT,
                                       CourseSiteManagerProp.SITE_TEMPLATE_MISSING_MSG);
            return;
        }
        // formatter: on

        File siteTemplate = new File(
          ((CourseSiteManagerData) appDataComponent).getCdmData()
                                                    .getTemplateDir());

        File exportLocation = new File(
          ((CourseSiteManagerData) appDataComponent).getCdmData()
                                                    .getExportDir());

        if (!siteTemplate.exists() || !siteTemplate.isDirectory() ||
            !exportLocation.exists() || !exportLocation.isDirectory())
        {
            AppMessageDialogSingleton.getSingleton().show(
              CourseSiteManagerProp.SITE_TEMPLATE_MISSING_ALERT,
              CourseSiteManagerProp.SITE_TEMPLATE_MISSING_MSG);
            return;
        }

        copyAllUsedFiles(siteTemplate, exportLocation.toString());

        // Project Data
        CDMData cdmData = ((CourseSiteManagerData) app.getDataComponent()).getCdmData();

        JsonObject cdmObject = Json.createObjectBuilder()
                                   .add(SUBJECT, cdmData.getSubject())
                                   .add(NUMBER, cdmData.getNumber())
                                   .add(SEMESTER, cdmData.getSemester())
                                   .add(YEAR, cdmData.getYear())
                                   .add(TITLE, cdmData.getTitle()).build();


        writeJsonObject(cdmObject,
          exportLocation.toString() + File.separator + "js" + File.separator +
          "CourseInfo.json");


        PDEData pdeData = ((CourseSiteManagerData) app.getDataComponent()).getPdeData();

        JsonObjectBuilder pdeBuilder = Json.createObjectBuilder();

        pdeBuilder.add(SUBJECT, cdmData.getSubject());
        pdeBuilder.add(NUMBER, cdmData.getNumber());
        pdeBuilder.add(SEMESTER, cdmData.getSemester());
        pdeBuilder.add(YEAR, cdmData.getYear());
        pdeBuilder.add(TITLE, cdmData.getTitle());


        JsonArrayBuilder workArrayBuilder = Json.createArrayBuilder();
        JsonObjectBuilder innerObject = Json.createObjectBuilder();

        innerObject.add("semester",
          cdmData.getSemester() + " " + cdmData.getYear());
        JsonArrayBuilder projectsBuilder = Json.createArrayBuilder();

        for (Team team : pdeData.getTeams()) {
            JsonArrayBuilder members = Json.createArrayBuilder();
            for (Student s : team.getMembers()) {
                members.add(s.getFirstName() + " " + s.getLastName());
            }
            JsonObject o = Json.createObjectBuilder()
                               .add("name", team.getName())
                               .add(LINK, team.getLink())
                               .add("students", members.build())
                               .build();
            projectsBuilder.add(o);
        }

        innerObject.add("projects", projectsBuilder.build());
        workArrayBuilder.add(innerObject);
        pdeBuilder.add("work", workArrayBuilder);

        File f = new File(exportLocation.toString() + File.separator + "js");
        f.mkdir();

        JsonObject pdeObject = pdeBuilder.build();

        writeJsonObject(pdeObject,
          exportLocation.toString() + File.separator + "js" + File.separator +
          "ProjectsData.json");

        //RDE Data

        RDEData rdeData = ((CourseSiteManagerData) app.getDataComponent()).getRdeData();


        JsonObjectBuilder rdeBuilder = Json.createObjectBuilder();
        rdeBuilder.add(SUBJECT, cdmData.getSubject());
        rdeBuilder.add(NUMBER, cdmData.getNumber());
        rdeBuilder.add(SEMESTER, cdmData.getSemester());
        rdeBuilder.add(YEAR, cdmData.getYear());
        rdeBuilder.add(TITLE, cdmData.getTitle());
        JsonArrayBuilder rdeArrayBuilder = Json.createArrayBuilder();
        for (Recitation r : rdeData.getRecitations()) {
            JsonObject o = Json.createObjectBuilder()
                               .add(SECTION,
                                 r.getSection() + "(" + r.getInstructor() + ")")
                               .add(DAY_TIME, r.getDayTime())
                               .add(LOCATION, r.getLocation())
                               .add("ta_1",
                                 r.getTa1() != null ? r.getTa1().getName() : "")
                               .add("ta_2",
                                 r.getTa2() != null ? r.getTa2().getName() : "")
                               .build();
            rdeArrayBuilder.add(o);
        }
        rdeBuilder.add("recitations", rdeArrayBuilder.build());

        JsonObject rdeObject = rdeBuilder.build();


        f = new File(exportLocation.toString() + File.separator + "js");
        f.mkdir();

        writeJsonObject(rdeObject,
          exportLocation.toString() + File.separator + "js" + File.separator +
          "RecitationsData.json");


        //Schedule data

        SDEData sdeData = ((CourseSiteManagerData) appDataComponent).getSdeData();
        JsonObjectBuilder sdeBuilder = Json.createObjectBuilder();
        sdeBuilder.add("startingMondayMonth", sdeData.getStartMonth());
        sdeBuilder.add("startingMondayDay", sdeData.getStartDay());
        sdeBuilder.add("startingMondayYear", sdeData.getStartYear());
        sdeBuilder.add("endingFridayMonth", sdeData.getEndMonth());
        sdeBuilder.add("endingFridayYear", sdeData.getEndYear());
        sdeBuilder.add("endingFridayDay", sdeData.getEndDay());

        sdeBuilder.add(SUBJECT, cdmData.getSubject());
        sdeBuilder.add(NUMBER, cdmData.getNumber());
        sdeBuilder.add(SEMESTER, cdmData.getSemester());
        sdeBuilder.add(YEAR, cdmData.getYear());
        sdeBuilder.add(TITLE, cdmData.getTitle());

        JsonArrayBuilder holidaysBuilder = Json.createArrayBuilder();
        JsonArrayBuilder lecturesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder referencesBuilder = Json.createArrayBuilder();
        JsonArrayBuilder recitationsBuilder = Json.createArrayBuilder();
        JsonArrayBuilder hwBuilder = Json.createArrayBuilder();
        for (ScheduleItem item : sdeData.getScheduleItems()) {
            JsonObject o = Json.createObjectBuilder()
                               .add("month", item.getDate().getMonth())
                               .add("day", item.getDate().getDate())
                               .add("title", item.getTitle())
                               .add("link", item.getLink())
                               .add("topic", item.getTopic())
                               .add("criteria", item.getCriteria())
                               .add("time", item.getTime())
                               .build();
            if (item.getType().equalsIgnoreCase("holiday")) {
                holidaysBuilder.add(o);
            } else if (item.getType().equalsIgnoreCase("lecture") ||
                       item.getType().equalsIgnoreCase("exam"))
            {
                lecturesBuilder.add(o);
            } else if (item.getType().equalsIgnoreCase("Preference") ||
                       item.getType().equalsIgnoreCase("Preferences"))
            {
                referencesBuilder.add(o);
            } else if (item.getType().equalsIgnoreCase("recitation")) {
                recitationsBuilder.add(o);
            } else if (item.getType().equalsIgnoreCase("homework") ||
                       item.getType().equalsIgnoreCase("hw") ||
                       item.getType().equalsIgnoreCase("Homework Due"))
            {
                hwBuilder.add(o);
            }
        }
        sdeBuilder.add(HOLIDAYS, holidaysBuilder.build());
        sdeBuilder.add(LECTURES, lecturesBuilder.build());
        sdeBuilder.add("references", referencesBuilder.build());
        sdeBuilder.add("recitations", recitationsBuilder.build());
        sdeBuilder.add("hws", hwBuilder.build());

        JsonObject sdeObject = sdeBuilder.build();

        f = new File(exportLocation.toString() + File.separator + "js");
        f.mkdir();

        writeJsonObject(sdeObject,
          exportLocation.toString() + File.separator + "js" + File.separator +
          "ScheduleData.json");


        // CDM Info Next
        // CDM Data already exists

        JsonObjectBuilder cdmBuilder = Json.createObjectBuilder();

        // TOP PANEL OF DATA

        cdmBuilder.add(SUBJECT, cdmData.getSubject());
        cdmBuilder.add(TITLE, cdmData.getTitle());
        cdmBuilder.add(NUMBER, cdmData.getNumber());
        cdmBuilder.add(SEMESTER, cdmData.getSemester());
        cdmBuilder.add(YEAR, cdmData.getYear());
        cdmBuilder.add(INSTRUCTOR_NAME, cdmData.getInstructorName());
        cdmBuilder.add(INSTRUCTOR_HOME, cdmData.getInstructorHome());
        cdmBuilder.add(EXPORT_DIR, cdmData.getExportDir() != null ?
                                   cdmData.getExportDir().toString() :
                                   "");

        cdmBuilder.add(SITE_TEMPLATE_DIR, cdmData.getTemplateDir() != null ?
                                          cdmData.getTemplateDir().toString() :
                                          "");

        // Table of pages is loaded dynamically from the saved directory location
        // no need to save them here

        // THIRD PANEL

        cdmBuilder.add(BANNER_IMAGE, cdmData.getBannerImagePath());
        cdmBuilder.add(LEFT_IMAGE, cdmData.getLeftImagePath());
        cdmBuilder.add(RIGHT_IMAGE, cdmData.getRightImagePath());
        cdmBuilder.add(STYLESHEET, cdmData.getStylesheet() != null ?
                                   cdmData.getStylesheet().toString() :
                                   "");


        File imagesDir = new File(
          exportLocation.toString() + File.separator + "images");

        if (imagesDir.exists() && !imagesDir.isDirectory()) {
            copyFile(imagesDir, new File(imagesDir.toString() + "_alt"));
            imagesDir.delete();
            imagesDir.mkdir();
        }

        if (!imagesDir.exists()) {
            imagesDir.mkdir();
        }

        copyFile(new File(cdmData.getLeftImagePath()), new File(
          exportLocation.toString() + File.separator + "images" +
          File.separator +
          "left_footer.jpg"));
        copyFile(new File(cdmData.getRightImagePath()), new File(
          exportLocation.toString() + File.separator + "images" +
          File.separator +
          "right_footer.png"));
        copyFile(new File(cdmData.getBannerImagePath()), new File(
          exportLocation.toString() + File.separator + "images" +
          File.separator + "banner_image.png"));


        JsonObject cdmJsonObject = cdmBuilder.build();

        f = new File(exportLocation.toString() + File.separator + "js");
        f.mkdir();

        writeJsonObject(cdmJsonObject,
          exportLocation.toString() + File.separator + "js" + File.separator +
          "CourseInfoData.json");


        // TADATA Section (officeHoursGridData.json)
        TAData taData = ((CourseSiteManagerData) appDataComponent).getTaData();

        JsonArrayBuilder gradTaArrayBuilder = Json.createArrayBuilder();
        for (TeachingAssistant ta : taData.getTeachingAssistants()) {
            if (ta.isUndergraduate()) {
                continue;
            }
            gradTaArrayBuilder.add(Json.createObjectBuilder()
                                       .add(TA_NAME, ta.getName())
                                       .add(TA_EMAIL, ta.getEmail())
                                       .add(TA_UNDERGRAD, ta.isUndergraduate())
                                       .build());
        }
        JsonArray gradTasArray = gradTaArrayBuilder.build();

        JsonArrayBuilder taArrayBuilder = Json.createArrayBuilder();
        for (TeachingAssistant ta : taData.getTeachingAssistants()) {
            if (!ta.isUndergraduate()) {
                continue;
            }
            taArrayBuilder.add(Json.createObjectBuilder()
                                   .add(TA_NAME, ta.getName())
                                   .add(TA_EMAIL, ta.getEmail())
                                   .add(TA_UNDERGRAD, ta.isUndergraduate())
                                   .build());
        }
        JsonArray tasArray = taArrayBuilder.build();
        // NOW BUILD THE TIME SLOT JSON OBJCTS TO SAVE
        JsonArrayBuilder timeSlotArrayBuilder = Json.createArrayBuilder();
        ArrayList<TimeSlot> officeHours = TimeSlot.buildOfficeHoursList(taData);
        for (TimeSlot ts : officeHours) {
            JsonObject tsJson = Json.createObjectBuilder()
                                    .add(DAY, ts.getDay())
                                    .add(TIME, ts.getTime())
                                    .add(NAME, ts.getName()).build();
            timeSlotArrayBuilder.add(tsJson);
        }
        JsonArray timeSlotsArray = timeSlotArrayBuilder.build();

        // OUTER MOST TA OBJECT 1 of 5 Main objects to put into JSON
        JsonObjectBuilder taJsonObjectB = Json.createObjectBuilder();

        taJsonObjectB.add(SUBJECT, cdmData.getSubject());
        taJsonObjectB.add(NUMBER, cdmData.getNumber());
        taJsonObjectB.add(SEMESTER, cdmData.getSemester());
        taJsonObjectB.add(YEAR, cdmData.getYear());
        taJsonObjectB.add(TITLE, cdmData.getTitle());

        taJsonObjectB.add(START_HOUR, taData.getStartHour());
        taJsonObjectB.add(END_HOUR, taData.getEndHour());
        taJsonObjectB.add(UNDERGRAD_TAS, tasArray);
        taJsonObjectB.add(GRAD_TAS, gradTasArray);
        taJsonObjectB.add(OFFICE_HOURS, timeSlotsArray);
        JsonObject taJsonObject = taJsonObjectB.build();

        f = new File(exportLocation.toString() + File.separator + "js");
        f.mkdir();

        writeJsonObject(taJsonObject,
          exportLocation.toString() + File.separator + "js" + File.separator +
          "OfficeHoursGridData.json");


        AppMessageDialogSingleton.getSingleton()
                                 .show(
                                   CourseSiteManagerProp.EXPORT_COMPLETE_TITLE,
                                   CourseSiteManagerProp.EXPORT_COMPLETE_MSG);

    }

    @Override
    public void importData(AppDataComponent appDataComponent, String s)
      throws IOException
    {

    }

    private JsonObject loadJSONFile(String jsonFilePath) throws IOException {
        InputStream is = new FileInputStream(jsonFilePath);
        JsonReader jsonReader = Json.createReader(is);
        JsonObject json = jsonReader.readObject();
        jsonReader.close();
        is.close();
        return json;
    }

    // formatter: off
    private void copyAllUsedFiles(File contents, String location)
      throws IOException
    {
        ObservableList<TemplateDirectoryFile> tdfs = ((CourseSiteManagerData) app
          .getDataComponent()).getCdmData().getTemplateFiles();
        File[] files = contents.listFiles();
        outerLoop:
        for (File file : files) {
            if (file.isDirectory()) {
                if (!new File(
                  location + File.separator + file.getName()).exists())
                {
                    new File(
                      location + File.separator + file.getName()).mkdir();
                }
                copyAllUsedFiles(file,
                  location + File.separator + file.getName());
            } else {
                for (TemplateDirectoryFile f : tdfs) {
                    if (f.getName().equalsIgnoreCase(file.getName()) &&
                        f.isUsed())
                    {
                        copyFile(file, new File(
                          location + File.separator + file.getName()));
                        continue outerLoop;
                    } else if (f.getName().equalsIgnoreCase(file.getName()) &&
                               !f.isUsed())
                    {
                        continue outerLoop;
                    }
                }
                copyFile(file, new File(
                  location + File.separator + file.getName()));
            }
        }
    }
    //formatter: on
}
