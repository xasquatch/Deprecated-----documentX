package net.xasquatch.document.controller;

import net.xasquatch.document.model.Member;
import net.xasquatch.document.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
@RequestMapping(path = "/management", produces = {"text/plain;charset=UTF-8", MediaType.ALL_VALUE})
public class ManagementController {

    @Autowired
    private MemberService memberService;

    @GetMapping("/members")
    public String manageMembers(Model model,
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

    @GetMapping("/members/{nick-name}")
    public String manageMember(Model model, Member member,
                               @AuthenticationPrincipal Member sessionMember) {


        return "contents/management/member-list";
    }

    @GetMapping("/chatting-rooms")
    public String manageChattingList() {
        return "contents/management/chatting-room-list";
    }

    @GetMapping("/files")
    public String manageFiles() {
        return "contents/management/file-list";
    }

}
