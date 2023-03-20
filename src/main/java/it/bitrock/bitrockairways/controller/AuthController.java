package it.bitrock.bitrockairways.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final Iterable<ClientRegistration> clientRepository;

    public AuthController(Iterable<ClientRegistration> clientRepository) {
        this.clientRepository = clientRepository;
    }

    @GetMapping("/login")
    public String login(Model model) {
        Map<String, String> repo = new HashMap<>();
        clientRepository.forEach(repository -> repo.put(repository.getClientName(), "oauth2/authorization/" + repository.getRegistrationId()));
        model.addAttribute("urls", repo);
        return "login";
    }

    @GetMapping("/login/failure")
    public String loginFailure() {
        return "login-failure";
    }

    @GetMapping("/login/success")
    public String loginSuccess(OAuth2AuthenticationToken token) {
        if (token == null) {
            return "page-404";
        }
        return "login-success";
    }


    @GetMapping("/not-found")
    public String page404() {
        return "page-404";
    }

}
