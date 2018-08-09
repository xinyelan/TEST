package com.jt.search.service;

import java.io.IOException;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;

import com.jt.dubbo.pojo.Item;
import com.jt.dubbo.service.DubboSearchService;

public class DubboSearchServiceImpl implements DubboSearchService{

	@Autowired
	private HttpSolrServer httpSolrServer;
	
	@Override
	public List<Item> findItemByKey(String key) {
		List<Item> itemList = null;
		try {
			SolrQuery query = new SolrQuery(key);
			query.setStart(0);  //暂时写死从0条记录开始
			query.setRows(20);  //每页显示多少条记录
			QueryResponse response = httpSolrServer.query(query);
			
			itemList = response.getBeans(Item.class);
			
		
		} catch (SolrServerException | IOException e) {
			e.printStackTrace();
		}
		
		return itemList;
	}

}
