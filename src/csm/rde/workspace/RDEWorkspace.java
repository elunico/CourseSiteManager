package csm.rde.workspace;

import csm.CourseSiteManagerApp;
import csm.CourseSiteManagerData;
import csm.rde.data.Recitation;
import csm.tam.data.TeachingAssistant;
import csm.ui.CourseSiteManagerWorkspace;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 4/5/17
 *         In Homework4
 */
public class RDEWorkspace extends AppWorkspaceComponent {
    public final TextField sectionTextField;
    public final TextField instructorTextField;
    public final TextField dayTimeTextField;
    public final TextField locationTextField;
    public final ChoiceBox<TeachingAssistant> supervisingTATextFieldTop;
    public final ChoiceBox<TeachingAssistant> supervisingTATextFieldBottom;
    public final Button clearRecitationDataButton;
    public final Button addUpdateRecitationDataButton;
    public final TableColumn<Recitation, String> instructorColumn;
    public final TableColumn<Recitation, String> sectionColumn;
    public final TableColumn<Recitation, String> dayTimeColumn;
    public final TableColumn<Recitation, String> locationColumn;
    public final TableColumn<Recitation, String> ta1Column;
    public final TableColumn<Recitation, String> ta2Column;
    public final Button minusButton;
    public final RDEController controller;
    VBox contentPane = new VBox();
    Label titleLabel;
    TableView<Recitation> recitationTable;
    VBox dataBox;
    CourseSiteManagerApp app;


    public RDEWorkspace(CourseSiteManagerApp app) {
        this.app = app;
        controller = new RDEController(app);
        contentPane.setPadding(new Insets(DEFAULT_PADDING));
        contentPane.setSpacing(DEFAULT_SPACING);

        HBox titleBox = new HBox();
        titleBox.setSpacing(DEFAULT_SPACING);

        titleLabel = new Label(RDE_TITLE_LABEL);
        titleLabel.setFont(Font.font(18));

        minusButton = CourseSiteManagerWorkspace.makeMinusButton();
        minusButton.setOnAction(controller::handleDeleteRecitationAction);

        titleBox.getChildren().addAll(titleLabel, minusButton);

        recitationTable = new TableView<>();

        recitationTable.setOnMouseClicked(e -> {
            controller.handleTableClickedAction();
        });

        // TODO: change d to delete key
        recitationTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                controller.handleDeleteRecitationAction(null);
            }
        });

        recitationTable.setItems(
          ((CourseSiteManagerData) app.getDataComponent()).getRdeData()
                                                          .getRecitations());

        sectionColumn = new TableColumn<>(RDE_SECTION_HEADER);
        instructorColumn = new TableColumn<>(RDE_INSTRUCTOR_HEADER);
        dayTimeColumn = new TableColumn<>(RDE_DAY_TIME_HEADER);
        locationColumn = new TableColumn<>(RDE_LOCATION_HEADER);
        ta1Column = new TableColumn<>(RDE_TA_HEADER);
        ta2Column = new TableColumn<>(RDE_TA_HEADER);

        sectionColumn.prefWidthProperty()
                     .bind(recitationTable.widthProperty().multiply(0.15));
        instructorColumn.prefWidthProperty()
                        .bind(recitationTable.widthProperty().multiply(0.15));
        dayTimeColumn.prefWidthProperty()
                     .bind(recitationTable.widthProperty().multiply(0.15));
        locationColumn.prefWidthProperty()
                      .bind(recitationTable.widthProperty().multiply(0.15));
        ta1Column.prefWidthProperty()
                 .bind(recitationTable.widthProperty().multiply(0.15));
        ta2Column.prefWidthProperty()
                 .bind(recitationTable.widthProperty().multiply(0.15));

        sectionColumn.setCellValueFactory(new PropertyValueFactory<Recitation, String>("section"));
        instructorColumn.setCellValueFactory(new PropertyValueFactory<Recitation, String>("instructor"));
        dayTimeColumn.setCellValueFactory(new PropertyValueFactory<Recitation, String>("dayTime"));
        locationColumn.setCellValueFactory(new PropertyValueFactory<Recitation, String>("location"));
        ta1Column.setCellValueFactory(new PropertyValueFactory<Recitation, String>("ta1"));
        ta2Column.setCellValueFactory(new PropertyValueFactory<Recitation, String>("ta2"));

        recitationTable.getColumns().addAll(sectionColumn, instructorColumn,
          dayTimeColumn, locationColumn, ta1Column, ta2Column);

        Label sectionLabel = new Label(RDE_SECTION_LABEL);
        Label instructorLabel = new Label(RDE_INSTRUCTOR_LABEL);
        Label dayTimeLabel = new Label(RDE_DAY_TIME_LABEL);
        Label locationLabel = new Label(RDE_LOCATION_LABEL);
        Label supervisingTALabelTop = new Label(RDE_SUPERVISING_TA_LABEL);
        Label supervisingTALabelBottom = new Label(RDE_SUPERVISING_TA_LABEL);

        Label addUpdateTitleLabel = new Label(RDE_ADD_EDIT_TITLE);
        addUpdateTitleLabel.setFont(Font.font(15));
        GridPane.setColumnSpan(addUpdateTitleLabel, 2);

        sectionTextField = new TextField();
        instructorTextField = new TextField();
        dayTimeTextField = new TextField();
        locationTextField = new TextField();
        supervisingTATextFieldTop = new ChoiceBox<>();
        supervisingTATextFieldBottom = new ChoiceBox<>();

        supervisingTATextFieldTop.setItems(((CourseSiteManagerData) app.getDataComponent())
          .getTaData()
          .getTeachingAssistants());
        supervisingTATextFieldBottom.setItems(((CourseSiteManagerData) app.getDataComponent())
          .getTaData()
          .getTeachingAssistants());


        // BIND THE CHOICE BOXES TO THE TAS PRESENT
