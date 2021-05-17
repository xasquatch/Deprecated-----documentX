package net.xasquatch.document.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.interceptor.parts.AccessorInfo;
import net.xasquatch.document.model.Member;
import net.xasquatch.document.repository.MemberDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class IPInterceptor implements HandlerInterceptor {

    @Autowired
    private AccessorInfo accessorInfo;

    @Autowired
    private MemberDao memberDao;


    //나중에...
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean permit = false;

        String ip = accessorInfo.getIpAddress(request);
        log.info("--------" + ip + "--------");
        if (!ip.isBlank())
            permit = true;
        Member sessionMember = (Member) request.getAttribute("sessionMember");

        request.getRequestURI();
        return permit;
    }
}
