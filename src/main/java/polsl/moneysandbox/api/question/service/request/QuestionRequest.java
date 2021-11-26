package polsl.moneysandbox.api.question.service.request;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionRequest<T> {

    private String type;

    private T structure;
}
