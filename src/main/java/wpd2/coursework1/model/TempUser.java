package wpd2.coursework1.model;

import org.joda.time.DateTime;
import wpd2.coursework1.util.IoC;
import wpd2.coursework1.util.PasswordService;

import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static wpd2.coursework1.model.BaseModel.getConnection;

public class TempUser {
    private final PasswordService passwordService;
    public int id;
    private String token;
/*    private User user;*/
    private Date joined;
    private String username;
    private String email;
    private /*char[]*/ String password;
    private String passwordHash;

    public TempUser() {
        passwordService = (PasswordService)IoC.get().getInstance(PasswordService.class);
    }

    public String getPasswordHash() {
        return passwordHash;
    }
/*    private void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}*/
    public int getId() {
        return id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(String token) {this.token = token;}
    public Date getJoined() {return joined;}
    public void setJoined(Date joined) {this.joined = joined; }
    public String getUsername() {return username; }
    public void setUsername(String username) {this.username = username; }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {this.email = email; }
    public /*char[]*/ String getPassword() {
        return password;
    }
    public void setPassword(/*char[]*/String password) {this.password = password; }

/*   public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }*/

public TempUser(String token, String username, String email, String password){
    this.token = token;
    this.username = username;
    this.email = email;
    this.password = password;
    passwordService = (PasswordService)IoC.get().getInstance(PasswordService.class);
}

public void create() {
    passwordHash = passwordService.hash(password.toCharArray());
    joined = new Date();

    String sql = "INSERT INTO tempusers (token, username, email, password, joined) VALUES (?, ?, ?, ?, ?)";
    try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
        statement.setString(1, token);
        statement.setString(2, username);
        statement.setString(3, email);
        /*statement.setString(4, password);*/
        statement.setString(4, passwordHash);
        statement.setTimestamp(5, new Timestamp(joined.getTime()));
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
        if (password != null) {
            passwordHash = passwordService.hash(password);
        }

        String sql = "UPDATE tempusers SET token = ?, username=?, email=?, password=?, joined=? WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, token);
            statement.setString(2, username);
            statement.setString(3, email);
            /*statement.setString(4, password);*/
            statement.setString(4, passwordHash);
            statement.setTimestamp(5, new Timestamp(joined.getTime()));
            statement.setInt(6,id);
            return statement.executeUpdate() > 0;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static TempUser findByToken(String token) {
        String sql = "SELECT id, token, username, email, password ,joined FROM tempusers WHERE token=?";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setString(1, token);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return getTempUserFromResult(result);
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

 private static TempUser getTempUserFromResult(ResultSet result) throws SQLException {
     TempUser user = new TempUser();
     user.id = result.getInt(1);
     user.token = result.getString(2);
     user.username = result.getString(3);
     user.email = result.getString(4);
     user.passwordHash = result.getString(5);
     user.joined = result.getTimestamp(6);
     return user;
 }
    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS tempusers (" +
                "id INTEGER PRIMARY KEY AUTO_INCREMENT, " +
                "token NVARCHAR(1024) NOT NULL, " +
                "username NVARCHAR(32) NOT NULL, " +
                "email NVARCHAR(1024) NOT NULL, " +
                "password NVARCHAR(128) NOT NULL," +
                "joined TIMESTAMP NOT NULL" +
                ")";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.execute(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean delete() {

        String sql = "DELETE FROM tempusers WHERE id =?";

        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean delete(int min) {
    min = -min;
        /*String sql = "DELETE FROM tempusers WHERE id =(SELECT ? WHERE joined <= TIMESTAMPADD('MINUTE',min,CURRENT_TIMESTAMP))";*/
            Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MINUTE,min);
        Timestamp criteriaTimestamp = new Timestamp(cal.getTime().getTime());
        /*String sql = "DELETE FROM tempusers WHERE id =(SELECT ? WHERE joined <= ?)";*/
        String sql = "DELETE FROM tempusers WHERE id IN (SELECT id WHERE joined <= ?)";
        try (Connection conn = getConnection(); PreparedStatement statement = conn.prepareStatement(sql)) {
            /*statement.setInt(1, id);*/
            statement.setTimestamp(1, criteriaTimestamp);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void destroyTable() {
        String sql = "DROP TABLE IF EXISTS tempusers;";
        try (Connection conn = getConnection(); Statement statement = conn.createStatement()) {
            statement.executeUpdate(sql);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
