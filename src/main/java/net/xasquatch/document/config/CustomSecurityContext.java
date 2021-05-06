package net.xasquatch.document.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@Slf4j
public class CustomSecurityContext extends WebSecurityConfigurerAdapter {
/*

    @Autowired
    private UserDetailService userDetailService;
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;

*/

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        log.debug("hi");
        http
                .authorizeRequests()
                    .antMatchers("/", "/index.jsp", "/home", "/favicon.ico", "/resources/**", "/publish/**")
                    .permitAll()
                    .antMatchers("/secure/**", "/manage/**", "/admin/**", "/comment/admin/**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .authenticated()
                    .and()
                .formLogin()
                    .loginPage("/user/loginForm")
                    .loginProcessingUrl("/j_spring_security_check")
                    .usernameParameter("j_username")
                    .passwordParameter("j_password")
    //                .successHandler(loginSuccessHandler)
    //                .permitAll()
                    .and()
                .logout()
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/home")
                    .and()
                .httpBasic();
    }

/*
    @Override
    protected UserDetailsService userDetailsService() {
        return userDetailService;
    }
*/

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
