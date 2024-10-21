package com.example.rabbitmq;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageProducer {
    private static Logger logger = LoggerFactory.getLogger(MessageProducer.class);
    @Autowired
    private RabbitTemplate rabbitTemplate;

    public boolean sendMessageAndWaitForReply(String message) {
      //  System.out.println("Sending message: " + message);
        logger.info("Sending message: " + message);
        // Установим replyTo для обратного ответа
        rabbitTemplate.setReplyTimeout(5000); // Устанавливаем тайм-аут ожидания ответа (5 секунд)

        Object response = rabbitTemplate.convertSendAndReceive(
                RabbitMQConfig.EXCHANGE_NAME,
                RabbitMQConfig.ROUTING_KEY,
                message,
                m -> {
                    m.getMessageProperties().setReplyTo(RabbitMQConfig.REPLY_QUEUE_NAME); // Указываем очередь для ответа
                    return m;
                });

        if (response != null) {
            // Преобразуем байтовый массив в строку
            String replyMessage;
            if (response instanceof byte[]) {
                replyMessage = new String((byte[]) response); // Преобразование байтового массива в строку
            } else {
                replyMessage = response.toString(); // Если это не байтовый массив
            }

           // System.out.println("Received response: " + replyMessage);
            logger.info("Received response: " + replyMessage);
            return Boolean.parseBoolean(replyMessage); // Преобразование строки в булево значение
        } else {
           // System.out.println("No response received within timeout");
            logger.info("No response received within timeout");
            return false;
        }
    }
}
