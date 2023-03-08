package com.itxiaohu.rabbitmq;

import com.itxiaohu.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ParamMandatoryTest {

    private static final Logger logger = LoggerFactory.getLogger(ParamMandatoryTest.class);

    @Test
    public void testMandatoryTrue() throws Exception {
        testMandatory(true);
    }

    @Test
    public void testMandatoryFalse() throws Exception {
        testMandatory(false);
    }

    private void testMandatory(boolean mandatory) throws Exception {

        Channel channel = RabbitMQUtil.createChannel();
        String exchange = "example_direct_exchange";
        String queue = "example_direct_queue";
        String bindingKey = "example_direct_binding_key";
        String routingKey = "unreachable_routing_key";
        String message = "example_message_" + System.currentTimeMillis();

        channel.exchangeDeclare(exchange, BuiltinExchangeType.DIRECT);
        channel.queueDeclare(queue, false, false, false, Collections.emptyMap());
        channel.queueBind(queue, exchange, bindingKey);

        channel.basicPublish(exchange,
                routingKey,
                mandatory,
                MessageProperties.PERSISTENT_TEXT_PLAIN,
                message.getBytes(Charset.defaultCharset()));

        CountDownLatch cdl = new CountDownLatch(1);

        channel.addReturnListener((replyCode, replyText, exchange1, routingKey1, properties, body) -> {
            String msg = new String(body);
            logger.info("handleReturn-replyCode:{}", replyCode);
            logger.info("handleReturn-replyText:{}", replyText);
            logger.info("handleReturn-exchange:{}", exchange1);
            logger.info("handleReturn-routingKey:{}", routingKey1);
            logger.info("handleReturn-msg:{}", msg);

            cdl.countDown();
        });

        cdl.await(3, TimeUnit.SECONDS);
    }
}
