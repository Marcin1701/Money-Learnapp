package polsl.moneysandbox.api.answer.service;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.answer.request.AnswersRequest;
import polsl.moneysandbox.api.answer.response.AnswersSummary;
import polsl.moneysandbox.api.answer.response.FormAnswerResponse;
import polsl.moneysandbox.api.answer.response.ResultsResponse;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.question.response.QuestionResponse;
import polsl.moneysandbox.model.Answer;
import polsl.moneysandbox.model.Form;
import polsl.moneysandbox.model.Question;
import polsl.moneysandbox.model.User;
import polsl.moneysandbox.model.answer.DragAndDropAnswer;
import polsl.moneysandbox.model.answer.MultipleChoiceAnswer;
import polsl.moneysandbox.model.answer.OrderedListAnswer;
import polsl.moneysandbox.model.answer.SingleChoiceAnswer;
import polsl.moneysandbox.model.question.DragAndDrop;
import polsl.moneysandbox.model.question.MultipleChoice;
import polsl.moneysandbox.model.question.OrderedList;
import polsl.moneysandbox.model.question.SingleChoice;
import polsl.moneysandbox.repository.AnswerRepository;
import polsl.moneysandbox.repository.FormRepository;
import polsl.moneysandbox.repository.QuestionRepository;
import polsl.moneysandbox.repository.UserRepository;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

@Service
@AllArgsConstructor
public class AnswerService {

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;

    private final AnswerRepository answerRepository;

    private final UserRepository userRepository;

    private final JwtTokenUtility jwtTokenUtility;

