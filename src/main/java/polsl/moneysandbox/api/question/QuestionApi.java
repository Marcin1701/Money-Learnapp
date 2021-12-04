package polsl.moneysandbox.api.question;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.question.service.QuestionService;
import polsl.moneysandbox.api.question.service.request.QuestionRequest;
import polsl.moneysandbox.api.question.service.response.QuestionResponse;
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

    @PostMapping("/new")
    public void addQuestion(@RequestBody QuestionRequest<?> question, @RequestHeader("Authorization") String token) {
        this.questionService.addQuestion(question, token);
    }
}
