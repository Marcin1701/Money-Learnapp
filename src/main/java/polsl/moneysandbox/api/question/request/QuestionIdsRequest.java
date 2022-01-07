package polsl.moneysandbox.api.question.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class QuestionIdsRequest {

    private List<String> ids;
}
