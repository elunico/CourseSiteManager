package csm.sde.data;

import csm.CourseSiteManagerApp;
import csm.sde.workspace.SDEWorkspace;
import csm.ui.CourseSiteManagerWorkspace;
import djf.components.AppDataComponent;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalDate;

/**
 * @author Thomas Povinelli
 *         Created 4/19/17
 *         In Homework5
 */
public class SDEData implements AppDataComponent {

    private SimpleIntegerProperty startMonth, startDay, startYear;
    private SimpleIntegerProperty endMonth, endDay, endYear;
    private ObservableList<ScheduleItem> scheduleItems = FXCollections.observableArrayList();
    private CourseSiteManagerApp app;

    public SDEData(CourseSiteManagerApp app) {
        this.app = app;

        startDay = new SimpleIntegerProperty();
        startMonth = new SimpleIntegerProperty();
        startYear = new SimpleIntegerProperty();
        endDay = new SimpleIntegerProperty();
        endMonth = new SimpleIntegerProperty();
        endYear = new SimpleIntegerProperty();

    }

    @Override
    public String toString() {
        return "SDEData{" +
               "startMonth=" + startMonth +
               ", startDay=" + startDay +
               ", startYear=" + startYear +
               ", endMonth=" + endMonth +
               ", endDay=" + endDay +
               ", endYear=" + endYear +
               ", scheduleItems=" + scheduleItems +
               ", app=" + app +
               '}';
    }

    public int getStartMonth() {
        return startMonth.get();
    }

    public void setStartMonth(int startMonth) {
        this.startMonth.set(startMonth);
    }

    public SimpleIntegerProperty startMonthProperty() {
        return startMonth;
    }

    public int getStartDay() {
        return startDay.get();
    }

    public void setStartDay(int startDay) {
        this.startDay.set(startDay);
    }

    public SimpleIntegerProperty startDayProperty() {
        return startDay;
    }

    public int getStartYear() {
        return startYear.get();
    }

    public void setStartYear(int startYear) {
        this.startYear.set(startYear);
    }

    public SimpleIntegerProperty startYearProperty() {
        return startYear;
    }

    public int getEndMonth() {
        return endMonth.get();
    }

    public void setEndMonth(int endMonth) {
        this.endMonth.set(endMonth);
    }

    public SimpleIntegerProperty endMonthProperty() {
        return endMonth;
    }

    public int getEndDay() {
        return endDay.get();
    }

    public void setEndDay(int endDay) {
        this.endDay.set(endDay);
    }

    public SimpleIntegerProperty endDayProperty() {
        return endDay;
    }

    public int getEndYear() {
        return endYear.get();
    }

    public void setEndYear(int endYear) {
        this.endYear.set(endYear);
    }

    public SimpleIntegerProperty endYearProperty() {
        return endYear;
    }

    public ObservableList<ScheduleItem> getScheduleItems() {
        return scheduleItems;
    }

    public void setScheduleItems(
      ObservableList<ScheduleItem> scheduleItems)
    {
        this.scheduleItems = scheduleItems;
    }

    public CourseSiteManagerApp getApp() {
        return app;
    }

    @Override
    public void resetData() {
        scheduleItems.clear();
        startDay.set(0);
        startMonth.set(0);
        startYear.set(0);
        endDay.set(0);
        endMonth.set(0);
        endYear.set(0);

    }

    public void updateDatePickers() {
        SDEWorkspace workspace = ((CourseSiteManagerWorkspace) app.getWorkspaceComponent())
          .getScheduleDataTab()
          .getWorkspace();
        workspace.startingPicker.setValue(LocalDate.of(startYear.get(), startMonth
          .get(), startDay.get()));
        workspace.endingPicker.setValue(LocalDate.of(endYear.get(), endMonth.get(), endDay
          .get()));

    }
}
