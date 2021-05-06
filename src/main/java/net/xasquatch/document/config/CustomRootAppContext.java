package net.xasquatch.document.config;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

//@Import({SecurityConfig.class})
@Configuration
@PropertySource("/WEB-INF/setting.properties")
public class CustomRootAppContext {

    /*--------TODO:DB 접속관리--------------*/
    @Value("${db.classname}")
    private String dbClassName;
    @Value("${db.url}")
    private String dbUrl;
    @Value("${db.username}")
    private String dbUsername;
    @Value("${db.password}")
    private String dbPassword;

//    /*--------TODO:Mail 접속관리--------------*/
//    @Value("${email.host}")
//    private String emailHost;
//    @Value("${email.port}")
//    private int emailPort;
//    @Value("${email.username}")
//    private String emailUserName;
//    @Value("${email.password}")
//    private String emailPwd;


    @Bean
    public BasicDataSource dataSource() {
        BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setDriverClassName(dbClassName);
        basicDataSource.setUrl(dbUrl);
        basicDataSource.setUsername(dbUsername);
        basicDataSource.setPassword(dbPassword);

        return basicDataSource;
    }

    @Bean
    public SqlSessionFactory factory(BasicDataSource basicDataSource) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(basicDataSource);

        return sqlSessionFactoryBean.getObject();

    }


    //------------------------------------------------------
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

/*
    @Bean
    public ReloadableResourceBundleMessageSource msgSource() {
        ReloadableResourceBundleMessageSource msg = new ReloadableResourceBundleMessageSource();
        msg.setBasenames("/WEB-INF/properties/home/login", "/WEB-INF/properties/home/signUp");

        return msg;
    }


    @Bean
    public JavaMailSenderImpl mailSender() {
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        Properties mailProperties = new Properties();
        mailProperties.put("mail.transport.protocol", "smtp");
        mailProperties.put("mail.smtp.auth", true);
        mailProperties.put("mail.smtp.starttls.enable", true);
        mailProperties.put("mail.smtp.starttls.required", true);
        mailProperties.put("mail.debug", true);

        mailSender.setJavaMailProperties(mailProperties);
        mailSender.setDefaultEncoding("UTF-8");
        mailSender.setHost(emailHost);
        mailSender.setPort(emailPort);
        mailSender.setUsername(emailUserName);
        mailSender.setPassword(emailPwd);

        return mailSender;
    }
*/


}
