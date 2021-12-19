package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.FormToVerify;

import java.util.List;
import java.util.Optional;

public interface FormToVerifyRepository extends MongoRepository<FormToVerify, String> {

    Optional<FormToVerify> findFormToVerifyBySheetId(String sheetId);

    List<FormToVerify> findAllByIsPendingVerificationIsTrue();

    void deleteBySheetId(String sheetId);
}
