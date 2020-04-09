package BaseCrawlerTest;

import org.junit.Test;

import com.ruc.entity.CrawlMeta;
import com.ruc.entity.CrawlResult;
import com.ruc.fetcher.Fetcher;
import com.ruc.job.DefaultAbstractCrawlJob;

public class QueueCrawlerTest
{
	public static class QueueCrawlerJob extends DefaultAbstractCrawlJob{

		public void beforeRun(){
			//设置返回的网页编码
			super.setResponseCode("gbk");
		}
		@Override
		protected void visit(CrawlResult crawlResult)
		{
			//System.out.println(Thread.currentThread().getName() + "____" + getCrawlMeta().getCurrentDepth() + "____" + crawlResult.getUrl());
			
		}
		
	}
	
	
	/*public static void main(String[] args) throws Exception{
		Fetcher fetcher = new Fetcher(1);
		String url = "http://chengyu.t086.com/gushi/1.htm";
		CrawlMeta crawlMeta = new CrawlMeta();
		crawlMeta.setUrl(url);
		crawlMeta.addPositiveRegex("http://chengyu.t086.com/gushi/[0-9]+\\.htm$");
		fetcher.addFeed(crawlMeta);
		fetcher.start(QueueCrawlerJob.class);
	}*/
	
	
	@Test
	public void testCrawl() throws Exception{
		Fetcher fetcher = null;
		CrawlMeta crawlMeta = null;
		//int i = 1;
		//String url = "https://catalog.lib.uchicago.edu/vufind/Search/Results?filter%5B%5D=format%3A%22E-Resource%22&filter%5B%5D=format%3A%22Book%22&type=AllFields&page=";
		String url = "https://searchworks.stanford.edu/?f%5Bformat_main_ssim%5D%5B%5D=Book";
		//while(i<=3){
			fetcher = new Fetcher(2,QueueCrawlerJob.class);
			//String url1 = url + i;
			//System.out.println(url1);
			//i++;
			crawlMeta = new CrawlMeta();
			crawlMeta.setUrl(url);
			//crawlMeta.addPositiveRegex("https://searchworks.stanford.edu/view/(.*){0,5}\\d+$");
			//crawlMeta.addPositiveRegex("https://searchworks.stanford.edu/?f%5Bformat_main_ssim%5D%5B%5D=Book");
			crawlMeta.addPositiveRegex("https://searchworks.stanford.edu/view/(.*){0,5}\\d+$");
			//fetcher.fetchQueue.isOver = false;
			fetcher.addFeed(crawlMeta);
			fetcher.start();
			
		//}	
	
	}
	
	/*@Test
    public void testCrawel() throws Exception {
        Fetcher fetcher = new Fetcher(2, QueueCrawlerJob.class);

        String url = "http://chengyu.911cha.com/zishu_4.html";
        CrawlMeta crawlMeta = new CrawlMeta();
        crawlMeta.setUrl(url);
        crawlMeta.addPositiveRegex("http://chengyu.911cha.com/zishu_4_p[0-9]+\\.html$");

        fetcher.addFeed(crawlMeta);


        fetcher.start();
    }*/
	
}
