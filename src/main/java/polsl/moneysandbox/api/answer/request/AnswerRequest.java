package polsl.moneysandbox.api.answer.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AnswerRequest<T> {

    private String questionType;

    private T answer;
}
