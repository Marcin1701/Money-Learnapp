package polsl.moneysandbox.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import polsl.moneysandbox.model.User;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findAccountByEmailOrLogin(String email, String login);
}
