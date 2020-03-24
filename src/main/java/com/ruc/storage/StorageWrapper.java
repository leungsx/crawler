package com.ruc.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.ruc.entity.CrawlResult;

public class StorageWrapper
{

	private static StorageWrapper instance = new StorageWrapper();
	
	private IStorage storage;
	
	private Map<String, Lock> lockMap = new ConcurrentHashMap<>();
	
	public static StorageWrapper getInstance(){
		return instance;
	}
	
	private StorageWrapper(){
		storage = new RamStorage();
	}
	
	/**
	 * 爬完之后，新增一条爬去记录
	 */
	public void addFetchRecord(String url,CrawlResult crawlResult){
		try
		{
			if(crawlResult != null){
				storage.putIfNoExist(url, crawlResult);
				this.unlock(url);
			}
		} catch (Exception e)
		{
			System.out.println(Thread.currentThread().getName() + "result:" + url +"e:" + e);
		}
		
	}
	
	/**
	 * 判断URL是否被怕去过，是则返回true，否则返回false
	 * @author 大哥
	 * @date 2020年3月13日
	 * @param url
	 * @return
	 * @throws InterruptedException 
	 */

	public boolean ifUrlFetched(String url) throws InterruptedException
	{
		if(storage.contain(url)){
			return true;
		}
		
		synchronized(this){
			if(!lockMap.containsKey(url)){
				//不存在是加一个锁
				lockMap.put(url, new ReentrantLock());
			}
			
			this.lock(url);
			Thread.sleep(20);
			if(storage.contain(url)){
				return true;
			}
			return false;
		}
		
	}

	private void unlock(String url)
	{
		lockMap.get(url).unlock();
	}

	private void lock(String url)
	{
		lockMap.get(url).lock();
	}
}
