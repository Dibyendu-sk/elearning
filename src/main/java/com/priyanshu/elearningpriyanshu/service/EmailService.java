package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.constants.MailConstants;
import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.username}")
    public static String ORG_MAIL;
    private final AuthService authService;
    @Autowired
    public EmailService(JavaMailSender javaMailSender, AuthService authService) {
        this.javaMailSender = javaMailSender;
        this.authService = authService;
    }
    @Async
    public void sendEmail(UserEntity userEntity,String body,String operation) {
        SimpleMailMessage message = new SimpleMailMessage();
        switch (operation){
            case "SIGN-UP":
                message.setTo(userEntity.getEmail());
                message.setFrom(ORG_MAIL);
                message.setSubject(MailConstants.SIGNUP_SUBJECT);
                message.setText(generateSignUpMailBody(userEntity));
                break;
            case "CONTACT-US":
                message.setTo(ORG_MAIL);
                message.setFrom(authService.getLoggedInUserDtls().getEmail());
                message.setSubject(body);
                message.setText(generateSignUpMailBody(userEntity));
                break;
        }


        javaMailSender.send(message);
    }
    private String generateSignUpMailBody(UserEntity userEntity){
        return "Hi "+userEntity.getFirstName()+", you have registerd as "+userEntity.getRole()+" with "+ MailConstants.ORG_NAME;
    }
}
