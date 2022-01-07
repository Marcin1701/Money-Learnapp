package polsl.moneysandbox.api.question.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.question.mapper.QuestionMapper;
import polsl.moneysandbox.api.question.request.QuestionIdsRequest;
import polsl.moneysandbox.api.question.request.QuestionRequest;
import polsl.moneysandbox.api.question.response.QuestionResponse;
import polsl.moneysandbox.model.Form;
import polsl.moneysandbox.model.User;
import polsl.moneysandbox.model.Question;
import polsl.moneysandbox.model.question.DragAndDrop;
import polsl.moneysandbox.model.question.MultipleChoice;
import polsl.moneysandbox.model.question.OrderedList;
import polsl.moneysandbox.model.question.SingleChoice;
import polsl.moneysandbox.repository.FormRepository;
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

    private final FormRepository formRepository;

    private final QuestionMapper questionMapper;

    public void addQuestion(QuestionRequest<?> question, String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        switch (question.getType()) {
            case "SINGLE_CHOICE" -> {
                Question<SingleChoice> createdSingleChoiceQuestion = new Question<>();
                SingleChoice singleChoiceQuestion = questionMapper.createSingleChoice((LinkedHashMap<?, ?>) question.getStructure());
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
                MultipleChoice multipleChoiceQuestion = questionMapper.createMultipleChoice((LinkedHashMap<?, ?>) question.getStructure());
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
            case "ORDERED_LIST"-> {
                Question<OrderedList> createdOrderedListQuestion = new Question<>();
                OrderedList orderedListQuestion = questionMapper.createOrderedList((LinkedHashMap<?, ?>) question.getStructure());
                createdOrderedListQuestion.setType(question.getType());
                createdOrderedListQuestion.setCreationDate(LocalDateTime.now().toString());
                createdOrderedListQuestion.setCreatorId(user.getId());
                createdOrderedListQuestion.setQuestion(orderedListQuestion);
                List<Question<OrderedList>> createdOrderedListQuestions = user.getOrderedListQuestions();
                if (createdOrderedListQuestions == null) {
                    createdOrderedListQuestions = new ArrayList<>();
                }
                createdOrderedListQuestions.add(createdOrderedListQuestion);
                user.setOrderedListQuestions(createdOrderedListQuestions);
                userRepository.save(user);
                questionRepository.save(createdOrderedListQuestion);
            }
            case "DRAG_AND_DROP" -> {
                Question<DragAndDrop> createdDragAndDropQuestion = new Question<>();
                DragAndDrop dragAndDropQuestion = questionMapper.createDragAndDrop((LinkedHashMap<?, ?>) question.getStructure());
                createdDragAndDropQuestion.setType(question.getType());
                createdDragAndDropQuestion.setCreationDate(LocalDateTime.now().toString());
                createdDragAndDropQuestion.setCreatorId(user.getId());
                createdDragAndDropQuestion.setQuestion(dragAndDropQuestion);
                List<Question<DragAndDrop>> createdDragAndDropQuestions = user.getDragAndDropQuestions();
                if (createdDragAndDropQuestions == null) {
                    createdDragAndDropQuestions = new ArrayList<>();
                }
                createdDragAndDropQuestions.add(createdDragAndDropQuestion);
                user.setDragAndDropQuestions(createdDragAndDropQuestions);
                userRepository.save(user);
                questionRepository.save(createdDragAndDropQuestion);
            }
            default -> throw new IllegalStateException("Unexpected question type");
        }
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

    public List<QuestionResponse<OrderedList>> getOrderedListQuestions(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        @SuppressWarnings("unchecked")
        List<Question<OrderedList>> questionList = questionRepository
                .findAllByCreatorIdAndType(user.getId(), "ORDERED_LIST")
                .stream()
                .map(question -> (Question<OrderedList>) question)
                .toList();
        return questionList.stream().map(QuestionResponse::new).toList();
    }

    public List<QuestionResponse<DragAndDrop>> getDragAndDropQuestions(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        @SuppressWarnings("unchecked")
        List<Question<DragAndDrop>> questionList = questionRepository
                .findAllByCreatorIdAndType(user.getId(), "DRAG_AND_DROP")
                .stream()
                .map(question -> (Question<DragAndDrop>) question)
                .toList();
        return questionList.stream().map(QuestionResponse::new).toList();
    }

    public void deleteQuestion(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Question<?>> questions = questionRepository.findAllByCreatorId(user.getId());
        if (questions.stream().anyMatch(question -> question.getId().equals(id))) {
            List<Form> forms = formRepository.getAllByCreatorId(user.getId());
            forms.forEach(form -> {
                var formQuestionsIds = form.getQuestionsIds();
                if (formQuestionsIds.stream().anyMatch(question -> question.equals(id))) {
                    formQuestionsIds.remove(id);
                    if (formQuestionsIds.size() == 0) {
                        formRepository.deleteById(form.getId());
                    } else {
                        form.setIsPublic(false);
                    }
                }
            });
            formRepository.saveAll(forms);
            questionRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<? extends QuestionResponse<?>> previewQuestions(QuestionIdsRequest idsRequests, String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return questionRepository.findAllByIdIn(idsRequests.getIds()).stream().map(QuestionResponse::new).toList();
    }
}
