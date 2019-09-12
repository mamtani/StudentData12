package ca.javateacher.studentdata.controller;

import ca.javateacher.studentdata.security.LoginDataService;
import ca.javateacher.studentdata.passwords.PasswordGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/users")
public class UserFormController {

    private final Logger logger = LoggerFactory.getLogger(UserFormController.class);

    private LoginDataService loginDataService;
    private PasswordGenerator passwordGenerator;

    @Autowired
    public UserFormController(LoginDataService loginDataService, PasswordGenerator passwordGenerator) {
        this.loginDataService = loginDataService;
        this.passwordGenerator = passwordGenerator;
    }

    @RequestMapping(value={"/","/Index.do"})
    public String index(){
        logger.trace("index() is called");
        return "Users";
    }

    // an admin clicks "List Users" link in "Users.html",
    @RequestMapping("/ListUsers.do")
    public String listUsers(Model model) {
        logger.trace("listUsers() is called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("you", authentication.getName());
        model.addAttribute("users",
                loginDataService.getAllLogins("ROLE_USER"));
        model.addAttribute("admins",
                loginDataService.getAllLogins("ROLE_ADMIN"));
        return "ListUsers";
    }

    // an admin clicks "Add User" link in "ListUsers.html",
    @RequestMapping("/AddUser.do")
    public String addUser(Model model) {
        logger.trace("addUser() is called");
        String message = "Enter login and password for the new user account.";
        model.addAttribute("message", message);
        model.addAttribute("random", passwordGenerator.randomPassword());
        return "AddUser";
    }

    // an admin clicks on "Add User" button in "AddUser.html",
    // the form submits the data to "InsertUser"
    @RequestMapping("/InsertUser.do")
    public String insertUser(HttpServletRequest request) {
        logger.trace("insertUser() is called");
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        String role = request.getParameter("role");
        String message;
        if (login == null || login.trim().isEmpty()) {
            logger.trace("missing login input");
            message = "The account login cannot be left empty";
        } else if (loginDataService.userExists(login)) {
            logger.trace("the login is already in use");
            message = "The login is already in use.";
        } else if (password == null || password.trim().isEmpty()) {
            logger.trace("missing password input");
            message = "The account password cannot be left empty.";
        } else {
            login = login.trim();
            password = password.trim();
            loginDataService.insertUser(login, password);
            logger.trace("added user " + login);
            request.setAttribute("login",login);
            if(role != null && role.equals("admin")){
                loginDataService.insertRole(login, "ROLE_ADMIN");
                logger.trace("added ROLE_ADMIN to " + login);
                request.setAttribute("role","admin");
            }else{
                loginDataService.insertRole(login, "ROLE_USER");
                logger.trace("added ROLE_USER to " + login);
                request.setAttribute("role","user");
            }
            return "UserAdded";
        }
        request.setAttribute("message", message);
        request.setAttribute("random", passwordGenerator.randomPassword());
        return "AddUser";
    }

    // an admin clicks "Delete" link in "ListUsers.html",
    @RequestMapping("/DeleteUser.do")
    public String deleteUser() {
        logger.trace("deleteUser() is called");
        return "DeleteUser";
    }

    // an admin clicks on "Delete User" button in "DeleteUser.jsp",
    // the form submits the data to "RemoveUser"
    @RequestMapping("/RemoveUser.do")
    public String removeUser(@RequestParam String login) {
        loginDataService.removeRoles(login);
        loginDataService.removeUser(login);
        return "redirect:ListUsers.do";
    }
}
