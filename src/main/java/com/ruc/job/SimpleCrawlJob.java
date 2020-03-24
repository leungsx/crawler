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

import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.ruc.entity.CrawlHttpConf;
import com.ruc.entity.CrawlMeta;
import com.ruc.entity.CrawlResult;
import com.ruc.utils.HttpUtils;

@Getter
@Setter
@NoArgsConstructor
public class SimpleCrawlJob extends DefaultAbstractCrawlJob
{
	
	
	/**
	 * 存储爬取结果
	 */
	private CrawlResult crawlResult;
	
   
    /**
     * 批量查询的结果
     */
    private List<CrawlResult> crawlResults = new ArrayList<>();
    
    
    
    public SimpleCrawlJob(int depth) {
        super(depth);
    }

	@Override
	protected void visit(CrawlResult crawlResult)
	{
		crawlResults.add(crawlResult);		
	}

	public CrawlResult getCrawlResult(){
		if(crawlResults.size() == 0){
			return null;
		}
		return crawlResults.get(0);
	}
	
 
}
