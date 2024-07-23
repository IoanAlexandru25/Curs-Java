package models;

public class User {
    private String username;
    private String password;
    private boolean admin;

    public User() {}

    public User(String username, String password, boolean admin) {
        this.username = username;
        this.password = password;
        this.admin = admin;
    }

    public boolean isAdmin() {
        return admin;
    }
}
