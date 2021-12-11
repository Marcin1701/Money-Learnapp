package polsl.moneysandbox.api.answer.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class ResultsResponse {

    private String percentage;

    private Integer allQuestions;

    private Integer correctAnswers;

    private Integer wrongAnswers;
}
