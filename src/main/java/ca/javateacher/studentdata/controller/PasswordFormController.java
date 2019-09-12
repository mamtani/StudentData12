package ca.javateacher.studentdata.controller;

import ca.javateacher.studentdata.security.LoginDataService;
import ca.javateacher.studentdata.passwords.PasswordChangeForm;
import ca.javateacher.studentdata.passwords.PasswordChangeValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/password/*")
public class PasswordFormController {

    private final Logger logger = LoggerFactory.getLogger(PasswordFormController.class);

    private LoginDataService loginDataService;

    @Autowired
    public PasswordFormController(LoginDataService loginDataService) {
        this.loginDataService = loginDataService;
    }

    @InitBinder("pcform")
    protected void initBinder(WebDataBinder binder) {
        binder.setValidator(new PasswordChangeValidator());
    }

    // a user clicks "Change password" link
    @RequestMapping("/ChangePassword.do")
    public String changePassword(Model model) {
        logger.trace("changePassword() is called");
        model.addAttribute("pcform", new PasswordChangeForm());
        return "ChangePassword";
    }

    // a user clicks "Change Password" button in "ChangePassword.html",
    // the form submits data to "/UpdatePassword.do"
    @RequestMapping("/UpdatePassword.do")
    public String updatePassword(
            @Validated @ModelAttribute("pcform") PasswordChangeForm pcform,
            BindingResult result) {
        logger.trace("updatePassword() is called");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String login = authentication.getName();

        if (!result.hasFieldErrors("currentPassword")) {
            if (!loginDataService.checkPassword(login, pcform.getCurrentPassword().trim())) {
                result.rejectValue("currentPassword", "currentPassword.wrong");
                logger.trace("the entered current password is wrong");
            }
        }

        if (result.hasErrors()) {
            logger.trace("input validation errors");
            return "ChangePassword";
        } else {
            loginDataService.updatePassword(login, pcform.getNewPassword1().trim());
            logger.trace("the password is updated");
            return "PasswordChanged";
        }
    }

}
