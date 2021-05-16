package net.xasquatch.document.config;

import net.xasquatch.document.interceptor.ChatInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.*;

import java.util.Properties;

// Spring MVC 프로젝트에 관련된 설정을 하는 클래스
@Configuration
// Controller 어노테이션이 셋팅되어 있는 클래스를 Controller로 등록한다.
@EnableWebMvc
// 스캔할 패키지를 지정한다.
@ComponentScan("net.xasquatch.document.config")
@ComponentScan("net.xasquatch.document.controller")
@ComponentScan("net.xasquatch.document.service")
@ComponentScan("net.xasquatch.document.repository")
@ComponentScan("net.xasquatch.document.interceptor")
@ComponentScan("net.xasquatch.document.model")
@PropertySource("/WEB-INF/setting.properties")
public class CustomServletAppContext implements WebMvcConfigurer {

    @Value("${files.context.path}")
    private String filesContextPath;

    @Autowired
    private ChatInterceptor interceptor;

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        WebMvcConfigurer.super.configureViewResolvers(registry);
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
        registry.addResourceHandler(filesContextPath + "/**").addResourceLocations("file:///D:" + filesContextPath + "/");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/resources/favicon.ico");
    }

    @Bean
    HandlerExceptionResolver customExceptionResolver() {
        CustomMappingExceptionResolver resolver = new CustomMappingExceptionResolver();
        Properties mappings = new Properties();
        // Mapping Spring internal error NoHandlerFoundException to a view name
        mappings.setProperty(NoHandlerFoundException.class.getName(), "/error/404");
        mappings.setProperty(NullPointerException.class.getName(), "/error/500");
        mappings.setProperty(ClassNotFoundException.class.getName(), "/error/500");
        mappings.setProperty(Exception.class.getName(), "/error/generic");
        resolver.setExceptionMappings(mappings);
        // Set specific HTTP codes
        resolver.addStatusCode("404", HttpStatus.NOT_FOUND.value());
        resolver.addStatusCode("500", HttpStatus.INTERNAL_SERVER_ERROR.value());
        resolver.setDefaultErrorView("/error/generic");
        resolver.setDefaultStatusCode(200);
        // This resolver will be processed before the default ones
        resolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
        resolver.setExceptionAttribute("exception");
        return resolver;
    }

//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor((HandlerInterceptor) interceptor)
//                .addPathPatterns("/chat**");
//        WebMvcConfigurer.super.addInterceptors(registry);
//    }
}