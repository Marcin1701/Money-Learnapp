package polsl.moneysandbox.api.students.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class StudentRequest {

    private String firstName;

    private String lastName;

    private String login;

    private String email = null;

    private String className = null;

    private String accountType;

    private Boolean isCreatorAllowed;

    private Boolean isTemporaryPasswordActive;
}
