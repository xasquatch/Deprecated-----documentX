package net.xasquatch.document.controller;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.MailService;
import net.xasquatch.document.service.TokenMap;
import net.xasquatch.document.service.command.StorageServiceInterface;
import net.xasquatch.document.service.command.MemberServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;

@Slf4j
@Controller
@RequestMapping("/members")
@PropertySource("/WEB-INF/setting.properties")
public class MemberController {

    @Value("${project.domain}")
    private String domain;

    @Autowired
    private MemberServiceInterface memberService;

    @Autowired
    private StorageServiceInterface storageService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenMap tokenMap;

    @GetMapping("/available-email/{email}")
    @ResponseBody
    public String isAvailableEmail(@PathVariable String email) {
        String result = "false";
        result = String.valueOf(memberService.isAvailableEmail(email));

        return result;
    }

    @GetMapping("/confirm-token/count-down")
    @ResponseBody
    public String startTokenCountDown(@RequestParam(required = true, name = "email") String email) {
        String result = "true";
        try {
            String token = mailService.createToken(email, 6);
            mailService.sendAuthMail(email,
                    "Document X: 이메일 인증",
                    "<div>" +
                            "<h1>[Document X] " + email + "님 환영합니다</h1>" +
                            "<BR><BR><BR>" +
                            "<a href=\"" + domain + "/members/confirm-token/" + email + "?" +
                            "token=" + token + "\">" +
                            "해당 링크를 클릭하면 인증이 완료됩니다." +
                            "</a>" +
                            "</div>");

        } catch (Exception e) {
            return result;
        }

        return result;
    }

    @GetMapping("/confirm-token/{email}")
    @ResponseBody
    public void confirmEmail(@RequestParam(required = true, name = "token") String token) {
        memberService.isConfirmEmailToken(token);
    }

    @GetMapping("/confirm-token/{email}/status")
    @ResponseBody
    public String isConfirmedEmail(@PathVariable String email) {

        return String.valueOf(tokenMap.isConfirmedTokenAuthorization(email));
    }

    @GetMapping("/available-nick-name/{nickName}")
    @ResponseBody
    public String isAvailableNickName(@PathVariable String nickName) {
        String result = "false";
        result = String.valueOf(memberService.isAvailableNickName(nickName));

        return result;
    }

    @PostMapping("/new/{nickName}")
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
    @ResponseBody
    public String removeMember(Member member) {
        String result = "false";
        result = String.valueOf(memberService.removeMember(member));

        return result;
    }

    @GetMapping("/{nickName}")
    public String goMemberInfo(Model model, @AuthenticationPrincipal Member sessionMember) {
        model.addAttribute("sessionMember", sessionMember);

        return "contents/member/information";
    }

    @GetMapping("/{nickName}/files")
    public String goMemberFileList(Model model, @PathVariable String nickName) {


        return "contents/file/list";
    }

    @PostMapping("/{nickName}/files/new")
    @ResponseBody
    public String uploadFileList(@AuthenticationPrincipal Member member,
                                 MultipartHttpServletRequest request) {
        String result = "false";
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("acquired ImgUpload Exception");
        }
        result = String.valueOf(storageService.uploadFile(request, String.valueOf(member.getNo())));
        return result;
    }

    @DeleteMapping("/{nickName}/files/{fileNo}")
    @ResponseBody
    public String deleteFile(@AuthenticationPrincipal Member member,
                             @PathVariable String fileNo,
                             MultipartHttpServletRequest request) {
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("acquired ImgUpload Exception");
        }

        return null;
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
