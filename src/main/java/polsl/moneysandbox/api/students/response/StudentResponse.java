package polsl.moneysandbox.api.students.response;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class StudentResponse {

    private String id;

    private String firstName;

    private String lastName;

    private String className;

    private String email;

    private String creationDate;

    private Boolean isCreatorAllowed;

    private Boolean isTemporaryPasswordActive;

    private String login;
}
