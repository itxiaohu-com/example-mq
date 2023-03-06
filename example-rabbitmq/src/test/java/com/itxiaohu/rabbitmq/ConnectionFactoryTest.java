package com.itxiaohu.rabbitmq;

import com.rabbitmq.client.ConnectionFactory;
import org.junit.Assert;
import org.junit.Test;

public class ConnectionFactoryTest {

    @Test
    public void testBuildConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123456");
        factory.setVirtualHost("/");
        factory.setHost("127.0.0.1");
        factory.setPort(5672);
        Assert.assertNotNull(factory.newConnection());
    }

    @Test
    public void testBuildConnectionByUri() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        // 格式 amqp://userName:password@ipAddress:portNumber/virtualHost
        factory.setUri("amqp://admin:123456@127.0.0.1:5672/example");
        Assert.assertNotNull(factory.newConnection());
    }

}
