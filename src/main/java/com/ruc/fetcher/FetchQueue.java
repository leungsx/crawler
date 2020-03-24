package com.ruc.fetcher;

import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import lombok.extern.slf4j.Slf4j;

import com.ruc.entity.CrawlMeta;

/**
 * 代爬的网页队列
 * @author 大哥
 * @date 2020年3月16日
 */

@Slf4j
public class FetchQueue
{

	public static FetchQueue DEFAULT_INSTANCE = newInstance("default");
	
	/**
	 * 表示爬取队列的标识
	 */
	private String tag;
	
	/**
	 * 待爬取的网页队列
	 */
	private Queue<CrawlMeta> toFetchQueue = new ArrayBlockingQueue<>(2000);
	
	/**
	 * jobcount映射表，key为jobcount#id,value为对应的jobCount
	 */
	private Map<Integer,JobCount> jobCountMap = new ConcurrentHashMap<>();
	
	/**
	 * 爬取是否完成的标识
	 */
	public volatile boolean isOver = false;
	
	/**
	 * 所有爬取过的URL集合，用于去重
	 */
	public Set<String> urls = ConcurrentHashMap.newKeySet();
	

    public int size() {
        return toFetchQueue.size();
    }
	
	private FetchQueue(String tag){
		this.tag = tag;
	}
	
	public static FetchQueue newInstance(String tag){
		return new FetchQueue(tag);
	}
	
	/**
	 * 当没有爬取过时，才丢入队列；主要是避免重复爬取的问题
	 */
	public boolean addSeed(CrawlMeta crawlMeta){
		if(urls.contains(crawlMeta.getUrl())){
			return false;
		}
		synchronized(this){
			if(urls.contains(crawlMeta.getUrl())){
				return false;
			}
			urls.add(crawlMeta.getUrl());
			toFetchQueue.add(crawlMeta);
			return true;
		}
	}
	
	public CrawlMeta pollSeed(){
		return toFetchQueue.poll();
	}
	
	public void finishJob(CrawlMeta crawlMeta, int count, int maxDepth){
		if(finishOneJob(crawlMeta, count, maxDepth)){
			isOver = true;
			System.out.println("========finish crawl!===========");
		}
	}
	
	private boolean finishOneJob(CrawlMeta crawlMeta,int count,int maxDepth){
		JobCount jobCount = new JobCount(crawlMeta.getJobId(),
				crawlMeta.getParentJobId(),
				crawlMeta.getCurrentDepth(),
				count,0);
		jobCountMap.put(crawlMeta.getJobId(),jobCount);
		
		if(crawlMeta.getCurrentDepth() == 0){
			return count == 0;
		}
		
		if(count == 0 || crawlMeta.getCurrentDepth() == maxDepth){
			return finishOneJob(jobCountMap.get(crawlMeta.getParentJobId()));
		}
		
		return false;
		
	}

	private boolean finishOneJob(JobCount jobCount){
		if(jobCount.finishJob()){
			if(jobCount.getCurrentDepth() == 0){
				return true;
			}
			return finishOneJob(jobCountMap.get(jobCount.getParentId()));
		}
		return false;
	}

	
}
