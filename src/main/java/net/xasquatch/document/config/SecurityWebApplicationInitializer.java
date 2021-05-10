package net.xasquatch.document.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.*;
import java.util.Collections;

//webApplicationInitializer상속 관계
@PropertySource("/webapp/WEB-INF/setting.properties")
public class SecurityWebApplicationInitializer extends AbstractSecurityWebApplicationInitializer {

    @Value("${files.save.path}")
    private String filesSavePath;

    public SecurityWebApplicationInitializer(){
        super(CustomRootAppContext.class, CustomSecurityConfig.class);

    }

    @Override
    protected void beforeSpringSecurityFilterChain(ServletContext servletContext) {

        // Spring MVC 프로젝트 설정을 위해 작성하는 클래스의 객체를 생성한다.
        AnnotationConfigWebApplicationContext servletAppContext = new AnnotationConfigWebApplicationContext();
        servletAppContext.register(CustomServletAppContext.class);

        // 요청 발생 시 요청을 처리하는 서블릿을 DispatcherServlet으로 설정해준다.
        DispatcherServlet dispatcherServlet = new DispatcherServlet(servletAppContext);
        dispatcherServlet.setThrowExceptionIfNoHandlerFound(true);
        ServletRegistration.Dynamic servlet = servletContext.addServlet("dispatcher", dispatcherServlet);

        // 부가 설정
        servlet.setLoadOnStartup(1);
        servlet.addMapping("/");

        // Bean을 정의하는 클래스를 지정한다
//        AnnotationConfigWebApplicationContext rootAppContext = new AnnotationConfigWebApplicationContext();
//        rootAppContext.register(CustomRootAppContext.class);
//        rootAppContext.register(CustomSecurityConfig.class);
//
//        ContextLoaderListener listener = new ContextLoaderListener(rootAppContext);
//        servletContext.addListener(listener);

        // 파라미터 인코딩 설정
        FilterRegistration.Dynamic filter = servletContext.addFilter("encodingFilter", CharacterEncodingFilter.class);
        filter.setInitParameter("encoding", "UTF-8");
        filter.setInitParameter("forceEncoding", "true");
        filter.addMappingForServletNames(null, false, "dispatcher");

        // 멀티파일 설정
        servlet.setMultipartConfig(new MultipartConfigElement(filesSavePath, 1024 * 1024 * 1024, 1024 * 1024 * 1024, 0));


        //세션 리스너 설정
        servletContext.setSessionTimeout(60 * 60);
        servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));

    }


}