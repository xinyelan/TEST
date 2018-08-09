package com.jt.dubbo.service;

import java.util.List;

import com.jt.dubbo.pojo.Cart;

public interface DubboCartService {

	List<Cart> findCartByUserId(Long userId);

	void saveCart(Cart cart);

	void deleteCart(Cart cart);

	void updateCartNum(Cart cart);

}
