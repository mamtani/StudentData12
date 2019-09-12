package ca.javateacher.studentdata.data;

import ca.javateacher.studentdata.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Integer> {
}
