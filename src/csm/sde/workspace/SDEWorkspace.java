package csm.sde.workspace;

import csm.CourseSiteManagerData;
import csm.DateHybrid;
import csm.sde.data.SDEData;
import csm.sde.data.ScheduleItem;
import csm.ui.CourseSiteManagerWorkspace;
import djf.AppTemplate;
import djf.components.AppDataComponent;
import djf.components.AppWorkspaceComponent;
import djf.ui.AppMessageDialogSingleton;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import jtps.jTPS_Transaction;

import java.time.DayOfWeek;
import java.time.LocalDate;

import static csm.CourseSiteManagerProp.*;

/**
 * @author Thomas Povinelli
 *         Created 4/6/17
 *         In Homework4
 */
public class SDEWorkspace extends AppWorkspaceComponent {

    public final DatePicker startingPicker;
    public final DatePicker endingPicker;
    public final TableView<ScheduleItem> scheduleItemsTable;
    public final ChoiceBox<String> typeChoiceBox;
    public final DatePicker datePicker;
    public final TextField timeField;
    public final TextField titleField;
    public final TextField topicField;
    public final TextField linkField;
    public final TextField criteriaField;
    public final TableColumn<ScheduleItem, String> typeColumn;
    public final TableColumn<ScheduleItem, DateHybrid> dateColumn;
    public final TableColumn<ScheduleItem, String> titleColumn;
    public final TableColumn<ScheduleItem, String> topicColumn;
    AppTemplate app;
    VBox contentPane;

