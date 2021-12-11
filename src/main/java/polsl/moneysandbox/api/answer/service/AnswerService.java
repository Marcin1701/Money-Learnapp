package polsl.moneysandbox.api.answer.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.answer.request.AnswersRequest;
import polsl.moneysandbox.api.answer.response.FormAnswerResponse;
import polsl.moneysandbox.api.answer.response.ResultsResponse;
import polsl.moneysandbox.api.question.service.response.QuestionResponse;
import polsl.moneysandbox.model.Answer;
import polsl.moneysandbox.model.Form;
import polsl.moneysandbox.model.Question;
import polsl.moneysandbox.model.answer.DragAndDropAnswer;
import polsl.moneysandbox.model.answer.MultipleChoiceAnswer;
import polsl.moneysandbox.model.answer.OrderedListAnswer;
import polsl.moneysandbox.model.answer.SingleChoiceAnswer;
import polsl.moneysandbox.model.question.SingleChoice;
import polsl.moneysandbox.repository.AnswerRepository;
import polsl.moneysandbox.repository.FormRepository;
import polsl.moneysandbox.repository.QuestionRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class AnswerService {

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    public ResultsResponse addAnswers(AnswersRequest answersRequest) {
        Form form = formRepository.findById(answersRequest.getFormId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResultsResponse response = new ResultsResponse();
        response.setAllQuestions(form.getQuestionsIds().size());
        List<SingleChoiceAnswer> singleChoiceAnswers = new ArrayList<>();
        List<MultipleChoiceAnswer> multipleChoiceAnswers = new ArrayList<>();
        List<OrderedListAnswer> orderedListAnswers = new ArrayList<>();
        List<DragAndDropAnswer> dragAndDropAnswers = new ArrayList<>();
        AtomicReference<Integer> correctAnswers = new AtomicReference<>(0);
        AtomicReference<Integer> wrongAnswers = new AtomicReference<>(0);
        form.getQuestionsIds().forEach(question -> {
            Question<?> entityQuestion = questionRepository.findById(question)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            answersRequest.getAnswers().forEach(answer -> {
                LinkedHashMap<?, ?> answerMap = (LinkedHashMap<?, ?>) answer.getAnswer();
                switch (answer.getQuestionType()) {
                    case "SINGLE_CHOICE" -> {
                        SingleChoiceAnswer singleChoiceAnswer = getSingleChoiceAnswer(answerMap);
                        if (singleChoiceAnswer.getQuestionId().equals(entityQuestion.getId())) {
                            singleChoiceAnswers.add(singleChoiceAnswer);
                            SingleChoice entitySingleChoice = (SingleChoice) entityQuestion.getQuestion();
                            if (entitySingleChoice.getCorrectSingleChoiceIndex().equals(singleChoiceAnswer.getOptionChosen())) {
                                correctAnswers.getAndSet(correctAnswers.get() + 1);
                            } else {
                                wrongAnswers.getAndSet(wrongAnswers.get() + 1);
                            }
                        }
                    }
                    case "MULTIPLE_CHOICE" -> {

                    }
                }
            });
        });
        response.setCorrectAnswers(correctAnswers.get());
        response.setWrongAnswers(wrongAnswers.get());
        response.setPercentage(((response.getCorrectAnswers() * 100.0f) / response.getAllQuestions()) + "%");
        Answer answerEntity = Answer.builder()
                .sheetId(form.getId())
                .answerTime(LocalDateTime.now())
                .allAnswers(response.getAllQuestions())
                .correctAnswers(response.getCorrectAnswers())
                .wrongAnswers(response.getWrongAnswers())
                .timeUsed(form.getAnswerTime())
                .singleChoiceAnswer(singleChoiceAnswers)
                .multipleChoiceAnswer(multipleChoiceAnswers)
                .orderedListAnswer(orderedListAnswers)
                .dragAndDropAnswer(dragAndDropAnswers)
                .build();
        if (answersRequest.getAnswerer() != null) {
            answerEntity.setAnswerer(answersRequest.getAnswerer());
        }
        answerRepository.save(answerEntity);
        List<Answer> answers = form.getAnswers();
        if (answers == null) {
            answers = new ArrayList<>();
        }
        answers.add(answerEntity);
        form.setAnswers(answers);
        formRepository.save(form);
        return response;
    }

    public FormAnswerResponse getFormAnswerResponse(String id) {
        Form form = formRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Answer form not found"));
        FormAnswerResponse formAnswerResponse = new FormAnswerResponse();
        formAnswerResponse.setAnswerTime(form.getAnswerTime());
        formAnswerResponse.setName(form.getName());
        formAnswerResponse.setAnswerTime(form.getAnswerTime());
        formAnswerResponse.setId(form.getId());
        List<QuestionResponse<?>> questionResponses = new ArrayList<>();
        form.getQuestionsIds().forEach(question -> {
            Question<?> entityQuestion = questionRepository.findById(question)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            questionResponses.add(new QuestionResponse<>(entityQuestion));
        });
        formAnswerResponse.setQuestions(questionResponses);
        return formAnswerResponse;
    }

    private SingleChoiceAnswer getSingleChoiceAnswer(LinkedHashMap<?, ?> answer) {
        return SingleChoiceAnswer.builder()
                .optionChosen((Integer) answer.get("optionChosen"))
                .questionId((String) answer.get("questionId"))
                .build();
    }
}
