package polsl.moneysandbox.api.account;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.account.response.AccountResponse;
import polsl.moneysandbox.api.account.response.AccountRole;
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

    @GetMapping("/role")
    public AccountRole role(@RequestHeader("Authorization") String token) {
        return accountService.getAccountRole(token);
    }

    @PutMapping
    public void updateAccount(@RequestHeader("Authorization") String token, @RequestBody AccountResponse accountResponse) {
        this.accountService.updateAccount(token, accountResponse);
    }
}
