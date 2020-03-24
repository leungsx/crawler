package com.ruc.conf;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;

import com.ruc.utils.FileReadUtils;
@Slf4j
public class FileConfRead implements IConfRead
{

	
	@Override
	public Config initConf(String path)
	{
		try
		{ 
			Properties properties = read(path);
			
			Config config = new Config();
//			System.out.println(properties.getProperty("sleep")+properties.getProperty("emptyQueueWaitTime"));
			config.setSleep(properties.getProperty("sleep"),0);
			config.setEmptyQueueWaitTime(properties.getProperty("emptyQueueWaitTime"),200);
			config.setFetchQueueSize(properties.getProperty("fetchQueueSize"), 100);

			log.info("init config success!");
			return config;
		} catch (Exception e)
		{
			log.error("init config from file:{} error! e:{}",path,e);
			return new Config();
		}
	}

	private Properties read(String fileName) throws IOException
	{
		try(InputStream inputStream = FileReadUtils.getStreamByFileName(fileName))
		{
			Properties pro = new Properties();
			pro.load(inputStream);
			return pro;
		}
	}

	private File file;
	private long lastTime;
	
	
	@Override
	public void registerCheckTask(final String path)
	{
		try
		{
			file = FileReadUtils.getFile(path);
			lastTime = file.lastModified();
			ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
			scheduledExecutorService.scheduleAtFixedRate(()->{
				if(file.lastModified() > lastTime){
					lastTime = file.lastModified();
					ConfigWrapper.getInstance().post(new ConfigWrapper.UpdateConfEvent());
				}
			},
			1,
			1,
			TimeUnit.MINUTES);
		} catch (Exception e)
		{
			throw new RuntimeException(e);
		}
	}

}
