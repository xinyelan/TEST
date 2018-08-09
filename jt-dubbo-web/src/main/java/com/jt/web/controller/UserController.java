package com.jt.web.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.jt.common.vo.SysResult;
import com.jt.web.pojo.User;
import com.jt.web.service.UserService;

import redis.clients.jedis.JedisCluster;

@Controller
@RequestMapping("/user")
public class UserController {
	@Autowired
	private UserService userService;
	
	@Autowired
	private JedisCluster jedisCluster;

	//实现页面的通用跳转
	@RequestMapping("/{moduleName}")
	public String toModule(@PathVariable String moduleName){
		return moduleName;
	}
	
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user){
		try {
			userService.saveUser(user);
			return SysResult.oK();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.build(201, "用户新增失败");
	}
	
	//使用用户登陆
		/*
		 * 
		 */
		@RequestMapping("/doLogin")
		@ResponseBody
		public SysResult doLogin(User user,HttpServletResponse response){
			try {
				//获取服务端返回的Token数据
				String token = userService.findUserByUP(user);
				//判断返回值是否有效
				if(StringUtils.isEmpty(token)){
					throw new RuntimeException();
				}
				
				//表示获取token是正确的，将token保存到cookie中
				Cookie cookie = new Cookie("JT_TICKET", token);
				cookie.setPath("/");	//表示在浏览器根目录生效
				cookie.setMaxAge(3600*24*7);//设定生命周期 单位秒
				response.addCookie(cookie);
				
				return SysResult.oK();//表示程序正常运行
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			return SysResult.build(201, "用户登陆失败");
		}
		
		@RequestMapping("/logout")
		public String logout(HttpServletRequest request,HttpServletResponse response){
			
			//获取cookie数据
			Cookie[] cookies = request.getCookies();
			
			for (Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())){
					String ticket = cookie.getValue();
					jedisCluster.del(ticket);
					break;
				}
			}

			//清空cookie数据
			Cookie cookie = new Cookie("JT_TICKET", "");
			cookie.setPath("/");
			cookie.setMaxAge(0);//立即删除
			response.addCookie(cookie);
			
			//重定向到系统首页
			return "redirect:/index.html";
		}
}
