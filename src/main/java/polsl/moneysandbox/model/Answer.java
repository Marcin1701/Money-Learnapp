package polsl.moneysandbox.model;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import polsl.moneysandbox.model.answer.DragAndDropAnswer;
import polsl.moneysandbox.model.answer.MultipleChoiceAnswer;
import polsl.moneysandbox.model.answer.OrderedListAnswer;
import polsl.moneysandbox.model.answer.SingleChoiceAnswer;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Document("Answer")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Answer {

    @Id
    private String id;

    @Nullable
    private String userId;

    private String sheetId;

    private Integer correctAnswers;

    private Integer wrongAnswers;

    private Integer allAnswers;

    private Integer timeUsed;

    private LocalDateTime answerTime;

    @Nullable
    private List<SingleChoiceAnswer> singleChoiceAnswer;

    @Nullable
    private List<MultipleChoiceAnswer> multipleChoiceAnswer;

    @Nullable
    private List<DragAndDropAnswer> dragAndDropAnswer;

    @Nullable
    private List<OrderedListAnswer> orderedListAnswer;
}
