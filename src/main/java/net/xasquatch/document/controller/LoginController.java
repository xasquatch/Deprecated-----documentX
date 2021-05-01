package net.xasquatch.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginController{

    @GetMapping("/login")
    public String login() {
        return "contents/login";
    }

    @GetMapping("/management")
    public String admin(Model model) {
        model.addAttribute("obj", "admin");
        return "contents/management/dashboard";
    }

    @GetMapping("/member")
    public String member(Model model) {
        model.addAttribute("obj", "member");
        return "contents/member/dashboard";
    }

    @GetMapping("/guest")
    @ResponseBody
    public String all(Model model) {
//        model.addAttribute("obj", "GUEST");
        return "GUEST";
    }
}
