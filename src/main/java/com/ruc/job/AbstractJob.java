package com.ruc.job;

import com.ruc.entity.CrawlResult;
import com.ruc.pool.SimplePool;

public abstract class AbstractJob implements IJob
{

	

	@Override
	public void beforeRun()
	{
		
	}

	@Override
	public void afterRun()
	{
		
	}
	
	
	
	@Override
	public void run()
	{
		this.beforeRun();
		try
		{
			this.doFetchPage();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		this.afterRun();
		
		 SimplePool.getInstance().release(this);
	}
	
	public abstract void doFetchPage()throws Exception;
	
	protected abstract void visit(CrawlResult crawlResult);

}
