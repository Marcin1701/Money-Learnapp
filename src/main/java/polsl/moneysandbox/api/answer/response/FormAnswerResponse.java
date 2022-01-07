package polsl.moneysandbox.api.answer.response;

import lombok.*;
import polsl.moneysandbox.api.question.response.QuestionResponse;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class FormAnswerResponse {

    private String id;

    private String name;

    private Integer answerTime;

    private List<QuestionResponse<?>> questions;
}
