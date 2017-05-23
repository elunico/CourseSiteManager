package csm;

import properties_manager.PropertiesManager;

import java.util.ArrayList;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */

// @formatter:off
public class CourseSiteManagerProp {
    public static final double DEFAULT_GAP = 10;
    public static final double DEFAULT_PADDING = 5;
    public static final double DEFAULT_SPACING = 10;

    private static PropertiesManager pm = PropertiesManager.getPropertiesManager();
public static final String EXPORT_COMPLETE_MSG=pm.getProperty("EXPORT_COMPLETE_MSG");
    // CONSTANTS FOR TAB 1, CDM FIRST SECTION
    public static final String EXPORT_COMPLETE_TITLE = pm.getProperty("EXPORT_COMPLETE_TITLE");
    public static final String CDM_COURSE_INFO_LABEL = pm.getProperty("CDM_COURSE_INFO_LABEL");
    public static final String CDM_SUBJECT_LABEL = pm.getProperty("CDM_SUBJECT_LABEL");
    public static final String CDM_NUMBER_LABEL = pm.getProperty("CDM_NUMBER_LABEL");
    public static final String CDM_SEMESTER_LABEL = pm.getProperty("CDM_SEMESTER_LABEL");
    public static final String CDM_YEAR_LABEL = pm.getProperty("CDM_YEAR_LABEL");
    public static final String CDM_TITLE_LABEL = pm.getProperty("CDM_TITLE_LABEL");
    public static final String CDM_INSTRUCTOR_NAME_LABEL = pm.getProperty("CDM_INSTRUCTOR_NAME_LABEL");
    public static final String CDM_INSTRUCTOR_HOME_LABEL = pm.getProperty("CDM_INSTRUCTOR_HOME_LABEL");
    public static final String CDM_EXPORT_DIR_LABEL = pm.getProperty("CDM_EXPORT_DIR_LABEL");

    public static final String CDM_EXPORT_PLACE_DEFAULT_LABEL = pm.getProperty("CDM_EXPORT_PLACE_DEFAULT_LABEL");
    public static final String CDM_CHANGE_BUTTON = pm.getProperty("CDM_CHANGE_BUTTON");

    // CONSTANTS FOR TAB1 CDM Second Section
    public static final String CDM_SECOND_TITLE_LABEL = pm.getProperty("CDM_SECOND_TITLE_LABEL");
    public static final String CDM_INFORMATION_LABEL = pm.getProperty("CDM_INFORMATION_LABEL");
    public static final String CDM_SITE_TEMPLATE_LABEL = pm.getProperty("CDM_SITE_TEMPLATE_LABEL");
    public static final String CDM_SELECT_SITE_TEMPLATE_BUTTON = pm.getProperty("CDM_SELECT_SITE_TEMPLATE_BUTTON");
    public static final String CDM_SITE_PAGES_TITLE_LABEL = pm.getProperty("CDM_SITE_PAGES_TITLE_LABEL");

    // CONSTANTS FOR TAB1 CDM, Third Section
    public static final String CDM_THIRD_TITLE_LABEL = pm.getProperty("CDM_THIRD_TITLE_LABEL");
    public static final String CDM_SCHOOL_IMAGE_LABEL = pm.getProperty("CDM_SCHOOL_IMAGE_LABEL");
    public static final String CDM_LEFT_FOOTER_IMAGE_LABEL = pm.getProperty("CDM_LEFT_FOOTER_IMAGE_LABEL");
    public static final String CDM_RIGHT_FOOTER_IMAGE_LABEL = pm.getProperty("CDM_RIGHT_FOOTER_IMAGE_LABEL");
    public static final String CDM_STYLE_SHEET_LABEL = pm.getProperty("CDM_STYLE_SHEET_LABEL");
    public static final String CDM_NOTICE_LABEL = pm.getProperty("CDM_NOTICE_LABEL");

    public static final String CDM_TITLE_COLUMN = pm.getProperty("CDM_TITLE_COLUMN");
    public static final String CDM_USE_COLUMN = pm.getProperty("CDM_USE_COLUMN");
    public static final String CDM_NAME_COLUMN = pm.getProperty("CDM_NAME_COLUMN");
    public static final String CDM_SCRIPT_COLUMN = pm.getProperty("CDM_SCRIPT_COLUMN");

