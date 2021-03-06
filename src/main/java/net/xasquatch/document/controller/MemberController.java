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
import java.util.Map;

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
                    "Document X: ????????? ??????",
                    "<div>" +
                            "<h1>[Document X] " + email + "??? ???????????????</h1>" +
                            "<BR><BR><BR>" +
                            "<a href=\"" + domain + "/members/confirm-token/" + email + "?" +
                            "token=" + token + "\">" +
                            "??? ????????? ???????????? ????????? ???????????????.<BR>" +
                            "</a><BR>" +
                            "?????? ????????? ?????? ???<BR>" +
                            domain + "/members/confirm-token/" + email + "?token=" + token +
                            "<BR>?????? url??? ???????????? ??????????????????<BR>" +
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
    public String goToMemberFileList(Model model, @AuthenticationPrincipal Member member,
                                     @RequestParam(required = false, name = "search-value") String searchValue,
                                     @RequestParam(required = false, name = "current-page", defaultValue = "1") int currentPage,
                                     @RequestParam(required = false, name = "row-count", defaultValue = "30") int pageLimit) {

        model.addAttribute("sessionMember", member);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("rowCount", pageLimit);

        return "contents/file/list";
    }

    @GetMapping("/{nickName}/files")
    @ResponseBody
    public String getFileList(@AuthenticationPrincipal Member member,
                              @RequestParam(required = false, name = "search-value") String searchValue,
                              @RequestParam(required = false, name = "current-page", defaultValue = "1") int currentPage,
                              @RequestParam(required = false, name = "row-count", defaultValue = "30") int pageLimit) {

        Map<String, Object> resultMap = storageService.getFileList(member, searchValue, currentPage, pageLimit);
        ObjectWriter writer = new ObjectMapper().writerWithDefaultPrettyPrinter();
        try {
            return writer.writeValueAsString(resultMap);

        } catch (JsonProcessingException e) {
            log.error("??????????????? ?????? ?????????: {}", e.getMessage());
            return null;
        }
    }

    @PostMapping("/{nickName}/files/new")
    @ResponseBody
    public String uploadFileList(MultipartHttpServletRequest request,
                                 @AuthenticationPrincipal Member member) {
        String result = "false";
        try {
            request.setCharacterEncoding("UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.warn("acquired ImgUpload Exception");
        }
        int uploadedFileCount = storageService.uploadFile(request, String.valueOf(member.getNo()));
        if (uploadedFileCount != 0)
            result = uploadedFileCount + "?????? ????????? ???????????? ?????????????????????.";

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
    public String goToMemberChattingRoom(Model model, @AuthenticationPrincipal Member sessionMember,
                                         @PathVariable String nickName,
                                         @PathVariable String chattingRoomTitle,
                                         @RequestParam(required = false, name = "search-value") String searchValue,
                                         @RequestParam(required = false, name = "current-page", defaultValue = "1") int currentPage,
                                         @RequestParam(required = false, name = "row-count", defaultValue = "30") int pageLimit) {

        model.addAttribute("sessionMember", sessionMember);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("rowCount", pageLimit);

        return "contents/chatting/room";
    }

    @GetMapping("/email/{email}")
    @ResponseBody
    public String getMemberInfoByEmail(@PathVariable String email) throws JsonProcessingException {
        Map<String, Object> clientInfo = memberService.getMemberInfoByEmail(email);
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();

        return objectWriter.writeValueAsString(clientInfo);
    }
}