    public SDEWorkspace(AppTemplate app) {
        // First Section

        SDEController controller = new SDEController(app);

        contentPane = new VBox();
        workspace = contentPane;
        contentPane.setSpacing(DEFAULT_SPACING);
        contentPane.setPadding(new Insets(DEFAULT_PADDING));

        Label mainTitle = new Label(SDE_MAIN_TITLE);
        mainTitle.setFont(Font.font(18));
        contentPane.getChildren().add(mainTitle);

        GridPane firstPane = new GridPane();

        firstPane.setBackground(new Background(new BackgroundFill(
          Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));

        firstPane.setHgap(DEFAULT_GAP);
        firstPane.setVgap(DEFAULT_GAP);
        firstPane.setPadding(new Insets(DEFAULT_PADDING));

        Label firstTitle = new Label(SDE_FIRST_TITLE);

        Label startingMondayLabel = new Label(SDE_STARTING_MONDAY_LABEL);
        Label endingFridayLabel = new Label(SDE_ENDING_FRIDAY_LABEL);

        firstTitle.setFont(Font.font(18));

        startingPicker = new DatePicker();
        endingPicker = new DatePicker();


        startingPicker.valueProperty().addListener(
          new ChangeListener<LocalDate>() {
              @Override
              public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                  ChangeListener<LocalDate> spl = this;

                  if (!newValue.getDayOfWeek()
                               .equals(DayOfWeek.MONDAY))
                  {
                      AppMessageDialogSingleton.getSingleton()
                                               .show(SDE_NOT_MON_TITLE, SDE_NOT_MON_MSG);

                      startingPicker.valueProperty().removeListener(spl);
                      startingPicker.setValue(oldValue);
                      startingPicker.valueProperty().addListener(spl);
                      return;
                  }

                  if (endingPicker.getValue() != null &&
                      newValue.isAfter(endingPicker.getValue())) {
                      AppMessageDialogSingleton.getSingleton()
                                               .show(SDE_MONDAY_AFTER_FRIDAY_TITLE, SDE_MONDAY_AFTER_FRIDAY_MSG);
                      startingPicker.valueProperty().removeListener(spl);
                      startingPicker.setValue(oldValue);
                      startingPicker.valueProperty().addListener(spl);
                      return;
                  }

                  jTPS_Transaction action = new jTPS_Transaction() {
                      @Override
                      public void redoTransaction() {
                          startingPicker.valueProperty().removeListener(spl);
                          startingPicker.setValue(newValue);
                          startingPicker.valueProperty().addListener(spl);
                      }

                      @Override
                      public void undoTransaction() {
                          startingPicker.valueProperty().removeListener(spl);
                          startingPicker.setValue(oldValue);
                          startingPicker.valueProperty().addListener(spl);
                      }

                      @Override
                      public void doTransaction() {


                          SDEData data = ((CourseSiteManagerData) app.getDataComponent())
                            .getSdeData();
                          data.setStartDay(newValue.getDayOfMonth());
                          data.setStartMonth(newValue.getMonth().getValue());
                          data.setStartYear(newValue.getYear());
                      }
                  };

                  app.getGUI().getTps().addTransaction(action);

              }
          });

        endingPicker.valueProperty().addListener(
          new ChangeListener<LocalDate>() {

              ChangeListener<LocalDate> epl = this;

              @Override
              public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {

                  if (!newValue.getDayOfWeek().equals(DayOfWeek.FRIDAY)) {
                      AppMessageDialogSingleton.getSingleton()
                                               .show(SDE_NOT_FRI_TITLE, SDE_NOT_FRI_MSG);
                      endingPicker.setValue(oldValue);
                      return;
                  }

                  if (startingPicker.getValue() != null &&
                      newValue.isBefore(startingPicker.getValue()))
                  {
                      AppMessageDialogSingleton.getSingleton()
                                               .show(SDE_MONDAY_AFTER_FRIDAY_TITLE, SDE_MONDAY_AFTER_FRIDAY_MSG);
                      endingPicker.setValue(oldValue);
                      return;
                  }


                  jTPS_Transaction action = new jTPS_Transaction() {
                      @Override
                      public void undoTransaction() {
                          endingPicker.valueProperty().removeListener(epl);
                          endingPicker.setValue(oldValue);
                          endingPicker.valueProperty().addListener(epl);
                      }

                      @Override
                      public void doTransaction() {
                          SDEData data = ((CourseSiteManagerData) app.getDataComponent())
                            .getSdeData();
                          data.setEndDay(newValue.getDayOfMonth());
                          data.setEndMonth(newValue.getMonthValue());
                          data.setEndYear(newValue.getYear());
                      }

                      @Override
                      public void redoTransaction() {
                          endingPicker.valueProperty().removeListener(epl);
                          endingPicker.setValue(newValue);
                          endingPicker.valueProperty().addListener(epl);
                      }
                  };

                  app.getGUI().getTps().addTransaction(action);

              }
          });

        GridPane.setColumnSpan(firstTitle, 2);

        firstPane.add(firstTitle, 0, 0);
        firstPane.add(startingMondayLabel, 0, 1);
        firstPane.add(endingFridayLabel, 2, 1);
        firstPane.add(startingPicker, 1, 1);
        firstPane.add(endingPicker, 3, 1);

        contentPane.getChildren().add(firstPane);

        VBox secondPane = new VBox();
        secondPane.setBackground(new Background(
          new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY,
            Insets.EMPTY)));
        secondPane.setSpacing(DEFAULT_SPACING);
        secondPane.setPadding(new Insets(DEFAULT_PADDING));

        Label secondTitle = new Label(SDE_SECOND_TITLE);
        Button minusButtonBottom = CourseSiteManagerWorkspace.makeMinusButton();

        minusButtonBottom.setOnAction(controller::handleDeleteAction);

        HBox secondBox = new HBox(secondTitle, minusButtonBottom);
        secondBox.setSpacing(DEFAULT_SPACING);

        secondTitle.setFont(Font.font(18));

        Button addEditButton = new Button(SDE_ADD_EDIT_BUTTON);
        Button clearButton = new Button(SDE_CLEAR_BUTTON);

        scheduleItemsTable = new TableView<>();

        scheduleItemsTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                controller.handleScheduleItemsTableClicked(scheduleItemsTable.getSelectionModel()
                                                                             .getSelectedItem());
            }
        });

        // TODO: Change d to delete key
        scheduleItemsTable.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.DELETE) {
                controller.handleDeleteAction(null);
            }
        });

        addEditButton.setOnAction(e -> {
            controller.handleAddUpdateAction(scheduleItemsTable.getSelectionModel()
                                                               .getSelectedItem());
        });

        clearButton.setOnAction(e -> {
            controller.handleClearAction();
        });


        scheduleItemsTable.setItems(
          ((CourseSiteManagerData) app.getDataComponent()).getSdeData()
                                                          .getScheduleItems());

        typeColumn = new TableColumn<>(SDE_TYPE_HEADER);
        dateColumn = new TableColumn<>(SDE_DATE_HEADER);
        titleColumn = new TableColumn<>(SDE_TITLE_HEADER);
        topicColumn = new TableColumn<>(SDE_TOPIC_HEADER);

        typeColumn.prefWidthProperty()
                  .bind(scheduleItemsTable.widthProperty().multiply(0.2));
        dateColumn.prefWidthProperty()
                  .bind(scheduleItemsTable.widthProperty().multiply(0.2));
        titleColumn.prefWidthProperty()
                   .bind(scheduleItemsTable.widthProperty().multiply(0.2));
        topicColumn.prefWidthProperty()
                   .bind(scheduleItemsTable.widthProperty().multiply(0.2));

        typeColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("type"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, DateHybrid>("date"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("title"));
        topicColumn.setCellValueFactory(new PropertyValueFactory<ScheduleItem, String>("topic"));

        scheduleItemsTable.getColumns()
                          .addAll(typeColumn, dateColumn, titleColumn,
                            topicColumn);


        GridPane secondAddEditPane = new GridPane();
        secondAddEditPane.setVgap(DEFAULT_GAP);
        secondAddEditPane.setHgap(DEFAULT_GAP);
        secondAddEditPane.setPadding(new Insets(DEFAULT_PADDING));

        Label typeLabel = new Label(SDE_TYPE_LABEL);
        Label dateLabel = new Label(SDE_DATE_LABEL);
        Label timeLabel = new Label(SDE_TIME_LABEL);
        Label titleLabel = new Label(SDE_TITLE_LABEL);
        Label topicLabel = new Label(SDE_TOPIC_LABEL);
        Label linkLabel = new Label(SDE_LINK_LABEL);
        Label criteriaLabel = new Label(SDE_CRITERIA_LABEL);
        Label addEditLabel = new Label(RDE_ADD_EDIT_TITLE);
        addEditLabel.setFont(Font.font(16));

        typeChoiceBox = new ChoiceBox<>();
        datePicker = new DatePicker();
        timeField = new TextField();
        titleField = new TextField();
        topicField = new TextField();
        linkField = new TextField();
        criteriaField = new TextField();

        // INITALIZE SOME DFEAULT VALUES
        typeChoiceBox.getItems()
                     .addAll("Holiday", "Lecture", "Recitation", "Exam", "Homework Due");


        secondAddEditPane.addColumn(0, typeLabel, dateLabel, timeLabel,
          titleLabel, topicLabel, linkLabel, criteriaLabel, addEditButton);

        secondAddEditPane.addColumn(1, typeChoiceBox, datePicker, timeField,
          titleField, topicField, linkField, criteriaField, clearButton);

        secondPane.getChildren()
                  .addAll(secondBox, scheduleItemsTable, addEditLabel,
                    secondAddEditPane);

        contentPane.getChildren().add(secondPane);
    }

    public VBox getContentPane() {
        return contentPane;
    }

    @Override
    public void resetWorkspace() {
        scheduleItemsTable.getItems().clear();
        typeChoiceBox.getSelectionModel().select(0);
        datePicker.setValue(startingPicker.getValue());
        timeField.clear();
        titleField.clear();
        topicField.clear();
        linkField.clear();
        criteriaField.clear();
    }

    @Override
    public void reloadWorkspace(AppDataComponent appDataComponent) {

    }

    public DatePicker getStartingPicker() {
        return startingPicker;
    }

    public DatePicker getEndingPicker() {
        return endingPicker;
    }

    public TableView<ScheduleItem> getScheduleItemsTable() {
        return scheduleItemsTable;
    }
}
