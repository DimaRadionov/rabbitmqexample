package com.example.rabbitmq;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "exampleQueue";
    public static final String EXCHANGE_NAME = "exampleExchange";
    public static final String ROUTING_KEY = "exampleRoutingKey";
    public static final String REPLY_QUEUE_NAME = "replyQueue"; // Новая очередь для ответов

    // Очередь
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }

    @Bean
    public Queue replyQueue() {
        return new Queue(REPLY_QUEUE_NAME, true);
    }

    // Обмен
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // Связывание очереди с обменом
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }
}
