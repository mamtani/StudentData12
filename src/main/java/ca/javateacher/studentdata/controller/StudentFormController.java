package ca.javateacher.studentdata.controller;

import ca.javateacher.studentdata.data.StudentFormService;
import ca.javateacher.studentdata.model.StudentForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentFormController {

    private final Logger logger = LoggerFactory.getLogger(StudentFormController.class);

    private static final String[] programs = {
            "--- Select Program ---",
            "Computer Programmer", "Systems Technology",
            "Engineering Technician", "Systems Technician"};

    private StudentFormService studentFormService;

    @Autowired
    public StudentFormController(StudentFormService studentFormService){
        this.studentFormService = studentFormService;
    }

    @RequestMapping(value={"/","/Index.do"})
    public String index(){
        logger.trace("index() is called");
        return "Students";
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/AddStudent.do")
    public ModelAndView addStudent(){
        logger.trace("addStudent() is called");
        ModelAndView modelAndView =
                new ModelAndView("AddStudent",
                                    "form", new StudentForm());
        modelAndView.addObject("programs", programs);
        return modelAndView;
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/InsertStudent.do")
    public String insertStudent(
            @Validated @ModelAttribute("form") StudentForm form,
            BindingResult bindingResult,
            Model model){
        logger.trace("insertStudent() is called");
        // correcting the checkbox input values
        logger.debug("received programCoop=" + form.getProgramCoop());
        logger.debug("received programInternship=" + form.getProgramInternship());
        form.setProgramCoop((form.getProgramCoop() == null) ? "no" : "yes");
        form.setProgramInternship((form.getProgramInternship() == null) ? "no" : "yes");
        // checking for the input validation errors
        if (!bindingResult.hasErrors()) {
            logger.trace("the user inputs are correct");
            studentFormService.insertStudentForm(form);
            return "redirect:ConfirmInsert.do?id=" + form.getId();
        } else {
            logger.trace("input validation errors");
            model.addAttribute("form", form);
            model.addAttribute("programs", programs);
            return "AddStudent";
        }
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/ConfirmInsert.do")
    public String confirmInsert(@RequestParam(name = "id") String strId, Model model){
        logger.trace("confirmInsert() is called");
        if (strId == null || strId.isEmpty()) {
            logger.trace("no id in the request");
            return "DataNotFound";
        } else {
            try {
                int id = Integer.parseInt(strId);
                logger.trace("looking for the data in the database");
                StudentForm form = studentFormService.getStudentForm(id);
                if (form == null) {
                    logger.trace("no data for this id=" + id);
                    return "DataNotFound";
                } else {
                    logger.trace("showing the data");
                    model.addAttribute("student", form);
                    return "ConfirmInsert";
                }
            } catch (NumberFormatException e) {
                logger.trace("the id in not an integer");
                return "DataNotFound";
            }
        }
    }

    @RequestMapping("/ListStudents.do")
    public ModelAndView listStudents() {
        logger.trace("listStudents() is called");
        List<StudentForm> list = studentFormService.getAllStudentForms();
        return new ModelAndView("ListStudents",
                                "students", list);
    }

    @Secured("ROLE_ADMIN")
    @RequestMapping("/DeleteAll.do")
    public String deleteAll(){
        logger.trace("deleteAll() is called");
        studentFormService.deleteAllStudentForms();
        return "redirect:ListStudents.do";
    }

    @RequestMapping("StudentDetails.do")
    public String studentDetails(@RequestParam String id, Model model){
        logger.trace("studentDetails() is called");
        try {
            StudentForm form = studentFormService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("student", form);
                return "StudentDetails"; // show the student data in the form to edit
            } else {
                logger.trace("no data for this id=" + id);
                return "DataNotFound";
            }
        } catch (NumberFormatException e) {
            logger.trace("the id is missing or not an integer");
            return "DataNotFound";
        }
    }

    // a user clicks "Delete" link (in the table) to "DeleteStudent"
    @Secured("ROLE_ADMIN")
    @RequestMapping("/DeleteStudent.do")
    public String deleteStudent(@RequestParam String id, Model model) {
        logger.trace("deleteStudent() is called");
        try {
            StudentForm form = studentFormService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("student", form);
                return "DeleteStudent"; // ask "Do you really want to remove?"
            } else {
                return "redirect:ListStudents.do";
            }
        } catch (NumberFormatException e) {
            return "redirect:ListStudents.do";
        }
    }

    // a user clicks "Remove Record" button in "DeleteStudent" page,
    // the form submits the data to "RemoveStudent"
    @Secured("ROLE_ADMIN")
    @RequestMapping("/RemoveStudent.do")
    public String removeStudent(@RequestParam String id) {
        logger.trace("removeStudent() is called");
        try {
            studentFormService.deleteStudentForm(Integer.parseInt(id));
        } catch (NumberFormatException e) {
            logger.trace("the id is missing or not an integer");
        }
        return "redirect:ListStudents.do";
    }

    // a user clicks "Edit" link (in the table) to "EditStudent"
    @Secured("ROLE_ADMIN")
    @RequestMapping("/EditStudent.do")
    public String editStudent(@RequestParam String id, Model model) {
        logger.trace("editStudent() is called");
        try {
            StudentForm form = studentFormService.getStudentForm(Integer.parseInt(id));
            if (form != null) {
                model.addAttribute("form", form);
                model.addAttribute("programs", programs);
                return "EditStudent";
            } else {
                logger.trace("no data for this id=" + id);
                return "redirect:ListStudents.do";
            }
        } catch (NumberFormatException e) {
            logger.trace("the id is missing or not an integer");
            return "redirect:ListStudents.do";
        }
    }

    // the form submits the data to "UpdateStudent"
    @Secured("ROLE_ADMIN")
    @RequestMapping("/UpdateStudent.do")
    public String updateStudent(
            @Validated @ModelAttribute("form") StudentForm form,
            BindingResult bindingResult,
            Model model) {
        logger.trace("updateStudent() is called");
        // correcting the checkbox input values
        form.setProgramCoop((form.getProgramCoop() == null) ? "no" : "yes");
        form.setProgramInternship((form.getProgramInternship() == null) ? "no" : "yes");
        // checking for the input validation errors
        if (!bindingResult.hasErrors()) {
            logger.trace("the user inputs are correct");
            studentFormService.updateStudentForm(form);
            return "redirect:StudentDetails.do?id=" + form.getId();
        } else {
            logger.trace("input validation errors");
            model.addAttribute("form", form);
            model.addAttribute("programs", programs);
            return "EditStudent";
        }
    }
}
