package polsl.moneysandbox.api.students.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import polsl.moneysandbox.model.GeneratedPassword;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GeneratedPasswordResponse {

    private String generatedPassword;

    public GeneratedPasswordResponse(GeneratedPassword generatedPassword) {
        this.generatedPassword = generatedPassword.getGeneratedPassword();
    }
}
