package polsl.moneysandbox.api.entry.service.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class NewAccountRequest {

    private String firstName;

    private String lastName;

    private String login;

    private String email;

    private String password;

    private String accountType;
}
