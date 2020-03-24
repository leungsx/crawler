package com.ruc.fetcher;

import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;

@Getter
public class JobCount	
{

	/**
	 * 种子对应的id
	 */
	public static int SEED_ID = 1;
	
	public static AtomicInteger idGen = new AtomicInteger();
	
	public static int genId(){
		return idGen.addAndGet(1);
	}
	
	/**
	 * 该job对应的唯一ID
	 */
	private int id;
	
	/**
	 * 该job对应job的id
	 */
	private int parentId;
	
	/**
	 * 当前的层数
	 */
	private int currentDepth;
	
	/**
	 * 给job对应的网页中，子job的数量
	 */
	private AtomicInteger jobCount = new AtomicInteger(0);
	
	/**
	 * 给job对应的网页中，子job完成的数量
	 */
	private AtomicInteger finishCount = new AtomicInteger(0);
	
	public boolean fetchOver(){
		return jobCount.get() == finishCount.get();
	}
	
	/**
	 * 爬取完成一个子任务
	 */
	public synchronized boolean finishJob(){
		finishCount.addAndGet(1);
		return fetchOver();
	}
	
	public JobCount(int id,int parentId,int currentDepth,int jobCount,int finishCount){
		this.id = id;
		this.parentId = parentId;
		this.currentDepth = currentDepth;
		this.jobCount.set(jobCount);
		this.finishCount.set(finishCount);
	}
	
}
