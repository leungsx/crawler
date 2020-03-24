package com.ruc.conf;

public interface IConfRead
{

	/**
	 * 初始化配置信息
	 * @author 大哥
	 * @date 2020年3月18日
	 * @param var
	 * @return
	 */
	Config initConf(String var);
	
	/**
	 * 注册配置信息更新检测任务
	 * @author 大哥
	 * @date 2020年3月18日
	 * @param path
	 */
	
	void registerCheckTask(final String path);
	
}
