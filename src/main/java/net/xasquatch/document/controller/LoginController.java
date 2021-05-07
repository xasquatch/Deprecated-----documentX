package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "contents/login";
    }

    @PostMapping("/login")
    public String login() {
        log.debug("login");
        return "redirect:/";
    }

    @GetMapping("/management")
    public String admin(Model model) {
        model.addAttribute("obj", "admin");
        return "contents/management/dashboard";
    }

    @GetMapping("/members")
    public String member(Model model) {
        model.addAttribute("obj", "members");
        return "contents/member/dashboard";
    }

    @GetMapping("/guest")
    @ResponseBody
    public String all(Model model) {
//        model.addAttribute("obj", "GUEST");
        return "GUEST";
    }
}