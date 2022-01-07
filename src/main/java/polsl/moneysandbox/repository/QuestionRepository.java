package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polsl.moneysandbox.model.Question;

import java.util.List;

@Repository
public interface QuestionRepository extends MongoRepository<Question<?>, String> {

    List<Question<?>> findAllByCreatorIdAndType(String creatorId, String type);

    List<Question<?>> findAllByCreatorId(String creatorId);

    List<Question<?>> findAllByIdIn(List<String> ids);
}
