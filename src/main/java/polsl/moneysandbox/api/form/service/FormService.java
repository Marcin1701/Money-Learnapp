package polsl.moneysandbox.api.form.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.form.request.FormRequest;
import polsl.moneysandbox.api.form.response.FormResponse;
import polsl.moneysandbox.model.Form;
import polsl.moneysandbox.model.Question;
import polsl.moneysandbox.model.User;
import polsl.moneysandbox.repository.FormRepository;
import polsl.moneysandbox.repository.QuestionRepository;
import polsl.moneysandbox.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class FormService {

    private final UserRepository userRepository;

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;

    private final JwtTokenUtility jwtTokenUtility;

    public List<FormResponse> getForms(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return this.formRepository.getAllByCreatorId(user.getId()).stream().map(FormResponse::new).toList();
    }

    public void addForm(String token, FormRequest formRequest) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Form newForm = formRepository.save(Form.builder()
                        .name(formRequest.getName())
                        .answerTime(formRequest.getAnswerTime())
                        .creationDate(LocalDateTime.now())
                        .answers(new ArrayList<>())
                        .creatorId(user.getId())
                        .questionsIds(formRequest.getQuestionIds())
                        .difficulty(formRequest.getDifficulty())
                        .isPublic(false).build());
        formRequest.getQuestionIds().forEach(question -> {
            Question<?> questionEntity = questionRepository.findById(question)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            List<String> formIds = questionEntity.getFormIds();
            if (formIds == null) {
                formIds = new ArrayList<>();
            }
            formIds.add(newForm.getId());
            questionEntity.setFormIds(formIds);
            questionRepository.save(questionEntity);
        });
    }
}
