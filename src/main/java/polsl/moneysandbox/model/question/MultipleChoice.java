package polsl.moneysandbox.model.question;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class MultipleChoice {

    private String question;

    private String name;

    private String answerTime;

    private List<String> multipleChoiceOptions;

    private List<Integer> correctMultipleChoiceOptionIndices;
}
