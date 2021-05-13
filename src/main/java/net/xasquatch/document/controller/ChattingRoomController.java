package net.xasquatch.document.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.ChattingRoom;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.ChattingService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping(path = "/chatting", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
@RequiredArgsConstructor
public class ChattingRoomController {

    private final ChattingService chattingService;

    //TODO: 페이지네이션 구현 필요
    @GetMapping("/room-list")
    public String goToChattingRoomList(Model model, @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);
        model.addAttribute("chattingRoomList", chattingService.getChattingRoomList());

        return "contents/chatting/room-list";
    }

    @GetMapping("/{roomNo}")
    public String goToChattingRoom(Model model, @PathVariable String roomNo,
                                   @AuthenticationPrincipal Member member) {
        model.addAttribute("sessionMember", member);
        model.addAttribute("chattingRoom", chattingService.getChattingRoom(roomNo));

        return "contents/chatting/room";
    }

    @PostMapping("/new/{title}")
    @ResponseBody
    public String createChattingRoom(@PathVariable String title,
                                     @RequestParam(required = false, name = "pwd") String pwd) {
        String redirectUrl = "false";
        ChattingRoom chattingRoom = null;
        try {
            chattingRoom = chattingService.createChattingRoom(title, pwd);
            redirectUrl = "/chatting/" + chattingRoom.getNo();

        } catch (Exception e) {
            log.error("채팅방 생성에러: {}", e.getMessage());
        }

        return redirectUrl;
    }
}
