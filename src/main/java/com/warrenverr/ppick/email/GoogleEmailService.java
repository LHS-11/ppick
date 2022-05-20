package com.warrenverr.ppick.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class GoogleEmailService {

    @Autowired
    private JavaMailSender mailSender;

    public String sendMail(String email){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Hi");
        message.setText("Hello My name is HwaSup");
        mailSender.send(message);
        return "Success";
    }
}
