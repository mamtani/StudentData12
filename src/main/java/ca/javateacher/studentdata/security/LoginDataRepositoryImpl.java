package ca.javateacher.studentdata.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class LoginDataRepositoryImpl implements LoginDataRepository {

    private NamedParameterJdbcTemplate template;
    private PasswordEncoder encoder;

    @Autowired
    public LoginDataRepositoryImpl(DataSource dataSource,
                                   PasswordEncoder encoder) {
        this.template = new NamedParameterJdbcTemplate(dataSource);
        this.encoder = encoder;
    }

    @Override
    public boolean userExists(String login) {
        String query = "SELECT COUNT(user_login) FROM users "
                + "WHERE user_login = :user_login";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        int count =
                template.queryForObject(query, params, Integer.class);
        return count != 0;
    }

    @Override
    public void insertUser(String login, String password) {
        String update = "INSERT INTO users "
                + "(user_login, user_password) VALUES (:user_login, :user_password)";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        params.put("user_password", encoder.encode(password));
        template.update(update, params);
    }

    @Override
    public void insertRole(String login, String role) {
        String update = "INSERT INTO roles "
                + "(user_login, user_role) VALUES (:user_login, :user_role)";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        params.put("user_role", role);
        template.update(update, params);
    }

    @Override
    public void removeUser(String login) {
        String update = "DELETE FROM users WHERE user_login = :user_login";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        template.update(update, params);
    }

    @Override
    public void removeRole(String login, String role) {
        String update = "DELETE FROM roles "
                + "WHERE user_login = :user_login AND user_role = :user_role";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        params.put("user_role", role);
        template.update(update, params);
    }

    @Override
    public void removeRoles(String login) {
        String update = "DELETE FROM roles WHERE user_login = :user_login";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        template.update(update, params);
    }

    @Override
    public List<String> getAllLogins(String role) {
        String query = "SELECT user_login FROM roles WHERE user_role = :user_role";
        Map<String, Object> params = new HashMap<>();
        params.put("user_role", role);
        return template.queryForList(query, params, String.class);
    }

    @Override
    public List<String> getAllRoles(String login) {
        String query = "SELECT user_role FROM roles WHERE user_login = :user_login";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        return template.queryForList(query, params, String.class);
    }

    @Override
    public void updatePassword(String login, String password) {
        String update = "UPDATE users SET "
                + "user_password = :user_password WHERE user_login = :user_login";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        params.put("user_password", encoder.encode(password));
        template.update(update, params);
    }

    @Override
    public boolean checkPassword(String login, String password) {
        String stored_password = getPassword(login);
        return encoder.matches(password, stored_password);
    }

    @Override
    public String getPassword(String login) {
        String query = "SELECT user_password FROM users "
                + "WHERE user_login = :user_login";
        Map<String, Object> params = new HashMap<>();
        params.put("user_login", login);
        return template.queryForObject(query, params, String.class);
    }

}
