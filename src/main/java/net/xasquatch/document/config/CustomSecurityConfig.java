package net.xasquatch.document.config;

import lombok.extern.slf4j.Slf4j;
import net.xasquatch.document.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@PropertySource("/WEB-INF/setting.properties")
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value("${security.remember.me}")
    private String rememberKey;

    @Autowired
    private MemberService memberService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login",
                        "/members/available-email/**", "/members/available-nick-name/**",
                        "/members/new/**", "/members/confirm-token/**",
                        "/resources/**", "/webjars/**").permitAll()
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

        //TODO: 너무 제약이 심해서 일단 제한해서 구현완료하고 추가 작업할 예정
        http
                .csrf().disable();
        http
                .sessionManagement()
                .sessionConcurrency(concurrencyControlConfigurer -> {
                    concurrencyControlConfigurer.maximumSessions(1);
                    concurrencyControlConfigurer.maxSessionsPreventsLogin(false);
                });

        http
                .rememberMe().key(rememberKey);


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
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}