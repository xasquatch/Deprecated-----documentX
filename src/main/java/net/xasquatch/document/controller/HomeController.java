package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Member;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Controller
@RequestMapping(produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class HomeController {




    @GetMapping("/")
    public String Home(Model model, HttpServletRequest request,
                       @AuthenticationPrincipal Member sessionMember) {

        model.addAttribute("sessionMember", sessionMember);

        return "index";
    }

    @RequestMapping(path = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage() {
        return "contents/login";
    }

}
