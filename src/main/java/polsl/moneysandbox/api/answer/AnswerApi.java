package polsl.moneysandbox.api.answer;

import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.answer.request.AnswersRequest;
import polsl.moneysandbox.api.answer.response.AnswersSummary;
import polsl.moneysandbox.api.answer.response.FormAnswerResponse;
import polsl.moneysandbox.api.answer.response.ResultsResponse;
import polsl.moneysandbox.api.answer.service.AnswerService;

import java.awt.*;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/answer")
@AllArgsConstructor
public class AnswerApi {

    private final AnswerService answerService;

    @GetMapping("/form")
    public FormAnswerResponse getFromAnswerResponse(@RequestParam("id") String id) {
        return answerService.getFormAnswerResponse(id);
    }

    @GetMapping("/summary")
    public AnswersSummary getAnswersSummary(@RequestHeader("Authorization") String token) {
        return this.answerService.getAnswersSummary(token);
    }

    @PostMapping("/send")
    public ResultsResponse addAnswers(@RequestBody AnswersRequest answersRequest) {
        return this.answerService.addAnswers(answersRequest);
    }

    @GetMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getActivityReport(@RequestHeader("Authorization") String token) {
        var response = this.answerService.getActivityReport(token);
        String filename = "Report_" + response.right + "_" + LocalDateTime.now();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "inline; filename=" + filename + ".pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(response.left));
    }
}
