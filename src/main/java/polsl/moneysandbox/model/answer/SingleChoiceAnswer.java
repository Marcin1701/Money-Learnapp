package polsl.moneysandbox.model.answer;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class SingleChoiceAnswer {

    private String questionId;

    private Integer optionChosen;
}
