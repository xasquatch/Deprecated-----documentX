package net.xasquatch.document.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Slf4j
@EnableWebSecurity
public class CustomSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/resources/**", "/webjars/**").permitAll()
                .antMatchers("/management/**").hasAnyRole("MANAGEMENT")
                .antMatchers("/members/**").hasAnyRole("MANAGEMENT", "USER")
                .anyRequest().authenticated();

        http
                .formLogin()
                .loginPage("/sign-in")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .failureUrl("/sign-in")
                .successHandler(
                        (request, response, authentication) -> {
                            response.sendRedirect("/");
                        })
                .failureHandler(
                        (request, response, exception) -> {
                            request.setAttribute("navMassage", "로그인에 실패하였습니다.");
                            request.setAttribute("email", request.getParameter("email"));
                            request.getRequestDispatcher("/sign-in").forward(request, response);

                        })
                .usernameParameter("email")
                .passwordParameter("pwd")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user@test.com").password("{noop}a12345678").roles("USER").and()
                .withUser("admin@test.com").password("{noop}a12345678").roles("MANAGEMENT").and()
                .withUser("master@test.com").password("{noop}a12345678").roles("USER", "MANAGEMENT");
    }
}