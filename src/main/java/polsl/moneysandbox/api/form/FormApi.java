package polsl.moneysandbox.api.form;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.form.request.FormRequest;
import polsl.moneysandbox.api.form.response.FormResponse;
import polsl.moneysandbox.api.form.service.FormService;

import java.util.List;

@RestController
@RequestMapping("/api/form")
@AllArgsConstructor
public class FormApi {

    private final FormService formService;

    @GetMapping("/all")
    public List<FormResponse> getForms(@RequestHeader("Authorization") String token) {
        return this.formService.getForms(token);
    }

    @PostMapping("/add")
    public void addForm(@RequestHeader("Authorization") String token, @RequestBody FormRequest formRequest) {
        this.formService.addForm(token, formRequest);
    }
}