    // CONSTANTS FOR TAB3 RDE ALL SECTIONS
    public static final String RDE_TITLE_LABEL = pm.getProperty("RDE_TITLE_LABEL");
    public static final String RDE_SECTION_LABEL = pm.getProperty("RDE_SECTION_LABEL");
    public static final String RDE_INSTRUCTOR_LABEL = pm.getProperty("RDE_INSTRUCTOR_LABEL");
    public static final String RDE_DAY_TIME_LABEL = pm.getProperty("RDE_DAY_TIME_LABEL");
    public static final String RDE_LOCATION_LABEL = pm.getProperty("RDE_LOCATION_LABEL");
    public static final String RDE_SUPERVISING_TA_LABEL = pm.getProperty("RDE_SUPERVISING_TA_LABEL");
    public static final String RDE_ADD_UPDATE_BUTTON = pm.getProperty("RDE_ADD_UPDATE_BUTTON");
    public static final String RDE_CLEAR_BUTTON = pm.getProperty("RDE_CLEAR_BUTTON");
    public static final String RDE_ADD_EDIT_TITLE = pm.getProperty("RDE_ADD_EDIT_TITLE");

    public static final String RDE_SECTION_HEADER = pm.getProperty("RDE_SECTION_HEADER");
    public static final String RDE_INSTRUCTOR_HEADER = pm.getProperty("RDE_INSTRUCTOR_HEADER");
    public static final String RDE_DAY_TIME_HEADER = pm.getProperty("RDE_DAY_TIME_HEADER");
    public static final String RDE_LOCATION_HEADER = pm.getProperty("RDE_LOCATION_HEADER");
    public static final String RDE_TA_HEADER = pm.getProperty("RDE_TA_HEADER");

    // CONSTANTS FOR TAB 4 SDE FIRST SECTION
    public static final String SDE_FIRST_TITLE = pm.getProperty("SDE_FIRST_TITLE");
    public static final String SDE_STARTING_MONDAY_LABEL = pm.getProperty("SDE_STARTING_MONDAY_LABEL");
    public static final String SDE_ENDING_FRIDAY_LABEL = pm.getProperty("SDE_ENDING_FRIDAY_LABEL");

    // CONSTANTS FOR TAB 4 SDE SECOND SECTION
    public static final String SDE_SECOND_TITLE = pm.getProperty("SDE_SECOND_TITLE");
    public static final String SDE_TYPE_LABEL = pm.getProperty("SDE_TYPE_LABEL");
    public static final String SDE_DATE_LABEL = pm.getProperty("SDE_DATE_LABEL");
    public static final String SDE_TIME_LABEL = pm.getProperty("SDE_TIME_LABEL");
    public static final String SDE_TITLE_LABEL = pm.getProperty("SDE_TITLE_LABEL");
    public static final String SDE_TOPIC_LABEL = pm.getProperty("SDE_TOPIC_LABEL");
    public static final String SDE_LINK_LABEL = pm.getProperty("SDE_LINK_LABEL");
    public static final String SDE_CRITERIA_LABEL = pm.getProperty("SDE_CRITERIA_LABEL");
    public static final String SDE_ADD_EDIT_BUTTON = pm.getProperty("SDE_ADD_EDIT_BUTTON");
    public static final String SDE_CLEAR_BUTTON = pm.getProperty("SDE_CLEAR_BUTTON");

    public static final String SDE_TYPE_HEADER = pm.getProperty("SDE_TYPE_HEADER");
    public static final String SDE_DATE_HEADER = pm.getProperty("SDE_DATE_HEADER");
    public static final String SDE_TITLE_HEADER = pm.getProperty("SDE_TITLE_HEADER");
    public static final String SDE_TOPIC_HEADER = pm.getProperty("SDE_TOPIC_HEADER");

