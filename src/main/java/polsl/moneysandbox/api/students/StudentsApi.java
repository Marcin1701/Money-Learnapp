package polsl.moneysandbox.api.students;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.students.Response.GeneratedPasswordResponse;
import polsl.moneysandbox.api.students.Response.StudentResponse;
import polsl.moneysandbox.api.students.request.StudentRequest;
import polsl.moneysandbox.api.students.service.StudentsService;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@AllArgsConstructor
public class StudentsApi {

    private final StudentsService studentsService;

    @GetMapping("/all")
    public List<StudentResponse> getAllTeacherStudents(@RequestHeader("Authorization") String token) {
        return this.studentsService.getAllTeacherStudents(token);
    }

    @PostMapping("/new")
    public GeneratedPasswordResponse addNewStudent(@RequestBody StudentRequest studentRequest,
                                                   @RequestHeader("Authorization") String token
    ) {
        return this.studentsService.addNewStudent(studentRequest, token);
    }
}
