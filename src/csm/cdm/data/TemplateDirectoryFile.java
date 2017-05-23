package csm.cdm.data;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import java.io.File;

/**
 * @author Thomas Povinelli
 *         Created 4/13/17
 *         In Homework4
 *         <p>
 *         This class will be used to model files in a directory
 *         when selecting a template directory each file name will be read in
 *         and constructed as a class so it can be managed in the table
 */
public class TemplateDirectoryFile {
    private SimpleStringProperty name, script, title;
    private SimpleBooleanProperty used;
    private File file;

    public TemplateDirectoryFile(String name, String script, String title,
      boolean used, File file)
    {
        this.name = new SimpleStringProperty(name);
        this.script = new SimpleStringProperty(script);
        this.title = new SimpleStringProperty(title);
        this.used = new SimpleBooleanProperty(used);
        this.file = file;
    }

    public File getFile() {

        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public File asFile() {
        return file;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getScript() {
        return script.get();
    }

    public void setScript(String script) {
        this.script.set(script);
    }

    public SimpleStringProperty scriptProperty() {
        return script;
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

    public boolean isUsed() {
        return used.get();
    }

    public void setUsed(boolean used) {
        this.used.set(used);
    }

    public SimpleBooleanProperty usedProperty() {
        return used;
    }
}
