package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import polsl.moneysandbox.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findAccountByEmailOrLogin(String email, String login);

    List<User> findAllByRole(String role);
}
