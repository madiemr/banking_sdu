import java.util.Scanner;

public abstract class User {
    private String name;
    private String username;
    private String password;

    public User(String name, String username, String password) {
        this.name = name;
        this.username = username;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public abstract void showMenu(Scanner scanner, InMemoryDatabase database);
}