package com.jt.web.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.pojo.Order;
import com.jt.dubbo.service.DubboCartService;
import com.jt.dubbo.service.DubboOrderService;
import com.jt.web.thread.UserThreadLocal;

@Controller
@RequestMapping("/order")
public class OrderController {
	
	@Autowired
	private DubboCartService cartService;
	
	@Autowired
	private DubboOrderService orderService;
	
	@RequestMapping("/create")
	public String toCreate(Model model){
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = cartService.findCartByUserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	
	// /service/order/submit
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult savaOrder(Order order){
		try {
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId);
			
			String orderId = orderService.saveOrder(order);
			
			if(!StringUtils.isEmpty(orderId)){
				return SysResult.oK(orderId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "商品新增失败");
	}
	
	// /order/success.html?id=131533553915125
	
	@RequestMapping("/success")
	public String findOrderById(@RequestParam(value="id") String orderId,Model model){
		Order order = orderService.findOrderById(orderId);
		model.addAttribute("order", order);
		return "success";
		
	}
	
}
