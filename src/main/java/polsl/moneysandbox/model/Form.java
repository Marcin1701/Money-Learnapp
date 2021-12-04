package polsl.moneysandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Document("Sheet")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class Form {

    @Id
    private String id;

    private String creatorId;

    private List<String> questionsIds;

    private String name;

    private Integer difficulty;

    private Integer answerTime;

    private LocalDateTime creationDate;

    private List<Answer> answers;

    private Boolean isPublic;
}