    // CONSTANTS FOR TAB 5 PDE ALL SECTIONS
    public static final String PDE_TITLE_LABEL = pm.getProperty("PDE_TITLE_LABEL");
    public static final String PDE_ADD_EDIT_BUTTON = pm.getProperty("PDE_ADD_EDIT_BUTTON");
    public static final String PDE_CLEAR_BUTTON = pm.getProperty("PDE_CLEAR_BUTTON");
    public static final String PDE_NAME_LABEL = pm.getProperty("PDE_NAME_LABEL");
    public static final String PDE_COLOR_LABEL = pm.getProperty("PDE_COLOR_LABEL");
    public static final String PDE_TEXT_COLOR_LABEL = pm.getProperty("PDE_TEXT_COLOR_LABEL");
    public static final String PDE_LINK_LABEL = pm.getProperty("PDE_LINK_LABEL");
    public static final String PDE_ADD_EDIT_TITLE = pm.getProperty("PDE_ADD_EDIT_TITLE");

    public static final String PDE_SECOND_TITLE = pm.getProperty("PDE_SECOND_TITLE");
    public static final String PDE_FIRST_NAME_LABEL = pm.getProperty("PDE_FIRST_NAME_LABEL");
    public static final String PDE_LAST_NAME_LABEL = pm.getProperty("PDE_LAST_NAME_LABEL");
    public static final String PDE_TEAM_LABEL = pm.getProperty("PDE_TEAM_LABEL");
    public static final String PDE_ROLE_LABEL = pm.getProperty("PDE_ROLE_LABEL");

    public static final String PDE_NAME_HEADER = pm.getProperty("PDE_NAME_HEADER");
public static final String PDE_COLOR_HEADER = pm.getProperty("PDE_COLOR_HEADER");
public static final String PDE_TEXT_COLOR_HEADER = pm.getProperty("PDE_TEXT_COLOR_HEADER");
public static final String PDE_LINK_HEADER = pm.getProperty("PDE_LINK_HEADER");

public static final String PDE_FIRST_NAME_HEADER = pm.getProperty("PDE_FIRST_NAME_HEADER");
public static final String PDE_LAST_NAME_HEADER = pm.getProperty("PDE_LAST_NAME_HEADER");
public static final String PDE_TEAM_HEADER = pm.getProperty("PDE_TEAM_HEADER");
public static final String PDE_ROLE_HEADER = pm.getProperty("PDE_ROLE_HEADER");


    public static final String TAS_HEADER_TEXT = pm.getProperty("TAS_HEADER_TEXT");
    public static final String NAME_COLUMN_TEXT = pm.getProperty("NAME_COLUMN_TEXT");
    public static final String EMAIL_COLUMN_TEXT = pm.getProperty("EMAIL_COLUMN_TEXT");
    public static final String NAME_PROMPT_TEXT = pm.getProperty("NAME_PROMPT_TEXT");
    public static final String EMAIL_PROMPT_TEXT = pm.getProperty("EMAIL_PROMPT_TEXT");
    public static final String ADD_BUTTON_TEXT = pm.getProperty("ADD_BUTTON_TEXT");
    public static final String OFFICE_HOURS_SUBHEADER = pm.getProperty("OFFICE_HOURS_SUBHEADER");
    public static final ArrayList<String> OFFICE_HOURS_TABLE_HEADERS = pm.getPropertyOptionsList("OFFICE_HOURS_TABLE_HEADERS");
    public static final ArrayList<String> DAYS_OF_WEEK = pm.getPropertyOptionsList("DAYS_OF_WEEK");
    public static final String START_LABEL_TEXT = pm.getProperty("START_LABEL_TEXT");
    public static final String END_LABEL_TEXT = pm.getProperty("END_LABEL_TEXT");

    // TAB NAMES
    public static final String TAB_NAME_CDM = pm.getProperty("TAB_NAME_CDM");
    public static final String TAB_NAME_TAM = pm.getProperty("TAB_NAME_TAM");
    public static final String TAB_NAME_RDE = pm.getProperty("TAB_NAME_RDE");
    public static final String TAB_NAME_SDE = pm.getProperty("TAB_NAME_SDE");
    public static final String TAB_NAME_PDE = pm.getProperty("TAB_NAME_PDE");

