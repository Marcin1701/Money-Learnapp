package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polsl.moneysandbox.model.Form;

import java.util.List;

@Repository
public interface FormRepository extends MongoRepository<Form, String> {

    List<Form> getAllByCreatorId(String id);

    List<Form> getAllByIsPublic(Boolean isPublic);

    List<Form> findAllByIdIn(List<String> sheetIds);
}
