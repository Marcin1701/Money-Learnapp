package polsl.moneysandbox.api.form.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import polsl.moneysandbox.model.Form;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormResponse {

    private String name;

    private Integer questions;

    private Integer answerTime;

    private String creationDate;

    private Integer answers;

    private Boolean isPublic;

    public FormResponse(Form form) {
        this.name = form.getName();
        this.questions = form.getQuestionsIds().size();
        this.answerTime = form.getAnswerTime();
        this.creationDate = form.getCreationDate().toString();
        this.answers = form.getAnswers().size();
        this.isPublic = form.getIsPublic();
    }
}
