package polsl.moneysandbox.api.question.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.question.service.request.QuestionRequest;
import polsl.moneysandbox.api.question.service.response.QuestionResponse;
import polsl.moneysandbox.api.question.service.response.Questions.SingleChoiceResponse;
import polsl.moneysandbox.model.Account;
import polsl.moneysandbox.model.Question;
import polsl.moneysandbox.model.Questions.DragAndDrop;
import polsl.moneysandbox.model.Questions.MultipleChoice;
import polsl.moneysandbox.model.Questions.OrderedList;
import polsl.moneysandbox.model.Questions.SingleChoice;
import polsl.moneysandbox.repository.AccountRepository;
import polsl.moneysandbox.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final JwtTokenUtility jwtTokenUtility;

    private final AccountRepository accountRepository;

    private final QuestionRepository questionRepository;

    public void addQuestion(QuestionRequest<?> question, String token) {
        Account account = accountRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Question<SingleChoice> createdQuestion = new Question<>();
        switch (question.getType()) {
            case "SINGLE_CHOICE" -> {
                SingleChoice singleChoiceQuestion = createSingleChoice((LinkedHashMap<?, ?>) question.getStructure());
                createdQuestion.setType(question.getType());
                createdQuestion.setCreationDate(LocalDateTime.now().toString());
                createdQuestion.setCreatorId(account.getId());
                createdQuestion.setQuestion(singleChoiceQuestion);
                List<Question<SingleChoice>> createdSingleChoiceQuestions = account.getSingleChoiceQuestions();
                if (createdSingleChoiceQuestions == null) {
                    createdSingleChoiceQuestions = new ArrayList<>();
                }
                createdSingleChoiceQuestions.add(createdQuestion);
                account.setSingleChoiceQuestions(createdSingleChoiceQuestions);
            }
            // TODO INNE PYTANIA
            case "MULTIPLE_CHOICE", "LIST", "DRAG_AND_DROP" -> {
            }
            default -> throw new IllegalStateException("Unexpected question type");
        }
        accountRepository.save(account);
        questionRepository.save(createdQuestion);
    }

    private SingleChoice createSingleChoice(LinkedHashMap<?, ?> question) {
        @SuppressWarnings("unchecked")
        var singleChoiceOptions = (List<String>) ((LinkedHashMap<?, ?>)question.get("value")).get("singleChoiceOptions");
        return SingleChoice.builder()
                .answerTime((String) question.get("answerTime"))
                .name((String) question.get("name"))
                .question((String) question.get("question"))
                .correctSingleChoiceIndex((Integer) ((LinkedHashMap<?, ?>)question.get("value")).get("correctSingleChoiceOptionIndex"))
                .singleChoiceOptions(singleChoiceOptions)
                .build();

    }

    private MultipleChoice createMultipleChoice(LinkedHashMap<?, ?> question) {
        return null;
    }

    private OrderedList createOrderedList(LinkedHashMap<?, ?> question) {
        return null;
    }

    private DragAndDrop createDragAndDrop(LinkedHashMap<?, ?> question) {
        return null;
    }

    public List<QuestionResponse<SingleChoiceResponse>> getSingleChoiceQuestions(String token) {
        Account account = accountRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
/*        List<Question<SingleChoice>> questionList = questionRepository
                .findAllByCreatorIdAndType(account.getId(), "SINGLE_CHOICE")
                .stream()
                .map(question -> new Question<SingleChoice>().setId())
                .toList();
        questionList.stream().map(question -> (SingleChoice) question.getQuestion());*/
        return null;
    }
}
