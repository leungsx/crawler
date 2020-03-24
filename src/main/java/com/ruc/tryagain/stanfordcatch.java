package com.ruc.tryagain;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class stanfordcatch
{
	public static void main(String[] args){
		String url = "https://catalog.lib.uchicago.edu/vufind/Search/Results?lookfor=&type=AllFields&filter%5B%5D=format%3A%22E-Resource%22";
		List<String>  linkslist = new ArrayList<String>();
		try
		{
			Document document = Jsoup.connect(url).timeout(50000).header("user-agent","Mozilla/5.0 "
					+ "(Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) "
					+ "Chrome/71.0.3578.80 Safari/537.36").get();
			Element form = document.getElementById("search-cart-form");
			Elements links = form.select(".result.ajaxItem").select(".title");
			System.out.println("我能到这里了！");
			for(Element link : links){	
				System.out.println("我来到泽丽了");
				String linkHref = link.attr("abs:href");
				System.out.println("链接为："+linkHref);				
				if(linkHref != null){
					linkslist.add(linkHref);
				}
							
			}
			System.out.println("第一层的链接我已经收集到了,有"+linkslist.size()+"个");
			for(String targetlink : linkslist){
				catchtarget(targetlink);
			}
			
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	private static void catchtarget(String targetlink) throws IOException
	{
		HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(targetlink);
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        httpGet.setHeader("Accept", "Accept text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");    
        
        httpGet.setHeader("Accept-Charset", "GB2312,utf-8;q=0.7,*;q=0.7");    
            
        httpGet.setHeader("Accept-Encoding", "gzip, deflate");    
            
        httpGet.setHeader("Accept-Language", "zh-cn,zh;q=0.5");    
            
        httpGet.setHeader("Connection", "keep-alive");    
            
        httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; rv:6.0.2) Gecko/20100101 Firefox/6.0.2");
        HttpResponse response = httpClient.execute(httpGet);
        String contents = EntityUtils.toString(response.getEntity(),"utf-8");//utf-8
		Document doc = Jsoup.parse(contents);
		String text = doc.body().text();
		System.out.println(text);
	}
}
