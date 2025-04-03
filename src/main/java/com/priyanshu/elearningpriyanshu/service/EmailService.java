package com.priyanshu.elearningpriyanshu.service;

import com.priyanshu.elearningpriyanshu.constants.MailConstants;
import com.priyanshu.elearningpriyanshu.entity.UserEntity;
import com.priyanshu.elearningpriyanshu.model.ContactRequest;
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
    public void sendEmail(UserEntity userEntity, String operation, ContactRequest contactRequest) {
        SimpleMailMessage message = new SimpleMailMessage();
        switch (operation){
            case "SIGN-UP":
                message.setTo(userEntity.getEmail());
                message.setFrom(ORG_MAIL);
                message.setSubject(MailConstants.SIGNUP_SUBJECT);
                message.setText(generateSignUpMailBody(userEntity));
                break;
            case "CONTACT-US":
                message.setTo("p85332398@gmail.com");
                message.setFrom(authService.getLoggedInUserDtls().getEmail());
                message.setSubject(contactRequest.getSubject());
                message.setText(contactRequest.getBody());
                break;
        }


        javaMailSender.send(message);
    }
    private String generateSignUpMailBody(UserEntity userEntity){
        return "Hi "+userEntity.getFirstName()+", you have registerd as "+userEntity.getRole()+" with "+ MailConstants.ORG_NAME;
    }
}
