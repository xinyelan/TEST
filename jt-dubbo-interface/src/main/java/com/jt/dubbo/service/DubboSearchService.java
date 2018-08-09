package com.jt.dubbo.service;

import java.util.List;

import com.jt.dubbo.pojo.Item;

public interface DubboSearchService {

	List<Item> findItemByKey(String key);

}
