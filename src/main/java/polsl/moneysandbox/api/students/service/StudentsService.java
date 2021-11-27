package polsl.moneysandbox.api.students.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.students.Response.GeneratedPasswordResponse;
import polsl.moneysandbox.api.students.Response.StudentResponse;
import polsl.moneysandbox.api.students.request.StudentRequest;
import polsl.moneysandbox.model.Account;
import polsl.moneysandbox.model.GeneratedPassword;
import polsl.moneysandbox.model.Student;
import polsl.moneysandbox.repository.AccountRepository;
import polsl.moneysandbox.repository.GeneratedPasswordRepository;
import polsl.moneysandbox.utility.PasswordGenerator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Service
public class StudentsService {

    private final JwtTokenUtility jwtTokenUtility;

    private final AccountRepository accountRepository;

    private final GeneratedPasswordRepository generatedPasswordRepository;

    public GeneratedPasswordResponse addNewStudent(StudentRequest studentRequest, String token) {
        Account teacher = accountRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (accountRepository.findAccountByEmailOrLogin(studentRequest.getLogin(), studentRequest.getLogin()).isEmpty()) {
            GeneratedPassword generatedPassword = GeneratedPassword.builder().generatedPassword(PasswordGenerator.generatePassword(10)).build();
            Student student = Student.builder()
                    .accountType(studentRequest.getAccountType())
                    .creationDate(LocalDateTime.now())
                    .firstName(studentRequest.getFirstName())
                    .lastName(studentRequest.getLastName())
                    .password(generatedPassword.getGeneratedPassword())
                    .login(studentRequest.getLogin())
                    .isCreatorAllowed(studentRequest.getIsCreatorAllowed())
                    .isTemporaryPasswordActive(studentRequest.getIsTemporaryPasswordActive())
                    .build();
            if (studentRequest.getEmail() != null) {
                student.setEmail(studentRequest.getEmail());
            }
            if (studentRequest.getClassName() != null) {
                student.setClassName(student.getClassName());
            }
            List<Student> students = teacher.getStudents();
            if (students == null) {
                students = new ArrayList<>();
            }
            students.add(student);
            teacher.setStudents(students);
            generatedPasswordRepository.save(generatedPassword);
            accountRepository.save(student);
            accountRepository.save(teacher);
            return new GeneratedPasswordResponse(generatedPassword);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User exists");
        }
    }

    public List<StudentResponse> getAllTeacherStudents(String token) {
        Account teacher = accountRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return teacher.getStudents().stream().map(student -> {
            var studentBuilt = StudentResponse.builder()
                    .firstName(student.getFirstName())
                    .lastName(student.getLastName())
                    .creationDate(student.getCreationDate().toString())
                    .isCreatorAllowed(student.getIsCreatorAllowed())
                    .isTemporaryPasswordActive(student.getIsTemporaryPasswordActive())
                    .login(student.getLogin())
                    .build();
            if (student.getEmail() != null) {
                studentBuilt.setEmail(student.getEmail());
            }
            if (student.getClassName() != null) {
                studentBuilt.setClassName(student.getClassName());
            }
            return studentBuilt;
        }).toList();
    }
}
