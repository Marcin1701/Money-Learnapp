package polsl.moneysandbox.api.account.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.account.response.AccountDetailsResponse;
import polsl.moneysandbox.api.account.response.AccountResponse;
import polsl.moneysandbox.api.account.response.AccountRole;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.model.Answer;
import polsl.moneysandbox.model.Form;
import polsl.moneysandbox.model.User;
import polsl.moneysandbox.repository.AnswerRepository;
import polsl.moneysandbox.repository.FormRepository;
import polsl.moneysandbox.repository.QuestionRepository;
import polsl.moneysandbox.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AccountService {

    private final UserRepository userRepository;

    private final JwtTokenUtility jwtTokenUtility;

    private final FormRepository formRepository;

    private final AnswerRepository answerRepository;

    private final QuestionRepository questionRepository;

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

    public void updateAccount(String token, AccountResponse accountResponse) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getLogin().equals(accountResponse.getLogin()) || user.getEmail().equals(accountResponse.getEmail())) {
            Optional<User> doesUserExist = userRepository.findAccountByEmailOrLogin(accountResponse.getEmail(), accountResponse.getLogin());
            if (doesUserExist.isPresent() && !doesUserExist.get().getId().equals(user.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        user.setEmail(accountResponse.getEmail());
        user.setLastName(accountResponse.getLastName());
        user.setFirstName(accountResponse.getFirstName());
        user.setLogin(accountResponse.getLogin());
        userRepository.save(user);
    }

    public List<AccountResponse> getAllAccounts(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            return userRepository.findAllByRole("USER").stream().map(AccountResponse::new).toList();
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteAccount(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            userRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public AccountDetailsResponse getAccountDetails(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            AccountDetailsResponse accountDetailsResponse = new AccountDetailsResponse();
            User responseUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            accountDetailsResponse.setSingleChoiceCount(checkList(responseUser.getSingleChoiceQuestions()));
            accountDetailsResponse.setMultipleChoiceCount(checkList(responseUser.getMultipleChoiceQuestions()));
            accountDetailsResponse.setOrderedListCount(checkList(responseUser.getOrderedListQuestions()));
            accountDetailsResponse.setDragAndDropCount(checkList(responseUser.getDragAndDropQuestions()));
            List<Form> userForms = formRepository.getAllByCreatorId(responseUser.getId());
            List<Answer> userAnswers = answerRepository.findAllByUserId(responseUser.getId());
            accountDetailsResponse.setTotalAnswers(checkList(userAnswers));
            accountDetailsResponse.setSheetsCreated(checkList(userForms));
            if (checkList(userForms) != 0) {
                accountDetailsResponse.setPublicSheets((int) userForms.stream().filter(form -> form.getIsPublic().equals(true)).count());
            }
            if (checkList(userAnswers) == 0) {
                accountDetailsResponse.setLatestAnswerDate("Brak odpowiedzi");
            } else {
                LocalDateTime earliestDate = Collections.min(userAnswers.stream().map(Answer::getAnswerTime).toList());
                accountDetailsResponse.setLatestAnswerDate(earliestDate.toString());
            }
            return accountDetailsResponse;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    private Integer checkList(List<?> list) {
        if (list == null) {
            return 0;
        } else {
            return list.size();
        }
    }

    public void deleteAccountForms(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            User editableUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            editableUser.setSheets(new ArrayList<>());
            userRepository.save(editableUser);
            formRepository.deleteAllByCreatorId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteAccountQuestions(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            User editableUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            editableUser.setSingleChoiceQuestions(new ArrayList<>());
            editableUser.setMultipleChoiceQuestions(new ArrayList<>());
            editableUser.setOrderedListQuestions(new ArrayList<>());
            editableUser.setDragAndDropQuestions(new ArrayList<>());
            userRepository.save(editableUser);
            questionRepository.deleteAllByCreatorId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void deleteAccountAnswers(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            User editableUser = userRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            editableUser.setAnswers(new ArrayList<>());
            userRepository.save(editableUser);
            answerRepository.deleteAllByUserId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
