package com.jt.web.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
//实现全文检索
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jt.dubbo.pojo.Item;
import com.jt.dubbo.service.DubboSearchService;
@Controller
public class SearchController {
	
	@Autowired
	private DubboSearchService searchService;

	@RequestMapping("/search")
	public String findItemByKey(@RequestParam("q")String keyWord,Model model){
		
		try {
			String key = new String(keyWord.getBytes("ISO-8859-1"),"UTF-8");
			
			//实现商品查询 调用全文检索系统
			List<Item> itemLists = searchService.findItemByKey(key);
			model.addAttribute("itemList", itemLists);
			model.addAttribute("query", key);
		} catch (Exception e) {
			
			e.printStackTrace();
		}

		//跳转到全文检索页面
		return "search";
	}
}
