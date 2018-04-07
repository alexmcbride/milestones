package wpd2.coursework1.model;

import wpd2.coursework1.util.PasswordService;
import wpd2.coursework1.util.IoC;
import wpd2.coursework1.helper.ValidationHelper;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class User extends ValidatableModel {
    private final PasswordService passwordService;
    private int id;
    private String username;
    private boolean usernameChanged;
    private String email;
    private boolean emailChanged;
    private char[] password;
    private boolean passwordChanged;
    private String passwordHash;
    private Date joined;
    private String resetToken;
    private int loginCount;

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
        usernameChanged = true;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
        emailChanged = true;
    }

    public char[] getPassword() {
        return password;
    }

    public void setPassword(char[] password) {
        this.password = password;
        passwordChanged = true;
    }

    private String getPasswordHash() {
        return passwordHash;
    }

    private void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public int getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(int loginCount) {
        this.loginCount = loginCount;
    }

    @Override
    public void validate() {
        ValidationHelper validation = new ValidationHelper(this);
        validation.required("username", username);
        validation.email("email", email);

        if (passwordChanged) {
            validation.password("password", password);
        }

        if (usernameChanged && usernameExists(username)) {
            addValidationError("username", "Username already exists");
        }

        if (emailChanged && emailExists(email)) {
            addValidationError("email", "Email address already exists");
        }
    }

    public void create() {
        passwordHash = passwordService.hash(password);
        joined = new Date();

        String sql = "INSERT INTO users (username, email, password, joined, resetToken, loginCount) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setTimestamp(4, new Timestamp(joined.getTime()));
            statement.setString(5, resetToken);
            statement.setInt(6, loginCount);
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            result.next();
            id = result.getInt(1);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean update() {
        if (passwordChanged) {
            passwordHash = passwordService.hash(password);
        }

        String sql = "UPDATE users SET username=?, email=?, password=?, resetToken=?, loginCount=? WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, passwordHash);
            statement.setString(4, resetToken);
            statement.setInt(5, loginCount);
            statement.setInt(6, id);
            return statement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete() {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        return valueExists(email, sql);
    }

    public static boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username=?";
        return valueExists(username, sql);
    }

    private static boolean valueExists(String value, String sql) {
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, value);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt(1) > 0;
            }
            return false;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "username NVARCHAR(32) NOT NULL UNIQUE, " +
                "email NVARCHAR(1024) NOT NULL UNIQUE, " +
                "password NVARCHAR(128) NOT NULL," +
                "joined TIMESTAMP NOT NULL," +
                "resetToken NVARCHAR(128) NULL," +
                "loginCount INTEGER NULL" +
                ")";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        String sql = "DROP TABLE IF EXISTS users;";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("Duplicates")
    public static User find(int id) {
        String sql = "SELECT id, username, email, password, joined, resetToken, loginCount FROM users WHERE id=?";
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

    public static User findByToken(String resetToken) {
        String sql = "SELECT id, username, email, password, joined, resetToken, loginCount FROM users WHERE resetToken=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, resetToken);
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
        String sql = "SELECT id, username, email, password, joined, resetToken, loginCount FROM users WHERE email=?";
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
        String sql = "SELECT id, username, email, password, joined, resetToken, loginCount FROM users";
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
        user.id = result.getInt(1);
        user.username = result.getString(2);
        user.email = result.getString(3);
        user.passwordHash = result.getString(4);
        user.joined = result.getTimestamp(5);
        user.resetToken = result.getString(6);
        user.loginCount = result.getInt(7);
        return user;
    }

    public boolean authenticate(char[] password) {
        return passwordService.authenticate(password, getPasswordHash());
    }

    public static User dummyUser() {
        User user = User.find(1);
        if (user == null) {
            user = new User();
            user.setUsername("user1");
            user.setEmail("user@email.com");
            user.setPassword("password1".toCharArray());
            user.create();
        }
        return user;
    }

    public static List<User> search(String query) {
        query = "%" + query.toLowerCase() + "%"; // Add wildcards
        String sql = "SELECT * FROM users WHERE LOWER(username) LIKE ? OR LOWER(email) LIKE ?";
        List<User> users = new ArrayList<>();
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, query);
            statement.setString(2, query);
            ResultSet result = statement.executeQuery();
            while (result.next()) {
                users.add(getUserFromResult(result));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    public List<Project> getSharedProjects() {
        List<Project> projects = new ArrayList<>();
        List<SharedProject> sharedProjects = SharedProject.findAll(this);
        for (SharedProject sharedProject : sharedProjects) {
            Project project = Project.find(sharedProject.getProjectId());
            projects.add(project);
        }
        return projects;
    }

    public int getUnvisited() {
        String sql = "SELECT COUNT(*) FROM sharedProjects WHERE userId=? AND viewed IS NULL";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, getId());
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getInt(1);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
