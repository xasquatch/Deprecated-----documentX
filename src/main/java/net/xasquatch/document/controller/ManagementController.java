package net.xasquatch.document.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.MemberService;
import net.xasquatch.document.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping(path = "/management", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class ManagementController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/members")
    public String goToManageMembers(Model model,
                                    @AuthenticationPrincipal Member member,
                                    @RequestParam(required = false, name = "search-value") String searchValue,
                                    @RequestParam(required = false, name = "current-page", defaultValue = "1") int currentPage,
                                    @RequestParam(required = false, name = "row-count", defaultValue = "30") int pageLimit) {

        Map<String, Object> resultMap = memberService.searchMemberList(searchValue, currentPage, pageLimit);
        model.addAttribute("memberList", resultMap.get("memberList"));
        model.addAttribute("pagination", resultMap.get("pagination"));
        model.addAttribute("sessionMember", member);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("rowCount", pageLimit);

        return "contents/management/member-list";
    }

    @GetMapping("/members/{memberNo}")
    public String goToManageMember(Model model,
                                   @PathVariable long memberNo) throws JsonProcessingException {
        Member member = memberService.getMemberToNumber(memberNo);

        model.addAttribute("member", member);
        model.addAttribute("permissionList", memberService.getPermissionList());

        return "contents/management/member-information";
    }

    @DeleteMapping("/members/{memberNo}")
    @ResponseBody
    public String removeMember(@PathVariable long memberNo,
                               @RequestParam(required = true, name = "email") String email) {
        Member tempMember = new Member();
        tempMember.setNo(memberNo);
        tempMember.setEmail(email);
        return String.valueOf(memberService.removeMember(tempMember));
    }

    @PutMapping("/members/{memberNo}")
    @ResponseBody
    public String modifyMember(Member member) {
        return String.valueOf(memberService.modifyMemberForManagement(member));
    }

    @GetMapping("/chatting-rooms")
    public String goToManageChattingList() {
        return "contents/management/chatting-room-list";
    }

    @GetMapping("/files")
    public String goToManageFiles(Model model,
                                  @AuthenticationPrincipal Member member,
                                  @RequestParam(required = false, name = "search-value") String searchValue,
                                  @RequestParam(required = false, name = "current-page", defaultValue = "1") int currentPage,
                                  @RequestParam(required = false, name = "row-count", defaultValue = "30") int pageLimit) {

        Map<String, Object> resultMap = storageService.searchFileLIst(member, searchValue, currentPage, pageLimit);

        model.addAttribute("fileList", resultMap.get("storageList"));
        model.addAttribute("pagination", resultMap.get("pagination"));
        model.addAttribute("sessionMember", member);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("rowCount", pageLimit);

        return "contents/management/file-list";
    }

    @PostMapping("/members/{memberNo}/permissions")
    @ResponseBody
    public String addPermission(@PathVariable long memberNo,
                                   @RequestParam(required = true, name = "permission") String authName) {

        return String.valueOf(memberService.addPermission(memberNo, authName));
    }

    @PatchMapping("/members/{memberNo}/permissions")
    @ResponseBody
    public String modifyPermission(@PathVariable long memberNo,
                                   @RequestParam(required = true, name = "permission") String authName) {

        return String.valueOf(memberService.modifyPermission(memberNo, authName));
    }

    @DeleteMapping("/members/{memberNo}/permissions")
    @ResponseBody
    public String removePermission(@PathVariable long memberNo,
                                   @RequestParam(required = true, name = "permission") String authName){
        return String.valueOf(memberService.removePermission(memberNo, authName));
    }

    @GetMapping("/permissions")
    @ResponseBody
    public String getPermissionsList() throws JsonProcessingException {
        ObjectWriter objectWriter = new ObjectMapper().writerWithDefaultPrettyPrinter();
        return objectWriter.writeValueAsString(memberService.getPermissionList());
    }


}