    public ResultsResponse addAnswers(AnswersRequest answersRequest) {
        Form form = formRepository.findById(answersRequest.getFormId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        ResultsResponse response = new ResultsResponse();
        response.setAllQuestions(form.getQuestionsIds().size());
        List<SingleChoiceAnswer> singleChoiceAnswers = new ArrayList<>();
        List<MultipleChoiceAnswer> multipleChoiceAnswers = new ArrayList<>();
        List<OrderedListAnswer> orderedListAnswers = new ArrayList<>();
        List<DragAndDropAnswer> dragAndDropAnswers = new ArrayList<>();
        AtomicReference<Integer> allAnswers = new AtomicReference<>(0);
        AtomicReference<Integer> correctAnswers = new AtomicReference<>(0);
        AtomicReference<Integer> wrongAnswers = new AtomicReference<>(0);
        AtomicReference<Float> percentages = new AtomicReference<>(0.f);
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
                                percentages.getAndSet(percentages.get() + 1.f);
                            } else {
                                wrongAnswers.getAndSet(wrongAnswers.get() + 1);
                            }
                        }
                        allAnswers.getAndSet(allAnswers.get() + 1);
                    }
                    case "MULTIPLE_CHOICE" -> {
                        MultipleChoiceAnswer multipleChoiceAnswer = getMultipleChoiceAnswer(answerMap);
                        if (multipleChoiceAnswer.getQuestionId().equals(entityQuestion.getId())) {
                            multipleChoiceAnswers.add(multipleChoiceAnswer);
                            MultipleChoice multipleChoice = (MultipleChoice) entityQuestion.getQuestion();
                            AtomicInteger multipleChoiceCorrectAnswers = new AtomicInteger();
                            multipleChoiceAnswer.getOptionsChosen().forEach(chosenOption -> {
                                if (multipleChoice.getCorrectMultipleChoiceOptionIndices().stream().anyMatch(correct -> correct.equals(chosenOption))) {
                                    correctAnswers.getAndSet(correctAnswers.get() + 1);
                                    multipleChoiceCorrectAnswers.addAndGet(1);
                                } else {
                                    wrongAnswers.getAndSet(wrongAnswers.get() + 1);
                                }
                            });
                            percentages.getAndSet(percentages.get() + ((float) multipleChoiceCorrectAnswers.get() / multipleChoice.getCorrectMultipleChoiceOptionIndices().size()));
                            allAnswers.getAndSet(allAnswers.get() + multipleChoice.getMultipleChoiceOptions().size());
                        }
                    }
                    case "ORDERED_LIST" -> {
                        OrderedListAnswer orderedListAnswer = getOrderedListAnswer(answerMap);
                        if (orderedListAnswer.getQuestionId().equals(entityQuestion.getId())) {
                            orderedListAnswers.add(orderedListAnswer);
                            OrderedList orderedList = (OrderedList) entityQuestion.getQuestion();
                            for (int optionIndex = 0; optionIndex < orderedListAnswer.getOptionsChosen().size(); optionIndex++) {
                                if (orderedListAnswer.getOptionsChosen().get(optionIndex).equals(orderedList.getOrderedListOptions().get(optionIndex))) {
                                    correctAnswers.getAndSet(correctAnswers.get() + 1);
                                } else {
                                    wrongAnswers.getAndSet(wrongAnswers.get() + 1);
                                }
                            }
                            allAnswers.getAndSet(allAnswers.get() + orderedList.getOrderedListOptions().size());
                        }
                    }
                    case "DRAG_AND_DROP" -> {
                        DragAndDropAnswer dragAndDropAnswer = getDragAndDropAnswer(answerMap);
                        if (dragAndDropAnswer.getQuestionId().equals(entityQuestion.getId())) {
                            dragAndDropAnswers.add(dragAndDropAnswer);
                            DragAndDrop dragAndDrop = (DragAndDrop) entityQuestion.getQuestion();
                            if (dragAndDropAnswer.getBalance().equals(dragAndDrop.getBalance())) {
                                correctAnswers.getAndSet(correctAnswers.get() + 1);
                            } else {
                                wrongAnswers.getAndSet(wrongAnswers.get() + 1);
                            }
                            allAnswers.getAndSet(allAnswers.get() + 1);
                        }
                    }
                }
            });
        });
        response.setCorrectAnswers(correctAnswers.get());
        response.setWrongAnswers(wrongAnswers.get());
        response.setPercentage((100f * percentages.get() / response.getAllQuestions()) + "%");
        Answer answerEntity = Answer.builder()
                .sheetId(form.getId())
                .answerTime(LocalDateTime.now())
                .allAnswers(allAnswers.get())
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
        if (answersRequest.getUserId() != null) {
            answerEntity.setUserId(answersRequest.getUserId());
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

    private MultipleChoiceAnswer getMultipleChoiceAnswer(LinkedHashMap<?, ?> answer) {
        @SuppressWarnings("unchecked")
        var multipleChoiceOptions = (List<Integer>) answer.get("optionChosen");
        return MultipleChoiceAnswer.builder()
                .optionsChosen(multipleChoiceOptions)
                .questionId((String) answer.get("questionId"))
                .build();
    }

    private OrderedListAnswer getOrderedListAnswer(LinkedHashMap<?, ?> answer) {
        @SuppressWarnings("unchecked")
        var orderedListOptions = (List<String>) answer.get("optionChosen");
        return OrderedListAnswer.builder()
                .optionsChosen(orderedListOptions)
                .questionId((String) answer.get("questionId"))
                .build();
    }

    private DragAndDropAnswer getDragAndDropAnswer(LinkedHashMap<?,?> answer) {
        return DragAndDropAnswer.builder()
                .balance((Integer) answer.get("optionChosen"))
                .questionId((String) answer.get("questionId"))
                .build();
    }

    public AnswersSummary getAnswersSummary(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Answer> answers = answerRepository.findAllByUserId(user.getId());
        AnswersSummary answersSummary = new AnswersSummary();
        Integer allAnswers = answers.size();
        AtomicReference<Integer> allQuestions = new AtomicReference<>(0);
        AtomicReference<Integer> allCorrectQuestions = new AtomicReference<>(0);
        AtomicReference<Integer> allWrongQuestions = new AtomicReference<>(0);
        AtomicReference<Float> sumOfPercentages = new AtomicReference<>((float) 0);
        answers.forEach(answer -> {
            allCorrectQuestions.updateAndGet(v -> v + answer.getCorrectAnswers());
            allWrongQuestions.updateAndGet(v -> v + answer.getWrongAnswers());
            allQuestions.updateAndGet(v -> v + answer.getAllAnswers());
            sumOfPercentages.updateAndGet(v -> v + (float) answer.getCorrectAnswers() / answer.getAllAnswers());
        });
        answersSummary.setAllAnswers(allAnswers);
        answersSummary.setAllAnswersPercentage(String.format("%.2f", (100f * allCorrectQuestions.get() / allQuestions.get())) + "%");
        answersSummary.setAllCorrectQuestions(allCorrectQuestions.get());
        answersSummary.setAllWrongQuestions(allWrongQuestions.get());
        answersSummary.setAllQuestions(allQuestions.get());
        return answersSummary;
    }

    public ImmutablePair<ByteArrayInputStream, String> getActivityReport(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        try {
            return new ImmutablePair<>(PdfReportService.generatePdfReport(getAnswersSummary(token), user), user.getLogin());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
