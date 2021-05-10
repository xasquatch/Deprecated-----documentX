package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;

@Slf4j
@Controller
public class LoginController {

    @RequestMapping(path = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage() {
        return "contents/login";
    }
/*

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
*/
}