package com.ruc.fetcher;

import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import com.ruc.conf.ConfigWrapper;
import com.ruc.entity.CrawlMeta;
import com.ruc.job.DefaultAbstractCrawlJob;
import com.ruc.pool.ObjectFactory;
import com.ruc.pool.SimplePool;
@Slf4j
public class Fetcher
{

	private int maxDepth;
	
	public FetchQueue fetchQueue;
	
	private Executor executor;
	
	@Setter
	private ThreadConf threadConf; 
	
	public FetchQueue addFeed(CrawlMeta feed){
		fetchQueue.addSeed(feed);
		return fetchQueue;
	}
	
	public <T extends DefaultAbstractCrawlJob> Fetcher(Class<T> jobClz){
		this(0,jobClz);
	}
	
	public <T extends DefaultAbstractCrawlJob> Fetcher(int maxDepth,Class<T> jobClz){
		this(maxDepth, ()->{
			try{
				return jobClz.newInstance();
			}catch(Exception e){
				log.error("create job error! e:{}",e);
				return null;
			}
		});
	}
	
	public <T extends DefaultAbstractCrawlJob> Fetcher(int maxDepth,ObjectFactory<T> jobFactory){
		this.maxDepth = maxDepth;
		fetchQueue = FetchQueue.DEFAULT_INSTANCE;
		threadConf = ThreadConf.DEFAULT_CONF;
		initExecutor();
		SimplePool simplePool = new SimplePool<>(ConfigWrapper.getInstance().getConfig().getFetchQueueSize(),jobFactory);
		SimplePool.initInstance(simplePool);
	}
	
	
	

	public <T extends DefaultAbstractCrawlJob> void start() throws Exception{
		long start = System.currentTimeMillis();
		CrawlMeta crawlMeta;
		if(fetchQueue.size() == 0){
			throw new IllegalArgumentException("please choose one seed to start crawling!");
		}
		
		log.info(">>>>>>>>>>>>> start  crawl <<<<<<<<<<<<<<<<");
		
		
		while(!fetchQueue.isOver){
			crawlMeta = fetchQueue.pollSeed();
			if(crawlMeta == null){
				Thread.sleep(ConfigWrapper.getInstance().getConfig().getEmptyQueueWaitTime());
                continue;
			}
			
			try{
				long sleep = ConfigWrapper.getInstance().getConfig().getSleep();
				Thread.sleep(sleep);
				if(log.isDebugEnabled()){
					log.debug("Sleep {} ms",sleep);
				}
			}catch(Exception e){
				log.error("fetcher sleep exception! e:{}",e);
			}
			
			DefaultAbstractCrawlJob job = (DefaultAbstractCrawlJob) SimplePool.getInstance().get();
			job.setDepth(this.maxDepth);
			job.setCrawlMeta(crawlMeta);
			job.setFetchQueue(fetchQueue);
			executor.execute(job);
		}
		long end = System.currentTimeMillis();
		log.info(">>>>>>>>>>>>>>>> crawler over! total url num: {}, cost: {}ms <<<<<<<<<<<<<<<<",fetchQueue.urls.size(),end - start);
	}
	
	/**
	 * 初始化线程池
	 */
	private void initExecutor()
	{
		executor = new ThreadPoolExecutor(threadConf.getCoreNum(),
				threadConf.getMaxNum(),
				threadConf.getAliveTime(),
				threadConf.getTimeUnit(),
				new LinkedBlockingQueue<>(threadConf.getQueueSize()),
				new CustomThreadFactory(threadConf.getThreadName()),
				new ThreadPoolExecutor.CallerRunsPolicy());
	}
	
	
	/**
	 * 线程的创建工厂
	 * @author 大哥
	 * @date 2020年3月17日
	 */
	
	public static class CustomThreadFactory implements ThreadFactory{

		private String name;
		
		private AtomicInteger count = new AtomicInteger(0);
		
		public CustomThreadFactory(String name){
			this.name = name;
		}
		@Override
		public Thread newThread(Runnable r)
		{
			return new Thread(r,name+"-"+count.addAndGet(1));
		}
		
	}
	
	/**
	 * 线程池配置类
	 */
	@Getter
	@Setter
	@ToString
	@NoArgsConstructor
	public static class ThreadConf{
		private int coreNum = 6;
		private int maxNum = 10;
		private int queueSize = 10;
		private int aliveTime = 1;
		private TimeUnit timeUnit = TimeUnit.MINUTES;
		private String threadName = "crawl-fetch";
		public final static ThreadConf DEFAULT_CONF = new ThreadConf();
		

//		//参数初始化
//		private int CPU_COUNT = Runtime.getRuntime().availableProcessors();
//		//核心线程数量大小
//		private int coreNum = Math.max(2, Math.min(CPU_COUNT - 1, 4));
//		//线程池最大容纳线程数
//		private int maxNum = CPU_COUNT * 2 + 1;
//		//线程空闲后的存活时长
//		private int aliveTime = 30;
//		
//		private int queueSize = 10;
//		
//		private TimeUnit timeUnit = TimeUnit.MINUTES;
//		
//		private String threadName = "crawl-fetch";
//		
//		public final static ThreadConf DEFAULT_CONF = new ThreadConf();
	}
	
	
	
}
