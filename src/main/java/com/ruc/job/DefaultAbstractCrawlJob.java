package com.ruc.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.ruc.entity.CrawlHttpConf;
import com.ruc.entity.CrawlMeta;
import com.ruc.entity.CrawlResult;
import com.ruc.fetcher.FetchQueue;
import com.ruc.filter.ResultFilter;
import com.ruc.utils.HttpUtils;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public abstract class DefaultAbstractCrawlJob extends AbstractJob
{
	


	/**
	 * 配置项信息
	 */
	private CrawlMeta crawlMeta;
	
	/**
	 * 存储爬取结果
	 */
	private CrawlResult crawlResult;
	
    /**
     * http配置信息
     */
    private CrawlHttpConf httpConf = new CrawlHttpConf();
    
    /**
     * 批量查询的结果
     */
    private List<CrawlResult> crawlResults = new ArrayList<>();
    
    /**
     * 爬取网页的深度，默认为零，机制爬取当前页面
     */
    protected int depth = 0;
    
    /**
     * 待爬取的任务队列
     */
    private FetchQueue fetchQueue;
    
    
    
    public DefaultAbstractCrawlJob(int depth) {
        this.depth = depth;
    }

    
	/**
	 * 执行抓取网页
	 */
	@Override
	public void doFetchPage() throws Exception
	{
		if(log.isDebugEnabled()){
			log.debug("start crawl url:{}",crawlMeta.getUrl());
		}
		long start = System.currentTimeMillis();
		HttpResponse response = HttpUtils.request(this.crawlMeta,httpConf);
		String res = EntityUtils.toString(response.getEntity(), httpConf.getCode());
		long end = System.currentTimeMillis();
		if(log.isDebugEnabled()){
			log.debug("crawl url:{} response code: {} cost time: {} ms\n",
					this.crawlMeta.getUrl(),
					response.getStatusLine().getStatusCode(),
					end-start);
		}
		if(response.getStatusLine().getStatusCode() != HttpStatus.SC_OK){
			this.crawlResult = new CrawlResult();
			this.crawlResult.setStatus(response.getStatusLine().getStatusCode(),response.getStatusLine().getReasonPhrase());
			this.crawlResult.setUrl(crawlMeta.getUrl());
			this.visit(this.crawlResult);
			return;
		}
		  // 网页解析
        this.crawlResult = doParse(res, this.crawlMeta);
		
		//回调用户的网页内容解析方法
		this.visit(this.crawlResult);
		
		ResultFilter.filter(crawlMeta, crawlResult, fetchQueue, depth);
    
		
		
		
		
	}

	/*private void doFetchNextPage(int currentDepth, String url) throws Exception
	{
		CrawlResult result = null;
		
		try
		{
			if (StorageWrapper.getInstance().ifUrlFetched(url))
			{
				return;
			}
			CrawlMeta subCrawlMeta = new CrawlMeta(url,
					this.crawlMeta.getSelectorRules(),
					this.crawlMeta.getPositiveRegex(),
					this.crawlMeta.getNegativeRegex());
			HttpResponse response = HttpUtils.request(subCrawlMeta, httpConf);
			String res = EntityUtils.toString(response.getEntity());
			if (response.getStatusLine().getStatusCode() != HttpStatus.SC_OK)
			{//请求成功

				result = new CrawlResult();
				result.setStatus(response.getStatusLine().getStatusCode(),
						response.getStatusLine().getReasonPhrase());
				result.setUrl(crawlMeta.getUrl());
				this.visit(result);
				return;
			}
			//网页解析
			result = doParse(res, subCrawlMeta);
		} finally 
		{
			//添加一条记录，并释放锁
			StorageWrapper.getInstance().addFetchRecord(url,result);
		}
		
		
		//回调用户的网页内容解析方法
		this.visit(result);
		
		//如果当前深度超过最大深度，则停止爬取
		if(currentDepth > depth){
			return;
		}
		
		if(currentDepth == depth){
			Elements eles = result.getHtmlDoc().select(".table.table-striped");
			String s = eles.text();
			System.out.println(s);
			return;
		}
		 
		Elements elements = result.getHtmlDoc().select("a[href]");
		String src;
		for(Element element : elements){
			//确保相对地址转为绝对地址
			src = element.attr("abs:href");
			System.out.println(src);
			if(matchRegex(src)){
				doFetchNextPage(currentDepth+1,src);
			}
			
		}
		
	}*/

	//规则匹配方法
	private static boolean matchRegex(CrawlMeta crawlMeta ,String src)
	{
		Matcher matcher;
		for(Pattern pattern: crawlMeta.getPositiveRegex()){
			matcher = pattern.matcher(src);
			if(matcher.find()){
				return true;
			}
		}
		
		for(Pattern pattern : crawlMeta.getNegativeRegex()){
			matcher = pattern.matcher(src);
			if(matcher.find()){
				return false;
			}
		}
		return crawlMeta.getPositiveRegex().size() == 0;
	}

	
	
	
	private CrawlResult doParse(String html,CrawlMeta subCrawlMeta)
	{
		 Document doc = Jsoup.parse(html,subCrawlMeta.getUrl());

	        Map<String, List<String>> map = new HashMap<>(crawlMeta.getSelectorRules().size());
	        for (String rule: crawlMeta.getSelectorRules()) {
	            List<String> list = new ArrayList<>();
	            for (Element element: doc.select(rule)) {
	                list.add(element.text());
	            }

	            map.put(rule, list);
	        }


	        CrawlResult crawlResult = new CrawlResult();
	        crawlResult.setHtmlDoc(doc);
	        crawlResult.setUrl(crawlMeta.getUrl());
	        crawlResult.setResult(map);
	        crawlResult.setStatus(CrawlResult.SUCCESS);
			return crawlResult;
	}

	protected void setResponseCode(String code){
		httpConf.setCode(code);
	}
	
	@Override
	public void clear()
	{
		this.depth = 0;
		this.crawlMeta = null;
		this.fetchQueue = null;
		this.crawlResult = null;
		
	}



}
