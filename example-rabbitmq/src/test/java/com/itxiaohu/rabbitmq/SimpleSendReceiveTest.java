package com.itxiaohu.rabbitmq;

import com.rabbitmq.client.*;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class SimpleSendReceiveTest {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String BINDING_KEY = "routing_key_demo";
    private static final String ROUTING_KEY = "routing_key_demo";
    private static final String QUEUE_NAME = "queue_demo";
    private static final String IP_ADDRESS = "127.0.0.1";
    private static final int PORT = 5672;

    @Test
    public void testSend() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(IP_ADDRESS);
        factory.setPort(PORT);
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();

        // 创建一个 type="direct" 、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME, "direct", true, false, null);

        // 创建一个持久化、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME, true, false, false, null);

        // 将交换器与队列通过路由键绑定
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, BINDING_KEY);

        String message = "hello,hello!";

        // 发送消息
        channel.basicPublish(EXCHANGE_NAME, ROUTING_KEY, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());

        // 关闭资源
        channel.close();
        connection.close();
    }

    @Test
    public void testReceive() throws Exception {
        Address[] addresses = new Address[]{
                new Address(IP_ADDRESS, PORT)
        };

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("admin");
        factory.setPassword("123456");

        // 创建连接
        Connection connection = factory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 设置客户端最多接收未被 ack 的消息的个数
        channel.basicQos(64);

        // 注意这里采用的是继承DefaultConsumer的方式来实现消费,有过RabbitMQ使用经验的读者也许会喜欢采用
        // QueueingConsumer的方式来实现消费,但是我们并不推荐，使用QueueingConsumer会有一些隐患。
        // 同时在RabbitMQ的Java客户端4.0.0版本开始将QueueingConsumer标记为@Deprecated,在后面的大版
        // 本中会删除这个类
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag,
                                       Envelope envelope,
                                       AMQP.BasicProperties properties,
                                       byte[] body) throws IOException {

                System.out.println("receive message:" + new String(body));
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume(QUEUE_NAME, consumer);

        // 等待回调函数执行完毕之后关闭资源
        TimeUnit.SECONDS.sleep(10);
        channel.close();
        connection.close();
    }

}
