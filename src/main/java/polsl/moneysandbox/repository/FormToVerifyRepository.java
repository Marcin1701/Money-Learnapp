package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polsl.moneysandbox.model.FormToVerify;

import java.util.List;
import java.util.Optional;

@Repository
public interface FormToVerifyRepository extends MongoRepository<FormToVerify, String> {

    Optional<FormToVerify> findFormToVerifyBySheetId(String sheetId);

    List<FormToVerify> findAllByIsPendingVerificationIsTrue();
}
