package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polsl.moneysandbox.model.Answer;

import java.util.List;

@Repository
public interface AnswerRepository extends MongoRepository<Answer, String> {

    List<Answer> findAllByUserId(String userId);

    void deleteAllByUserId(String id);
}
