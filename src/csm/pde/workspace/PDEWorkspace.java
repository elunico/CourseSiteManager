package csm.pde.workspace;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.pde.data.Student;
import csm.pde.data.Team;
import csm.ui.CourseSiteManagerWorkspace;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 4/9/17
 *         In Homework4
 */
public class PDEWorkspace extends AppWorkspaceComponent {
    public final Button minusButtonBottom;
    private final TextField projectNameField;
    private final ColorPicker colorPicker;
    private final ColorPicker textColorPicker;
    private final TextField linkTextField;
    private final TextField roleField;
    private final ChoiceBox<Team> teamChoiceBox;
    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TableView<Team> teamTableView;
    private final GridPane addEditGridPane;
    private final TableView<Student> studentTableView;
    private final GridPane studentsGridPane;
    private final TableColumn<Team, String> nameColumn;
    private final TableColumn<Team, String> colorColumn;
    private final TableColumn<Team, String> textColorColumn;
    private final TableColumn<Team, String> linkColumn;
    private final TableColumn<Student, String> firstNameColumn;
    private final TableColumn<Student, Team> teamColumn;
    private final TableColumn<Student, String> lastNameColumn;
    private final TableColumn<Student, String> roleColumn;
    AppTemplate app;
    VBox contentPane = new VBox();

