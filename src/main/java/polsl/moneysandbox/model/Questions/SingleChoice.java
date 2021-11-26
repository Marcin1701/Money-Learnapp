package polsl.moneysandbox.model.Questions;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class SingleChoice {

    private String question;

    private String name;

    private String answerTime;

    private Integer correctSingleChoiceIndex;

    private List<String> singleChoiceOptions;
}
