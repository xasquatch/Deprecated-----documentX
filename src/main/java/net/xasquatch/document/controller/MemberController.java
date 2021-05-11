package net.xasquatch.document.controller;

import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.command.MemberServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/members")
public class MemberController {

    @Autowired
    private MemberServiceInterface memberService;

    @GetMapping("/available-email/{email}")
    @ResponseBody
    public String isAvailableEmail(@PathVariable String email) {
        String result = "false";
        result = String.valueOf(memberService.isAvailableEmail(email));

        return result;
    }

    @GetMapping("/confirm-token/{email}")
    @ResponseBody
    public String isConfirmedEmail(@PathVariable String email) {
        String result = "false";
        result = String.valueOf(memberService.isConfirmEmail(email));

        return result;
    }

    @GetMapping("/available-nick-name/{nickName}")
    @ResponseBody
    public String isAvailableNickName(@PathVariable String nickName) {
        String result = "false";
        result = String.valueOf(memberService.isAvailableNickName(nickName));

        return result;
    }

    @PostMapping("/{nickName}")
    @ResponseBody
    public String signUpMember(@Valid Member member, BindingResult bindingResult) {
        String result = "false";

        if (bindingResult.hasErrors()) return result;
        result = String.valueOf(memberService.addMember(member));

        return result;
    }

    @PutMapping("/{nickName}")
    @ResponseBody
    public String modifyMember(@Valid Member member, BindingResult bindingResult) {
        String result = "false";

        if (bindingResult.hasErrors()) return result;
        result = String.valueOf(memberService.modifyMember(member));

        return result;
    }

    @DeleteMapping("/{nickName}")
    public String removeMember(Model model, Member member) {
        boolean isRemoved = memberService.removeMember(member);
        if (!isRemoved) {
            model.addAttribute("navMassage", "[회원탈퇴 완료] 그 동안 이용해주셔서 감사합니다.");
            return "contents/login";

        }else{
            model.addAttribute("navMassage", "[회원탈퇴 에러] 회원탈퇴에 실패하였습니다. 다시시도해주세요");
            return "contents/member/information";
        }
    }

    @GetMapping("/{nickName}")
    public String goMemberInfo(@PathVariable String nickName) {

        return "contents/member/information";
    }

    @GetMapping("/{nickName}/files")
    public String goMemberFileList(@PathVariable String nickName) {

        return "contents/file/list";
    }

    @GetMapping("/{nickName}/chatting-rooms")
    public String goMemberChattingList(@PathVariable String nickName) {

        return "contents/chatting/room-list";
    }

    @GetMapping("/{nickName}/chatting-rooms/{chattingRoomTitle}")
    public String goMemberChattingRoom(@PathVariable String nickName,
                                       @PathVariable String chattingRoomTitle) {

        return "contents/chatting/room";
    }


}
