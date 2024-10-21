package com.example.rabbitmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveMessage(String message, Message amqpMessage) throws InterruptedException {
        System.out.println("Received message: " + message);

        // Логика обработки сообщения
        boolean messageHandled = processMessage(message);

        // Получаем свойства исходного сообщения
        MessageProperties props = amqpMessage.getMessageProperties();
        String correlationId = props.getCorrelationId();
        String replyToQueue = props.getReplyTo();

        if (replyToQueue != null && correlationId != null) {
            // Создаем новое сообщение для ответа
            MessageProperties replyProperties = new MessageProperties();
            // Устанавливаем CorrelationId
            replyProperties.setCorrelationId(correlationId);
            Message replyMessage = new Message(String.valueOf(messageHandled).getBytes(), replyProperties);

            // Отправляем результат обратно в очередь для ответов
            rabbitTemplate.send(replyToQueue, replyMessage);
        }
    }

    private boolean processMessage(String message) throws InterruptedException {
        // Логика обработки сообщения
        Thread.sleep(6000);
        return "XXX".equals(message);
    }
}



