package net.xasquatch.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/management")
public class ManagementController {

    @GetMapping("/members")
    public String manageMembers(){
        return "contents/management/member-list";
    }

    @GetMapping("/chatting-rooms")
    public String manageChattingList(){
        return "contents/management/chatting-room-list";
    }

    @GetMapping("/files")
    public String manageFiles(){
        return "contents/management/file-list";
    }

}
