package com.ruc.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import com.ruc.fetcher.JobCount;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class CrawlMeta
{
	/**
	 * 当前任务对应的jobcount#id
	 */
	@Getter
	@Setter
	private int jobId;
	
	/**
	 * 当前任务对应的jobcount#parentid
	 */
	@Getter
	@Setter
	private int parentJobId;
	
	/**
	 * 当前爬取的深度
	 */
	@Getter
	@Setter
	private int currentDepth;
	
	/**
	 * 待爬取的网页
	 */
	@Getter
	@Setter
	private String url;
	
	/**
	 * 获取指定内容的规则，因为一个网页中，你可能获取多个不同的内容，所以放在集合中
	 */
	@Setter
	@Getter
	private	Set<String> selectorRules = new HashSet<>();
	
	/**
	 * 正向的过滤规则
	 */
	@Setter
	@Getter
	private Set<Pattern> positiveRegex = new HashSet<>();
	
	/**
	 * 逆向的过滤规则
	 */
	@Setter
	@Getter
	private Set<Pattern> negativeRegex = new HashSet<>();
	
	
	public Set<Pattern> addNegativeRegex(String regex){
		this.negativeRegex.add(Pattern.compile(regex));
		return this.negativeRegex;
	}
	
	public Set<Pattern> addPositiveRegex(String regex){
		this.positiveRegex.add(Pattern.compile(regex));
		return this.positiveRegex;
	}
	
	//这样做的目的是为了防止NPE，也就是说支持不指定选择规则
	public Set<String> addSelectorRules(String rule){
		this.selectorRules.add(rule);
        return selectorRules;
	}


	public CrawlMeta(){
		this.jobId = JobCount.genId();
	}
	
}
