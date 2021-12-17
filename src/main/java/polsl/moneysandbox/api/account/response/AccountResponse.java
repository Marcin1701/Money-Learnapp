package polsl.moneysandbox.api.account.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import polsl.moneysandbox.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountResponse {

    private String id;

    private String login;

    private String firstName;

    private String lastName;

    private String email;

    private String creationDate;

    public AccountResponse(User user) {
        this.id = user.getId();
        this.login = user.getLogin();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.creationDate = user.getCreationDate().toString();
    }
}
