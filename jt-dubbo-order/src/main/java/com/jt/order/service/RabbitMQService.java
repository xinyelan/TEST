package com.jt.order.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.pojo.OrderItem;
import com.jt.dubbo.pojo.OrderShipping;
import com.jt.order.mapper.OrderItemMapper;
import com.jt.order.mapper.OrderMapper;
import com.jt.order.mapper.OrderShippingMapper;

public class RabbitMQService {

	@Autowired
	private OrderMapper orderMapper;

	@Autowired
	private OrderShippingMapper orderShippingMapper;

	@Autowired
	private OrderItemMapper orderItemMapper;

	public void saveOrder(Order order) {
		System.out.println(order.getOrderId());
		String orderId = order.getOrderId();

		Date date = new Date();
		// 入库订单
		order.setStatus(1);
		order.setOrderId(orderId);
		order.setCreated(date);
		order.setUpdated(date);
		orderMapper.insert(order);

		// 获取订单物流信息
		OrderShipping orderShipping = order.getOrderShipping();
		orderShipping.setOrderId(orderId);
		orderShipping.setCreated(date);
		orderShipping.setUpdated(date);
		orderShippingMapper.insert(orderShipping);

		// 实现订单商品入库
		List<OrderItem> orderItems = order.getOrderItems();
		for (OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId);
			orderItem.setCreated(date);
			orderItem.setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("消息队列执行成功!!!");
	}
}
