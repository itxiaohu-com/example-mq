package com.itxiaohu.rabbitmq;

import com.itxiaohu.rabbitmq.util.RabbitMQUtil;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BackupExchangeTest {

    @Test
    public void test() throws Exception {

        Channel channel = RabbitMQUtil.createChannel();

        // 备份交换器&队列
        String exchange_backup = "example_exchange_backup";
        String queue_backup = "example_queue_backup";

        channel.exchangeDeclare(exchange_backup, BuiltinExchangeType.FANOUT, true, false, Collections.emptyMap());
        channel.queueDeclare(queue_backup, true, false, false, Collections.emptyMap());
        channel.queueBind(queue_backup, exchange_backup, "");

        // 正常交换器&队列
        String exchange_normal = "example_exchange_normal";
        String queue_normal = "example_queue_normal";
        String bindingKeyNormal = "example_binding_key_normal";

        Map<String,Object> arg = new HashMap<>();
        arg.put("alternate-exchange", exchange_backup);
        channel.exchangeDeclare(exchange_normal, BuiltinExchangeType.DIRECT, true, false, arg);
        channel.queueDeclare(queue_normal, true, false, false, Collections.emptyMap());
        channel.queueBind(queue_normal, exchange_normal, bindingKeyNormal);

        String message = "example_message_" + System.currentTimeMillis();
        String routingKey = "unreachable_routing_key";

        channel.basicPublish(exchange_normal, bindingKeyNormal, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        channel.basicPublish(exchange_normal, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
    }

}
