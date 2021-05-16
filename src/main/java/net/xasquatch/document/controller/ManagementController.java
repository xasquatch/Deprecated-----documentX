package net.xasquatch.document.controller;

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
                                    @RequestParam(required = false, name = "current-page", defaultValue = "1") String currentPage,
                                    @RequestParam(required = false, name = "row-count", defaultValue = "50") String pageLimit) {

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
                                   @PathVariable long memberNo) {
        Member member = memberService.getMemberToNumber(memberNo);

        model.addAttribute("member", member);

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

    @GetMapping("/chatting-rooms")
    public String goToManageChattingList() {
        return "contents/management/chatting-room-list";
    }

    @GetMapping("/files")
    public String goToManageFiles(Model model,
                                  @AuthenticationPrincipal Member member,
                                  @RequestParam(required = false, name = "search-value") String searchValue,
                                  @RequestParam(required = false, name = "current-page", defaultValue = "1") String currentPage,
                                  @RequestParam(required = false, name = "row-count", defaultValue = "50") String pageLimit) {

        Map<String, Object> resultMap = storageService.searchFileLIst(member, searchValue, currentPage, pageLimit);

        model.addAttribute("fileList", resultMap.get("storageList"));
        model.addAttribute("pagination", resultMap.get("pagination"));
        model.addAttribute("sessionMember", member);
        model.addAttribute("searchValue", searchValue);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("rowCount", pageLimit);

        return "contents/management/file-list";
    }

}
