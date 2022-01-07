package polsl.moneysandbox.api.form;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.form.request.FormRequest;
import polsl.moneysandbox.api.form.response.FormPublicityResponse;
import polsl.moneysandbox.api.form.response.FormResponse;
import polsl.moneysandbox.api.form.response.HomeFormResponse;
import polsl.moneysandbox.api.form.service.FormService;

import java.util.List;

@RestController
@RequestMapping("/api/form")
@AllArgsConstructor
public class FormApi {

    private final FormService formService;

    @GetMapping
    public List<FormResponse> getForms(@RequestHeader("Authorization") String token) {
        return this.formService.getForms(token);
    }

    @GetMapping("/public")
    public List<HomeFormResponse> getPublicForms() {
        return this.formService.getPublicForms();
    }

    @GetMapping("/publish")
    public FormPublicityResponse requestPublish(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        return this.formService.requestPublish(token, id);
    }

    @GetMapping("/publish/check")
    public FormPublicityResponse isFormInPublish(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        return this.formService.isFormInPublish(token, id);
    }

     @GetMapping("/publish/waiting")
     public List<FormResponse> getFormsWaitingForPublicity(
            @RequestHeader("Authorization") String token) {
        return this.formService.getFormsWaitingForPublicity(token);
     }

    @GetMapping("/publish/approve")
    public void publishForm(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        this.formService.publishForm(token, id, true);
    }

    @GetMapping("/answers")
    public List<FormResponse> getAnsweredForms(@RequestHeader("Authorization") String token) {
        return this.formService.getAnsweredForms(token);
    }

    @PostMapping("/add")
    public void addForm(@RequestHeader("Authorization") String token, @RequestBody FormRequest formRequest) {
        this.formService.addForm(token, formRequest);
    }

    @DeleteMapping
    public void deleteForm(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        this.formService.deleteForm(token, id);
    }
}
