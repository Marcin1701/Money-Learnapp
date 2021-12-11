package polsl.moneysandbox.api.entry;

import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.entry.service.EntryService;
import polsl.moneysandbox.api.entry.request.LoginRequest;
import polsl.moneysandbox.api.entry.request.NewAccountRequest;
import polsl.moneysandbox.api.entry.response.JsonWebTokenResponse;

@RestController
@RequestMapping("/api/entry")
@AllArgsConstructor
public class EntryApi {

    private final EntryService entryService;

    private final AuthenticationManager authenticationManager;

    private final JwtTokenUtility jwtTokenUtility;

    private void authenticate(String login, String password) {
        this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
    }

    @GetMapping("/validate")
    public JsonWebTokenResponse validateToken(@RequestHeader("Authorization") String token) {
        return this.entryService.validateToken(token);
    }

    @PostMapping("/login")
    public JsonWebTokenResponse login(@RequestBody LoginRequest loginRequest) {
        this.authenticate(loginRequest.getLogin(), loginRequest.getPassword());
        return this.entryService.login(loginRequest, jwtTokenUtility.generateToken(loginRequest));
    }

    @PostMapping("/new")
    public void register(@RequestBody NewAccountRequest newAccountRequest) {
        this.entryService.register(newAccountRequest);
    }
}
