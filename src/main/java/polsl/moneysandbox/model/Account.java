package polsl.moneysandbox.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("student")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    @Id
    private String id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String accountType;

    private String email;

    private LocalDateTime creationDate;
}
