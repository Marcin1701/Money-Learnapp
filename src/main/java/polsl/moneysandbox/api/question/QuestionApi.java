package polsl.moneysandbox.api.question;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.question.request.QuestionIdsRequest;
import polsl.moneysandbox.api.question.service.QuestionService;
import polsl.moneysandbox.api.question.request.QuestionRequest;
import polsl.moneysandbox.api.question.response.QuestionResponse;
import polsl.moneysandbox.model.question.DragAndDrop;
import polsl.moneysandbox.model.question.MultipleChoice;
import polsl.moneysandbox.model.question.OrderedList;
import polsl.moneysandbox.model.question.SingleChoice;

import java.util.List;

@RestController
@RequestMapping("/api/question")
@AllArgsConstructor
public class QuestionApi {

    private final QuestionService questionService;

    @GetMapping("/single_choice")
    public List<QuestionResponse<SingleChoice>> getSingleChoiceQuestions(@RequestHeader("Authorization") String token) {
        return this.questionService.getSingleChoiceQuestions(token);
    }

    @GetMapping("/multiple_choice")
    public List<QuestionResponse<MultipleChoice>> getMultipleChoiceQuestions(@RequestHeader("Authorization") String token) {
        return this.questionService.getMultipleChoiceQuestions(token);
    }

    @GetMapping("/ordered_list")
    public List<QuestionResponse<OrderedList>> getOrderedListQuestions(@RequestHeader("Authorization") String token) {
        return this.questionService.getOrderedListQuestions(token);
    }

    @GetMapping("/drag_and_drop")
    public List<QuestionResponse<DragAndDrop>> getDragAndDropQuestions(@RequestHeader("Authorization") String token) {
        return this.questionService.getDragAndDropQuestions(token);
    }

    @PostMapping("/new")
    public void addQuestion(@RequestBody QuestionRequest<?> question, @RequestHeader("Authorization") String token) {
        this.questionService.addQuestion(question, token);
    }

    @PostMapping("/preview")
    public List<? extends QuestionResponse<?>> previewQuestions(@RequestBody QuestionIdsRequest idsRequests, @RequestHeader("Authorization") String token) {
        return this.questionService.previewQuestions(idsRequests, token);
    }

    @DeleteMapping
    public void deleteQuestion(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        this.questionService.deleteQuestion(token, id);
    }
}
