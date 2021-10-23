package polsl.moneysandbox.api.student.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import polsl.moneysandbox.model.Student;
import polsl.moneysandbox.repository.StudentRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;

    public void addStudent() {
        Student student = Student.builder().username("Marcin").creationDate(LocalDateTime.now()).build();
        this.studentRepository.save(student);
    }

    public List<Student> getAllStudents() {
        return this.studentRepository.findAll();
    }
}
