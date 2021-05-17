package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.ChattingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(path = "/chatting", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class ChattingRoomController {

    @Autowired
    private ChattingService chattingService;

    @GetMapping("/room-list")
    public String goToChattingRoomList(Model model,
                                       @RequestParam(required = false, name = "search-value") String searchValue,
                                       @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);
        model.addAttribute("chattingRoomList", chattingService.getChattingRoomList(searchValue));

        return "contents/chatting/room-list";
    }

    @GetMapping("/{roomNo}")
    public String goToChattingRoom(Model model, @PathVariable long roomNo,
                                   @AuthenticationPrincipal Member member) {

        model.addAttribute("sessionMember", member);
        model.addAttribute("chattingRoom", chattingService.getChattingRoom(roomNo));

        return "contents/chatting/room";
    }

    @PostMapping("/new/{title}")
    @ResponseBody
    public String createChattingRoom(@PathVariable String title,
                                     @AuthenticationPrincipal Member member,
                                     @RequestParam(required = false, name = "pwd") String pwd) {
        String redirectUrl = "false";
        ChattingRoom chattingRoom = null;
        try {
            chattingRoom = chattingService.createChattingRoom(member.getNo(), title, pwd);
            redirectUrl = "/chatting/" + chattingRoom.getNo();

        } catch (Exception e) {
            log.error("채팅방 생성에러: {}", e.getMessage());
        }

        return redirectUrl;
    }
}
