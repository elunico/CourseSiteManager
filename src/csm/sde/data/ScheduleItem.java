package csm.sde.data;

import csm.DateHybrid;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import org.jetbrains.annotations.NotNull;

/**
 * @author Thomas Povinelli
 *         Created 4/13/17
 *         In Homework4
 */
public class ScheduleItem implements Comparable<ScheduleItem>{
    private final SimpleStringProperty type, title, topic, link;
    private final SimpleStringProperty time;
    private final SimpleObjectProperty<DateHybrid> date;
    private final SimpleStringProperty criteria;


    public ScheduleItem(String type, String title, String topic, DateHybrid date) {
        this(type, title, topic, date, "");
    }


    public ScheduleItem(String type, String title, String topic, DateHybrid date, String link) {
        this(type, title, topic, date, "", link, "");
    }

    public ScheduleItem(String type, String title, String topic, DateHybrid date, String time, String link, String criteria) {
        this.type = new SimpleStringProperty(type);
        this.title = new SimpleStringProperty(title);
        this.topic = new SimpleStringProperty(topic);
        this.time = new SimpleStringProperty(time);
        this.date = new SimpleObjectProperty<>(date);
        this.link = new SimpleStringProperty(link);
        this.criteria = new SimpleStringProperty(criteria);
    }

    public ScheduleItem() {
        this("", "", "", DateHybrid.now());
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public SimpleStringProperty typeProperty() {
        return type;
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

    public String getTopic() {
        return topic.get();
    }

    public void setTopic(String topic) {
        this.topic.set(topic);
    }

    public SimpleStringProperty topicProperty() {
        return topic;
    }

    public DateHybrid getDate() {
        return date.get();
    }

    public void setDate(DateHybrid date) {
        this.date.set(date);
    }

    public SimpleObjectProperty<DateHybrid> dateProperty() {
        return date;
    }

    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public SimpleStringProperty timeProperty() {
        return time;
    }

    @Override
    public String toString() {
        return "ScheduleItem{" +
               "type=" + type +
               ", title=" + title +
               ", topic=" + topic +
               ", link=" + link +
               ", time=" + time +
               ", date=" + date +
               ", criteria=" + criteria +
               '}';
    }

    public String getLink() {
        return link.get();
    }

    public void setLink(String link) {
        this.link.set(link);
    }

    public SimpleStringProperty linkProperty() {
        return link;
    }

    public String getCriteria() {
        return criteria.get();
    }

    public void setCriteria(String criteria) {
        this.criteria.set(criteria);
    }

    public SimpleStringProperty criteriaProperty() {
        return criteria;
    }

    @Override
    public int compareTo(@NotNull ScheduleItem o) {
        return this.date.get().compareTo(o.date.get());
    }
}
