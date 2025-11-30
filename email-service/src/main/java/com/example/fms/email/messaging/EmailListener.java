package com.example.fms.email.messaging;

import com.example.fms.common.dto.EmailEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import static com.example.fms.email.config.RabbitMQConfig.QUEUE;

@Component
@RequiredArgsConstructor
public class EmailListener {

    private final JavaMailSender mailSender;

    @RabbitListener(queues = QUEUE)
    public void handleEmail(EmailEvent event) {

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(event.to());
        msg.setSubject(event.subject());
        msg.setText(event.body());

        mailSender.send(msg);

        System.out.println("EMAIL SENT TO: " + event.to());
    }
}
