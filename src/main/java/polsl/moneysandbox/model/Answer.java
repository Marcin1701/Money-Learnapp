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

    @Nullable
    private String answerer;

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
