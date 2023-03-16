package it.bitrock.bitrockairways.controller;

import ch.qos.logback.core.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private static String authorizationRequestBaseUri
            = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

    @GetMapping("/login/success")
    public String success(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        return "success.html";
    }



    @GetMapping("/login/oauth2")
    public String loginOauth() {
        return "success.html";
    }

    @GetMapping("/success")
    public String main(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        return "success.html";
    }

    @GetMapping("/")
    public String tempMain(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        return "success.html";
    }

    @GetMapping("/login")
    public String login() {
        Iterable<ClientRegistration> clientRegistrations = null;
        ResolvableType type = ResolvableType.forInstance(clientRegistrationRepository)
                .as(Iterable.class);
        if (type != ResolvableType.NONE &&
                ClientRegistration.class.isAssignableFrom(type.resolveGenerics()[0])) {
            clientRegistrations = (Iterable<ClientRegistration>) clientRegistrationRepository;
        }

        clientRegistrations.forEach(registration ->
                oauth2AuthenticationUrls.put(registration.getClientName(),
                        authorizationRequestBaseUri + "/" + registration.getRegistrationId()));

        //model.addAttribute("urls", oauth2AuthenticationUrls);

        return "login.html";
    }


    /*private final Iterable<ClientRegistration> clientRegistrationRepository;

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
    }*/
}
