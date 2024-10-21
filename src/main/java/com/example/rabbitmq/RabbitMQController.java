package com.example.rabbitmq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQController {

    @Autowired
    private MessageProducer messageProducer;

    @GetMapping("/send")
    public String sendMessage(@RequestParam String message) {
        boolean result = messageProducer.sendMessageAndWaitForReply(message);
        return "Message processed with result: " + result;
    }
}


