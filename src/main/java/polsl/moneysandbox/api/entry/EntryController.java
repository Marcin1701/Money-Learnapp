package polsl.moneysandbox.api.entry;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.entry.service.EntryService;
import polsl.moneysandbox.api.entry.service.request.LoginRequest;
import polsl.moneysandbox.api.entry.service.request.NewAccountRequest;
import polsl.moneysandbox.api.entry.service.response.JsonWebTokenResponse;
import polsl.moneysandbox.model.Account;

import java.util.List;

@RestController
@RequestMapping("api/entry")
@AllArgsConstructor
public class EntryController {

    private EntryService entryService;

    @PostMapping("/login")
    public JsonWebTokenResponse login(@RequestBody LoginRequest loginRequest) {
        return this.entryService.login(loginRequest);
    }

    @PostMapping("/new")
    public void register(@RequestBody NewAccountRequest newAccountRequest) {
        this.entryService.register(newAccountRequest);
    }

}
