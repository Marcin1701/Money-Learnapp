package polsl.moneysandbox.api.question.response;

import lombok.*;
import polsl.moneysandbox.model.Question;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionResponse<T> {

    private String id;

    private String type;

    private String creationDate;

    private T question;

    public QuestionResponse(Question<T> question) {
        this.id = question.getId();
        this.type = question.getType();
        this.creationDate = question.getCreationDate();
        this.question = question.getQuestion();
    }
}
