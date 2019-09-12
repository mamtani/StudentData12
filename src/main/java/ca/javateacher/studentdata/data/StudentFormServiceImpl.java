package ca.javateacher.studentdata.data;

import ca.javateacher.studentdata.model.Student;
import ca.javateacher.studentdata.model.StudentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class StudentFormServiceImpl implements StudentFormService{

    private StudentRepository studentRepository;

    @Autowired
    StudentFormServiceImpl(StudentRepository studentRepository){
        this.studentRepository = studentRepository;
    }

    private static void copyFormToStudent(StudentForm form, Student student){
        student.setId(form.getId());
        student.setFirstName(form.getFirstName());
        student.setLastName(form.getLastName());
        student.setProgramName(form.getProgramName());
        student.setProgramYear(Integer.parseInt(form.getProgramYear()));
        student.setProgramCoop(form.getProgramCoop().equals("yes"));
        student.setProgramInternship(form.getProgramInternship().equals("yes"));
    }

    private static void copyStudentToForm(Student student, StudentForm form){
        form.setId(student.getId());
        form.setFirstName(student.getFirstName());
        form.setLastName(student.getLastName());
        form.setProgramName(student.getProgramName());
        form.setProgramYear(student.getProgramYear().toString());
        form.setProgramCoop(student.getProgramCoop()?"yes":"no");
        form.setProgramInternship(student.getProgramInternship()?"yes":"no");
    }

    @Override
    public void insertStudentForm(StudentForm form) {
        Student student = new Student();
        copyFormToStudent(form, student);
        student = studentRepository.save(student);
        studentRepository.flush();
        form.setId(student.getId());
    }

    @Override
    public List<StudentForm> getAllStudentForms() {
        List<StudentForm> formList = new ArrayList<>();
        List<Student> studentList = studentRepository.findAll();
        for(Student student: studentList){
            StudentForm form = new StudentForm();
            copyStudentToForm(student, form);
            formList.add(form);
        }
        return formList;
    }

    @Override
    public void deleteAllStudentForms() {
        studentRepository.deleteAll();
    }

    @Override
    public void deleteStudentForm(int id) {
        studentRepository.deleteById(id);
    }

    @Override
    public StudentForm getStudentForm(int id) {
        Optional<Student> result = studentRepository.findById(id);
        if(result.isPresent()){
            StudentForm form = new StudentForm();
            Student student = result.get();
            copyStudentToForm(student, form);
            return form;
        }
        return null;
    }

    @Override
    public void updateStudentForm(StudentForm form) {
        Optional<Student> result = studentRepository.findById(form.getId());
        if(result.isPresent()){
            Student student = result.get();
            copyFormToStudent(form, student);
            studentRepository.save(student);
            studentRepository.flush();
        }
    }
}
