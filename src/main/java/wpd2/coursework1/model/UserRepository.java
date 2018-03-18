package wpd2.coursework1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private final Connection conn;

    public UserRepository(Connection conn) {
        this.conn = conn;
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "username NVARCHAR(32) NOT NULL , " +
                "email NVARCHAR(1024) NOT NULL, " +
                "password NVARCHAR(128) NOT NULL" +
                ")";
        try (Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void destroyTable() {
        String sql = "DROP TABLE users;";
        try (Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void seedTable(List<User> users) {
        for (User user : users) {
            insert(user);
        }
    }

    public void insert(User user) {
        String sql = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.executeUpdate();

            ResultSet result = statement.getGeneratedKeys();
            if (result.next()) {
                user.setId(result.getInt(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user) {
        String sql = "UPDATE users SET username=?, email=?, password=? WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPasswordHash());
            statement.setInt(4, user.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete(User user) {
        String sql = "DELETE FROM users WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, user.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User select(int id) {
        String sql = "SELECT id, username, email, password FROM users WHERE id=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
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

    public User select(String email) {
        String sql = "SELECT id, username, email, password FROM users WHERE email=?";
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
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

    public List<User> selectAll() {
        String sql = "SELECT id, username, email, password FROM users";
        List<User> users = new ArrayList<>();
        try (Statement statement = conn.createStatement()) {
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

    private User getUserFromResult(ResultSet result) throws SQLException {
        User user = new User();
        user.setId(result.getInt(1));
        user.setUsername(result.getString(2));
        user.setEmail(result.getString(3));
        user.setPasswordHash(result.getString(4));
        return user;
    }

    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email=?";
        return valueExists(email, sql);
    }

    public boolean usernameExists(String username) {
        String sql = "SELECT COUNT(*) FROM users WHERE username=?";
        return valueExists(username, sql);
    }

    private boolean valueExists(String value, String sql) {
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, value);
            ResultSet result = statement.executeQuery();
            return result.getInt(1) > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
