package polsl.moneysandbox.model.question;

import lombok.*;

import java.util.List;

@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Setter
@ToString
public class DragAndDrop {

    private String question;

    private String name;

    private Integer balance;

    private List<String> optionName;

    private List<Integer> optionCost;
}
