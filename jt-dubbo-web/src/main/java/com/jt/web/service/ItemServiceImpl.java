package com.jt.web.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.common.service.HttpClientService;
import com.jt.web.pojo.Item;
import com.jt.web.pojo.ItemDesc;

@Service
public class ItemServiceImpl implements ItemService{
	
	private static final ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private HttpClientService httpClient;

	/**
	 * 
	 */
	@Override
	public Item findItemById(Long itemId) {
		Item item = null;
		
		String url = "http://manage.jt.com/web/item/findItemById";
		
		Map<String,String> params = new HashMap<String,String>();
		//利用+""将Long类型转换为String类型
		params.put("itemId", itemId+"");
		
		String result = httpClient.doGet(url, params);

		try {
			
			if(StringUtils.isEmpty(result)){
				throw new RuntimeException();
			}
			//将数据格式化为JSON串
			item = objectMapper.readValue(result, Item.class);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return item;
	}
	
	@Override
	public ItemDesc findItemDescById(Long itemId) {
		ItemDesc itemDesc = null;
		
		String url = "http://manage.jt.com/web/item/findItemDescById/"+itemId;
		
		Map<String,String> params = new HashMap<String,String>();
		//利用+""将Long类型转换为String类型
		params.put("itemId", itemId+"");
		
		String result = httpClient.doGet(url, params);

		try {
			
			if(StringUtils.isEmpty(result)){
				throw new RuntimeException();
			}
			//将JSON串转换为java对象
			itemDesc = objectMapper.readValue(result, ItemDesc.class);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		return itemDesc;
	}

}
