package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping(produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class HomeController {

    @Autowired
    private MemberService memberService;


    @GetMapping("/")
    public String Home(Model model, @AuthenticationPrincipal Member sessionMember) {

        List<Map<String, Object>> messageCountList = memberService.getMemberMessageCount(sessionMember.getNo());

        model.addAttribute("sessionMember", sessionMember);
        model.addAttribute("messageCountList", messageCountList);

        return "index";
    }

    @RequestMapping(path = "/login", method = {RequestMethod.GET, RequestMethod.POST})
    public String loginPage() {
        return "contents/login";
    }

}
