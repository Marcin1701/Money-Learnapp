package polsl.moneysandbox.api.entry.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.entry.service.request.LoginRequest;
import polsl.moneysandbox.api.entry.service.request.NewAccountRequest;
import polsl.moneysandbox.api.entry.service.response.JsonWebTokenResponse;
import polsl.moneysandbox.model.Account;
import polsl.moneysandbox.repository.AccountRepository;

import java.time.LocalDateTime;
import java.util.Date;

@Service
@AllArgsConstructor
public class EntryService {

    private final AccountRepository accountRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtTokenUtility jwtTokenUtility;

    public void register(NewAccountRequest newAccountRequest) {
        if (this.accountRepository.findAccountByEmailOrLogin(newAccountRequest.getEmail(), newAccountRequest.getLogin()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already exists");
        }
        this.accountRepository.save(
                Account.builder()
                        .accountType(newAccountRequest.getAccountType())
                        .creationDate(LocalDateTime.now())
                        .firstName(newAccountRequest.getFirstName())
                        .lastName(newAccountRequest.getLastName())
                        .email(newAccountRequest.getEmail())
                        .password(this.passwordEncoder.encode(newAccountRequest.getPassword()))
                        .login(newAccountRequest.getLogin())
                        .build());
    }

    public JsonWebTokenResponse login(LoginRequest loginRequest, String token) {
        var user = this.accountRepository.findAccountByEmailOrLogin(loginRequest.getLogin(), loginRequest.getLogin());
        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            return new JsonWebTokenResponse(token);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
    }

    public JsonWebTokenResponse validateToken(String token) {
        if (new Date().before(jwtTokenUtility.getExpirationDateFromToken(token))) {
            return new JsonWebTokenResponse(token);
        }
        return null;
    }
}
