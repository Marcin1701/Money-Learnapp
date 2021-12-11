package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.Answer;

public interface AnswerRepository extends MongoRepository<Answer, String> {
}
