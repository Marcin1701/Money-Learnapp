package polsl.moneysandbox.api.question.service.response;

import lombok.*;
import polsl.moneysandbox.model.Question;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionResponse<T> {

    private String type;

    private String creationDate;

    private T question;

    public QuestionResponse(Question<T> question) {
        this.type = question.getType();
        this.creationDate = question.getCreationDate();
        this.question = question.getQuestion();
    }
}
