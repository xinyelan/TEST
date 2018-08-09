package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

//@Service 已经交给spring (applicationContext-provider.xml)管理了
public class OrderServiceImpl implements DubboOrderService{
	
	@Autowired
	private OrderMapper orderMapper;
	
	@Autowired
	private OrderItemMapper orderItemMapper;
	
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	//通过消息队列实现订单入库
	@Override
	public String saveOrder(Order order) {
		String orderId = order.getUserId()+""+System.currentTimeMillis();
		order.setOrderId(orderId);
		
		String routingKey = "save.order"; //定义路由key
		rabbitTemplate.convertAndSend(routingKey,order);
		return orderId;
	}


	/**
	 * 1.获取orderId: userId + 时间戳
	 * 2.补全数据 状态信息 时间信息
	 * 2.分别使用通用mapper实现三张表入库
	 */
/*	@Override
	public String saveOrder(Order order) {
		String orderId = order.getUserId()+""+System.currentTimeMillis();
		Date date = new Date();		
		order.setStatus(1);
		
		order.setOrderId(orderId);
		order.setCreated(date);
		order.setUpdated(date);
		orderMapper.insert(order);
		
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);
		
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		
		return orderId;
	}*/

	//三张表同时查询
	@Override
	public Order findOrderById(String orderId) {
		//1.获取order数据  2.获取orderShipping对象 3.获取orderItem数据
		//4.将数据进行组装
		Order order = orderMapper.selectByPrimaryKey(orderId);
		OrderShipping orderShipping = orderShippingMapper.selectByPrimaryKey(orderId);
		
		OrderItem orderItem = new OrderItem();
		orderItem.setOrderId(orderId);
		//select() 根据实体类不为null的字段进行查询,条件全部使用=号and条件
		List<OrderItem> orderItems = orderItemMapper.select(orderItem);
		
		order.setOrderShipping(orderShipping);
		order.setOrderItems(orderItems);
		
		return order;
	}

}
