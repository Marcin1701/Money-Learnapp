package polsl.moneysandbox.api.question.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.question.service.request.QuestionRequest;
import polsl.moneysandbox.api.question.service.response.QuestionResponse;
import polsl.moneysandbox.model.User;
import polsl.moneysandbox.model.Question;
import polsl.moneysandbox.model.question.DragAndDrop;
import polsl.moneysandbox.model.question.MultipleChoice;
import polsl.moneysandbox.model.question.OrderedList;
import polsl.moneysandbox.model.question.SingleChoice;
import polsl.moneysandbox.repository.UserRepository;
import polsl.moneysandbox.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

@Service
@AllArgsConstructor
public class QuestionService {

    private final JwtTokenUtility jwtTokenUtility;

    private final UserRepository userRepository;

    private final QuestionRepository questionRepository;

    public void addQuestion(QuestionRequest<?> question, String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        switch (question.getType()) {
            case "SINGLE_CHOICE" -> {
                Question<SingleChoice> createdSingleChoiceQuestion = new Question<>();
                SingleChoice singleChoiceQuestion = createSingleChoice((LinkedHashMap<?, ?>) question.getStructure());
                createdSingleChoiceQuestion.setType(question.getType());
                createdSingleChoiceQuestion.setCreationDate(LocalDateTime.now().toString());
                createdSingleChoiceQuestion.setCreatorId(user.getId());
                createdSingleChoiceQuestion.setQuestion(singleChoiceQuestion);
                List<Question<SingleChoice>> createdSingleChoiceQuestions = user.getSingleChoiceQuestions();
                if (createdSingleChoiceQuestions == null) {
                    createdSingleChoiceQuestions = new ArrayList<>();
                }
                createdSingleChoiceQuestions.add(createdSingleChoiceQuestion);
                user.setSingleChoiceQuestions(createdSingleChoiceQuestions);
                userRepository.save(user);
                questionRepository.save(createdSingleChoiceQuestion);
            }
            case "MULTIPLE_CHOICE" -> {
                Question<MultipleChoice> createdMultipleChoiceQuestion = new Question<>();
                MultipleChoice multipleChoiceQuestion = createMultipleChoice((LinkedHashMap<?, ?>) question.getStructure());
                createdMultipleChoiceQuestion.setType(question.getType());
                createdMultipleChoiceQuestion.setCreationDate(LocalDateTime.now().toString());
                createdMultipleChoiceQuestion.setCreatorId(user.getId());
                createdMultipleChoiceQuestion.setQuestion(multipleChoiceQuestion);
                List<Question<MultipleChoice>> createdMultipleChoiceQuestions = user.getMultipleChoiceQuestions();
                if (createdMultipleChoiceQuestions == null) {
                    createdMultipleChoiceQuestions = new ArrayList<>();
                }
                createdMultipleChoiceQuestions.add(createdMultipleChoiceQuestion);
                user.setMultipleChoiceQuestions(createdMultipleChoiceQuestions);
                userRepository.save(user);
                questionRepository.save(createdMultipleChoiceQuestion);
            }
            // TODO INNE PYTANIA
            case "LIST", "DRAG_AND_DROP" -> {
            }
            default -> throw new IllegalStateException("Unexpected question type");
        }
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
        @SuppressWarnings("unchecked")
        var multipleChoiceOptions = (List<String>) ((LinkedHashMap<?, ?>)question.get("value")).get("multipleChoiceOptions");
        @SuppressWarnings("unchecked")
        var multipleChoiceCorrectIndices = (List<Integer>) ((LinkedHashMap<?, ?>)question.get("value")).get("correctMultipleChoiceOptionIndices");
        return MultipleChoice.builder()
                .answerTime((String) question.get("answerTime"))
                .name((String) question.get("name"))
                .question((String) question.get("question"))
                .multipleChoiceOptions(multipleChoiceOptions)
                .correctMultipleChoiceOptionIndices(multipleChoiceCorrectIndices)
                .build();
    }

    private OrderedList createOrderedList(LinkedHashMap<?, ?> question) {
        return null;
    }

    private DragAndDrop createDragAndDrop(LinkedHashMap<?, ?> question) {
        return null;
    }

    public List<QuestionResponse<SingleChoice>> getSingleChoiceQuestions(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        @SuppressWarnings("unchecked")
        List<Question<SingleChoice>> questionList = questionRepository
                .findAllByCreatorIdAndType(user.getId(), "SINGLE_CHOICE")
                .stream()
                .map(question -> (Question<SingleChoice>) question)
                .toList();
        return questionList.stream().map(QuestionResponse::new).toList();
    }

    public List<QuestionResponse<MultipleChoice>> getMultipleChoiceQuestions(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        @SuppressWarnings("unchecked")
        List<Question<MultipleChoice>> questionList = questionRepository
                .findAllByCreatorIdAndType(user.getId(), "MULTIPLE_CHOICE")
                .stream()
                .map(question -> (Question<MultipleChoice>) question)
                .toList();
        return questionList.stream().map(QuestionResponse::new).toList();
    }
}
