package com.jt.web.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.web.pojo.User;
import com.jt.web.thread.UserThreadLocal;

import redis.clients.jedis.JedisCluster;

public class UserInterceptor implements HandlerInterceptor{

	@Autowired
	private JedisCluster jedisCluster;
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		Cookie[] cookies = request.getCookies();
		
		String token = null;
		for (Cookie cookie : cookies) {
			
			if("JT_TICKET".equals(cookie.getName())){
				
				token = cookie.getValue();
				break;
			}
		}
		
		if(!StringUtils.isEmpty(token)){
			//判断redis缓存中是否有数据
			String userJSON = jedisCluster.get(token);
						
			if(!StringUtils.isEmpty(userJSON)){
				User user = objectMapper.readValue(userJSON, User.class);
				UserThreadLocal.set(user);
				return true;
			}
		}
		
		response.sendRedirect("/user/login.html");
		//表示拦截
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//将threadLocal销毁
		UserThreadLocal.remove();
		
	}

}
