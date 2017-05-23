package csm.cdm.workspace;

import csm.CourseSiteManagerData;
import csm.CourseSiteManagerProp;
import csm.cdm.data.TemplateDirectoryFile;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import properties_manager.PropertiesManager;

import java.io.File;
import java.util.ArrayList;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class CDMWorkspace extends AppWorkspaceComponent {
    public final CDMController controller;
    public final ChoiceBox<String> subjectBox;
    public final ChoiceBox<Integer> numberBox;
    public final ChoiceBox<String> semesterBox;
    public final ChoiceBox<String> yearBox;
    public final Label exportDirLabel;
    public final VBox contentPane;
    public final TableColumn<TemplateDirectoryFile, String> navbarTitleColumn;
    public final TableColumn<TemplateDirectoryFile, String> filenameColumn;
    public final TableColumn<TemplateDirectoryFile, String> scriptColumn;
    public final TableColumn<TemplateDirectoryFile, Boolean> useColumn;
    public final TableView<TemplateDirectoryFile> sitePagesTable;
    public final Label sitePagesTitleLabel;
    public final Label siteTemplateLabel;
    public final ChoiceBox<String> styleSheetChoiceBox;
    public TextField titleField;
    public TextField instructorNameField;
    public TextField instructorHomeField;
    public Label exportDirPlaceLabel = new Label("");
    public ImageView bannerSchoolImage;
    public ImageView leftFooterImage;
    public ImageView rightFooterImage;

    AppTemplate app;


    public CDMWorkspace(AppTemplate app) {
        this.app = app;

        controller = new CDMController(app);
        contentPane = new VBox();
        contentPane.setSpacing(DEFAULT_SPACING);
        contentPane.setPadding(new Insets(DEFAULT_PADDING));

        GridPane firstPane = new GridPane();
        firstPane.setBackground(new Background(new BackgroundFill(
          Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        firstPane.setVgap(CourseSiteManagerProp.DEFAULT_GAP);
        firstPane.setHgap(CourseSiteManagerProp.DEFAULT_GAP);
        firstPane.setPadding(new Insets(CourseSiteManagerProp.DEFAULT_PADDING));
        Label title = new Label(CDM_COURSE_INFO_LABEL);
        title.setFont(Font.font(18));
        GridPane.setColumnSpan(title, 5);

        PropertiesManager pm = PropertiesManager.getPropertiesManager();

        Label subjectLabel = new Label(CDM_SUBJECT_LABEL);
        Label numberLabel = new Label(CDM_NUMBER_LABEL);
        Label semesterLabel = new Label(CDM_SEMESTER_LABEL);
        Label yearLabel = new Label(CDM_YEAR_LABEL);
        Label titleLabel = new Label(CDM_TITLE_LABEL);
        Label instructorNameLabel = new Label(CDM_INSTRUCTOR_NAME_LABEL);
        Label instructorHomeLabel = new Label(CDM_INSTRUCTOR_HOME_LABEL);
        exportDirLabel = new Label(CDM_EXPORT_DIR_LABEL);

        subjectBox = new ChoiceBox<>();
        numberBox = new ChoiceBox<>();
        semesterBox = new ChoiceBox<>();
        yearBox = new ChoiceBox<>();

        initSubjectBox(subjectBox);
        initNumberBox(numberBox);
        initSemesterBox(semesterBox);
        initYearBox(yearBox);

        titleField = new TextField();
        instructorNameField = new TextField();
        instructorHomeField = new TextField();

        initPropertyBindings(app);


        GridPane.setColumnSpan(titleField, 2);
        GridPane.setColumnSpan(instructorNameField, 2);
        GridPane.setColumnSpan(instructorHomeField, 2);

        exportDirPlaceLabel = new Label(CDM_EXPORT_PLACE_DEFAULT_LABEL);
        exportDirPlaceLabel.textProperty()
                           .bindBidirectional(((CourseSiteManagerData) app.getDataComponent())
                             .getCdmData()
                             .exportDirProperty());

        Button changeExportDirButton = new Button(CDM_CHANGE_BUTTON);


        firstPane.addColumn(0, title, subjectLabel, semesterLabel, titleLabel,
          instructorNameLabel, instructorHomeLabel,
          exportDirLabel);
        firstPane.addColumn(1, subjectBox, semesterBox, titleField,
          instructorNameField, instructorHomeField, exportDirPlaceLabel);
        firstPane.addColumn(3, numberLabel, yearLabel);
        firstPane.addColumn(4, numberBox, yearBox);

        firstPane.add(changeExportDirButton, 3, 6);

        changeExportDirButton.setOnAction(controller::changeExportDirAction);


        VBox secondPane = new VBox(DEFAULT_SPACING);

        Label secondTitleLabel = new Label(CDM_SECOND_TITLE_LABEL);
        secondTitleLabel.setFont(Font.font(18));
        Label informationLabel = new Label(
          CDM_INFORMATION_LABEL);
        siteTemplateLabel = new Label(CDM_SITE_TEMPLATE_LABEL);
        Button selectSiteTemplateButton = new Button(
          CDM_SELECT_SITE_TEMPLATE_BUTTON);
        selectSiteTemplateButton.setOnAction(controller::changeSiteTemplateDirectory);
        sitePagesTitleLabel = new Label(CDM_SITE_PAGES_TITLE_LABEL);
        sitePagesTable = new TableView<>();
        sitePagesTable.setEditable(true);
        sitePagesTable.setItems(((CourseSiteManagerData) app.getDataComponent())
          .getCdmData()
          .getTemplateFiles());

        siteTemplateLabel.textProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .templateDirProperty());

        navbarTitleColumn = new TableColumn<>(CDM_TITLE_COLUMN);
        filenameColumn = new TableColumn<>(CDM_NAME_COLUMN);
        scriptColumn = new TableColumn<>(CDM_SCRIPT_COLUMN);
        useColumn = new TableColumn<>(CDM_USE_COLUMN);

        navbarTitleColumn.prefWidthProperty()
                         .bind(sitePagesTable.widthProperty().multiply(0.2));
        filenameColumn.prefWidthProperty()
                      .bind(sitePagesTable.widthProperty().multiply(0.2));
        scriptColumn.prefWidthProperty()
                    .bind(sitePagesTable.widthProperty().multiply(0.2));

        useColumn.setEditable(true);
        useColumn.setCellFactory(tc -> new CheckBoxTableCell<>());

        useColumn.setCellValueFactory(new PropertyValueFactory<>("used"));
        navbarTitleColumn.setCellValueFactory(
          new PropertyValueFactory<>("title"));
        filenameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        scriptColumn.setCellValueFactory(new PropertyValueFactory<>("script"));

        sitePagesTable.getColumns()
                      .addAll(useColumn, navbarTitleColumn, filenameColumn,
                        scriptColumn);


        secondPane.setPadding(new Insets(DEFAULT_PADDING));
        secondPane.getChildren()
                  .addAll(secondTitleLabel, informationLabel, siteTemplateLabel,
                    selectSiteTemplateButton, sitePagesTitleLabel,
                    sitePagesTable);

        secondPane.setBackground(new Background(
          new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY,
            Insets.EMPTY)));

        VBox thirdPane = new VBox(DEFAULT_SPACING);

        thirdPane.setBackground(new Background(
          new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY,
            Insets.EMPTY)));

        Label thirdTitleLabel = new Label(CDM_THIRD_TITLE_LABEL);
        thirdTitleLabel.setFont(Font.font(18));
        Label bannerSchoolImageLabel = new Label(CDM_SCHOOL_IMAGE_LABEL);
        Label leftFooterImageLabel = new Label(CDM_LEFT_FOOTER_IMAGE_LABEL);
        Label rightFooterImageLabel = new Label(CDM_RIGHT_FOOTER_IMAGE_LABEL);
        Label stylesheetsLabel = new Label(CDM_STYLE_SHEET_LABEL);
        Label noticeLabel = new Label(CDM_NOTICE_LABEL);

        bannerSchoolImage = new ImageView();
        leftFooterImage = new ImageView();
        rightFooterImage = new ImageView();

        bannerSchoolImage.imageProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .bannerImageProperty());

        leftFooterImage.imageProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .leftImageProperty());

        rightFooterImage.imageProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .rightImageProperty());

        bannerSchoolImageLabel.setPrefWidth(200);
        leftFooterImageLabel.setPrefWidth(200);
        rightFooterImageLabel.setPrefWidth(200);
        stylesheetsLabel.setPrefWidth(200);


        Button changeBannerSchoolImageButton = new Button(CDM_CHANGE_BUTTON);
        Button changeLeftFooterImageButton = new Button(CDM_CHANGE_BUTTON);
        Button changeRightFooterImageButton = new Button(CDM_CHANGE_BUTTON);

        changeBannerSchoolImageButton.setOnAction(e -> controller.changeBannerSchoolImageAction());
        changeLeftFooterImageButton.setOnAction(e -> controller.changeLeftFooterImageAction());
        changeRightFooterImageButton.setOnAction(e -> controller.changeRightFooterImageAction());

        styleSheetChoiceBox = new ChoiceBox<>();
        styleSheetChoiceBox.setItems(FXCollections.observableArrayList(new File("work/css")
          .list()));
        styleSheetChoiceBox.valueProperty()
                           .bindBidirectional(((CourseSiteManagerData) app.getDataComponent())
                             .getCdmData()
                             .stylesheetProperty());

        HBox bannerBox = new HBox(DEFAULT_SPACING, bannerSchoolImageLabel,
          bannerSchoolImage,
          changeBannerSchoolImageButton);
        HBox leftFooterBox = new HBox(DEFAULT_SPACING, leftFooterImageLabel,
          leftFooterImage,
          changeLeftFooterImageButton);
        HBox rightFooterBox = new HBox(DEFAULT_SPACING, rightFooterImageLabel,
          rightFooterImage,
          changeRightFooterImageButton);
        HBox stylesheetsBox = new HBox(DEFAULT_SPACING, stylesheetsLabel,
          styleSheetChoiceBox);

        thirdPane.getChildren()
                 .addAll(thirdTitleLabel, bannerBox, leftFooterBox,
                   rightFooterBox, stylesheetsBox, noticeLabel);

        contentPane.getChildren().addAll(firstPane, secondPane, thirdPane);

        initListeners();

    }

    void initPropertyBindings(AppTemplate app) {
        subjectBox.valueProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .subjectProperty());
        numberBox.valueProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .numberProperty()
                                                          .asObject());
        semesterBox.valueProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .semesterProperty());
        yearBox.valueProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .yearProperty());


        initActionHandlers(app);

        titleField.textProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .titleProperty());

        instructorHomeField.textProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .instructorHomeProperty());

        instructorNameField.textProperty().bindBidirectional(
          ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                          .instructorNameProperty());

        exportDirPlaceLabel.textProperty()
                           .bindBidirectional(
                             ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                                             .exportDirProperty());

    }

    private void initActionHandlers(AppTemplate app) {
        subjectBox.setOnAction(e -> {
            ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                            .setSubject(subjectBox
                                                              .getValue());

            app.getGUI().getFileController().markAsEdited(app.getGUI());

        });

        numberBox.setOnAction(e -> {

            ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                            .setNumber(numberBox
                                                              .getValue());
            app.getGUI().getFileController().markAsEdited(app.getGUI());

        });
        semesterBox.setOnAction(e -> {

            ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                            .setSemester(semesterBox
                                                              .getValue());
            app.getGUI().getFileController().markAsEdited(app.getGUI());

        });

        yearBox.setOnAction(e -> {

            ((CourseSiteManagerData) app.getDataComponent()).getCdmData()
                                                            .setYear(yearBox
                                                              .getValue());
            app.getGUI().getFileController().markAsEdited(app.getGUI());

        });


    }

    public void initSubjectBox(ChoiceBox<String> subjectBox) {
        ObservableList<String> boxItems = FXCollections.observableArrayList(
          "CSE", "ISE", "ESE");
        subjectBox.setItems(boxItems);
//        subjectBox.getSelectionModel().select(0);
    }

    public void initNumberBox(ChoiceBox<Integer> numberBox) {
        ObservableList<Integer> boxItems = FXCollections.observableArrayList(
          114, 214, 219, 220, 320, 308, 373);
        numberBox.setItems(boxItems);
//        numberBox.getSelectionModel().select(2);
    }

    public void initYearBox(ChoiceBox<String> yearBox) {
        ObservableList<String> boxItems = FXCollections.observableArrayList(
          new ArrayList<String>());
        for (int i = 5; i < 15; i++) {
            boxItems.add(String.format("20%d", i + 10));
        }
        yearBox.setItems(boxItems);
//        yearBox.getSelectionModel().select(2);
    }

    public void initSemesterBox(ChoiceBox<String> semesterBox) {
        ObservableList<String> boxItems = FXCollections.observableArrayList(
          SEMESTER_FALL, SEMESTER_SUMMER, SEMESTER_SPRING, SEMESTER_WINTER);
        semesterBox.setItems(boxItems);
//        semesterBox.getSelectionModel().select(SEMESTER_FALL);
    }

    private void initListeners() {
        titleField.textProperty().addListener((observable, oldValue, newValue) -> app.getGUI().getFileController().markAsEdited(app.getGUI()));
        instructorNameField.textProperty()
                           .addListener((observable, oldValue, newValue) -> app.getGUI().getFileController().markAsEdited(app.getGUI()));
        instructorHomeField.textProperty()
                           .addListener((observable, oldValue, newValue) -> app.getGUI().getFileController().markAsEdited(app.getGUI()));
        styleSheetChoiceBox.valueProperty().addListener((observable, oldValue, newValue) -> app.getGUI().getFileController().markAsEdited(app.getGUI()));

    }

    public VBox getContent() {
        return contentPane;
    }

    @Override
    public void resetWorkspace() {
        titleField.clear();
        instructorHomeField.clear();
        instructorNameField.clear();
        exportDirPlaceLabel.setText(CDM_EXPORT_PLACE_DEFAULT_LABEL);
        siteTemplateLabel.setText(CDM_SITE_TEMPLATE_LABEL);
        sitePagesTable.getItems().clear();

        bannerSchoolImage.setImage(null);
        leftFooterImage.setImage(null);
        rightFooterImage.setImage(null);

        styleSheetChoiceBox.getSelectionModel().clearSelection();
    }

    @Override
    public void reloadWorkspace(AppDataComponent appDataComponent) {


    }

    public ChoiceBox<String> getSemesterBox() {
        return semesterBox;
    }

    public ChoiceBox<String> getYearBox() {
        return yearBox;
    }

    public ChoiceBox<Integer> getNumberBox() {
        return numberBox;
    }

    public ChoiceBox<String> getSubjectBox() {
        return subjectBox;
    }

    public TableView<TemplateDirectoryFile> getSitePagesTable() {
        return sitePagesTable;
    }
}
