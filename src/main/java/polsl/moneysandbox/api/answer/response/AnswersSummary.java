package polsl.moneysandbox.api.answer.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AnswersSummary {

    private Integer allAnswers;

    private String allAnswersPercentage;

    private Integer allQuestions;

    private Integer allCorrectQuestions;

    private Integer allWrongQuestions;
}
