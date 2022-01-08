package polsl.moneysandbox.api.form.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import polsl.moneysandbox.api.entry.jwt.JwtTokenUtility;
import polsl.moneysandbox.api.form.request.FormRequest;
import polsl.moneysandbox.api.form.response.FormManageResponse;
import polsl.moneysandbox.api.form.response.FormPublicityResponse;
import polsl.moneysandbox.api.form.response.FormResponse;
import polsl.moneysandbox.api.form.response.HomeFormResponse;
import polsl.moneysandbox.model.*;
import polsl.moneysandbox.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FormService {

    private final UserRepository userRepository;

    private final FormRepository formRepository;

    private final QuestionRepository questionRepository;

    private final FormToVerifyRepository formToVerifyRepository;

    private final AnswerRepository answerRepository;

    private final JwtTokenUtility jwtTokenUtility;

    public List<FormResponse> getForms(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        return this.formRepository.getAllByCreatorId(user.getId()).stream().map(FormResponse::new).toList();
    }

    public void addForm(String token, FormRequest formRequest) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Form newForm = formRepository.save(Form.builder()
                        .name(formRequest.getName())
                        .answerTime(formRequest.getAnswerTime())
                        .creationDate(LocalDateTime.now())
                        .answers(new ArrayList<>())
                        .creatorId(user.getId())
                        .questionsIds(formRequest.getQuestionIds())
                        .difficulty(formRequest.getDifficulty())
                        .isPublic(false).build());
        formRequest.getQuestionIds().forEach(question -> {
            Question<?> questionEntity = questionRepository.findById(question)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST));
            List<String> formIds = questionEntity.getFormIds();
            if (formIds == null) {
                formIds = new ArrayList<>();
            }
            formIds.add(newForm.getId());
            questionEntity.setFormIds(formIds);
            questionRepository.save(questionEntity);
        });
    }

    public List<HomeFormResponse> getPublicForms() {
        List<HomeFormResponse> homeFormResponses = new ArrayList<>();
        formRepository.getAllByIsPublic(true).forEach(form -> {
            User creator = userRepository.findById(form.getCreatorId()).orElse(User.builder().firstName("Brak").lastName("Konta").build());
            homeFormResponses.add(new HomeFormResponse(form, creator));
        });
        return homeFormResponses;
    }


    public List<FormResponse> getFormsWaitingForPublicity(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            List<String> formsToVerifyIds = formToVerifyRepository.findAllByIsPendingVerificationIsTrue().stream().map(FormToVerify::getSheetId).toList();
            return formRepository.findAllByIdIn(formsToVerifyIds).stream().map(FormResponse::new).toList();
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }

    public FormPublicityResponse requestPublish(String token, String id) {
        Optional<FormToVerify> formToVerify = this.validatePublishRequest(token, id);
        if (formToVerify.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return new FormPublicityResponse(
                formToVerifyRepository.save(
                        FormToVerify.builder()
                                .isPendingVerification(true)
                                .requestedVerificationDate(LocalDateTime.now())
                                .sheetId(id)
                                .build()));
    }

    public FormPublicityResponse isFormInPublish(String token, String id) {
        FormToVerify formToVerify = this.validatePublishRequest(token, id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return new FormPublicityResponse(formToVerify);
    }

    private Optional<FormToVerify> validatePublishRequest(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Form form = formRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        if (formRepository.getAllByCreatorId(user.getId()).stream().noneMatch(sheet -> sheet.getId().equals(form.getId()))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        return formToVerifyRepository.findFormToVerifyBySheetId(id);
    }

    public void publishForm(String token, String id, Boolean publishFlag) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            Form form = formRepository.findById(id)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            FormToVerify formToVerify = formToVerifyRepository.findFormToVerifyBySheetId(id)
                            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
            form.setIsPublic(publishFlag);
            formRepository.save(form);
            formToVerifyRepository.delete(formToVerify);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public List<FormResponse> getAnsweredForms(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Answer> answers = answerRepository.findAllByUserId(user.getId());
        List<String> answeredFormIds = answers.stream().map(Answer::getSheetId).toList();
        return formRepository.findAllByIdIn(answeredFormIds).stream().map(FormResponse::new).toList();
    }

    public void deleteForm(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        List<Form> userForms = formRepository.getAllByCreatorId(user.getId());
        if (userForms.stream().anyMatch(form -> form.getId().equals(id))) {
            formRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    public List<FormManageResponse> getManageForms(String token) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            List<FormManageResponse> formManageResponses = new ArrayList<>();
            List<Form> forms = formRepository.findAll();
            forms.forEach(form -> {
                Optional<User> creator = userRepository.findById(form.getCreatorId());
                if (creator.isPresent()) {
                    formManageResponses.add(new FormManageResponse(form, creator.get()));
                } else {
                    formManageResponses.add(new FormManageResponse(form));
                }
            });
            return formManageResponses;
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public void adminDeleteForm(String token, String id) {
        User user = userRepository
                .findAccountByEmailOrLogin(
                        jwtTokenUtility.getUsernameFromToken(token),
                        jwtTokenUtility.getUsernameFromToken(token))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        if (user.getRole().equals("ADMIN")) {
            formRepository.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}
