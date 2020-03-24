package com.ruc.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.ruc.entity.CrawlResult;

public class RamStorage implements IStorage
{
	private Map<String,CrawlResult> map = new ConcurrentHashMap<>();

	@Override
	public boolean putIfNoExist(String url, CrawlResult result)
	{
		if(map.containsKey(url)){
			return false;	
		}
		map.put(url,result);
		return true;
	}

	@Override
	public boolean contain(String url)
	{
		return map.containsKey(url);
	}

}
