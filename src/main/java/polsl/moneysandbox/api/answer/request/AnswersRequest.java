package polsl.moneysandbox.api.answer.request;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class AnswersRequest {
    private String formId;

    private String answerer;

    private String userId;

    private List<AnswerRequest<?>> answers;
}
