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
public class HomeFormResponse {
    private String id;

    private String firstName;

    private String lastName;

    private String login;

    private String email;

    private String name;

    private Integer questions;

    private Integer answerTime;

    private String creationDate;

    private Integer answers;

    private Integer difficulty;

    private Boolean isPublic;

    public HomeFormResponse(Form form, User creator) {
        this.id = form.getId();
        this.firstName = creator.getFirstName();
        this.lastName = creator.getLastName();
        if (creator.getEmail() != null) {
            this.email = creator.getEmail();
        }
        if (creator.getLogin() != null) {
            this.login = creator.getLogin();
        }
        this.name = form.getName();
        this.questions = form.getQuestionsIds().size();
        this.answerTime = form.getAnswerTime();
        this.creationDate = form.getCreationDate().toString();
        this.answers = form.getAnswers().size();
        this.isPublic = form.getIsPublic();
        this.difficulty = form.getDifficulty();
    }
}
