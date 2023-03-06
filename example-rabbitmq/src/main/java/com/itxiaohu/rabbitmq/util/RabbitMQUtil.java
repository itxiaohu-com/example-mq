package com.itxiaohu.rabbitmq.util;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class RabbitMQUtil {

    public static Connection createConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri("amqp://admin:123456@127.0.0.1:5672/example");
        return factory.newConnection();
    }

    public static Channel createChannel() throws Exception {
        return createConnection().createChannel();
    }

}
