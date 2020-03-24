package com.ruc.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import com.ruc.entity.CrawlHttpConf;
import com.ruc.entity.CrawlMeta;

public class HttpUtils
{

	public static HttpResponse request(CrawlMeta crawlMeta,CrawlHttpConf httpConf)throws Exception{
		switch(httpConf.getMethod()){
		
			case GET:
				return doGet(crawlMeta.getUrl(),httpConf);
			case POST:
				return doPost(crawlMeta.getUrl(),httpConf);
			default:
				return null;
		}
	}

	private static HttpResponse doPost(String url,
			CrawlHttpConf httpConf) throws Exception
	{

		//HttpClient httpClient = HttpClient.createDefault();
		SSLContextBuilder builder = new SSLContextBuilder();
		//全部信任，不做身份鉴定
		builder.loadTrustMaterial(null, (x509Certificates, s) -> true);
		HttpClient httpClient = HttpClientBuilder.create().setSslcontext(builder.build()).build();
		HttpPost httpPost = new HttpPost(url);
		//建立一个NameValuePair数组，用于存储域传送的参数
		List<NameValuePair> params = new ArrayList<>();
		for(Map.Entry<String, Object> param : httpConf.getRequestParams().entrySet()){
			params.add(new BasicNameValuePair(param.getKey(),param.getValue().toString()));
			
		}
		httpPost.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
		
		//设置头请求
		for(Map.Entry<String,String> head : httpConf.getRequestHeaders().entrySet()){
			httpPost.addHeader(head.getKey(),head.getValue());
		}
		
		//执行网络请求
		return httpClient.execute(httpPost);	
		
	}

	
	private static HttpResponse doGet(String url,	
			CrawlHttpConf httpConf) throws Exception
	{
		SSLContextBuilder builder = new SSLContextBuilder();
		//全部信任，不做身份鉴定
		builder.loadTrustMaterial(null,(x509Certificates,s) -> true);
		HttpClient httpClient = HttpClientBuilder.create().setSslcontext(builder.build()).build();
		
		//设置头参数
		StringBuilder param = new StringBuilder(url).append("?");
		
		for(Map.Entry<String, Object> entry : httpConf.getRequestParams().entrySet()){
			param.append(entry.getKey())
			.append("=")
			.append(entry.getValue())
			.append("&");
		}
		
		//过滤掉最后一个无效字符
		HttpGet httpGet = new HttpGet(param.substring(0,param.length() - 1));
		
		//设置请求头
		for(Map.Entry<String, String> head : httpConf.getRequestHeaders().entrySet()){
			httpGet.addHeader(head.getKey(),head.getValue());
		
		}
		
		//执行网络请求
		
		return httpClient.execute(httpGet);
	}
}
