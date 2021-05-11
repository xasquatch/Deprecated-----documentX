package net.xasquatch.document.service;

import net.xasquatch.document.service.command.MailServiceDecorator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;

@Service
@PropertySource("/WEB-INF/setting.properties")
public class MailService extends MailServiceDecorator {

    @Value("${email.username}")
    private String mailAddress;

    @Value("${email.personal}")
    private String mailPersonal;

    @Autowired
    private JavaMailSenderImpl mailSender;

    @Autowired
    private TokenMap tokenMap;

    @Override
    protected void createToken(int size) {

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < size; i++) {
            long j = Math.round(Math.random() * 10);
            if (!(j >= 10)) result.append(j);
        }
        String token = result.toString();
        this.tokenSize = token;
        tokenMap.addToken(this.email + token);
    }

    @Override
    protected void setEmail(String email) {
        this.email = email;
    }

    @Override
    protected void setTitle(String title) {
        this.title = title;
    }

    @Override
    protected void setContents(String contents) {
        this.contents = contents;
    }

    @Override
    protected void send() {
        MimeMessage mail = mailSender.createMimeMessage();

        MimeMessageHelper messageHelper = null;
        try {
            messageHelper = new MimeMessageHelper(mail, true, "UTF-8");
            messageHelper.setTo(new InternetAddress(this.email));
            messageHelper.setSubject(this.title);
            messageHelper.setText(this.contents, true);
            messageHelper.setFrom(new InternetAddress(mailAddress, mailPersonal));

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }


        mailSender.send(mail);

    }


/*
            if (this.mainContents.contains("Token Number")) {
                mainContents = createMainContents(sessionMember.getNick_name() + "님<BR>My Blog By Xasquatch에<BR>오신것을 환영합니다.",
                        "            Token Number: " + token,
                        "<a href=\"https://myblog.xasquatch.net/members/" + sessionMember.getNo() + "/check-email\" style=\"text-decoration: none; color: darkred; font-weight: bold;\">이메일 다시 인증하러가기</a>");

            } else {
                mainContents = this.mainContents;

            }
        } catch (NullPointerException e) {
            mainContents = createMainContents(sessionMember.getNick_name() + "님<BR>My Blog By Xasquatch에<BR>오신것을 환영합니다.",
                    "            Token Number: " + token,
                    "<a href=\"https://myblog.xasquatch.net/members/" + sessionMember.getNo() + "/check-email\" style=\"text-decoration: none; color: darkred; font-weight: bold;\">이메일 다시 인증하러가기</a>");

        }

*/
}
