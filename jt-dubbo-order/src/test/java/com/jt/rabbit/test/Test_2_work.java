package com.jt.rabbit.test;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

public class Test_2_work {

	private Connection connection;

	private String queueName = "work";

	@Before
	public void init() throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.220.141");
		factory.setPort(5672);
		factory.setUsername("jtadmin");
		factory.setPassword("jtadmin");
		factory.setVirtualHost("/jt");
		connection = factory.newConnection();
	}

	@Test
	public void provider() throws Exception {
		// 定义通道对象
		Channel channel = connection.createChannel();

		// 定义队列
		channel.queueDeclare(queueName, false, false, false, null);

		// 定义广播的消息
		String msg = "我是工作模式";

		// 发送消息
		channel.basicPublish("", queueName, null, msg.getBytes());

		// 关闭流文件
		channel.close();
		connection.close();
	}

	// 定义消费者
	@Test
	public void consumer1() throws Exception{
		// 定义通道
		Channel channel = connection.createChannel();

		// 定义队列
		channel.queueDeclare(queueName, false, false, false, null);

		// 定义消费数 每次只能消费一条记录.当消息执行后需要返回ack确认消息 才能执行下一条
		channel.basicQos(1);

		// 定义消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);

		// 将队列和消费者绑定 false表示手动返回ack
		channel.basicConsume(queueName, false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("队列A获取消息:" + msg);
			// deliveryTag 队列下标位置
			// multiple是否批量返回
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
		}
	}

	// 定义消费者
	@Test
	public void consumer2()	throws Exception{
		// 定义通道
		Channel channel = connection.createChannel();

		// 定义队列
		channel.queueDeclare(queueName, false, false, false, null);

		// 定义消费数 每次只能消费一条记录.当消息执行后需要返回ack确认消息 才能执行下一条
		channel.basicQos(1);

		// 定义消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);

		// 将队列和消费者绑定 false表示手动返回ack
		channel.basicConsume(queueName, false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("队列B获取消息:" + msg);
			// deliveryTag 队列下标位置
			// multiple是否批量返回
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
		}
	}

	// 定义消费者
	@Test
	public void consumer3() throws Exception{
		// 定义通道
		Channel channel = connection.createChannel();

		// 定义队列
		channel.queueDeclare(queueName, false, false, false, null);

		// 定义消费数 每次只能消费一条记录.当消息执行后需要返回ack确认消息 才能执行下一条
		channel.basicQos(1);

		// 定义消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);

		// 将队列和消费者绑定 false表示手动返回ack
		channel.basicConsume(queueName, false, consumer);

		while (true) {
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("队列C获取消息:" + msg);
			// deliveryTag 队列下标位置
			// multiple是否批量返回
			channel.basicAck(delivery.getEnvelope().getDeliveryTag(), true);
		}
	}
}
