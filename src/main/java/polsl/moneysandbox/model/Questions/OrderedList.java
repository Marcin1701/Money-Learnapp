package polsl.moneysandbox.model.Questions;

import lombok.*;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
public class OrderedList {

    private String question;

    private String name;

    private String answerTime;
}
