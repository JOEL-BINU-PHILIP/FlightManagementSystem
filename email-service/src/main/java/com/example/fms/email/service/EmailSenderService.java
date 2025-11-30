package com.example.fms.email.service;

import com.example.fms.common.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSenderService {

    private final JavaMailSender mailSender;

    public void sendEmail(EmailEvent event) {

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(event.to());
            message.setSubject(event.subject());
            message.setText(event.body());

            mailSender.send(message);

            log.info("Email sent successfully to {}", event.to());

        } catch (Exception ex) {
            log.error("Failed to send email to {} :: {}", event.to(), ex.getMessage());
        }
    }
}
