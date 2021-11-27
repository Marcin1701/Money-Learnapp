package polsl.moneysandbox.model;

import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Document("student")
@Data
@SuperBuilder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Student extends Account {

    private String className;

    private Boolean isCreatorAllowed;

    private Boolean isTemporaryPasswordActive;
}
