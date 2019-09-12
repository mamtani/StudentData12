package ca.javateacher.studentdata.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginDataServiceImpl implements LoginDataService {

    private LoginDataRepository loginDataRepository;

    @Autowired
    public LoginDataServiceImpl(LoginDataRepository loginDataRepository) {
        this.loginDataRepository = loginDataRepository;
    }

    @Override
    public boolean userExists(String login) {
        return loginDataRepository.userExists(login);
    }

    @Override
    public void insertUser(String login, String password) {
        loginDataRepository.insertUser(login, password);
    }

    @Override
    public void insertRole(String login, String role) {
        loginDataRepository.insertRole(login, role);
    }

    @Override
    public void removeUser(String login) {
        loginDataRepository.removeUser(login);
    }

    @Override
    public void removeRole(String login, String role) {
        loginDataRepository.removeRole(login, role);
    }

    @Override
    public void removeRoles(String login) {
        loginDataRepository.removeRoles(login);
    }

    @Override
    public List<String> getAllLogins(String role) {
        return loginDataRepository.getAllLogins(role);
    }

    @Override
    public List<String> getAllRoles(String login) {
        return loginDataRepository.getAllRoles(login);
    }

    @Override
    public void updatePassword(String login, String password) {
        loginDataRepository.updatePassword(login, password);
    }

    @Override
    public boolean checkPassword(String login, String password) {
        return loginDataRepository.checkPassword(login, password);
    }

    @Override
    public String getPassword(String login) {
        return loginDataRepository.getPassword(login);
    }
}
