package wpd2.coursework1.model;

import wpd2.coursework1.service.PasswordService;
import wpd2.coursework1.util.IoC;
import wpd2.coursework1.util.ValidationError;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class User extends BaseModel {
    private final PasswordService passwordService;
    private int id;
    private String username;
    private String email;
    private char[] password;
    private String passwordHash;

    public User() {
        passwordService = (PasswordService)IoC.get().getInstance(PasswordService.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    @Override
    protected void validate() {
        addValidationError("username", ValidationError.required(username));
        addValidationError("email", ValidationError.email(email));
        addValidationError("password", ValidationError.password(password));

        if (usernameExists(username)) {
            addValidationError("username", "already exists");
        }

        if (emailExists(email)) {
            addValidationError("email", "address already exists");
        }
    }

    public void create() {
        passwordHash = passwordService.hash(this.password);
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, getUsername());
            statement.setString(2, getEmail());
            statement.setString(3, getPasswordHash());
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                setId(result.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        if (passwordChanged()) {
            passwordHash = passwordService.hash(this.password);
        }

        String sql = "UPDATE users SET username=?, email=?, password=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, getUsername());
            statement.setString(2, getEmail());
            statement.setString(3, getPasswordHash());
            statement.setInt(4, getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean passwordChanged() {
        return password != null && password.length > 0;
    }

    public void delete() {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        return valueExists(email, sql);
    }

    private boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username=?";
        return valueExists(username, sql);
    }

    private boolean valueExists(String value, String sql) {
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, value);
            ResultSet result = statement.executeQuery();
            return result.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "username NVARCHAR(32) NOT NULL , " +
                "email NVARCHAR(1024) NOT NULL, " +
                "password NVARCHAR(128) NOT NULL" +
                ")";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        String sql = "DROP TABLE users;";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    public static User find(int id) {
        String sql = "SELECT id, username, email, password FROM users WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getUserFromResult(result);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    public static User find(String email) {
        String sql = "SELECT id, username, email, password FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getUserFromResult(result);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @SuppressWarnings("Duplicates")
    public static List<User> findAll() {
        String sql = "SELECT id, username, email, password FROM users";
        List<User> users = new ArrayList<>();
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            ResultSet result = statement.executeQuery(sql);
            while (result.next()) {
                users.add(getUserFromResult(result));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    private static User getUserFromResult(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getInt(1));
        user.setUsername(result.getString(2));
        user.setEmail(result.getString(3));
        user.setPasswordHash(result.getString(4));
        return user;
    }
}
