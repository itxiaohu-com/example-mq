package com.itxiaohu.rabbitmq;

import com.itxiaohu.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.Connection;
import org.junit.Assert;
import org.junit.Test;

public class ChannelTest {

    @Test
    public void test() throws Exception {
        Connection connection = RabbitMQUtil.createConnection();
        Assert.assertNotNull(connection.createChannel());
    }

}
