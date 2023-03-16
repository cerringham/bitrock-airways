package it.bitrock.bitrockairways.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login/oauth2")
    public String loginOauth() {
        return "success.html";
    }

    @GetMapping("/success")
    public String main() {
        //OAuth2AuthenticationToken token
        //System.out.println(token.getPrincipal());
        return "success.html";
    }

    @GetMapping("/")
    public String tempMain(OAuth2AuthenticationToken token) {
        System.out.println(token.getPrincipal());
        return "success.html";
    }

    @GetMapping("/login")
    public String login() {
        return "login.html";
    }



    /*

    @Autowired
    private ClientRegistrationRepository clientRegistrationRepository;

    private static String authorizationRequestBaseUri
            = "oauth2/authorization";
    Map<String, String> oauth2AuthenticationUrls
            = new HashMap<>();

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

        model.addAttribute("urls", oauth2AuthenticationUrls);*/

}
