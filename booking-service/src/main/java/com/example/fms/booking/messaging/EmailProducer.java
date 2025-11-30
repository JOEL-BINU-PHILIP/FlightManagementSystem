package com.example.fms.booking.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmailProducer {

    private final RabbitTemplate rabbitTemplate;

    public void sendEmailMessage(String email, String message) {
        rabbitTemplate.convertAndSend("emailQueue", email + "|" + message);
    }
}
