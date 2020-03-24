package com.ruc.conf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import com.ruc.utils.NumUtil;

@Getter
@Setter
@ToString
public class Config
{

	/**s
	 * 爬取任务的间隔时间
	 */
	private	long sleep;
	
	/**
	 * 从队列中获取任务，返回空时，等待时间之后进行重试
	 */
	private long emptyQueueWaitTime;
	
	/**
	 * 对象池大小
	 */
	private int fetchQueueSize;
	
	
	
	public void setSleep(String str,long sleep){
		this.sleep = NumUtil.str2long(str,sleep);
	}
	
	public void setEmptyQueueWaitTime(String str,long emptyQueueWaitTime){
		this.emptyQueueWaitTime = NumUtil.str2long(str,sleep);
	}
	
	public void setFetchQueueSize(String str,int size){
		this.fetchQueueSize = NumUtil.str2int(str,size);
	}
	
}
