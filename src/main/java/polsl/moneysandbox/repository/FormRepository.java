package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.Form;

import java.util.List;

public interface FormRepository extends MongoRepository<Form, String> {

    List<Form> getAllByCreatorId(String id);

    List<Form> getAllByIsPublic(Boolean isPublic);
}
