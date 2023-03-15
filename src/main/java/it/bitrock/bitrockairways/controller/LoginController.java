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
public class LoginController {

    private final Iterable<ClientRegistration> clientRegistrationRepository;

    public LoginController(Iterable<ClientRegistration> clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/oauth_login")
    public String login(Model model) {
        Map<String, String> repositories = new HashMap<>();
        clientRegistrationRepository.forEach(repository ->
                repositories.put(
                        repository.getClientName(),
                        "oauth2/authorization/" + repository.getRegistrationId()
                ));
        model.addAttribute("urls", repositories);
        return "login";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(Model model, OAuth2AuthenticationToken token) {
        OAuth2User principal = token.getPrincipal();
        model.addAttribute("username", principal.getAttribute("name"));
        return "login-success";
    }

    @GetMapping("/loginFailure")
    public String loginFailure() {
        return "login-failure";
    }
}
