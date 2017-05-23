package csm.cdm.data;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.ui.CourseSiteManagerWorkspace;
import djf.components.AppDataComponent;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.io.File;

/**
 * @author Thomas Povinelli
 *         Created 4/19/17
 *         In Homework5
 */
public class CDMData implements AppDataComponent {
    private CourseSiteManagerApp app;

    private SimpleStringProperty subject, semester;
    private SimpleIntegerProperty number;
    private SimpleStringProperty year;
    private SimpleStringProperty title, instructorName, instructorHome;
    private SimpleStringProperty exportDir;
    private SimpleObjectProperty<String> templateDir;
    private ObservableList<TemplateDirectoryFile> templateFiles = FXCollections.observableArrayList();
    private SimpleObjectProperty<Image> bannerImage, leftImage, rightImage;
    private SimpleObjectProperty<String> stylesheet;
    private SimpleStringProperty bannerImagePath, leftImagePath, rightImagePath;

    public CDMData(CourseSiteManagerApp courseSiteManagerApp)
    {
        subject = new SimpleStringProperty("");
        semester = new SimpleStringProperty("");
        number = new SimpleIntegerProperty();
        year = new SimpleStringProperty("");
        exportDir = new SimpleStringProperty();
        templateDir = new SimpleObjectProperty<>();
        bannerImage = new SimpleObjectProperty<>();
        leftImage = new SimpleObjectProperty<>();
        rightImage = new SimpleObjectProperty<>();
        stylesheet = new SimpleObjectProperty<>();
        title = new SimpleStringProperty("");
        instructorHome = new SimpleStringProperty("");
        instructorName = new SimpleStringProperty("");
        bannerImagePath = new SimpleStringProperty("");
        leftImagePath = new SimpleStringProperty("");
        rightImagePath = new SimpleStringProperty("");

        initListeners();


        this.app = courseSiteManagerApp;
    }

    private void initListeners() {
        semester.addListener(
          (observable, oldValue, newValue) -> Platform.runLater(() -> {
              ((CourseSiteManagerWorkspace) app.getWorkspaceComponent()).getCourseDataTab()
                                                                        .getWorkspace()
                                                                        .getSemesterBox()
                                                                        .getSelectionModel()
                                                                        .select(
                                                                          newValue);
              return;
          }));
        year.addListener(
          (observable, oldValue, newValue) -> Platform.runLater(() -> {
              ((CourseSiteManagerWorkspace) app.getWorkspaceComponent()).getCourseDataTab()
                                                                        .getWorkspace()
                                                                        .getYearBox()
                                                                        .getSelectionModel()
                                                                        .select(
                                                                          String
                                                                            .valueOf(
                                                                              newValue));
              return;
          }));
        number.addListener(
          (observable, oldValue, newValue) -> Platform.runLater(() -> {
              ((CourseSiteManagerWorkspace) app.getWorkspaceComponent()).getCourseDataTab()
                                                                        .getWorkspace()
                                                                        .getNumberBox()
                                                                        .getSelectionModel()
                                                                        .select(
                                                                          (Integer) newValue
                                                                            .intValue());
              return;
          }));
        subject.addListener(
          (observable, oldValue, newValue) -> Platform.runLater(() -> {
              ((CourseSiteManagerWorkspace) app.getWorkspaceComponent()).getCourseDataTab()
                                                                        .getWorkspace()
                                                                        .getSubjectBox()
                                                                        .getSelectionModel()
                                                                        .select(
                                                                          newValue);
              return;
          }));


    }

    public String getBannerImagePath() {
        return bannerImagePath.get();
    }

    /**
     * Don't use this setter it is just to keep the path of the image
     * it WILL NOT change the image use {@link #setBannerImage(String)}  instead
     *
     * @param dontusethis
     */
    private void setBannerImagePath(String dontusethis) {
        this.bannerImagePath.set(dontusethis);
    }

    public SimpleStringProperty bannerImagePathProperty() {
        return bannerImagePath;
    }

    public String getLeftImagePath() {
        return leftImagePath.get();
    }