//        supervisingTATextFieldTop.itemsProperty().bind();

        addUpdateRecitationDataButton = new Button(RDE_ADD_UPDATE_BUTTON);
        clearRecitationDataButton = new Button(RDE_CLEAR_BUTTON);

        addUpdateRecitationDataButton.setOnAction(e -> {
            controller.handleAddUpdateRecitationAction(recitationTable.getSelectionModel()
                                                                      .getSelectedItem());
        });

        clearRecitationDataButton.setOnAction(controller::handleClearRecitationDataAction);

        GridPane secondPane = new GridPane();
        secondPane.setBackground(new Background(new BackgroundFill(
          Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        secondPane.setVgap(DEFAULT_GAP);
        secondPane.setPadding(new Insets(DEFAULT_PADDING));

        secondPane.add(addUpdateTitleLabel, 0, 0);

        secondPane.addColumn(0, sectionLabel, instructorLabel, dayTimeLabel,
          locationLabel, supervisingTALabelTop, supervisingTALabelBottom,
          addUpdateRecitationDataButton);
        secondPane.addColumn(1, sectionTextField, instructorTextField,
          dayTimeTextField, locationTextField, supervisingTATextFieldTop,
          supervisingTATextFieldBottom, clearRecitationDataButton);

        contentPane.getChildren()
                   .addAll(titleBox, recitationTable, secondPane);


    }

    public VBox getContentPane() {
        return contentPane;
    }

    @Override
    public void resetWorkspace() {
        recitationTable.getItems().clear();
        sectionTextField.clear();
        instructorTextField.clear();
        dayTimeTextField.clear();
        locationTextField.clear();
        supervisingTATextFieldTop.getSelectionModel().clearSelection();
        supervisingTATextFieldBottom.getSelectionModel().clearSelection();
    }

    @Override
    public void reloadWorkspace(AppDataComponent appDataComponent) {

    }
}
