package com.etsyclone.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin
public class UserAuthController {

    @RequestMapping("/auth/login")
    public String login() {
        return "login-form";
    }

    @RequestMapping("/auth/signup")
    public String signup() {
        return "register-form";
    }

}
