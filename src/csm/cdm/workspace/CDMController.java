package csm.cdm.workspace;

import csm.CourseSiteManagerData;
import csm.cdm.data.CDMData;
import djf.AppTemplate;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CDMController {
    AppTemplate app;

    public CDMController(AppTemplate app) {
        this.app = app;
    }

    public void changeExportDirAction(ActionEvent event) {
        String locationName = getDirectoryAsString();
        if (locationName == null) {
            return;
        }

        ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                        .setExportDir(locationName);

        app.getGUI().getFileController().markAsEdited(app.getGUI());

    }

    private String getDirectoryAsString() {
        DirectoryChooser dc = new DirectoryChooser();
        File location = dc.showDialog(app.getGUI().getWindow());
        if (location == null) {
            return null;
        }
        String locationName = location.toString();
        return locationName;
    }

    public void changeSiteTemplateDirectory(ActionEvent event) {
        String locationName = getDirectoryAsString();
        if (locationName == null) {
            return;
        }

        ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                        .setTemplateDir(locationName);
        ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                        .loadTemplateDir();

        app.getGUI().getFileController().markAsEdited(app.getGUI());

    }

    public void changeRightFooterImageAction() {
        CDMData data = ((CourseSiteManagerData) app.getDataComponent()).getCdmData();

        data.setRightImage(getPathForImage());

        app.getGUI().getFileController().markAsEdited(app.getGUI());
    }

    public String getPathForImage() {
        FileChooser chooser = new FileChooser();
        File f = chooser.showOpenDialog(app.getGUI().getWindow());

        if (f == null) {
            return "file:";
        } else {
            return "file:" + f.getAbsolutePath();
        }

    }

    public void changeLeftFooterImageAction() {
        CDMData data = ((CourseSiteManagerData) app.getDataComponent()).getCdmData();

        data.setLeftImage(getPathForImage());
        app.getGUI().getFileController().markAsEdited(app.getGUI());
    }

    public void changeBannerSchoolImageAction() {
        CDMData data = ((CourseSiteManagerData) app.getDataComponent()).getCdmData();

        data.setBannerImage(getPathForImage());
        app.getGUI().getFileController().markAsEdited(app.getGUI());
    }
}
