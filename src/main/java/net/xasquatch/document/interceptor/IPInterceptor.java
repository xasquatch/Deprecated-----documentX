package net.xasquatch.document.interceptor;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.interceptor.parts.AccessorInfo;
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



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean permit = false;

        String ip = accessorInfo.getIpAddress(request);
        log.info("--------" + ip + "--------");
        if (!ip.isBlank())
            permit = true;

        return permit;
    }
}