    /**
     * Don't use this setter it is just to keep the path of the image
     * it WILL NOT change the image use {@link #setLeftImage(String)} instead
     *
     * @param dontusethis
     */
    private void setLeftImagePath(String dontusethis) {
        this.leftImagePath.set(dontusethis);
    }

    public SimpleStringProperty leftImagePathProperty() {
        return leftImagePath;
    }

    public String getRightImagePath() {
        return rightImagePath.get();
    }

    /**
     * Don't use this setter it is just to keep the path of the image
     * it WILL NOT change the image use {@link #setRightImage(String)} instead
     *
     * @param dontusethis
     */
    private void setRightImagePath(String dontusethis) {
        this.rightImagePath.set(dontusethis);
    }

    public SimpleStringProperty rightImagePathProperty() {
        return rightImagePath;
    }

    public ObservableList<TemplateDirectoryFile> getTemplateFiles() {
        return templateFiles;
    }

    public void setTemplateFiles(
      ObservableList<TemplateDirectoryFile> templateFiles)
    {
        this.templateFiles = templateFiles;
    }

    public CourseSiteManagerApp getApp() {
        return app;
    }

    public void setApp(CourseSiteManagerApp app) {
        this.app = app;
    }

    public String getSubject() {
        return subject.get();
    }

    public void setSubject(String subject) {
        this.subject.set(subject);
    }

    public SimpleStringProperty subjectProperty() {
        return subject;
    }

    public String getSemester() {
        return semester.get();
    }

    public void setSemester(String semester) {
        this.semester.set(semester);
    }

    public SimpleStringProperty semesterProperty() {
        return semester;
    }

    public int getNumber() {
        return number.get();
    }

    public void setNumber(int number) {
        this.number.set(number);
    }

    public SimpleIntegerProperty numberProperty() {
        return number;
    }

    public String getYear() {
        return year.get();
    }

    public void setYear(String year) {
        this.year.set(year);
    }

