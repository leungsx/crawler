package com.ruc.entity;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import org.jsoup.nodes.Document;



@Setter
@Getter
@ToString
public class CrawlResult
{
	
	public static Status SUCCESS = new Status(200, "success");
    public static Status NOT_FOUND = new Status(494, "not found");
	
	/**
	 * 爬取的网址
	 */
	private String url;
	/**
	 * 爬取的网址对应的docment
	 */
	private Document htmlDoc;
	/**
	 * 选择的结果，key为选择规则，value为根据规则匹配的结果
	 */
	
	private Map<String,List<String>> result;

	private Status status;
	
	public void setStatus(int code,String msg){
		this.status = new Status(code,msg);
	}
	
	@Getter
	@Setter
	@ToString
	@AllArgsConstructor
	static class Status{
		private int code;
		private String msg;
	}
	
}
