package wpd2.coursework1.model;

import wpd2.coursework1.util.PasswordAuth;
import wpd2.coursework1.util.ValidationError;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class User extends BaseModel {
    private int id;
    private String username;
    private String email;
    private char[] password;
    private String passwordHash;

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

    private boolean emailExists(String email)  {
        try (Connection conn = getConnection()) {
            return new UserRepository(conn).emailExists(email);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean usernameExists(String username) {
        try (Connection conn = getConnection()) {
            return new UserRepository(conn).usernameExists(username);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void create() {
        PasswordAuth auth = new PasswordAuth();
        passwordHash = auth.hash(password);
        try (Connection conn = getConnection()) {
            new UserRepository(conn).insert(this);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void update() {
        try (Connection conn = getConnection()) {
            new UserRepository(conn).update(this);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void delete() {
        try (Connection conn = getConnection()) {
            new UserRepository(conn).delete(this);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User find(int id) {
        try (Connection conn = getConnection()) {
            return new UserRepository(conn).select(id);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static User find(String email) {
        try (Connection conn = getConnection()) {
            return new UserRepository(conn).select(email);
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static List<User> findAll() {
        try (Connection conn = getConnection()) {
            return new UserRepository(conn).selectAll();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
