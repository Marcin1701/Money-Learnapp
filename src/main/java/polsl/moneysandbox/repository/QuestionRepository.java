package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.Question;

import java.util.List;

public interface QuestionRepository extends MongoRepository<Question<?>, String> {

    List<Question<?>> findAllByCreatorIdAndType(String creatorId, String type);
}
