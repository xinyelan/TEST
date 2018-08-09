package com.jt.rabbit.test;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;

public class Test_3_publish {
	private Connection connection;

	private String queueName = "publish";

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
	
	//定义生产者
		@Test
		public void  proverder() throws IOException{
			//定义通道
			Channel channel = connection.createChannel();
			
			//定义交换机名称
			String exchange_name = "E1";
			
			
			//定义发布订阅模式    fanout    redirect 路由模式    topic 主题模式
			channel.exchangeDeclare(exchange_name, "fanout");
			
			for(int i=0;i<10; i++){
				String msg = "发布订阅模式"+i;
				channel.basicPublish(exchange_name, "", null, msg.getBytes());
			}
			
			channel.close();
			connection.close();
		}
		
		@Test
		public void consumer1() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
			Channel channel = connection.createChannel();
			
			String exchange_name = "E1";
			String queue_name = "c_1";
			
			//定义交换机模式
			channel.exchangeDeclare(exchange_name, "fanout");
			
			//定义队列
			channel.queueDeclare(queue_name, false, false, false, null);
			
			//将队列和交换机绑定
			channel.queueBind(queue_name, exchange_name, "");
			
			//定义消费数量 
			channel.basicQos(1);
			
			//定义消费者
			QueueingConsumer consumer = new QueueingConsumer(channel);
			
			//将消费者和队列绑定,并且需要手动返回
			channel.basicConsume(queue_name, false, consumer);
			
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				
				String msg = "发布订阅模式-消费者1"+new String(delivery.getBody());
				System.out.println(msg);
				
				//false表示一个一个返回
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		}
		
		@Test
		public void consumer2() throws IOException, ShutdownSignalException, ConsumerCancelledException, InterruptedException{
			Channel channel = connection.createChannel();
			
			String exchange_name = "E1";
			String queue_name = "c_2";
			
			//定义交换机模式
			channel.exchangeDeclare(exchange_name, "fanout");
			
			//定义队列
			channel.queueDeclare(queue_name, false, false, false, null);
			
			//将队列和交换机绑定
			channel.queueBind(queue_name, exchange_name, "");
			
			//定义消费数量 
			channel.basicQos(1);
			
			//定义消费者
			QueueingConsumer consumer = new QueueingConsumer(channel);
			
			//将消费者和队列绑定,并且需要手动返回
			channel.basicConsume(queue_name, false, consumer);
			
			while(true){
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				
				String msg = "发布订阅模式-消费者2"+new String(delivery.getBody());
				System.out.println(msg);
				
				//false表示一个一个返回
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}
		}
}
