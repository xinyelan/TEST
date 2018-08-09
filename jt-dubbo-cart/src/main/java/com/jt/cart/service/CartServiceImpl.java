package com.jt.cart.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jt.cart.mapper.CartMapper;
import com.jt.dubbo.pojo.Cart;
import com.jt.dubbo.service.DubboCartService;

@Service  //id="cartService"
public class CartServiceImpl implements DubboCartService{
	
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCartByUserId(Long userId) {
		Cart cart = new Cart();
		cart.setUserId(userId);		
		return cartMapper.select(cart);
	}

	@Override
	public void saveCart(Cart cart) {
		Cart cartDB = cartMapper.findCartByUI(cart);
		
		if(cartDB == null){
			cart.setCreated(new Date());
			cart.setUpdated(cart.getCreated());
			cartMapper.insert(cart);
		}else{
			cartDB.setNum(cartDB.getNum()+cart.getNum());
			cartDB.setUpdated(new Date());
			cartMapper.updateByPrimaryKeySelective(cartDB);
		}
		
	}

	@Override
	public void deleteCart(Cart cart) {
		cartMapper.delete(cart);
		
	}

	@Override
	public void updateCartNum(Cart cart) {
		cartMapper.updateCartNum(cart);
		
	}

	
	
}
