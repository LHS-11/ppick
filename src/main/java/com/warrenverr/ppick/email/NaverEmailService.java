package com.warrenverr.ppick.email;


import com.warrenverr.ppick.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NaverEmailService {

    private final JavaMailSender mailSender;

    private final UserRepository userRepository;

    @Value("${spring.mail.username}")
    private String sender;

    public String sendNaverEmail(String email){

//        Optional<User> user = userRepository.findByEmail(email);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom(sender);
        message.setSubject("Hi");
        message.setText("Hello My name is HwaSup");
        mailSender.send(message);
        return "Success";
    }

}
