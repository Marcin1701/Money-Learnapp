package polsl.moneysandbox.api.form.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import polsl.moneysandbox.model.Form;
import polsl.moneysandbox.model.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormManageResponse {

    private String formId;

    private String login;

    private String firstName;

    private String lastName;

    private String name;

    private Integer answerTime;

    private String creationDate;

    private Integer numberOfQuestions;

    private Integer numberOfAnswers;

    private Boolean isPublic;

    public FormManageResponse(Form form, User creator) {
        this.formId = form.getId();
        this.login = creator.getLogin();
        this.firstName = creator.getFirstName();
        this.lastName = creator.getLastName();
        this.name = form.getName();
        this.answerTime = form.getAnswerTime();
        this.creationDate = form.getCreationDate().toString();
        this.numberOfQuestions = form.getQuestionsIds().size();
        this.numberOfAnswers = form.getAnswers().size();
        this.isPublic = form.getIsPublic();
    }

    public FormManageResponse(Form form) {
        this.formId = form.getId();
        this.name = form.getName();
        this.answerTime = form.getAnswerTime();
        this.creationDate = form.getCreationDate().toString();
        this.numberOfQuestions = form.getQuestionsIds().size();
        this.numberOfAnswers = form.getAnswers().size();
        this.isPublic = form.getIsPublic();
    }
}
