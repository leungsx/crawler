package com.ruc.storage;

import com.ruc.entity.CrawlResult;

public interface IStorage
{
	/**
	 * 如爬取的URL不在storage中，则写入，否则，则忽略
	 * @author 大哥
	 * @date 2020年3月11日
	 * @param url 爬取的网址
	 * @param result
	 * @return true 表示写入成功，寄之前没有没有这个记录；false表示之前已经有记录了
	 */

	boolean putIfNoExist(String	url,CrawlResult	result);
	
	/**
	 * 判断是否存在
	 * @author 大哥
	 * @date 2020年3月11日
	 * @return
	 */
	boolean contain(String url);
}
