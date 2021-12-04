package polsl.moneysandbox.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import polsl.moneysandbox.model.question.DragAndDrop;
import polsl.moneysandbox.model.question.MultipleChoice;
import polsl.moneysandbox.model.question.OrderedList;
import polsl.moneysandbox.model.question.SingleChoice;

import java.time.LocalDateTime;
import java.util.List;

@Document("User")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String role;

    private LocalDateTime creationDate;

    private List<Question<SingleChoice>> singleChoiceQuestions;

    private List<Question<MultipleChoice>> multipleChoiceQuestions;

    private List<Question<OrderedList>> orderedListQuestions;

    private List<Question<DragAndDrop>> dragAndDropQuestions;

    private List<Form> sheets;

    private List<Answer> answers;
}
