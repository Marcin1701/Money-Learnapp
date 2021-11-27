package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.GeneratedPassword;

public interface GeneratedPasswordRepository extends MongoRepository<GeneratedPassword, String> {
}
