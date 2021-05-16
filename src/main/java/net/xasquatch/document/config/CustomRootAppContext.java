package net.xasquatch.document.config;

import net.xasquatch.document.mapper.*;
import net.xasquatch.document.repository.AuthorizationDao;
import net.xasquatch.document.repository.MemberDao;
import net.xasquatch.document.service.MemberService;
import net.xasquatch.document.service.TokenMap;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.mapper.MapperFactoryBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;

import java.util.Properties;

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

    /*--------TODO:Mail 접속관리--------------*/
    @Value("${email.host}")
    private String emailHost;
    @Value("${email.port}")
    private String emailPort;
    @Value("${email.username}")
    private String emailUserName;
    @Value("${email.password}")
    private String emailPwd;

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

    @Bean
    public MapperFactoryBean<MemberMapper> getMemberMapper(SqlSessionFactory factory) {
        MapperFactoryBean<MemberMapper> factoryBean = new MapperFactoryBean<MemberMapper>(MemberMapper.class);
        factoryBean.setSqlSessionFactory(factory);
        return factoryBean;

    }

    @Bean
    public MapperFactoryBean<AuthorizationMapper> getAuthorizationMapper(SqlSessionFactory factory) {
        MapperFactoryBean<AuthorizationMapper> factoryBean = new MapperFactoryBean<AuthorizationMapper>(AuthorizationMapper.class);
        factoryBean.setSqlSessionFactory(factory);
        return factoryBean;

    }

    @Bean
    public MapperFactoryBean<StorageMapper> getStorageMapper(SqlSessionFactory factory) {
        MapperFactoryBean<StorageMapper> factoryBean = new MapperFactoryBean<StorageMapper>(StorageMapper.class);
        factoryBean.setSqlSessionFactory(factory);
        return factoryBean;

    }

    @Bean
    public MapperFactoryBean<ChattingMapper> getChattingMapper(SqlSessionFactory factory) {
        MapperFactoryBean<ChattingMapper> factoryBean = new MapperFactoryBean<ChattingMapper>(ChattingMapper.class);
        factoryBean.setSqlSessionFactory(factory);
        return factoryBean;

    }

    @Bean
    public MapperFactoryBean<MnagementMapper> getMnagementMapper(SqlSessionFactory factory) {
        MapperFactoryBean<MnagementMapper> factoryBean = new MapperFactoryBean<MnagementMapper>(MnagementMapper.class);
        factoryBean.setSqlSessionFactory(factory);
        return factoryBean;

    }

    //------------------------------------------------------
    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
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
        mailSender.setPort(Integer.parseInt(emailPort));
        mailSender.setUsername(emailUserName);
        mailSender.setPassword(emailPwd);

        return mailSender;
    }

    //    ---------------security----------------
    @Bean
    public MemberService memberService() {
        return new MemberService();
    }

    @Bean
    public MemberDao memberDao() {
        return new MemberDao();
    }

    @Bean
    public AuthorizationDao authorizationDao() {
        return new AuthorizationDao();
    }

    //    ---------email인증 tokenMap-----------
    @Bean
    public TokenMap tokenMap() {
        return new TokenMap();
    }

//    ---------chatting 서비스-----------
//    @Bean
//    public ChattingService chattingService() {
//        return new ChattingService();
//    }
//
//    @Bean
//    public ChattingRoomDao chattingRoomDao() {
//        return new ChattingRoomDao();
//    }

}