    public static final String MISSING_TA_NAME_TITLE = pm.getProperty("MISSING_TA_NAME_TITLE");
    public static final String MISSING_TA_NAME_MESSAGE = pm.getProperty("MISSING_TA_NAME_MESSAGE");
    public static final String MISSING_TA_EMAIL_TITLE = pm.getProperty("MISSING_TA_EMAIL_TITLE");
    public static final String MISSING_TA_EMAIL_MESSAGE = pm.getProperty("MISSING_TA_EMAIL_MESSAGE");
    public static final String TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE = pm.getProperty("TA_NAME_AND_EMAIL_NOT_UNIQUE_TITLE");
    public static final String TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE = pm.getProperty("TA_NAME_AND_EMAIL_NOT_UNIQUE_MESSAGE");
    public static final String TA_GRID_INVALID_START_END_TIME_TITLE = pm.getProperty("TA_GRID_INVALID_START_END_TIME_TITLE");
    public static final String TA_GRID_INVALID_START_END_TIME_MESSAGE = pm.getProperty("TA_GRID_INVALID_START_END_TIME_MESSAGE");

    public static final String UNDERGRAD_COLUMN_TEXT = pm.getProperty("UNDERGRAD_COLUMN_TEXT");

    public static final String SEMESTER_FALL = pm.getProperty("SEMESTER_FALL");
    public static final String SEMESTER_WINTER = pm.getProperty("SEMESTER_WINTER");
    public static final String SEMESTER_SUMMER = pm.getProperty("SEMESTER_SUMMER");
    public static final String SEMESTER_SPRING = pm.getProperty("SEMESTER_SPRING");

    public static final String PDE_MAIN_TITLE = pm.getProperty("PDE_MAIN_TITLE");
    public static final String SDE_MAIN_TITLE = pm.getProperty("SDE_MAIN_TITLE");

    public static final String PDE_EMPTY_TEAM_DATA_TITLE = pm.getProperty("PDE_EMPTY_TEAM_DATA_TITLE");
public static final String PDE_EMPTY_TEAM_DATA_MSG = pm.getProperty("PDE_EMPTY_TEAM_DATA_MSG");

    public static final String TAM_CHANGE_BUTTON = pm.getProperty("TAM_CHANGE_BUTTON");

    public static final String SITE_TEMPLATE_MISSING_ALERT = pm.getProperty("SITE_TEMPLATE_MISSING_ALERT");
public static final String SITE_TEMPLATE_MISSING_MSG = pm.getProperty("SITE_TEMPLATE_MISSING_MSG");


public static final String ABOUT_TITLE = pm.getProperty("ABOUT_TITLE");
public static final String ABOUT_MESSAGE = pm.getProperty("ABOUT_MESSAGE");

public static final String SDE_INVALID_DATE_TITLE = pm.getProperty("SDE_INVALID_DATE_TITLE");
public static final String SDE_INVALID_DATE_MSG = pm.getProperty("SDE_INVALID_DATE_MSG");
public static final String SDE_INVALID_TIME_TITLE = pm.getProperty("SDE_INVALID_TIME_TITLE");
public static final String SDE_INVALID_TIME_MSG = pm.getProperty("SDE_INVALID_TIME_MSG");
public static final String SDE_INVALID_LINK_TITLE = pm.getProperty("SDE_INVALID_LINK_TITLE");
public static final String SDE_INVALID_LINK_MSG = pm.getProperty("SDE_INVALID_LINK_MSG");

public static final String SDE_NOT_FRI_TITLE = pm.getProperty("SDE_NOT_FRI_TITLE");
public static final String SDE_NOT_FRI_MSG = pm.getProperty("SDE_NOT_FRI_MSG");
public static final String SDE_NOT_MON_TITLE = pm.getProperty("SDE_NOT_MON_TITLE");
public static final String SDE_NOT_MON_MSG = pm.getProperty("SDE_NOT_MON_MSG");

public static final String TA_CUT_TA = pm.getProperty("TA_CUT_TA");
public static final String TA_CUT_TA_MSG = pm.getProperty("TA_CUT_TA_MSG");

public static final String SDE_MONDAY_AFTER_FRIDAY_TITLE = pm.getProperty("SDE_MONDAY_AFTER_FRIDAY_TITLE");
public static final String SDE_MONDAY_AFTER_FRIDAY_MSG = pm.getProperty("SDE_MONDAY_AFTER_FRIDAY_MSG");


public static final String APP_DELETE_PROMPT_TITLE = pm.getProperty("APP_DELETE_PROMPT_TITLE");
public static final String APP_DELETE_PROMPT_MSG = pm.getProperty("APP_DELETE_PROMPT_MSG");
}
// @formatter:on
