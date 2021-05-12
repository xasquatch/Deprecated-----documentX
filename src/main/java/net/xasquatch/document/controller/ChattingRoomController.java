package net.xasquatch.document.controller;

import net.xasquatch.document.model.Member;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = "/chatting", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class ChattingRoomController {

    @GetMapping("/room-list")
    public String goChattingRoomList(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);

        return "contents/chatting/room-list";
    }

    @GetMapping("/{roomTitle}")
    public String goChattingRoom(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);

        return "contents/chatting/room";
    }

}
