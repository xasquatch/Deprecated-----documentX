package net.xasquatch.document.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/members")
public class MemberController {

    @GetMapping("/{memberNickName}")
    public String userInfo(@PathVariable String memberNickName) {

        return "contents/member/information";
    }

    @GetMapping("/{memberNickName}/files")
    public String userFileList(@PathVariable String memberNickName) {

        return "contents/file/list";
    }

    @GetMapping("/{memberNickName}/chatting-room")
    public String userChattingList(@PathVariable String memberNickName) {

        return "contents/chatting/room-list";
    }


}
