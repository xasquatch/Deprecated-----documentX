package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.repository.ChattingRoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping(path = "/chatting", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class ChattingRoomController {

    @Autowired
    private ChattingRoomDao chattingRoomDao;

    @GetMapping("/room-list")
    public String goToChattingRoomList(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);
        model.addAttribute("chattingRoomList", chattingRoomDao.selectChattingRoomList());

        return "contents/chatting/room-list";
    }

    @GetMapping("/{roomNo}")
    public String goToChattingRoom(Model model, @PathVariable String roomNo,
                                   @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);
        model.addAttribute("chattingRoom", chattingRoomDao.selectChattingRoom(Long.parseLong(roomNo)));

        return "contents/chatting/room";
    }

    @GetMapping("/new")
    public String make(Model model) {
        ChatRoomForm form = new ChatRoomForm();
        model.addAttribute("form", form);
        return "newRoom";
    }

    @PostMapping("/room/new")
    public String makeRoom(ChatRoomForm form) {
        chatRoomRepository.createChatRoom(form.getName());

        return "redirect:/";
    }
}
