package polsl.moneysandbox.api.form.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FormRequest {

    private String name;

    private Integer difficulty;

    private Integer answerTime;

    private List<String> questionIds;
}
