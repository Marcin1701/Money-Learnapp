package polsl.moneysandbox.api.account.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDetailsResponse {

    private Integer singleChoiceCount;

    private Integer multipleChoiceCount;

    private Integer orderedListCount;

    private Integer dragAndDropCount;

    private Integer totalAnswers;

    private String latestAnswerDate;

    private Integer sheetsCreated;

    private Integer publicSheets;
}