    public SimpleStringProperty yearProperty() {
        return year;
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public SimpleStringProperty titleProperty() {
        return title;
    }

    public String getInstructorName() {
        return instructorName.get();
    }

    public void setInstructorName(String instructorName) {
        this.instructorName.set(instructorName);
    }

    public SimpleStringProperty instructorNameProperty() {
        return instructorName;
    }

    public String getInstructorHome() {
        return instructorHome.get();
    }

    public void setInstructorHome(String instructorHome) {
        this.instructorHome.set(instructorHome);
    }

    public SimpleStringProperty instructorHomeProperty() {
        return instructorHome;
    }

    public String getExportDir() {
        return exportDir.get();
    }

    public void setExportDir(String exportDir) {
        this.exportDir.set(exportDir);
    }

    public SimpleStringProperty exportDirProperty() {
        return exportDir;
    }

    public void setBannerImage(Image bannerImage) {
        this.bannerImage.set(bannerImage);
    }

    public void setLeftImage(Image leftImage) {
        this.leftImage.set(leftImage);
    }

    public void setRightImage(Image rightImage) {
        this.rightImage.set(rightImage);
    }

    public SimpleObjectProperty<String> templateDirProperty() {
        return templateDir;
    }

    public Image getBannerImage() {
        return bannerImage.get();
    }

    public void setBannerImage(String imagePath) {
        this.bannerImage.set(new Image(imagePath));
        this.bannerImagePath.set(imagePath);
    }

    public SimpleObjectProperty<Image> bannerImageProperty() {
        return bannerImage;
    }

    public Image getLeftImage() {
        return leftImage.get();
    }

    public void setLeftImage(String leftImagePath) {
        this.leftImage.set(new Image(leftImagePath));
        this.leftImagePath.set(leftImagePath);
    }

    public SimpleObjectProperty<Image> leftImageProperty() {
        return leftImage;
    }

    public Image getRightImage() {
        return rightImage.get();
    }

    public void setRightImage(String rightImagePath) {
        this.rightImage.set(new Image(rightImagePath));
        this.rightImagePath.set(rightImagePath);
    }

    public SimpleObjectProperty<Image> rightImageProperty() {
        return rightImage;
    }

    public String getStylesheet() {
        return stylesheet.get();
    }

    public void setStylesheet(String stylesheet) {
        this.stylesheet.set(stylesheet);
    }

    public SimpleObjectProperty<String> stylesheetProperty() {
        return stylesheet;
    }

    @Override
    public void resetData() {
        templateFiles.clear();
        subject.set("");
        semester.set("");
        number.set(0);
        year.set("");
        exportDir.set("");
        templateDir.set(null);
        bannerImage.set(new Image("file:"));
        leftImage.set(new Image("file:"));
        rightImage.set(new Image("file:"));
        stylesheet.set("");
        title.set("");
        instructorHome.set("");
        instructorName.set("");
        bannerImagePath.set("");
        leftImagePath.set("");
        rightImagePath.set("");
    }

    public void loadTemplateDir() {
        ((CourseSiteManagerData) app.getDataComponent()).getCdmData().templateFiles.clear();
        File f = new File(getTemplateDir());
        if (f.exists() && f.isDirectory()) {
            File[] contents = f.listFiles();
            for (File file : contents) {
                if (file.isDirectory() ||
                    file.getName().equalsIgnoreCase(".ds_store"))
                {
                    continue;
                }
                TemplateDirectoryFile fx;
                if (file.getName().equalsIgnoreCase("index.html")) {
                    fx = new TemplateDirectoryFile("index.html",
                      "./js/HomeBuilder.js", "Home", true, file);
                } else if (file.getName().equalsIgnoreCase("syllabus.html")) {
                    fx = new TemplateDirectoryFile("syllabus.html",
                      "./js/SyllabusBuilder.js", "Syllabus", true, file);
                } else if (file.getName().equalsIgnoreCase("schedule.html")) {
                    fx = new TemplateDirectoryFile("schedule.html",
                      "./js/ScheduleBuilder.js", "Schedule", true, file);
                } else if (file.getName().equalsIgnoreCase("projects.html")) {
                    fx = new TemplateDirectoryFile("projects.html",
                      "./js/ProjectBuilder.js", "Projects", true, file);
                } else if (file.getName().equalsIgnoreCase("hws.html")) {
                    fx = new TemplateDirectoryFile("hws.html", "./js/HWBuilder.js",
                      "HW", true, file);
                } else {
                    fx = new TemplateDirectoryFile(file.getName(), "", file.getName().replaceAll("\\.html", ""),
                      false, file);
                }
                ((CourseSiteManagerData) app.getDataComponent()).getCdmData().templateFiles.add(fx);
            }
        }
    }

    public String getTemplateDir() {
        return templateDir.get();
    }

    public void setTemplateDir(String templateDir) {
        this.templateDir.set(templateDir);
    }

    public String toString() {
        return "CDMData{" +
               ", subject=" + (subject != null ? subject.get() : "") +
               ", semester=" + (semester != null ? semester.get() : "") +
               ", number=" + (number != null ? number.get() : "") +
               ", year=" + (year != null ? year.get() : "") +
               ", title=" + (title != null ? title.get() : "") +
               ", instructorName=" +
               (instructorName != null ? instructorName.get() : "") +
               ", instructorHome=" +
               (instructorHome != null ? instructorHome.get() : "") +
               ", \nexportDir=" + (exportDir != null ? exportDir.get() : "") +
               ", templateDir=" +
               (templateDir != null ? templateDir.get() : "") +
               ", templateFiles=" + templateFiles +
//               ", bannerImage=" + (bannerImage!=null ? bannerImage.get().toString() : "") +
//               ", leftImage=" + (leftImage!=null ? leftImage.get().toString() : "") +
//               ", rightImage=" + (rightImage!=null ? rightImage.get().toString(): "") +
               ", stylesheet=" + (stylesheet != null ? stylesheet.get() : "") +
               ", bannerImagePath=" +
               (bannerImagePath != null ? bannerImagePath.get() : "") +
               ", leftImagePath=" +
               (leftImagePath != null ? leftImagePath.get() : "") +
               ", rightImagePath=" +
               (rightImagePath != null ? rightImagePath.get() : "") +
               "}\n\n";

    }
}
