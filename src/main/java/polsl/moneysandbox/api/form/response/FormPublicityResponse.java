package polsl.moneysandbox.api.form.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import polsl.moneysandbox.model.FormToVerify;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormPublicityResponse {

    private Boolean pendingPublicity;

    private String requestedVerificationDate;

    public FormPublicityResponse(FormToVerify formToVerify) {
        this.pendingPublicity = formToVerify.getIsPendingVerification();
        this.requestedVerificationDate = formToVerify.getRequestedVerificationDate().toString();
    }
}