    public PDEWorkspace(AppTemplate app) {
        this.app = app;
        workspace = contentPane;

        PDEController controller = new PDEController((CourseSiteManagerApp) app);

        contentPane.setSpacing(DEFAULT_SPACING);
        contentPane.setPadding(new Insets(DEFAULT_PADDING));

        VBox firstPane = new VBox();

        Label mainTitle = new Label(PDE_MAIN_TITLE);

        mainTitle.setFont(Font.font(18));

        contentPane.getChildren().add(mainTitle);

        firstPane.setBackground(new Background(new BackgroundFill(
          Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        firstPane.setSpacing(DEFAULT_SPACING);
        firstPane.setPadding(new Insets(DEFAULT_PADDING));

        Label firstTitleLabel = new Label(PDE_TITLE_LABEL);
        Button minusButtonTop = CourseSiteManagerWorkspace.makeMinusButton();
        HBox firstBox = new HBox(firstTitleLabel, minusButtonTop);
        firstTitleLabel.setFont(Font.font(18));
        teamTableView = new TableView<>();

        nameColumn = new TableColumn<>(PDE_NAME_HEADER);
        colorColumn = new TableColumn<>(PDE_COLOR_HEADER);
        textColorColumn = new TableColumn<>(PDE_TEXT_COLOR_HEADER);
        linkColumn = new TableColumn<>(PDE_LINK_HEADER);

        nameColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("name"));
        colorColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("color"));
        textColorColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("textColor"));
        linkColumn.setCellValueFactory(new PropertyValueFactory<Team, String>("link"));

        teamTableView.getColumns()
                     .addAll(nameColumn, colorColumn, textColorColumn,
                       linkColumn);
        teamTableView.setItems(
          ((CourseSiteManagerData) app.getDataComponent()).getPdeData()
                                                          .getTeams());

        nameColumn.prefWidthProperty()
                  .bind(teamTableView.widthProperty().multiply(0.24));
        colorColumn.prefWidthProperty()
                   .bind(teamTableView.widthProperty().multiply(0.24));
        textColorColumn.prefWidthProperty()
                       .bind(teamTableView.widthProperty().multiply(0.24));
        linkColumn.prefWidthProperty()
                  .bind(teamTableView.widthProperty().multiply(0.24));

        Label addEditLabel = new Label(PDE_ADD_EDIT_TITLE);
        addEditLabel.setFont(Font.font(16));

        Label nameLabel = new Label(PDE_NAME_LABEL);
        Label colorLabel = new Label(PDE_COLOR_LABEL);
        Label textColorLabel = new Label(PDE_TEXT_COLOR_LABEL);
        Label linkLabel = new Label(PDE_LINK_LABEL);

        projectNameField = new TextField();
        colorPicker = new ColorPicker();
        textColorPicker = new ColorPicker();
        linkTextField = new TextField();

        colorPicker.setValue(Color.NAVY);
        textColorPicker.setValue(Color.WHITE);

        Button addUpdateProjectButton = new Button(PDE_ADD_EDIT_BUTTON);
        Button clearProjectButton = new Button(PDE_CLEAR_BUTTON);

        // INIT CONTROLLER ACTIONS

        teamTableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.handleTeamTableClick();
            }
        });


        // TODO: Make change code from D to Delete key

        teamTableView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                controller.handleRemoveTeamAction();
            }
        });
        minusButtonTop.setOnAction(event -> {
            controller.handleRemoveTeamAction();
        });

        addUpdateProjectButton.setOnAction(e ->
          controller.handleAddUpdateTeamAction(teamTableView.getSelectionModel()
                                                            .getSelectedItem()));

        clearProjectButton.setOnAction(e ->
          controller.handleClearProjectAction(teamTableView));


        addEditGridPane = new GridPane();
        addEditGridPane.setHgap(DEFAULT_GAP);

        addEditGridPane.setVgap(DEFAULT_GAP);
        addEditGridPane.setPadding(new Insets(DEFAULT_PADDING));

        addEditGridPane.add(addEditLabel, 0, 0);
        addEditGridPane.add(nameLabel, 0, 1);
        addEditGridPane.add(projectNameField, 1, 1);
        addEditGridPane.add(colorLabel, 0, 2);
        addEditGridPane.add(colorPicker, 1, 2);
        addEditGridPane.add(textColorLabel, 2, 2);
        addEditGridPane.add(textColorPicker, 3, 2);
        addEditGridPane.add(linkLabel, 0, 3);
        addEditGridPane.add(linkTextField, 1, 3);
        addEditGridPane.add(addUpdateProjectButton, 0, 4);
        addEditGridPane.add(clearProjectButton, 1, 4);

        firstPane.getChildren()
                 .addAll(firstBox, teamTableView, addEditGridPane);

        contentPane.getChildren().add(firstPane);

        VBox secondPane = new VBox();
        secondPane.setSpacing(DEFAULT_SPACING);
        secondPane.setPadding(new Insets(DEFAULT_PADDING));
        secondPane.setBackground(new Background(
          new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY,
            Insets.EMPTY)));

        Label secondTitleLabel = new Label(PDE_SECOND_TITLE);
        minusButtonBottom = CourseSiteManagerWorkspace.makeMinusButton();
        HBox secondBox = new HBox(secondTitleLabel, minusButtonBottom);
        secondTitleLabel.setFont(Font.font(18));
        studentTableView = new TableView<>();

        studentTableView.setItems(
          ((CourseSiteManagerData) app.getDataComponent()).getPdeData()
                                                          .getStudents());

        firstNameColumn = new TableColumn<>(PDE_FIRST_NAME_HEADER);
        lastNameColumn = new TableColumn<>(PDE_LAST_NAME_HEADER);
        teamColumn = new TableColumn<>(PDE_TEAM_HEADER);
        roleColumn = new TableColumn<>(PDE_ROLE_HEADER);

        firstNameColumn.setCellValueFactory(
          new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(
          new PropertyValueFactory<>("lastName"));
        teamColumn.setCellValueFactory(new PropertyValueFactory<>("team"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        firstNameColumn.prefWidthProperty()
                       .bind(studentTableView.widthProperty().multiply(0.24));
        lastNameColumn.prefWidthProperty()
                      .bind(studentTableView.widthProperty().multiply(0.24));
        teamColumn.prefWidthProperty()
                  .bind(studentTableView.widthProperty().multiply(0.24));
        roleColumn.prefWidthProperty()
                  .bind(studentTableView.widthProperty().multiply(0.24));

        studentTableView.getColumns()
                        .addAll(firstNameColumn, lastNameColumn, teamColumn,
                          roleColumn);

        Label firstNameLabel = new Label(PDE_FIRST_NAME_LABEL);
        Label lastNameLabel = new Label(PDE_LAST_NAME_LABEL);
        Label teamLabel = new Label(PDE_TEAM_LABEL);
        Label roleLabel = new Label(PDE_ROLE_LABEL);

        firstNameField = new TextField();
        lastNameField = new TextField();
        teamChoiceBox = new ChoiceBox<>();
        teamChoiceBox.setItems(((CourseSiteManagerData) app.getDataComponent()).getPdeData()
                                                                               .getTeams());
        // BIND THE LIST OF TEAMS TO THE CONTENTS OF THE TABLE LATER WHEN CONTENT IS IN THE TABLE
        // teamChoiceBox.itemsProperty().bind();
        roleField = new TextField();

        Button addUpdateStudentButton = new Button(PDE_ADD_EDIT_BUTTON);
        Button clearStudentButton = new Button(PDE_CLEAR_BUTTON);

        // INIT CONTROLLER ACTIONS FOR THE NEXT PANE


        // TODO: Change D to delete key

        studentTableView.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                controller.handleRemoveStudentAction();
            }
        });
        studentTableView.setOnMouseClicked(controller::handleStudentTableClicked);
        minusButtonBottom.setOnAction(event -> {
            controller.handleRemoveStudentAction();

        });

        addUpdateStudentButton.setOnAction(e ->
          controller.handleAddUpdateStudentAction(studentTableView.getSelectionModel()
                                                                  .getSelectedItem()));

        clearStudentButton.setOnAction(e ->
          controller.handleClearStudentAction(studentTableView));


        studentsGridPane = new GridPane();
        studentsGridPane.setHgap(DEFAULT_GAP);
        studentsGridPane.setVgap(DEFAULT_GAP);
        studentsGridPane.setPadding(new Insets(DEFAULT_PADDING));

        studentsGridPane.addColumn(0, firstNameLabel, lastNameLabel, teamLabel,
          roleLabel, addUpdateStudentButton);
        studentsGridPane.addColumn(1, firstNameField, lastNameField,
          teamChoiceBox, roleField, clearStudentButton);

        secondPane.getChildren()
                  .addAll(secondBox, studentTableView, addEditLabel,
                    studentsGridPane);
        contentPane.getChildren().add(secondPane);

    }

    public TextField getProjectNameField() {
        return projectNameField;
    }

    public TextField getLinkTextField() {
        return linkTextField;
    }

    public ColorPicker getColorPicker() {
        return colorPicker;
    }

    public ColorPicker getTextColorPicker() {
        return textColorPicker;
    }

    public TextField getRoleField() {
        return roleField;
    }

    public ChoiceBox<Team> getTeamChoiceBox() {
        return teamChoiceBox;
    }

    public TextField getFirstNameField() {
        return firstNameField;
    }

    public TextField getLastNameField() {
        return lastNameField;
    }

    public TableView<Team> getTeamTableView() {
        return teamTableView;
    }

    public GridPane getAddEditGridPane() {
        return addEditGridPane;
    }

    public TableView<Student> getStudentTableView() {
        return studentTableView;
    }

    public GridPane getStudentsGridPane() {
        return studentsGridPane;
    }

    public VBox getContentPane() {
        return contentPane;
    }

    @Override
    public void resetWorkspace() {
        teamTableView.getItems().clear();
        projectNameField.clear();
        colorPicker.setValue(Color.NAVY);
        textColorPicker.setValue(Color.WHITE);
        linkTextField.clear();

        studentTableView.getItems().clear();
        firstNameField.clear();
        lastNameField.clear();
        teamChoiceBox.getSelectionModel().clearSelection();
        roleField.clear();
    }

    @Override
    public void reloadWorkspace(AppDataComponent appDataComponent) {

    }
}
