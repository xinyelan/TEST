package com.jt.rabbit.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.QueueingConsumer;

public class Test_1_simple {
	
	private Connection connection;
	
	/**
	 * 1.初始化链接
	 * 	链接rabbitMQ步骤：
	 * 		定义主机地址IP/5672/用户名和密码/虚拟机主机名称
	 * @throws Exception 
	 */
	@Before
	public void init() throws Exception{
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("192.168.220.141");
		factory.setPort(5672);
		factory.setUsername("jtadmin");
		factory.setPassword("jtadmin");
		factory.setVirtualHost("/jt");
		connection = factory.newConnection();
	}
	
	/**
	 * 1.定义通道
	 * 		主要作用：创建队列/消费者/发布消息
	 * @throws Exception 
	 */
	@Test
	public void provider() throws Exception{
		//1.定义通道
		Channel channel = connection.createChannel();
		
		//2.定义消息队列
		String queueName = "simple";
		/**
		 * queue 队列的名称
		 * durable 是否持久化 true/false
		 * exclusive 是否是生成者独有 false
		 * autoDelete 是否自动删除
		 * arguments是否需要传递其他的参数  null
		 */
		
		channel.queueDeclare(queueName, true, false, false, null);
		
		//3.定义队列的消息 rpc 默认使用utf-8编码
		String msg = "我是简单模式";
		
		//4.发送消息
		/**
		 * exchange ：交换机名称 如果没有交换机 则为""串
		 * routingKey ：路由key 如果没有路由key则使用队列的名称代替
		 * props : 表示消息队列的配置文件
		 * body : 表示消息内容的二进制数组
		 */
		channel.basicPublish("", queueName, null, msg.getBytes());
		
		channel.close();
		connection.close();
		
		System.out.println("消息队列执行成功");
	}
	
	@Test
	public void consumer() throws Exception{
		//定义队列
		Channel channel = connection.createChannel();
		
		String queueName = "simple";
		
		channel.queueDeclare(queueName, true, false, false, null);
		
		//定义消费者队列
		QueueingConsumer consumer = new QueueingConsumer(channel);
		
		//将消费者与队列进行绑定
		/**
		 * queue : 队列
		 * callback : 回调给消费者
		 * autoAck : 发送确认消息
		 */
		channel.basicConsume(queueName, true, consumer);
		//让消费者主动去队列中获取消息 配置监听器
		while(true){
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String msg = new String(delivery.getBody());
			System.out.println("获取消息："+msg);
		}
	}
	
}
