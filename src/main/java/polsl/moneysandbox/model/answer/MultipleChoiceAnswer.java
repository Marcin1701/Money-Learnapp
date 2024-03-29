package polsl.moneysandbox.model.answer;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class MultipleChoiceAnswer {

    private String questionId;

    private List<Integer> optionsChosen;
}
