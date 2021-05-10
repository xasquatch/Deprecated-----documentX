package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.repository.MemberDao;
import net.xasquatch.document.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Collection;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String Home(Model model) {
        model.addAttribute("home", "success");
        return "index";
    }

    @GetMapping("/connection")
    @ResponseBody
    public String isConnect() {

        log.debug("hi");

        return "true";
    }

}
