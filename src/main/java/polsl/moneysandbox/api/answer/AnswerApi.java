package polsl.moneysandbox.api.answer;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.answer.request.AnswersRequest;
import polsl.moneysandbox.api.answer.response.FormAnswerResponse;
import polsl.moneysandbox.api.answer.response.ResultsResponse;
import polsl.moneysandbox.api.answer.service.AnswerService;

@RestController
@RequestMapping("/api/answer")
@AllArgsConstructor
public class AnswerApi {

    private final AnswerService answerService;

    @GetMapping("/form")
    public FormAnswerResponse getFromAnswerResponse(@RequestParam("id") String id) {
        return answerService.getFormAnswerResponse(id);
    }

    @PostMapping("/send")
    public ResultsResponse addAnswers(@RequestBody AnswersRequest answersRequest) {
        return this.answerService.addAnswers(answersRequest);
    }
}
