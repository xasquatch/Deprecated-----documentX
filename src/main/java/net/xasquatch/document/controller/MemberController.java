package net.xasquatch.document.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.model.StorageEntity;
import net.xasquatch.document.service.MailService;
import net.xasquatch.document.service.StorageService;
import net.xasquatch.document.service.TokenMap;
import net.xasquatch.document.service.command.MemberServiceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/members", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
@PropertySource("/WEB-INF/setting.properties")
public class MemberController {

    @Value("${project.domain}")
    private String domain;

    @Autowired
    private MemberServiceInterface memberService;

    @Autowired
    private StorageService storageService;

    @Autowired
    private MailService mailService;

    @Autowired
    private TokenMap tokenMap;

    @GetMapping("/available-email/{email}")
    @ResponseBody
    public String isAvailableEmail(@PathVariable("email") String email) {
        return String.valueOf(memberService.isAvailableEmail(email));

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
                            "이 링크를 클릭하면 인증이 완료됩니다.<BR>" +
                            "</a><BR>" +
                            "링크 입장이 안될 시<BR>" +
                            domain + "/members/confirm-token/" + email + "?token=" + token +
                            "<BR>위의 url을 주소창에 입력해주세요<BR>" +
                            "</div>");
            log.debug("send To: {}", domain + "/members/confirm-token/" + email + "?token=" + token);
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
    public String modifyMember(@AuthenticationPrincipal Member sessionMember,
                               @Valid Member member, BindingResult bindingResult) {
        String result = "false";
        if (bindingResult.hasErrors()) return result;

        member.setNo(sessionMember.getNo());
        result = String.valueOf(memberService.modifyMember(member));

        return result;
    }

    @DeleteMapping("/{nickName}")
    @ResponseBody
    public String removeMember(@AuthenticationPrincipal Member member) {
        return String.valueOf(memberService.removeMember(member));
    }

    @GetMapping("/{nickName}")
    public String goToMemberInfo(Model model, @AuthenticationPrincipal Member sessionMember) {
        model.addAttribute("sessionMember", sessionMember);

        return "contents/member/information";
    }

    @GetMapping("/{nickName}/files/management")
    public String goToMemberFileList(Model model, @AuthenticationPrincipal Member member) {

        model.addAttribute("sessionMember", member);

        return "contents/file/list";
    }

    @GetMapping("/{nickName}/files")
    @ResponseBody
    public String getFileList(@AuthenticationPrincipal Member member) {

        List<StorageEntity> fileList = storageService.getFileList(member.getNo());
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(fileList);

        } catch (JsonProcessingException e) {
            log.error("파일리스트 얻기 익셉션: {}", e.getMessage());
            return null;
        }
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
        int uploadedFileCount = storageService.uploadFile(request, String.valueOf(member.getNo()));
        if (uploadedFileCount != 0)
            result = uploadedFileCount + "개의 파일이 업로드가 완료되었습니다.";

        return result;
    }

    @PutMapping("/{nickName}/files/{fileName}")
    @ResponseBody
    public String renameFile(StorageEntity entity,
                             @RequestParam(required = true, name = "renameString") String renameString) {

        return String.valueOf(storageService.renameFile(entity, renameString));
    }

    @DeleteMapping("/{nickName}/files/{fileName}")
    @ResponseBody
    public String deleteFile(@RequestParam(required = true, name = "fileNo") long fileNo) {
        return String.valueOf(storageService.deleteFile(fileNo));
    }

    @GetMapping("/{nickName}/chatting-rooms")
    public String goToMemberChattingList(@PathVariable String nickName) {

        return "contents/chatting/room-list";
    }

    @GetMapping("/{nickName}/chatting-rooms/{chattingRoomTitle}")
    public String goToMemberChattingRoom(@PathVariable String nickName,
                                         @PathVariable String chattingRoomTitle) {


        return "contents/chatting/room";
    }
}