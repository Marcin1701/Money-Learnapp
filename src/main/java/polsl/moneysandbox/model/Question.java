package polsl.moneysandbox.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Question<T> {

    @Id
    private String id;

    private String creatorId;

    private String type;

    private String creationDate;

    private T question;
}
