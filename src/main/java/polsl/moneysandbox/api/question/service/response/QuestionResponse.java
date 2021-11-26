package polsl.moneysandbox.api.question.service.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionResponse<T> {

    private String type;

    private String creationDate;

    private T question;
}
