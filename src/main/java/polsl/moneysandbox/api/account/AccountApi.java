package polsl.moneysandbox.api.account;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.account.response.AccountDetailsResponse;
import polsl.moneysandbox.api.account.response.AccountResponse;
import polsl.moneysandbox.api.account.response.AccountRole;
import polsl.moneysandbox.api.account.service.AccountService;

import java.util.List;

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

    @GetMapping("/all")
    public List<AccountResponse> getAllAccounts(@RequestHeader("Authorization") String token) {
        return accountService.getAllAccounts(token);
    }

    @GetMapping("/details")
    public AccountDetailsResponse getAccountDetails(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        return this.accountService.getAccountDetails(token, id);
    }

    @PutMapping
    public void updateAccount(@RequestHeader("Authorization") String token, @RequestBody AccountResponse accountResponse) {
        accountService.updateAccount(token, accountResponse);
    }

    @DeleteMapping
    public void deleteAccount(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        accountService.deleteAccount(token, id);
    }

    @DeleteMapping("/forms")
    public void deleteAccountForms(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        accountService.deleteAccountForms(token, id);
    }

    @DeleteMapping("/questions")
    public void deleteAccountQuestions(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        accountService.deleteAccountQuestions(token, id);
    }

    @DeleteMapping("/answers")
    public void deleteAccountAnswers(@RequestHeader("Authorization") String token, @RequestParam("id") String id) {
        accountService.deleteAccountAnswers(token, id);
    }
}
