package polsl.moneysandbox.api.student;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polsl.moneysandbox.api.student.service.StudentService;
import polsl.moneysandbox.model.Student;

import java.util.List;

@RestController
@RequestMapping("api/student")
@AllArgsConstructor
public class StudentController {

    private StudentService studentService;

    @GetMapping
    public List<Student> getStudents() {
        return this.studentService.getAllStudents();
    }

    @PostMapping
    public void addStudent() {
        this.studentService.addStudent();
    }

}
