package wpd2.coursework1.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static wpd2.coursework1.model.BaseModel.getConnection;

public class User extends BaseModel {
    private int userid;
    private String email;
    private String password;
    private Date joined;

    public int getUserid() {
        return userid;
    }

    public void setId(int userid) {
        this.userid = userid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String email) {
        this.password = password;
    }

    public Date getJoined() {
        return joined;
    }

    public void setJoined(Date joined) {
        this.joined = joined;
    }

    private User() {
        // Private
    }

    @Override
    protected void validate() {
        // This is called by isValid() in the parent class to check if the model is valid.
        // If there are any validation errors after this method has been run the model will
        // be considered to be invalid.
        if (email == null || email.trim().length() == 0) {
            addValidationError("email", "Email is required");
        }

        if (password == null || password.trim().length() == 0) {
            addValidationError("password", "Password is required");
        }


        if (joined == null || joined.getTime() == 0) {
            addValidationError("joined", "Joined is required");
        }
    }

    private static User getUserFromResult(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(1));
        user.setEmail(resultSet.getString(2));
        user.setPassword(resultSet.getString(3));
        user.setJoined(resultSet.getTimestamp(4));
        return user;
    }

    public void create() {
        String sql = "INSERT INTO users (email, password, joined) VALUES (?, ?, ?)";
        try (Connection conn = getConnection()) {
            try (PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, getEmail());
                statement.setString(1, getPassword());
                statement.setTimestamp(2, new Timestamp(getJoined().getTime()));
                statement.executeUpdate();

                // Get userid
                ResultSet result = statement.getGeneratedKeys();
                if (result.next()) {
                    int userid = result.getInt(1);
                    setId(userid); // Set for model.
                }
            }
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        String sql = "UPDATE users SET email=? , password=? WHERE userid=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, getEmail());
            statement.setString(1, getPassword());
            statement.setInt(2, getUserid());
            statement.executeUpdate();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static User create(String email, String password) {
        User user = empty();
        user.setEmail(email);
        user.setPassword(password);
        user.setJoined(new Date());
        return user;
    }

    public static User empty() {
        return new User();
    }

    public static List<User> loadAll() {
        String sql = "SELECT * FROM users ORDER BY joined DESC";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            // Query for users.
            ResultSet result = statement.executeQuery(sql);

            // Create user list.
            List<User> users = new ArrayList<>();
            while (result.next()) {
                users.add(getUserFromResult(result));
            }
            return users;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static User find(int userid) {
        String sql = "SELECT * FROM users WHERE userid=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            // Query for specific user.
            statement.setInt(1, userid);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getUserFromResult(result);
            }
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static User findUserByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            // Query for specific user.
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getUserFromResult(result);
            }
            return null;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
