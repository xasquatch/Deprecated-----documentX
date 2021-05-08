package net.xasquatch.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

    @GetMapping("/{memberNickName}")
    public String goUserInfo(@PathVariable String memberNickName) {

        return "contents/member/information";
    }

    @GetMapping("/{memberNickName}/files")
    public String goUserFileList(@PathVariable String memberNickName) {

        return "contents/file/list";
    }

    @GetMapping("/{memberNickName}/chatting-rooms")
    public String goUserChattingList(@PathVariable String memberNickName) {

        return "contents/chatting/room-list";
    }

    @GetMapping("/{memberNickName}/chatting-rooms/{chattingRoomTitle}")
    public String goUserChattingRoom(@PathVariable String memberNickName,
                                     @PathVariable String chattingRoomTitle) {

        return "contents/chatting/room";
    }


}
