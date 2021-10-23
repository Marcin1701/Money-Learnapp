package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.Student;

public interface StudentRepository extends MongoRepository<Student, String> {
}
