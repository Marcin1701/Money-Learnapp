package polsl.moneysandbox.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import polsl.moneysandbox.model.Questions.DragAndDrop;
import polsl.moneysandbox.model.Questions.MultipleChoice;
import polsl.moneysandbox.model.Questions.OrderedList;
import polsl.moneysandbox.model.Questions.SingleChoice;

import java.time.LocalDateTime;
import java.util.List;

@Document("account")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Account {

    @Id
    private String id;

    private String login;

    private String password;

    private String firstName;

    private String lastName;

    private String accountType;

    private String email;

    private LocalDateTime creationDate;

    private List<Question<SingleChoice>> singleChoiceQuestions;

    private List<Question<MultipleChoice>> multipleChoiceQuestions;

    private List<Question<OrderedList>> orderedListQuestions;

    private List<Question<DragAndDrop>> dragAndDropQuestions;
}
