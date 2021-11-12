package polsl.moneysandbox.api.entry.service.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class LoginRequest {

    private String login;

    private String password;
}
