package ca.javateacher.studentdata.security;

import java.util.List;

public interface LoginDataRepository {
    boolean userExists(String login);
    void insertUser(String login, String password);
    void insertRole(String login, String role);
    void removeUser(String login);
    void removeRole(String login, String role);
    void removeRoles(String login);
    List<String> getAllLogins(String role);
    List<String> getAllRoles(String login);
    void updatePassword(String login, String password);
    boolean checkPassword(String login, String password);
    String getPassword(String login);
}
