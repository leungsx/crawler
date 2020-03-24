package BaseCrawlerTest;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.ruc.entity.CrawlMeta;
import com.ruc.entity.CrawlResult;
import com.ruc.job.DefaultAbstractCrawlJob;


public class BaseCrawlertest
{

	/*@Test
	public void testFetch() throws InterruptedException{
		String url = "https://catalog.lib.uchicago.edu/vufind/Search/Results?type=AllFields&filter%5B%5D=format%3A%22Book%22";
		Set<String> selectRule = new HashSet<>();
		selectRule.add("div[class=mainbody]");
		selectRule.add("div[class=custom-marc-record]");
		selectRule.add("div[class=title]");
		CrawlMeta crawlMeta = new CrawlMeta();
		crawlMeta.setUrl(url); 
		crawlMeta.setSelectorRules(selectRule);
		SimpleCrawlJob simpleCrawlJob = new SimpleCrawlJob();
		simpleCrawlJob.setCrawlMeta(crawlMeta);
		Thread thread = new Thread(simpleCrawlJob,"crawler-test");
		thread.start();
		thread.join();
		CrawlResult result = simpleCrawlJob.getCrawlResult();
		System.out.println(result);
	}*/
	
/*	
	@Test
	public void testDepthFetch() throws InterruptedException{
		String url = "http://chengyu.911cha.com/zishu_3_p1.html";
	    CrawlMeta crawlMeta = new CrawlMeta();
	    crawlMeta.setUrl(url);
	    crawlMeta.addPositiveRegex("http://chengyu.911cha.com/zishu_([0-9]+).html");
		SimpleCrawlJob job = new SimpleCrawlJob(1);
	    job.setCrawlMeta(crawlMeta);
	    Thread thread = new Thread(job, "crawlerDepth-test");
	    thread.start();
	    thread.join();
	    List<CrawlResult> result = job.getCrawlResults();
	    System.out.println("完成了");

		
	}*/
	
	@Test
	public void testSelfCwralFetch() throws InterruptedException {
		String url = "https://catalog.lib.uchicago.edu/vufind/Search/Results?filter%5B%5D=format%3A%22E-Resource%22&filter%5B%5D=format%3A%22Book%22&type=AllFields";
        CrawlMeta crawlMeta = new CrawlMeta();
    	/*Set<String> selectRule = new HashSet<>();
		selectRule.add("div[class=mainbody]");
		selectRule.add("div[class=custom-marc-record]");
		selectRule.add("div[class=title]");
		selectRule.add("div[class=pagination]");*/
        crawlMeta.setUrl(url);
//        crawlMeta.setSelectorRules(selectRule);
        //crawlMeta.addPositiveRegex("https://catalog.lib.uchicago.edu/vufind/Record/(.*){0,5}\\d+$");
        crawlMeta.addPositiveRegex("https://catalog.lib.uchicago.edu/vufind/Search/Results[\\s\\S]+page=\\d$");
        
        DefaultAbstractCrawlJob job = new DefaultAbstractCrawlJob(1) {
            @Override
            protected void visit(CrawlResult crawlResult) {
                System.out.println("job1 >>> " + crawlResult.getUrl());
            }
        };
        job.setCrawlMeta(crawlMeta);



       /* String url2 = "http://chengyu.t086.com/gushi/2.htm";
        CrawlMeta crawlMeta2 = new CrawlMeta();
        crawlMeta2.setUrl(url2);
        crawlMeta2.addPositiveRegex("http://chengyu.t086.com/gushi/[0-9]+\\.htm$");
        DefaultAbstractCrawlJob job2 = new DefaultAbstractCrawlJob(1) {
            @Override
            protected void visit(CrawlResult crawlResult) {
                System.out.println("job2 >>> " + crawlResult.getUrl());
            }
        };
        job2.setCrawlMeta(crawlMeta2);*/



        Thread thread = new Thread(job, "crawlerDepth-test");
//        Thread thread2 = new Thread(job2, "crawlerDepth-test2");
        thread.start();
//        thread2.start();


        thread.join();
//        thread2.join();
    }
	
	

	

	
}
