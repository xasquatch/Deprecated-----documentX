package net.xasquatch.document.config;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login", "/resources/**", "/webjars/**").permitAll()
                .antMatchers("/management/**").hasAnyRole("MANAGEMENT")
                .antMatchers("/members/**").hasAnyRole("MANAGEMENT", "USER")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/loginProcess")
                .successHandler(
                        (request, response, authentication) -> {
                            response.sendRedirect("/");
                        })
                .failureHandler(
                        (request, response, exception) -> {
                            request.setAttribute("navMassage", "로그인에 실패하였습니다.");
                            request.setAttribute("email", request.getParameter("email"));
                            request.getRequestDispatcher("/login").forward(request, response);

                        })
                .usernameParameter("email")
                .passwordParameter("pwd")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("user@test.com").password("{noop}11111111").roles("USER").and()
//                .withUser("admin@test.com").password("{noop}11111111").roles("MANAGEMENT").and()
//                .withUser("master@test.com").password("{noop}11111111").roles("USER", "MANAGEMENT");
        auth
                .userDetailsService(memberService)
                .passwordEncoder(encoder())
        ;

    }

    @Bean
    public PasswordEncoder encoder(){
        return new BCryptPasswordEncoder();
    }

}