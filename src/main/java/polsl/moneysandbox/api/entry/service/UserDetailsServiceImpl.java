package polsl.moneysandbox.api.entry.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import polsl.moneysandbox.api.entry.service.request.LoginRequest;
import polsl.moneysandbox.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        var user = userRepository.findAccountByEmailOrLogin(login, login);
        if (user.isPresent()) {
            return new LoginRequest(user.get().getLogin(), user.get().getPassword());
        }
        throw new UsernameNotFoundException("User not found!");
    }
}
