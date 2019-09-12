package ca.javateacher.studentdata.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

public class StudentForm implements Serializable {

    private int id = 0;

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "[A-Za-z]*")
    private String firstName = "";

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "[A-Za-z]*")
    private String lastName = "";

    @NotBlank
    @Pattern(regexp = "(Computer Programmer|Systems Technology|Engineering Technician|Systems Technician)?")
    private String programName = "";

    @NotNull
    @Pattern(regexp = "[1-2]")
    private String programYear = "";

    private String programCoop = "";

    private String programInternship = "";

    public StudentForm() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProgramName() {
        return programName;
    }

    public void setProgramName(String program) {
        this.programName = program;
    }

    public String getProgramYear() {
        return programYear;
    }

    public void setProgramYear(String year) {
        this.programYear = year;
    }

    public String getProgramCoop() {
        return programCoop;
    }

    public void setProgramCoop(String coop) {
        this.programCoop = coop;
    }

    public String getProgramInternship() {
        return programInternship;
    }

    public void setProgramInternship(String internship) {
        this.programInternship = internship;
    }


}

