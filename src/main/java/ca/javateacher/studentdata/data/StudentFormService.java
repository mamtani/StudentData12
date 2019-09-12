package ca.javateacher.studentdata.data;

import ca.javateacher.studentdata.model.StudentForm;

import java.util.List;

public interface StudentFormService {

    void insertStudentForm(StudentForm form);

    List<StudentForm> getAllStudentForms();

    void deleteAllStudentForms();

    void deleteStudentForm(int id);

    StudentForm getStudentForm(int id);

    void updateStudentForm(StudentForm form);
}
