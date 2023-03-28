package it.bitrock.bitrockairways.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecureController {

    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/success")
    public String success() {
        return "success";
    }

    @GetMapping("/failure")
    public String failure() {
        return "failure";
    }


}
