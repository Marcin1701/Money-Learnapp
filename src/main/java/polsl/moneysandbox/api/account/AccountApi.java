package polsl.moneysandbox.api.account;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import polsl.moneysandbox.api.account.response.AccountResponse;
import polsl.moneysandbox.api.account.service.AccountService;

@RestController
@RequestMapping("/api/account")
@AllArgsConstructor
public class AccountApi {

    private final AccountService accountService;

    @GetMapping
    public AccountResponse getAccount(@RequestHeader("Authorization") String token) {
           return accountService.getUsernameData(token);
    }
}
