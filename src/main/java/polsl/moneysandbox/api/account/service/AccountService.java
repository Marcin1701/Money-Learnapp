package polsl.moneysandbox.api.account.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.account.response.AccountResponse;
import polsl.moneysandbox.api.account.response.AccountRole;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.model.User;
import polsl.moneysandbox.repository.UserRepository;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserRepository userRepository;

    private final JwtTokenUtility jwtTokenUtility;

    public AccountResponse getUsernameData(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new AccountResponse(user);
    }

    public AccountRole getAccountRole(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return new AccountRole(user.getRole());
    }
}
