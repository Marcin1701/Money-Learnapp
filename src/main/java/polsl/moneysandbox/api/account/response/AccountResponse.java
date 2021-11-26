package polsl.moneysandbox.api.account.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import polsl.moneysandbox.model.Account;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountResponse {

    private String login;

    private String firstName;

    private String lastName;

    private String accountType;

    private String email;

    private String creationDate;

    public AccountResponse(Account account) {
        this.login = account.getLogin();
        this.firstName = account.getFirstName();
        this.lastName = account.getLastName();
        this.accountType = account.getAccountType();
        this.email = account.getEmail();
        this.creationDate = account.getCreationDate().toString();
    }
}
