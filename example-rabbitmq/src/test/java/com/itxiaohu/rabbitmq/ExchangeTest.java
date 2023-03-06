package com.itxiaohu.rabbitmq;

import com.itxiaohu.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import org.junit.Test;

import java.util.Collections;
import java.util.Map;

public class ExchangeTest {

    @Test
    public void testSimple() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        channel.exchangeDeclare("exchange_test", "direct");
    }

    @Test
    public void testAllParam() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        boolean durable = true;
        boolean autoDelete = true;
        boolean internal = false;
        Map<String, Object> arguments = Collections.emptyMap();
        AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare("exchange_test",
                                                                       "direct",
                                                                            durable,
                                                                            autoDelete,
                                                                            internal,
                                                                            arguments);
    }

    @Test
    public void testAllParamWithTypeEnum() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        boolean durable = true;
        boolean autoDelete = true;
        boolean internal = false;
        Map<String, Object> arguments = Collections.emptyMap();
        AMQP.Exchange.DeclareOk declareOk = channel.exchangeDeclare("exchange_test", BuiltinExchangeType.DIRECT, durable, autoDelete, internal, arguments);
    }

    @Test
    public void testNoWait() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        boolean durable = true;
        boolean autoDelete = true;
        boolean internal = false;
        Map<String, Object> arguments = Collections.emptyMap();
        channel.exchangeDeclareNoWait("exchange_test", BuiltinExchangeType.DIRECT, durable, autoDelete, internal, arguments);
    }

    // 主要用来检测相应的交换器是否存在
    // 如果存在则正常返回:如果不存在则抛出异常 : 404 channel exception，同时 Channel 也会被关闭。
    @Test
    public void testExchangeDeclarePassive() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        channel.exchangeDeclarePassive("exchange_test");
    }

    @Test
    public void testDelete() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        channel.exchangeDelete("exchange_test");
    }

    @Test
    public void testDeleteIfUnused() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        boolean ifUnused = true;
        channel.exchangeDelete("exchange_test", ifUnused);
    }

    @Test
    public void testDeleteNoWait() throws Exception {
        Channel channel = RabbitMQUtil.createChannel();
        boolean ifUnused = true;
        channel.exchangeDeleteNoWait("exchange_test", ifUnused);
    }


}
