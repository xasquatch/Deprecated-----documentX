package net.xasquatch.document.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
@PropertySource("/WEB-INF/setting.properties")
public class CustomServletAppContext implements WebMvcConfigurer {

    @Value("${files.context.path}")
    private String filesContextPath;

    // Controller의 메서드가 반환하는 jsp의 이름 앞뒤에 경로와 확장자를 붙혀주도록 설정한다.
    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        WebMvcConfigurer.super.configureViewResolvers(registry);
        registry.jsp("/WEB-INF/views/", ".jsp");
    }

    // 정적 파일의 경로를 매핑한다.
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        WebMvcConfigurer.super.addResourceHandlers(registry);
        registry.addResourceHandler("/webjars/**").addResourceLocations("/webjars/");
        registry.addResourceHandler(filesContextPath + "/**").addResourceLocations("file:///D:" + filesContextPath + "/");
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
        registry.addResourceHandler("/favicon.ico").addResourceLocations("/resources/favicon.ico");
    }

    @Bean
    HandlerExceptionResolver customExceptionResolver () {
        CustomSimpleMappingExceptionResolver resolver = new CustomSimpleMappingExceptionResolver();
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

}