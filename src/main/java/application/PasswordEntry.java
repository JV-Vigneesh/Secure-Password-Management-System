package application;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PasswordEntry {
    private final StringProperty site;
    private final StringProperty username;
    private final StringProperty password;

    public PasswordEntry(String site, String username, String password) {
        this.site = new SimpleStringProperty(site);
        this.username = new SimpleStringProperty(username);
        this.password = new SimpleStringProperty(password);
    }

    public String getSite() { return site.get(); }
    public StringProperty siteProperty() { return site; }

    public String getUsername() { return username.get(); }
    public StringProperty usernameProperty() { return username; }

    public String getPassword() { return password.get(); }
    public StringProperty passwordProperty() { return password; }
}
